package com.gmail.rgizmalkov.dev.tspace;

import com.gmail.rgizmalkov.dev.meta.Default;
import com.gmail.rgizmalkov.dev.meta.NonNull;
import com.gmail.rgizmalkov.dev.meta.Pattern;
import lombok.ToString;

@ToString
public class IdentityDocument3 {

    @NonNull
    private String str;
    @NonNull @Default(key = "str1.key")
    private String str1;
    @Pattern(value = "^\\d{2}\\.\\d{2}\\.\\d{2}$")
    private String str2;
    private String str3;

    private String str4;


    public IdentityDocument3(String str, String str1, String str2, String str3, String str4) {
        this.str = str;
        this.str1 = str1;
        this.str2 = str2;
        this.str3 = str3;
        this.str4 = str4;
    }

}
