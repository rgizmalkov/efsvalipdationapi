package efs.validation.machine;

import com.gmail.rgizmalkov.dev.meta.Validation;
import efs.validation.core.Decomposed;
import efs.validation.core.meta.*;
import efs.validation.core.DecomposeService;
import efs.validation.core.meta.stratgy.Strategy;
import efs.validation.decompose.AnnotationDecomposeService;
import efs.validation.decompose.DecomposedRs;
import efs.validation.machine.vo.DecomposedObject;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.*;

public class EfsDecomposeService implements DecomposeService {

    private AnnotationDecomposeService annotationDecomposeService = new AnnotationDecomposeService();

    @Override
    public DecomposedObject reachDecomposeObject(DecomposedObject decomposedObject) {

        Field field = decomposedObject.getField();
        Annotation[] declaredAnnotations = field.getDeclaredAnnotations();

        //Step 1. Find first condition annotation
        Composite annotation = findFirstCompositeAnnotation(declaredAnnotations);
        if(annotation != null){
            //Step 2. Parse only one Condition annotation
            Annotation strategyAnnotation = findAnyStrategyAnnotationInFirstLayer(annotation.annotationType());
            AnnotationDecomposeService.CreateStrategyRs createStrategyRs = annotationDecomposeService.createStrategy(strategyAnnotation != null ? strategyAnnotation.annotationType() : null);
            AnnotationDecomposeService.CreateAnnotationRs createAnnotationRs = annotationDecomposeService.createCompositeAnnotation(annotation.annotationType(), new HashSet<Class<? extends Annotation>>());
            List<Decomposed.CreatingError> creatingErrorsList = new ArrayList<>();
            if (createStrategyRs.getError() != null) creatingErrorsList.add(createStrategyRs.getError());
            creatingErrorsList.addAll(createAnnotationRs.getErrors());
            decomposedObject.setDefaultValue(createAnnotationRs.getDefaultValue());
            Decomposed decomposed = new Decomposed();
            decomposed.setDefaultStrategy(createStrategyRs.getStrategy());
            decomposed.setCreatingErrors(creatingErrorsList);
            Decomposed.ValidatorsManager validatorsManager = new Decomposed.ValidatorsManager();
            for (Decomposed.ValidationCondition validationCondition : createAnnotationRs.getConditions()) {
                validatorsManager.add(validationCondition);
            }
            decomposed.setVman(validatorsManager);
            decomposedObject.setDecomposed(decomposed);

        }else {
            //Step 2. Find all annotations like Behaviour, Validation, Strategy
            DecomposedRs enrichment = enrichment(declaredAnnotations);
            if(isNotEmptyRs(enrichment)) {
                decomposedObject.setDecomposed(enrichment.getDecomposed());
            }
            decomposedObject.setDefaultValue(enrichment.getDefaultValue());
        }

        return decomposedObject;
    }

    private boolean isNotEmptyRs(DecomposedRs enrichment) {
        Decomposed decomposed = enrichment.getDecomposed();
        Collection<Decomposed.ValidationCondition> all = decomposed.getVman().getAll();
        return all != null && all.size() > 0;
    }

    private Annotation findAnyStrategyAnnotationInFirstLayer(Class<? extends Annotation> acls) {
        Annotation[] annotations = acls.getAnnotations();
        for (Annotation annotation : annotations) {
            Strategy strategy = annotation.annotationType().getAnnotation(Strategy.class);
            if (strategy != null) {
                return strategy;
            }
        }
        return null;
    }

    private Composite findFirstCompositeAnnotation(Annotation[] declaredAnnotations) {
        for (Annotation declaredAnnotation : declaredAnnotations) {
            Composite annotation = declaredAnnotation.annotationType().getAnnotation(Composite.class);
            if(annotation != null){
                return annotation;
            }
        }
        return null;
    }

    private DecomposedRs enrichment(Annotation[] annotations) {
        Default defaultValue = null;
        AnnotationDecomposeService.CreateStrategyRs strategy = null;
        Decomposed decomposed = Decomposed.newInstance();
        for (Annotation annotation : annotations) {
            Class<? extends Annotation> atp = annotation.annotationType();
            if(isCA(atp)){
                AnnotationDecomposeService.CreateAnnotationRs createAnnotationRs = annotationDecomposeService.new CreateAnnotationRs();
                createAnnotationRs.merge(annotationDecomposeService.createComplexAnnotation(atp));
                List<Decomposed.ValidationCondition> conditions = createAnnotationRs.getConditions();
                for (Decomposed.ValidationCondition condition : conditions) {
                    decomposed.addValidationCondition(condition);
                }
                if(createAnnotationRs.getDefaultValue() != null) defaultValue = createAnnotationRs.getDefaultValue();
            }else if(strategy == null && isSA(atp)){
                strategy = annotationDecomposeService.createStrategy(atp);
                decomposed.setDefaultStrategy(strategy.getStrategy());
                if(strategy.getError() != null) decomposed.addCreatingError(strategy.getError());
            }else if(isDefaultValue(atp)) {
                defaultValue = (Default) annotation;
            }else if (isDMV(atp)) {
                decomposed.addDmv(atp);
            }
        }
        return new DecomposedRs(decomposed, defaultValue);
    }

    private boolean isCA(Class<? extends Annotation> annotation) {
        return annotation.isAnnotationPresent(Complex.class);
    }

    private boolean isDefaultValue(Class<? extends Annotation> atp) {
        return atp.equals(Default.class);
    }

    private boolean isDMV(Class<? extends Annotation> annotation) {
        return annotation.isAnnotationPresent(DynamicManagedValue.class);
    }

    private boolean isSA(Class<? extends Annotation> annotation) {
        return annotation.isAnnotationPresent(Strategy.class);
    }

    private boolean isVA(Class<? extends Annotation> annotation){
        return annotation.isAnnotationPresent(Validation.class) && (annotation.isAnnotationPresent(RelatedValidator.class) || annotation.isAnnotationPresent(RelatedValidators.class));
    }



}
