package efs.validation.example;

import com.gmail.rgizmalkov.dev.impl.NotNullValidator;
import com.gmail.rgizmalkov.dev.meta.Pattern;
import efs.validation.core.meta.Condition;
import efs.validation.core.meta.RelatedValidator;
import efs.validation.core.meta.map.DecomposedElement;
import efs.validation.core.meta.map.DecomposedMap;
import efs.validation.core.meta.stratgy.ObjectStrategy;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@DecomposedMap(
        target = Person.class,
        elements = {
                @DecomposedElement(
                        fields = {"firstName", "lastName"},
                        validators = {@RelatedValidator(NotNullValidator.class)},
                        conditions = {@Condition("FIRST_FLOW"), @Condition("THIRD_FLOW")}
                ),
                @DecomposedElement(
                        fields = {"email"},
                        validationAnnotations = {Pattern.class},
                        conditions = {@Condition("THIRD_FLOW")},
                        strategy = ObjectStrategy.class
                )
        }
)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface CustomPersonValidation {
}
