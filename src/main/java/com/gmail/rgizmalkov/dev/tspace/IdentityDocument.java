package com.gmail.rgizmalkov.dev.tspace;

import com.gmail.rgizmalkov.dev.ival.Format;
import com.gmail.rgizmalkov.dev.meta.Default;
import com.gmail.rgizmalkov.dev.meta.NonNull;
import com.gmail.rgizmalkov.dev.meta.Pattern;
import com.gmail.rgizmalkov.dev.meta.Set;
import lombok.Getter;
import lombok.ToString;
import lombok.Value;

@ToString
public class IdentityDocument {

    @NonNull
    private String str;
    @NonNull @Default(key = "str1.key", format = Format.JSON)
    private String str1;
    @Pattern("^\\d{2}\\.\\d{2}\\.\\d{2}$")
    private String str2;
    private String str3;

    private String str4;


    private Person person;

    public IdentityDocument(String str, String str1, String str2, String str3, String str4) {
        this.str = str;
        this.str1 = str1;
        this.str2 = str2;
        this.str3 = str3;
        this.str4 = str4;
    }

    public IdentityDocument setPerson(Person person) {
        this.person = person;
        return this;
    }
}
