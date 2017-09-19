package efs.validation.decompose;

import com.gmail.rgizmalkov.dev.meta.Validation;
import efs.validation.core.Decomposed;
import efs.validation.core.meta.*;
import efs.validation.core.meta.behaviour.ThrowEvent;
import efs.validation.core.meta.behaviour.ExcludeOn;
import efs.validation.core.meta.stratgy.Strategy;
import efs.validation.enums.ValidationStrategy;
import efs.validation.util.LockValue;

import java.lang.annotation.Annotation;
import java.util.*;

import static java.lang.String.format;

public final class AnnotationDecomposeService {

    public final class CreateAnnotationRs {
        List<Decomposed.ValidationCondition> conditions;
        List<Decomposed.CreatingError> errors;

        LockValue<Default> defaultValue = LockValue.<Default>newInstance();
        Map<Annotation, DynamicManagedValue> dmv;

        public CreateAnnotationRs() {
        }

        public CreateAnnotationRs addCondition(Decomposed.ValidationCondition condition) {
            if (condition == null) return this;
            if (conditions == null) conditions = new ArrayList<>();
            this.conditions.add(condition);
            return this;
        }

        public CreateAnnotationRs addErrors(Decomposed.CreatingError error) {
            if (error == null) return this;
            if (errors == null) errors = new ArrayList<>();
            this.errors.add(error);
            return this;
        }

        public List<Decomposed.ValidationCondition> getConditions() {
            return conditions;
        }

        public List<Decomposed.CreatingError> getErrors() {
            return errors;
        }

        public Default getDefaultValue() {
            return defaultValue.get();
        }

        public CreateAnnotationRs addDefaultValue(Default defaultValue) {
            this.defaultValue.lock(defaultValue);
            return this;
        }

        public CreateAnnotationRs addDMV(Annotation dmv) {
            if(dmv == null || !isMarkedByDMV(dmv.annotationType())) return this;
            if (this.dmv == null) this.dmv = new HashMap<>();
            this.dmv.put(dmv, dmv.annotationType().getAnnotation(DynamicManagedValue.class));
            return this;
        }

        public void merge(CreateAnnotationRs createAnnotationRs) {
            if (createAnnotationRs != null) {
                if (this.conditions == null) this.conditions = createAnnotationRs.conditions;
                else this.conditions.addAll(createAnnotationRs.getConditions());

                if (this.errors == null) this.errors = createAnnotationRs.errors;
                else this.errors.addAll(createAnnotationRs.getErrors());

                if(this.dmv == null) this.dmv = createAnnotationRs.dmv;
                else this.dmv.putAll(createAnnotationRs.dmv);

                this.defaultValue.lock(createAnnotationRs.defaultValue.get());
            }
        }
    }

    public CreateAnnotationRs createValidationAnnotation(Class<? extends Annotation> validation) {
        CreateAnnotationRs createAnnotationRs = new CreateAnnotationRs();

        RelatedValidator validator = validation.getAnnotation(RelatedValidator.class);
        RelatedValidators validators = validation.getAnnotation(RelatedValidators.class);
        if (validator == null && validators == null) {
            createAnnotationRs.addErrors(
                    Decomposed.CreatingError.error(
                            "Non one related validator found", format("class marked by %s have to contains one or more %s", Validation.class, RelatedValidator.class),
                            format("%s.%s(args)", this.getClass().getName(), "createValidationAnnotation")).action("ignored")
            );
            return createAnnotationRs;
        }
        ExcludeOn excludeOn = validation.getAnnotation(ExcludeOn.class);
        String exclusion = getExclusionOrNull(excludeOn);
        Condition condition = validation.getAnnotation(Condition.class);
        Set<String> conditions = getConditionsOrNull(condition);

        if (validator != null) {
            setRelatedValidator(conditions, exclusion, validator, createAnnotationRs);
        }
        if (validators != null) {
            for (RelatedValidator relatedValidator : validators.value()) {
                setRelatedValidator(conditions, exclusion, relatedValidator, createAnnotationRs);
            }
        }

        return createAnnotationRs;
    }


    public CreateAnnotationRs createComplexAnnotation(Class<? extends Annotation> complex) {
        CreateAnnotationRs createAnnotationRs = new CreateAnnotationRs();
        ExcludeOn excludeOn = complex.getAnnotation(ExcludeOn.class);
        String exclusion = getExclusionOrNull(excludeOn);
        Condition condition = complex.getAnnotation(Condition.class);
        Set<String> conditions = getConditionsOrNull(condition);


        Annotation[] annotations = complex.getAnnotations();
        for (Annotation annotation : annotations) {
            if (isMarkedByRelatedValidationSign(annotation.annotationType())) {
                RelatedValidator validator = complex.getAnnotation(RelatedValidator.class);
                RelatedValidators validators = complex.getAnnotation(RelatedValidators.class);
                if (validator != null) {
                    setRelatedValidator(conditions, exclusion, validator, createAnnotationRs);
                }
                if (validators != null) {
                    for (RelatedValidator relatedValidator : validators.value()) {
                        setRelatedValidator(conditions, exclusion, relatedValidator, createAnnotationRs);
                    }
                }
            } else if (isMarkedByValidation(annotation.annotationType())) {
                CreateAnnotationRs validationAnnotation = createValidationAnnotation(annotation.annotationType());
                createAnnotationRs.merge(validationAnnotation);
            }
        }

        healthCheckAndMergeRs(createAnnotationRs, exclusion, conditions, Complex.class);

        return createAnnotationRs;
    }


    public CreateAnnotationRs createCompositeAnnotation(Class<? extends Annotation> composite, Set<Class<? extends Annotation>> compositeClasses) {
        CreateAnnotationRs createAnnotationRs = new CreateAnnotationRs();
        if (compositeClasses == null) compositeClasses = new HashSet<>();
        if (compositeClasses.contains(composite)) return createAnnotationRs;
        compositeClasses.add(composite);

        RelatedValidator validator = composite.getAnnotation(RelatedValidator.class);
        RelatedValidators relatedValidators = composite.getAnnotation(RelatedValidators.class);
        ExcludeOn excludeOn = composite.getAnnotation(ExcludeOn.class);
        Condition condition = composite.getAnnotation(Condition.class);

        String exclusion = getExclusionOrNull(excludeOn);
        Set<String> conditions = getConditionsOrNull(condition);

        if (validator != null) {
            setRelatedValidator(conditions, exclusion, validator, createAnnotationRs);
        }
        if (relatedValidators != null) {
            for (RelatedValidator relatedValidator : relatedValidators.value()) {
                setRelatedValidator(conditions, exclusion, relatedValidator, createAnnotationRs);
            }
        }

        Annotation[] annotations = composite.getAnnotations();
        for (Annotation annotation : annotations) {
            Class<? extends Annotation> type = annotation.annotationType();
            if(isMarkedByComposite(type)){
                createAnnotationRs.merge(createCompositeAnnotation(type, compositeClasses));
            }else if(isMarkedByComplex(type)){
                createAnnotationRs.merge(createComplexAnnotation(type));
            }else if(isValidValidationAnnotation(type)){
                createAnnotationRs.merge(createValidationAnnotation(type));
            }else if(isMarkedByDMV(type)){
                createAnnotationRs.addDMV(annotation);
            }
        }

        healthCheckAndMergeRs(createAnnotationRs, exclusion, conditions, Composite.class);

        return createAnnotationRs;
    }

    private void healthCheckAndMergeRs(CreateAnnotationRs createAnnotationRs, String exclusion, Set<String> conditions, Class<? extends Annotation> acls){
        if (createAnnotationRs.conditions == null || createAnnotationRs.conditions.size() == 0) {
            createAnnotationRs.addErrors(
                    Decomposed.CreatingError.error(
                            format("@%s - creating exception", acls), format("annotation did not marked validators via [%s, %s, %s]", RelatedValidator.class, RelatedValidators.class, Validation.class),
                            format("%s.%s(args)", this.getClass(), "healthCheckAndMergeRs"))
                            .action("ignored")
            );
        } else if (exclusion != null || conditions != null) {
            List<Decomposed.ValidationCondition> conditionList = createAnnotationRs.conditions;
            for (Decomposed.ValidationCondition validationCondition : conditionList) {
                if (exclusion != null) {
                    validationCondition.addExcludeOn(exclusion);
                }
                if (conditions != null) {
                    //Было
//                    Set<String> conditionConditions = validationCondition.getConditions();
//                    if (conditionConditions == null) conditionConditions = conditions;
//                    else conditionConditions.addAll(conditions);
                    validationCondition.setConditions(validationCondition.getConditions());
                }
            }
        }
    }

    private void setRelatedValidator(Set<String> conditions, String exclusion, RelatedValidator relatedValidator, CreateAnnotationRs createAnnotationRs) {
        createAnnotationRs.addCondition(new Decomposed.ValidationCondition(conditions, relatedValidator.value()).addExcludeOn(exclusion));
    }

    private String getExclusionOrNull(ExcludeOn excludeOn) {
        if (excludeOn != null) return excludeOn.key();
        return null;
    }

    private Set<String> getConditionsOrNull(Condition conditions) {
        if (conditions != null) {
            String[] strings = conditions.value();
            HashSet<String> set = new HashSet<>();
            set.addAll(Arrays.asList(strings));
            return set;
        }
        return null;
    }

    public static final class CreateStrategyRs {
        Decomposed.Strategy strategy;
        Decomposed.CreatingError error;

        CreateStrategyRs(Decomposed.Strategy strategy, Decomposed.CreatingError error) {
            this.strategy = strategy;
            this.error = error;
        }

        public Decomposed.Strategy getStrategy() {
            return strategy;
        }

        public Decomposed.CreatingError getError() {
            return error;
        }
    }

    public AnnotationDecomposeService.CreateStrategyRs createStrategy(Class<? extends Annotation> strg) {
        if (strg == null || strg.isAnnotationPresent(Strategy.class)) {
            Decomposed.Strategy strategy = setStrategy(null, strg, new HashSet<Class<? extends Annotation>>());
            return new AnnotationDecomposeService.CreateStrategyRs(strategy, null);
        } else {
            Decomposed.CreatingError error = Decomposed.CreatingError.error(
                    "Not valid root strategy", format("in class [%s] annotation Strategy.class not presented", strg),
                    format("%s.%s(args)", this.getClass().getName(), "parse")
            ).action(format("Changed not valid strategy class [%s] to default motion [%s without events]", strg, "ValidationStrategy.ONE_FAIL_ALL_DOWN"));
            return new AnnotationDecomposeService.CreateStrategyRs(new Decomposed.Strategy(ValidationStrategy.ONE_FAIL_ALL_DOWN), error);
        }
    }

    private Decomposed.Strategy setStrategy(Decomposed.Strategy strategy, Class<? extends Annotation> strg, Set<Class<? extends Annotation>> strategies) {
        if (strategies.contains(strg)) return strategy;
        Strategy strategyObject = strg.getAnnotation(Strategy.class);
        if (strategyObject == null) return strategy;
        if (strategy == null) strategy = new Decomposed.Strategy(strategyObject.value());
        setAllEvents(strategy, strg);
        strategies.add(strg);

        for (Annotation annotation : strg.getAnnotations()) {
            Class<? extends Annotation> type = annotation.annotationType();
            if (!Strategy.class.equals(type) && type.isAnnotationPresent(Strategy.class)) {
                setStrategy(strategy, type, strategies);
            }
        }
        return strategy;
    }

    private void setAllEvents(Decomposed.Strategy strategy, Class<? extends Annotation> strg) {
        Annotation[] annotations = strg.getAnnotations();
        for (Annotation annotation : annotations) {
            ThrowEvent event = annotation.annotationType().getAnnotation(ThrowEvent.class);
            if (event != null) {
                Decomposed.Strategy.Event eventObject = new Decomposed.Strategy.Event(event.event(), annotation);
                strategy.setEvent(eventObject);
            }
        }
    }


    public boolean isMarkedByRelatedValidationSign(Class<? extends Annotation> type) {
        return type.isAnnotationPresent(RelatedValidator.class) || type.isAnnotationPresent(RelatedValidators.class);
    }

    public boolean isMarkedByValidation(Class<? extends Annotation> type) {
        return type.isAnnotationPresent(Validation.class);
    }

    public boolean isValidValidationAnnotation(Class<? extends Annotation> type) {
        return isMarkedByValidation(type) && isMarkedByRelatedValidationSign(type);
    }

    public boolean isMarkedByExcludeOn(Class<? extends Annotation> type) {
        return type.isAnnotationPresent(ExcludeOn.class);
    }

    public boolean isConditionsExist(Class<? extends Annotation> type) {
        return type.isAnnotationPresent(Condition.class);
    }

    public boolean isMarkedByComposite(Class<? extends Annotation> type){
        return type.isAnnotationPresent(Composite.class);
    }

    public boolean isMarkedByComplex(Class<? extends Annotation> type){
        return type.isAnnotationPresent(Complex.class);
    }

    public boolean isMarkedByDMV(Class<? extends Annotation> type){
        return type.isAnnotationPresent(DynamicManagedValue.class);
    }
}
