package efs.validation.example;

import efs.validation.util.ObjectUtil;

import java.lang.reflect.Array;
import java.lang.reflect.Field;

public final class ValidationMapStaticCreator {

    private ValidationMapStaticCreator(){}

    //Везед должен быть конструктор по умолчанию!
    public static Object create(Class cls){
        try {
            return create(cls, cls.newInstance());
        } catch (IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static Object create(Class cls, Object root) throws IllegalAccessException, InstantiationException {
        if(root == null) root = cls.newInstance();
        Field[] fields = root.getClass().getDeclaredFields();

        for (Field field : fields) {
            Class<?> type = field.getType();
            Object set = null;

            boolean primitive = ObjectUtil.isPrimitive(type);
            boolean array = ObjectUtil.isArray(set, field);
            if(!primitive && !array){
                set = type.newInstance();
                set = create(type, (Object) set);
            }else if(array){
                Class<?> componentType = type.getComponentType();
                Object o = Array.newInstance(componentType, 1);
                Object arrayElementInstance = componentType.newInstance();
                Object set1 =  create(componentType, arrayElementInstance);
                Array.set(o, 0, set1);
                set = o;
            }else {
                if(type.isEnum()){
                    Object[] enumConstants = type.getEnumConstants();
                    set = enumConstants[0];
                }else {
                    set = type.newInstance();
                }
                set = setDefault(set);
            }
            field.set(root, set);
        }
        return root;
    }

    private static Object setDefault(Object object){
        Class<?> aClass = object.getClass();
        switch (aClass.getSimpleName()){
            case "Integer": object = 100; break;
            case "Short": object = 1; break;
            case "Byte": object = -1; break;
            case "Long": object = 400; break;
            case "Double": object = 500.01; break;
            case "Float": object = 101.01; break;
            case "String": object = "test"; break;
        }
        return object;
    }


    public static void main(String[] args) throws IllegalAccessException, InstantiationException {
        Class<Person> personClass = Person.class;
        Person person = personClass.newInstance();
        create(personClass, person);
        System.out.println(person);
    }
}
