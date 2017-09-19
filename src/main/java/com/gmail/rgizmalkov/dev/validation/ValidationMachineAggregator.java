package com.gmail.rgizmalkov.dev.validation;

import com.gmail.rgizmalkov.dev.enumeration.ArrayPrincipe;
import com.gmail.rgizmalkov.dev.errors.InitializingValidationMachineRuntimeException;
import com.gmail.rgizmalkov.dev.facade.KVStorage;
import com.gmail.rgizmalkov.dev.ival.ValidationMachine;
import com.gmail.rgizmalkov.dev.enumeration.ValidationPrincipe;
import com.gmail.rgizmalkov.dev.ival.ValidationResponse;
import com.gmail.rgizmalkov.dev.register.Register;

import java.util.List;

import static java.lang.String.format;

/*BEAN*/
public class ValidationMachineAggregator {
    //Instructions
    private ValidationPrincipe validationPrincipe = ValidationPrincipe.RECURSIVE;
    private ArrayPrincipe arrayPrincipe = ArrayPrincipe.INNER;
    //End of instructions

    private KVStorage storage;
    private Register register;

    public List<ValidationResponse> validation(Object o){
        return getValidationMachineAggregator().validation(o);
    }


    private ValidationMachine getValidationMachineAggregator(){
        switch (validationPrincipe){
            case RECURSIVE: return new RecursiveValidationMachine(storage, register);
        }
        throw new InitializingValidationMachineRuntimeException(format("Can not found validation machine for principe = %s", validationPrincipe));
    }
}
