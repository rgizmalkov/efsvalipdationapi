package efs.validation.core.meta.map;

import efs.validation.core.meta.Composite;
import efs.validation.core.meta.Condition;
import efs.validation.core.meta.RelatedValidator;
import efs.validation.core.meta.RelatedValidators;
import efs.validation.core.meta.stratgy.ObjectStrategy;

import java.lang.annotation.*;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface DecomposedElement {
    String[] fields();
    RelatedValidator[] validators() default {};
    Class<? extends Annotation>[] validationAnnotations() default {};
    Condition[] conditions() default {};
    Class<? extends Annotation> strategy() default ObjectStrategy.class;
}
