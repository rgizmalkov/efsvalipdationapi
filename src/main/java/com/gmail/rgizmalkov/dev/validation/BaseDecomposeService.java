package com.gmail.rgizmalkov.dev.validation;

import com.gmail.rgizmalkov.dev.errors.DecomposeServiceException;
import efs.validation.core.meta.RelatedValidator;
import efs.validation.core.meta.DynamicManagedValue;
import com.gmail.rgizmalkov.dev.meta.Validation;
import com.gmail.rgizmalkov.dev.meta.ValidationQualifier;
import com.gmail.rgizmalkov.dev.register.RegistrationService;
import efs.validation.util.ObjectUtil;
import com.gmail.rgizmalkov.dev.validation.RecursiveDecomposeObjectService.AnnotationObject;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
/*BEAN*/
public class BaseDecomposeService implements DecomposeService {

    RegistrationService registrationService;

    public BaseDecomposeService(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @Override
    public RecursiveDecomposeObjectService.DecomposedObject decompose(Field field) {
        if(field == null) throw new DecomposeServiceException("Null field");

        Class<?> declaringClass = field.getType();
        Annotation[] declaredAnnotations = field.getDeclaredAnnotations();

        AnnotationObject[] validationAnnotations = buildValidationAnnotations(declaredAnnotations);
        Annotation[] dmvAnnotations = buildDmvAnnotations(declaredAnnotations);

        return new RecursiveDecomposeObjectService.DecomposedObject()
                .setObjectClass(declaringClass)
                .setValidationMarkedAnnotations(validationAnnotations)
                .setDmvMarkedAnnotations(dmvAnnotations);
    }

    private Annotation[] buildDmvAnnotations(Annotation[] declaredAnnotations) {
        Annotation[] dmvAnnotations = new Annotation[1];

        Annotation setter = null;
        DynamicManagedValue vset = null;
        for (Annotation declaredAnnotation : declaredAnnotations) {
            Class<? extends Annotation> annotationClass = declaredAnnotation.annotationType();

            DynamicManagedValue dynamicManagedValue = annotationClass.getAnnotation(DynamicManagedValue.class);
            if(dynamicManagedValue != null){
                if(setter == null){setter = declaredAnnotation;vset = dynamicManagedValue;}
                else {
                    if(vset.priority() > dynamicManagedValue.priority()){
                        setter = declaredAnnotation;
                        vset = dynamicManagedValue;
                    }
                }
            }
        }

        if(setter != null) dmvAnnotations[0] = setter;

        return dmvAnnotations;
    }

    private AnnotationObject[] buildValidationAnnotations(Annotation[] declaredAnnotations) {
        ArrayList<AnnotationObject> validationAnnotations = new ArrayList<>(10);
        for (Annotation declaredAnnotation : declaredAnnotations) {
            Class<? extends Annotation> annotationClass = declaredAnnotation.annotationType();
            if(validateAnnotation(annotationClass)) {validationAnnotations.add(new AnnotationObject(declaredAnnotation, registrationService.instruction(declaredAnnotation)));continue;}

            boolean annotationPresentByQualifier = annotationClass.isAnnotationPresent(ValidationQualifier.class);

            if(annotationPresentByQualifier) {
                ValidationQualifier validationQualifier = annotationClass.getAnnotation(ValidationQualifier.class);
                Class<? extends Annotation> acls = validationQualifier.annotation();
                boolean isAVal = validateAnnotation(acls);
                if(isAVal) {
                    validationAnnotations.add(new AnnotationObject(ObjectUtil.<Annotation>newInstance((Class<Annotation>) validationQualifier.annotation()), registrationService.instruction(validationQualifier)));
                }
            }

        }

        return validationAnnotations.toArray(new AnnotationObject[validationAnnotations.size()]);
    }


    private boolean validateAnnotation(Class<? extends Annotation> annotationClass){
        boolean annotationPresent = annotationClass.isAnnotationPresent(Validation.class);
        boolean validatorAnnotation = annotationClass.isAnnotationPresent(RelatedValidator.class);
        return annotationPresent && validatorAnnotation;
    }
}
