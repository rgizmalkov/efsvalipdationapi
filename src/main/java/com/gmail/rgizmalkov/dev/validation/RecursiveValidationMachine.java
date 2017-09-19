package com.gmail.rgizmalkov.dev.validation;

import com.gmail.rgizmalkov.dev.facade.KVStorage;
import com.gmail.rgizmalkov.dev.ival.Status;
import com.gmail.rgizmalkov.dev.ival.ValidationMachine;
import com.gmail.rgizmalkov.dev.ival.ValidationResponse;
import com.gmail.rgizmalkov.dev.register.Register;
import com.gmail.rgizmalkov.dev.register.RegistrationService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RecursiveValidationMachine implements ValidationMachine {

    private RecursiveDecomposeObjectService decomposeObjectService;
    private ValidationExecutor validationExecutor = new BaseValidationExecutor();
    private DynamicObjectManager dynamicObjectManager = new BaseDynamicObjectManager();

    RecursiveValidationMachine(KVStorage storage, Register register) {
        this.decomposeObjectService = new RecursiveDecomposeObjectService(new BaseDecomposeService(new RegistrationService(storage, register)));
    }

    @Override
    public List<ValidationResponse> validation(Object o) {
        if (o == null) return Collections.singletonList(
                new ValidationResponse("Nullable root object!", "The root object is null, cannot validate object!", Status.FAILED)
        );

//        List<RecursiveDecomposeObjectService.DecomposedPackage> decomposedPackages = decomposeObjectService.recursiveDecompose(o, new ArrayList<>());
//        List<ValidationResponse> validationResponses = new ArrayList<>(decomposedPackages.size() * 4);
//        List<RecursiveDecomposeObjectService.DecomposedObject> toChange = new ArrayList<>();
//
//        for (RecursiveDecomposeObjectService.DecomposedPackage decomposedPackage : decomposedPackages) {
//            RecursiveDecomposeObjectService.DecomposedObject[] decomposedObjects = decomposedPackage.decomposedObjects;
//            for (RecursiveDecomposeObjectService.DecomposedObject decomposedObject : decomposedObjects) {
//                validationResponses.add(validationExecutor.validate(decomposedObject));
//                if (decomposedObject.dmvMarkedAnnotations != null && decomposedObject.dmvMarkedAnnotations.length > 0 && decomposedObject.dmvMarkedAnnotations[0] != null) {
//                    toChange.add(decomposedObject);
//                }
//            }
//        }
//
//        for (RecursiveDecomposeObjectService.DecomposedObject decomposedObject : toChange) {
//            dynamicObjectManager.change(decomposedObject);
//        }
//
//
//        return validationResponses;
        return null;
    }

}
