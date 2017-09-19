package efs.validation.example;

import lombok.ToString;

@ToString
public class IdentityDocument {

    String seria;
    String number;

    public IdentityDocument(String seria, String number) {
        this.seria = seria;
        this.number = number;
    }
}
