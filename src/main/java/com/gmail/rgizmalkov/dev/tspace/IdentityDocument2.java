package com.gmail.rgizmalkov.dev.tspace;

import com.gmail.rgizmalkov.dev.meta.Default;
import com.gmail.rgizmalkov.dev.meta.Exclude;
import com.gmail.rgizmalkov.dev.meta.NonNull;
import com.gmail.rgizmalkov.dev.meta.Pattern;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
@NonNull
public class IdentityDocument2 {

    private String str;
    @Default(key = "str1.key")
    private String str1;
    @Pattern(value = "^\\d{2}\\.\\d{2}\\.\\d{2}$")
    private String str2;

    private String str3;

    @Exclude
    private String str4;

    private Person person;

    public IdentityDocument2(String str, String str1, String str2, String str3, String str4) {
        this.str = str;
        this.str1 = str1;
        this.str2 = str2;
        this.str3 = str3;
        this.str4 = str4;
    }

    public IdentityDocument2 setPerson(Person person) {
        this.person = person;
        return this;
    }
}
