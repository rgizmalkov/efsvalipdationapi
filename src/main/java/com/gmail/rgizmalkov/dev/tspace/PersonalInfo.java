package com.gmail.rgizmalkov.dev.tspace;

import com.gmail.rgizmalkov.dev.meta.NonNull;
import com.gmail.rgizmalkov.dev.validation.ValidationMachineAggregator;

import java.util.Arrays;
import java.util.List;

public class PersonalInfo {
    String fio;

    List<IdentityDocument3> identityDocuments;

    public PersonalInfo(String fio, List<IdentityDocument3> identityDocuments) {
        this.fio = fio;
        this.identityDocuments = identityDocuments;
    }

    public static void main(String[] args) {
        ValidationMachineAggregator vma = new ValidationMachineAggregator();
        IdentityDocument3 ssd = new IdentityDocument3(null, "13.11.1994", "ssd", "", null);
        IdentityDocument3 ssd1 = new IdentityDocument3("", "13.11.1994", "ssd", "", null);
        IdentityDocument3 ssd2 = new IdentityDocument3(null, "13.11.1994", "ssd", "", "sds");

        PersonalInfo personalInfo = new PersonalInfo("", Arrays.asList(ssd, ssd1, ssd2));

        vma.validation(personalInfo);
    }
}
