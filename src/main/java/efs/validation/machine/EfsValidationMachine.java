package efs.validation.machine;

import com.gmail.rgizmalkov.dev.ival.ValidationMachine;
import com.gmail.rgizmalkov.dev.ival.ValidationResponse;
import efs.validation.documents.CreateDeclaredDocument;
import efs.validation.machine.vo.DecomposedObject;
import org.omg.CORBA.PRIVATE_MEMBER;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public class EfsValidationMachine implements ValidationMachine {

    @Autowired
    private EfsDecomposeObjectService decomposeObjectService = new EfsDecomposeObjectService();

    @Autowired
    private CreateDeclaredDocument declaredDocument = new CreateDeclaredDocument();

    @Override
    public List<ValidationResponse> validation(Object o) {
        DecomposedObject petal = decomposeObjectService.decompose(o);
        try {
            declaredDocument.createDocumentation(petal);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
