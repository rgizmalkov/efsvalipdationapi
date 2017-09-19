package efs.validation.documents;

import efs.validation.core.Decomposed;
import efs.validation.machine.vo.DecomposedObject;
import efs.validation.machine.vo.NamedObject;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import static java.lang.String.format;

public class CreateDeclaredDocument {

    public void createDocumentation(DecomposedObject decomposedObject) throws IOException {
        if(decomposedObject == null || decomposedObject.getVo() == null) return;

        String dir = "documentation/validation/" + decomposedObject.getVo().getClass().getSimpleName();
        File file = new File(dir);

        if(!file.exists()){
            boolean mkdir = file.mkdirs();
            System.out.println(mkdir);
        }

        create(dir, decomposedObject);

    }

    public void create(String dir, DecomposedObject object) throws IOException {
        Object vo = object.getVo();
        File file = new File(dir + "/" + vo.getClass().getName());
        BufferedWriter bf = new BufferedWriter(new FileWriter(file));
        StringBuilder sb = new StringBuilder();
        String title = format("Root object [%s].", vo.getClass());
        sb.append(title);

        Map<NamedObject, DecomposedObject> decomposedObjectMap = object.getDecomposedObjectMap();
        int fieldNumber = 1;
        for (Map.Entry<NamedObject, DecomposedObject> entry : decomposedObjectMap.entrySet()) {
            NamedObject namedObject = entry.getKey();
            String col = leftCol(fieldNumber);
            String naming = format("%s. Name: %s\n",fieldNumber, namedObject.getName());
            sb.append(naming).append("\n");
            if(namedObject.getAliases() != null && namedObject.getAliases().get(0) != null)
                sb.append(col).append("Alias: ").append(namedObject.getAliases().get(0)).append("\n");
            else
                sb.append(col).append("Alias: ").append("\n");

            DecomposedObject value = entry.getValue();

            Decomposed decomposed = value.getDecomposed();

            Decomposed.Strategy strategy = decomposed.getStrategy();
            sb.append(col).append("Strategy: ").append(format("%s (%s)", strategy.getValidationStrategy(), strategy.getValidationStrategy().getDescription())).append("\n");
            sb.append(col).append("Events: \n");
            for (Decomposed.Strategy.Event event : strategy.getEvents()) {
                sb.append(col).append(rep(7, " ")).append(format("%s = %s", event.getEventName(), event.getAnnotation()));
            }

            sb.append(col).append("Default: ").append(value.getDefaultValue()).append("\n");
            sb.append(col).append("Validators: \n");
            int j = 1;
            for (Decomposed.ValidationCondition validationCondition : decomposed.getVman().getAll()) {
                String tab = col+ rep(11," ") + "\t";
                String stab = col+ rep(11," ");

                sb.append(stab).append(j++).append(".").append("Class: ").append(validationCondition.getValidator()).append("\n");
                int i = 1;
                sb.append(tab).append("Exclude on: \n");
                for (String excl : validationCondition.getExcludeOn()) {
                    sb.append(tab).append(format("\t%s. %s\n", i++, excl));
                }
                i = 1;
                sb.append(tab).append("Condition: \n");
                for (String excl : validationCondition.getConditions()) {
                    sb.append(tab).append(format("\t%s. %s\n", i++, excl));
                }
            }
            bf.write(sb.toString());
            bf.flush();
            if(value.getDecomposedObjectMap().size() > 0){
                create(dir, value);
            }
        }
        //WRITE TO FILE

    }

    private static String leftCol(int num){
        return rep(String.valueOf(num).length() + 1, " ");
    }

    private static String rep(int num, String pattern){
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < num; i++) {
            sb.append(pattern);
        }
        return sb.toString();
    }

    public static void main(String[] args) throws IOException {
        Object vo = new Object();
        DecomposedObject decomposedObject = new DecomposedObject().setVo(vo);
        CreateDeclaredDocument createDeclaredDocument = new CreateDeclaredDocument();
        createDeclaredDocument.createDocumentation(decomposedObject);
    }

}
