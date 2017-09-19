package efs.validation.decompose;

import com.gmail.rgizmalkov.dev.meta.Validation;
import efs.validation.core.Decomposed;
import efs.validation.core.DecomposedClassService;
import efs.validation.core.meta.Condition;
import efs.validation.core.meta.RelatedValidator;
import efs.validation.core.meta.map.DecomposedElement;
import efs.validation.core.meta.map.DecomposedMap;

import java.lang.annotation.Annotation;
import java.util.*;

import static java.lang.String.format;

public class DecomposedMapParser implements DecomposedClassService {

    private AnnotationDecomposeService annotationDecomposeService = new AnnotationDecomposeService();

    @Override
    public Map<String, DecomposedRs> decompose(/*NonNull*/Class<?> cls) {
        DecomposedMap decomposedMapPresented = isDecomposedMapPresented(cls);
        if (cls == null || decomposedMapPresented == null) return new HashMap<>();
        if (!cls.equals(decomposedMapPresented.target())) return new HashMap<>();
        return parse(decomposedMapPresented);
    }


    private Map<String, DecomposedRs> parse(DecomposedMap decomposedMapPresented) {
        Class<?> target = decomposedMapPresented.target();
        DecomposedElement[] elements = decomposedMapPresented.elements();
        Map<String, DecomposedRs> decomposedMap = new HashMap<>();
        for (DecomposedElement element : elements) {
            Class<? extends Annotation> strategy = element.strategy();
            AnnotationDecomposeService.CreateStrategyRs createStrategyRs = annotationDecomposeService.createStrategy(strategy);
            Condition[] conditions = element.conditions();
            Set<String> cnds = new HashSet<>();
            if (conditions.length > 0) {
                for (Condition condition : conditions) {
                    cnds.addAll(Arrays.asList(condition.value()));
                }
            }
            String[] fields = element.fields();
            for (String field : fields) {
                Class<? extends Annotation>[] validationAnnotations = element.validationAnnotations();
                AnnotationDecomposeService.CreateAnnotationRs createAnnotationRs = annotationDecomposeService.new CreateAnnotationRs();
                for (Class<? extends Annotation> validationAnnotation : validationAnnotations) {
                    if (validationAnnotation.isAnnotationPresent(Validation.class)) {
                        createAnnotationRs.merge(annotationDecomposeService.createValidationAnnotation(validationAnnotation));
                    } else {
                        createAnnotationRs.addErrors(
                                Decomposed.CreatingError.error(
                                        "Not validation annotation in array", format("the instance of class [%s] does not marked by %s", validationAnnotation, Validation.class),
                                        format("Target [%s]: %s.%s(args)", target, this.getClass().getName(), "parse")).action("ignored")
                        );
                    }
                }
                RelatedValidator[] validators = element.validators();
                for (RelatedValidator validator : validators) {
                    createAnnotationRs.addCondition(new Decomposed.ValidationCondition(null, null, validator.value()));
                }

                for (Decomposed.ValidationCondition validationCondition : createAnnotationRs.conditions) {
                    validationCondition.setConditions(cnds);
                }
                List<Decomposed.CreatingError> creatingErrorsList = new ArrayList<>();
                if (createStrategyRs.error != null) creatingErrorsList.add(createStrategyRs.error);
                if(createAnnotationRs.errors != null)
                    creatingErrorsList.addAll(createAnnotationRs.errors);

                DecomposedRs decomposedRs = decomposedMap.get(field);
                Decomposed decomposed = decomposedRs != null ? decomposedRs.decomposed : null;
                if (decomposed == null) {
                    decomposed = new Decomposed();
                    decomposed.setDefaultStrategy(createStrategyRs.strategy);
                    decomposed.setCreatingErrors(creatingErrorsList);
                    Decomposed.ValidatorsManager validatorsManager = new Decomposed.ValidatorsManager();
                    for (Decomposed.ValidationCondition validationCondition : createAnnotationRs.conditions) {
                        validatorsManager.add(validationCondition);
                    }
                    decomposed.setVman(validatorsManager);
                    decomposedMap.put(field, new DecomposedRs(decomposed, createAnnotationRs.defaultValue.get()));
                }else {
                    //Стратегию не меняем, только добавляем валидаторы
                    for (Decomposed.ValidationCondition validationCondition : createAnnotationRs.conditions) {
                        decomposed.addValidationCondition(validationCondition);
                    }
                    if(decomposedRs.defaultValue == null)
                        decomposedRs.setDefaultValue(createAnnotationRs.defaultValue.get());
                }
            }
        }
        return decomposedMap;
    }


    private DecomposedMap isDecomposedMapPresented(Class<?> cls) {
        DecomposedMap explicitPresent = cls.getAnnotation(DecomposedMap.class);
        if (explicitPresent != null) return explicitPresent;

        for (Annotation annotation : cls.getAnnotations()) {
            DecomposedMap firstLayerInnerPresent = annotation.annotationType().getAnnotation(DecomposedMap.class);
            if (firstLayerInnerPresent != null) return firstLayerInnerPresent;
        }

        return null;
    }
}
