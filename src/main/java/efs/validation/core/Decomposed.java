package efs.validation.core;

import efs.validation.meta.related.Validator;
import com.google.common.base.Objects;
import efs.validation.enums.ValidationStrategy;

import java.lang.annotation.Annotation;
import java.util.*;

/**
 * Валидация одного ПОЛЯ.
 *
 */
public class Decomposed {

    //Объект валидации
    Object validationObject;

    //Стратегия валидации
    Strategy strategy;

    public static class Strategy {
        ValidationStrategy validationStrategy;
        Set<Event> events = new HashSet<>();
        public static class Event{
            String eventName;
            Annotation annotation;

            public Event(String eventName, Annotation annotation) {
                this.eventName = eventName;
                this.annotation = annotation;
            }

            @Override
            public boolean equals(Object o) {
                if (this == o) return true;
                if (o == null || getClass() != o.getClass()) return false;
                Event event = (Event) o;
                return Objects.equal(eventName, event.eventName);
            }

            @Override
            public int hashCode() {
                return Objects.hashCode(eventName);
            }

            public String getEventName() {
                return eventName;
            }

            public Event setEventName(String eventName) {
                this.eventName = eventName;
                return this;
            }

            public Annotation getAnnotation() {
                return annotation;
            }

            public Event setAnnotation(Annotation annotation) {
                this.annotation = annotation;
                return this;
            }
        }

        public Strategy(ValidationStrategy validationStrategy) {
            this.validationStrategy = validationStrategy;
        }

        public Strategy addEvent(Event event) {
            this.events.add(event);
            return this;
        }

        public ValidationStrategy getValidationStrategy() {
            return validationStrategy;
        }

        public Strategy setValidationStrategy(ValidationStrategy validationStrategy) {
            this.validationStrategy = validationStrategy;
            return this;
        }

        public Set<Event> getEvents() {
            return events;
        }

        public Strategy setEvents(Set<Event> events) {
            this.events = events;
            return this;
        }
    }

    public void setDefaultStrategy(Strategy strategy) {
        this.strategy = strategy;
    }

    //Настроенные валидаторы (включает условаия валидации, исключение валидации)
    ValidatorsManager vman;

    public static class ValidatorsManager {
        private Map<Class<? extends Validator>, ValidationCondition> validationConditionMap;

        private HashSet<ValidationCondition> cachedActiveValidators;

        public ValidatorsManager(Map<Class<? extends Validator>, ValidationCondition> validationConditionMap) {
            this.validationConditionMap = validationConditionMap != null ? validationConditionMap : new HashMap<Class<? extends Validator>, ValidationCondition>();
        }

        public ValidatorsManager() {
            this.validationConditionMap = new HashMap<>();
        }

        public ValidationCondition get(Class<? extends Validator> vcls) {
            return validationConditionMap.get(vcls);
        }

        public Collection<ValidationCondition> getAll() {
            return validationConditionMap.values();
        }

        public Collection<ValidationCondition> getAllActive(Set<String> conditions) {
            if (cachedActiveValidators != null) return cachedActiveValidators;
            Collection<ValidationCondition> av = new HashSet<>();
            for (ValidationCondition validationCondition : validationConditionMap.values()) {
                if (isActive(validationCondition, conditions)) {
                    av.add(validationCondition);
                }
            }
            this.cachedActiveValidators = (HashSet<ValidationCondition>) av;
            return av;
        }

        public void add(ValidationCondition validationCondition) {
            if (validationCondition != null && validationCondition.validator != null) {
                Class<? extends Validator> vcls = validationCondition.validator;

                ValidationCondition mappedValidator = validationConditionMap.get(vcls);
                if (mappedValidator == null) {
                    validationConditionMap.put(vcls, validationCondition);
                } else {
                    if (validationCondition.conditions != null && validationCondition.conditions.size() > 0) {
                        //Добавляем недостоющие условия для валидатора
                        if (!validationCondition.conditions.equals(mappedValidator.conditions)) {
                            if (mappedValidator.conditions == null) {
                                mappedValidator.conditions = new HashSet<>();
                            }
                            mappedValidator.conditions.addAll(validationCondition.conditions);
                        }
                        if(mappedValidator.excludeOn != null){
                            mappedValidator.excludeOn.addAll(validationCondition.excludeOn);
                        }else {
                            mappedValidator.excludeOn = validationCondition.excludeOn;
                        }
                    }

                }
            }


        }

        private boolean isActive(ValidationCondition validationCondition, Set<String> conditions) {
            /**
             * 1. У валидатора должны быть условия
             * 2. Условия менеджера не должны быть пусты
             * 3. Условия менеджера должны соджержать одно из условий валидатора
             */
            return (validationCondition.conditions != null && validationCondition.conditions.size() > 0) &&
                    (conditions != null && conditions.size() > 0) &&
                    containsOneOfCondition(validationCondition.conditions, conditions);
        }

        private boolean containsOneOfCondition(Set<String> inc, Set<String> conditions) {
            for (String condition : inc) {
                if (conditions.contains(condition)) {
                    return true;
                }
            }
            return false;
        }
    }

    public void addValidationCondition(ValidationCondition validationCondition){
        if(vman == null) vman = new ValidatorsManager();
        vman.add(validationCondition);
    }

    public Decomposed setVman(ValidatorsManager vman) {
        this.vman = vman;
        return this;
    }

    public static class ValidationCondition {
        Set<String> conditions = new HashSet<>();
        Set<String> excludeOn = new HashSet<>();
        Class<? extends Validator> validator;

        public ValidationCondition() {
        }


        public ValidationCondition setConditions(Set<String> conditions) {
            if(conditions == null) return this;
            this.conditions = conditions;
            return this;
        }

        public ValidationCondition setExcludeOn(Set<String> excludeOn) {
            if(excludeOn == null) return this;
            this.excludeOn = excludeOn;
            return this;
        }

        public ValidationCondition addExcludeOn(String excludeOn) {
            if(excludeOn == null) return this;
            this.excludeOn.add(excludeOn);
            return this;
        }

        public ValidationCondition setValidator(Class<? extends Validator> validator) {
            if(validator == null) return this;
            this.validator = validator;
            return this;
        }

        public Set<String> getConditions() {
            return conditions;
        }

        public Set<String> getExcludeOn() {
            return excludeOn;
        }

        public Class<? extends Validator> getValidator() {
            return validator;
        }
    }

    List<CreatingError> creatingErrors;

    public static class CreatingError {
        String lvl;
        String title;
        String message;
        String place;
        List<String> defaultActions;

        private CreatingError(String lvl, String title, String message, String place) {
            this.lvl = lvl;
            this.title = title;
            this.message = message;
            this.place = place;
        }

        public static CreatingError error(String title, String message, String place) {
            return new CreatingError("WARN", title, message, place);
        }

        public static CreatingError error(String lvl, String title, String message, String place) {
            return new CreatingError(lvl, title, message, place);
        }

        public CreatingError action(String str) {
            if (defaultActions == null) defaultActions = new ArrayList<>();
            defaultActions.add(str);
            return this;
        }

        @Override
        public String toString() {
            String format = String.format(
                    "[%s], #%s (%s)# in {%s}.", lvl, title, message, place
            );
            StringBuilder sb = new StringBuilder(format);
            for (String defaultAction : defaultActions) {
                sb.append("\t- ").append(defaultAction).append(";");
            }
            return sb.toString();
        }
    }

    public Decomposed setCreatingErrors(List<CreatingError> creatingErrors) {
        this.creatingErrors = creatingErrors;
        return this;
    }

    public void addCreatingError(CreatingError error) {
        if(error == null) return;
        if (creatingErrors == null) creatingErrors = new ArrayList<>();
        creatingErrors.add(error);
    }

    Set<Class<? extends Annotation>> dmv;

    public Set<Class<? extends Annotation>> getDmv() {
        return dmv;
    }

    public Decomposed addDmv(Class<? extends Annotation> dmv) {
        if(this.dmv == null) this.dmv = new HashSet<>();
        this.dmv.add(dmv);
        return this;
    }

    public Decomposed setDmv(Set<Class<? extends Annotation>> dmv) {
        this.dmv = dmv;
        return this;
    }

    public Object getValidationObject() {
        return validationObject;
    }

    public Strategy getStrategy() {
        return strategy;
    }

    public ValidatorsManager getVman() {
        return vman;
    }

    public List<CreatingError> getCreatingErrors() {
        return creatingErrors;
    }

    public static Decomposed newInstance(){
        Decomposed decomposed = new Decomposed();

        decomposed.vman = new ValidatorsManager();
        decomposed.creatingErrors = new ArrayList<>();
        decomposed.dmv = new HashSet<>();
        return decomposed;
    }
}

