package com.gmail.rgizmalkov.dev.validation;

import com.gmail.rgizmalkov.dev.enumeration.ArrayPrincipe;
import com.gmail.rgizmalkov.dev.register.AnnotationInstruction;
import efs.validation.util.ObjectUtil;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static efs.validation.util.ObjectUtil.*;

public class RecursiveDecomposeObjectService {

    private DecomposeService decomposeService;
    private ArrayPrincipe arrayPrincipe = ArrayPrincipe.INNER;

    public RecursiveDecomposeObjectService(DecomposeService decomposeService){
        this.decomposeService = decomposeService;
    }
    RecursiveDecomposeObjectService(DecomposeService decomposeService, ArrayPrincipe arrayPrincipe){
        this.decomposeService = decomposeService;
        this.arrayPrincipe = arrayPrincipe;
    }

    public static class AnnotationObject{
        Annotation annotation;
        AnnotationInstruction annotationInstruction;

        public AnnotationObject(Annotation annotation, AnnotationInstruction annotationInstruction) {
            this.annotation = annotation;
            this.annotationInstruction = annotationInstruction;
        }

        public Annotation getAnnotation() {
            return annotation;
        }

        public AnnotationInstruction getAnnotationInstruction() {
            return annotationInstruction;
        }
    }

    public static class DecomposedObject{
        Class<?> objectClass;
        Object object;
        AnnotationObject[] validationMarkedAnnotations;
        Annotation[] dmvMarkedAnnotations;

        DecomposedObject() {
        }

        DecomposedObject setObjectClass(Class<?> objectClass) {
            this.objectClass = objectClass;
            return this;
        }

        DecomposedObject setObject(Object object) {
            this.object = object;
            return this;
        }

        public DecomposedObject setValidationMarkedAnnotations(AnnotationObject[] validationMarkedAnnotations) {
            this.validationMarkedAnnotations = validationMarkedAnnotations;
            return this;
        }

        DecomposedObject setDmvMarkedAnnotations(Annotation[] dmvMarkedAnnotations) {
            this.dmvMarkedAnnotations = dmvMarkedAnnotations;
            return this;
        }
    }


    public static class DecomposedPackage{
        Class<?> baseClass;
        DecomposedObject[] decomposedObjects;

        DecomposedPackage(Class<?> baseClass, DecomposedObject[] decomposedObjects) {
            this.baseClass = baseClass;
            this.decomposedObjects = decomposedObjects;
        }

        public Class<?> getBaseClass() {
            return baseClass;
        }

        public DecomposedObject[] getDecomposedObjects() {
            return decomposedObjects;
        }
    }

    List<DecomposedPackage> recursiveDecompose(/*NonNull*/Object o){
        return recursiveDecompose(o, null);
    }

    List<DecomposedPackage> recursiveDecompose(/*NonNull*/Object o, List<DecomposedPackage> listDecomposedObjects){
        if(o == null) return null;
        if(listDecomposedObjects == null) listDecomposedObjects = new ArrayList<>();


        Class<?> objectClass = o.getClass();

        Field[] declaredFields = objectClass.getDeclaredFields();
        Object[] declaredObjects = ObjectUtil.getObjectsByFiled(declaredFields, o);
        DecomposedObject[] decomposedObjects = decompose(declaredFields, declaredObjects);
        listDecomposedObjects.add(new DecomposedPackage(objectClass, decomposedObjects));

        for (int f = 0; f < declaredFields.length; f++) {
            Field declaredField = declaredFields[f];
            Object declaredObject = declaredObjects[f];

            boolean primitive = ObjectUtil.isPrimitive(declaredField.getType());
            boolean isArray = isArray(declaredObject, declaredField);
            if (!primitive && !isArray) {
                listDecomposedObjects = recursiveDecompose(declaredObject, listDecomposedObjects);
            }else if(isArray){
                switch (arrayPrincipe){
                    case INNER:
                        listDecomposedObjects = innerArrayDecompose(declaredObject, declaredField, listDecomposedObjects);
                        break;
                    default: listDecomposedObjects = outerArrayDecompose(declaredObject, declaredField, listDecomposedObjects);
                }
            }
        }
        return listDecomposedObjects;
    }

    private List<DecomposedPackage> outerArrayDecompose(Object o, Field field, List<DecomposedPackage> decomposedPackages) {
        decomposedPackages.add(new DecomposedPackage(field.getDeclaringClass(), new DecomposedObject[]{decomposeService.decompose(field).setObject(o)}));
        return decomposedPackages;
    }

    private List<DecomposedPackage> innerArrayDecompose(Object o, Field field, List<DecomposedPackage> decomposedPackages) {
        decomposedPackages = outerArrayDecompose(o, field, decomposedPackages);
        Iterable iterable = null;
        try {
            iterable = castToIterable(o);
        }catch(Exception e){
            /*NuN*/
        }
        if (iterable != null) {
            for (Object innerObj : iterable) {
                decomposedPackages = recursiveDecompose(innerObj, decomposedPackages);
            }
        }
        return decomposedPackages;
    }



    private DecomposedObject[] decompose(Field[] fields, Object[] objarr){
        DecomposedObject[] decomposedObjects = new DecomposedObject[fields.length];
        for (int d = 0; d < fields.length; d++) {
            DecomposedObject decompose = decomposeService.decompose(fields[d]);
            decompose.setObject(objarr[d]);
            decomposedObjects[d] = decompose;
        }
        return decomposedObjects;
    }



}
