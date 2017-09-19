package com.gmail.rgizmalkov.dev.tspace;

import com.gmail.rgizmalkov.dev.meta.NonNull;
import com.gmail.rgizmalkov.dev.meta.Set;

public class Person {
    @NonNull @Set("Kolya")
    private String firstName;
    private String secondName;
    private String lastName;

    public Person(String firstName, String secondName, String lastName) {
        this.firstName = firstName;
        this.secondName = secondName;
        this.lastName = lastName;
    }


}
