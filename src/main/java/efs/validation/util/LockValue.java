package efs.validation.util;

//not thread-safe
public final class LockValue<T> {
    private T value;
    private boolean lock;

    private LockValue(){}

    public static <T> LockValue<T>  newInstance() {
        return new LockValue<T>();
    }

    public T lock(T value){
        if(lock) return this.value;
        else {
            this.value = value;
            lock = true;
            return this.value;
        }
    }

    public T get(){
        return value;
    }

}
