package efs.validation.enums;


public enum ValidationStrategy {
    ONE_FAIL_ALL_DOWN("Если хотя бы одно поле из класса сфейлилось, валидация всего объекта считается неуспешной."),
    ALL_FAIL_ITS_DOWN("Валидация считается неудачной только если все поля объекта сфейлили валидацию.");


    private String description;

    ValidationStrategy(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
