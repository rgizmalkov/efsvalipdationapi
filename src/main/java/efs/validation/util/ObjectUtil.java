package efs.validation.util;

import com.gmail.rgizmalkov.dev.errors.IterableDecomposeException;
import com.google.common.collect.Iterables;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static java.lang.String.format;

public class ObjectUtil {
    private ObjectUtil(){}

    private static Set<Class<?>> primesClassesSet = new HashSet<>();

    static {
        primesClassesSet.add(String.class);
        primesClassesSet.add(Byte.class);
        primesClassesSet.add(Short.class);
        primesClassesSet.add(Integer.class);
        primesClassesSet.add(Long.class);
        primesClassesSet.add(Float.class);
        primesClassesSet.add(Double.class);
        primesClassesSet.add(Void.class);
        primesClassesSet.add(Enum.class);
    }

    public static boolean isPrimitive(Class<?> o) {
        return o != null && primesClassesSet.contains(o);
    }

    public static <A> A newInstance(Class<A> ex){

        A a = null;
        try {
            a = ex.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            return null;
        }
        return a;
    }

    public static Object getObjectByFiled(Field field, Object baseClass){
        try {
            field.setAccessible(true);
            return field.get(baseClass);
        } catch (IllegalAccessException e) {
            return null;
        }
    }

    public static Object[] getObjectsByFiled(Field[] fields, Object baseClass){
        if(fields == null ||fields.length == 0 || baseClass == null) return null;
        Object[] arr = new Object[fields.length];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = getObjectByFiled(fields[i], baseClass);
        }
        return arr;
    }

    public static Iterable castToIterable(/*NonNull*/Object o){
        if(o == null) return null;
        if(o instanceof Iterable){
            return (Iterable) o;
        }
        if(o instanceof Map){
            Map map = (Map) o;
            return map.values();
        }
        if(o.getClass().isArray()){
            Object[] os = (Object[]) o;
            return Arrays.asList(os);
        }
        throw new IterableDecomposeException(format("Cannot reachDecomposeObject object (%s) = [%s]", o, o.getClass()));
    }

    public static Object[] castToArray(/*NonNull*/Object o) {
        if(o == null) return null;
        if(o instanceof Iterable){
            return Iterables.toArray((Iterable) o, Object.class);
        }
        if(o instanceof Map){
            Map map = (Map) o;
            return map.values().toArray();
        }
        if(o.getClass().isArray()){
            return (Object[]) o;
        }
        return null;
    }

    public static boolean isArray(/*NonNull*/Object cls, Field field){
        return field.getType().isArray() || (cls != null && ((cls instanceof Iterable) || (cls instanceof Map) || cls.getClass().isArray()));
    }
}
