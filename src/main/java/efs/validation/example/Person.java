package efs.validation.example;

import efs.validation.core.meta.naming.Naming;
import efs.validation.machine.EfsValidationMachine;

@CustomPersonValidation
public class Person {
    IdentityDocument[] identityDocument;
    IdentityDocument[] identityDocument2;
    String firstName;
    String middleName;
    String lastName;
    @Naming("personalEmail")
    String email;

    Gender gender;

    public Person(){}

    public Person(IdentityDocument[] identityDocument, IdentityDocument[] identityDocument2, String firstName, String middleName, String lastName) {
        this.identityDocument = identityDocument;
        this.identityDocument2 = identityDocument2;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
    }

    public static void main(String[] args) {
        EfsValidationMachine efsValidationMachine = new EfsValidationMachine();
//        IdentityDocument idoc1 = new IdentityDocument("222","333");
//        IdentityDocument idoc2 = new IdentityDocument("223","332");
//        IdentityDocument idoc3 = null;
//        IdentityDocument[] identityDocuments = {idoc1, idoc2, idoc3};
//        Person person = new Person(
//                identityDocuments, null, "Роман", "Геннадьевич", "Измалков"
//        );
        Object o = ValidationMapStaticCreator.create(Person.class);

        efsValidationMachine.validation(o);
    }
}
