package efs.validation.machine.vo;

import efs.validation.core.Decomposed;
import efs.validation.core.meta.Default;
import efs.validation.core.meta.naming.Naming;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class DecomposedObject {
    Map<NamedObject, DecomposedObject> decomposedObjectMap = new HashMap<>();

    NamedObject namedObject;

    DecomposedObject root;

    Field field;

    //нужно для массивов
    Class<?> validationObjectClass;

    Object vo;

    Decomposed decomposed;

    Default defaultValue;

    boolean isArray = false;

    public Map<NamedObject, DecomposedObject> getDecomposedObjectMap() {
        return decomposedObjectMap;
    }

    public DecomposedObject setDecomposedObjectMap(Map<NamedObject, DecomposedObject> decomposedObjectMap) {
        this.decomposedObjectMap = decomposedObjectMap;
        return this;
    }

    public DecomposedObject addBranch(DecomposedObject decomposedObject){
        decomposedObjectMap.put(decomposedObject.getNamedObject(), decomposedObject);
        return this;
    }

    public DecomposedObject getRoot() {
        return root;
    }

    public DecomposedObject setRoot(DecomposedObject root) {
        this.root = root;
        return this;
    }

    public Field getField() {
        return field;
    }

    public DecomposedObject setField(Field field) {
        this.field = field;
        this.namedObject = naming(field);
        return this;
    }

    private NamedObject naming(/*NonNull*/Field field) {
        Naming naming = field.getAnnotation(Naming.class);
        return new NamedObject(field.getName()).addAlias(naming != null ? naming.value() : null);
    }

    public Object getVo() {
        return vo;
    }

    public DecomposedObject setVo(Object vo) {
        this.vo = vo;
        return this;
    }

    public Decomposed getDecomposed() {
        return decomposed;
    }

    public DecomposedObject setDecomposed(Decomposed decomposed) {
        this.decomposed = decomposed;
        return this;
    }

    public boolean isArray() {
        return isArray;
    }

    public DecomposedObject setArray(boolean array) {
        isArray = array;
        return this;
    }

    public NamedObject getNamedObject() {
        return namedObject;
    }

    public DecomposedObject setNamedObject(NamedObject namedObject) {
        this.namedObject = namedObject;
        return this;
    }

    public Default getDefaultValue() {
        return defaultValue;
    }

    public DecomposedObject setDefaultValue(Default defaultValue) {
        this.defaultValue = defaultValue;
        return this;
    }

    public Class<?> getValidationObjectClass() {
        return validationObjectClass;
    }

    public DecomposedObject setValidationObjectClass(Class<?> validationObjectClass) {
        this.validationObjectClass = validationObjectClass;
        return this;
    }
}
