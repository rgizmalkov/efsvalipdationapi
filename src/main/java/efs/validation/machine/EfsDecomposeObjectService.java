package efs.validation.machine;

import com.gmail.rgizmalkov.dev.enumeration.ArrayPrincipe;
import efs.validation.util.ObjectUtil;
import efs.validation.core.DecomposeObjectService;
import efs.validation.core.DecomposeService;
import efs.validation.core.DecomposedClassService;
import efs.validation.decompose.DecomposedMapParser;
import efs.validation.decompose.DecomposedRs;
import efs.validation.machine.vo.DecomposedObject;
import efs.validation.machine.vo.NamedObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import static efs.validation.util.ObjectUtil.castToArray;
import static efs.validation.util.ObjectUtil.isArray;
import static efs.validation.util.ObjectUtil.isPrimitive;

@Component
public class EfsDecomposeObjectService implements DecomposeObjectService<DecomposedObject> {

    @Autowired
    private DecomposeService decomposeService = new EfsDecomposeService();

    @Autowired
    private DecomposedClassService decomposedClassService = new DecomposedMapParser();

    private ArrayPrincipe arrayPrincipe = ArrayPrincipe.INNER;

    @Override
    public DecomposedObject decompose(Object root) {
        return recursiveTreeDecompose(root, null, null);
    }

    private DecomposedObject recursiveTreeDecompose(/*NonNull*/Object o, DecomposedObject localRoot, Map<String, DecomposedRs> decomposeClassMap) {
        if (o == null) return null;
        if (localRoot == null) localRoot = new DecomposedObject().setVo(o);

        Class<?> objectClass = o.getClass();
        //Поле - валидаторы + условия + исключения + стратегии валидирования поля
        if(decomposeClassMap == null) decomposeClassMap = decomposedClassService.decompose(objectClass);

        Field[] declaredFields = objectClass.getDeclaredFields();
        Object[] declaredObjects = ObjectUtil.getObjectsByFiled(declaredFields, o);

        for (int f = 0; f < declaredFields.length; f++) {
            DecomposedObject decomposedObject = null;

            Field declaredField = declaredFields[f];
            Object declaredObject = declaredObjects[f];

            DecomposedRs decomposed = tryToFindDecomposed(decomposeClassMap, localRoot);

            boolean primitive = isPrimitive(declaredField.getType());
            boolean isArray = isArray(declaredObject, declaredField);
            if (!primitive && !isArray) {
                DecomposedObject localDecomposedObject = new DecomposedObject().setVo(o).setRoot(localRoot).setField(declaredField);
                if(decomposed != null){
                    localDecomposedObject.setDecomposed(decomposed.getDecomposed()).setDefaultValue(decomposed.getDefaultValue());
                }
                decomposedObject = recursiveTreeDecompose(declaredObject, localDecomposedObject, null);
            } else if (isArray) {
                switch (arrayPrincipe) {
                    case INNER:
                        decomposedObject = innerArrayDecompose(localRoot, o, declaredObject, declaredField, decomposed);
                        break;
                    default:
                        decomposedObject = outerArrayDecompose(localRoot, o, declaredField, decomposed);
                        break;
                }
            } else {
                decomposedObject = new DecomposedObject().setVo(o).setRoot(localRoot).setField(declaredField);
                if(decomposed != null){
                    decomposedObject.setDecomposed(decomposed.getDecomposed()).setDefaultValue(decomposed.getDefaultValue());
                }
            }
            localRoot.addBranch(decomposeService.reachDecomposeObject(decomposedObject));
        }
        return localRoot;
    }

    private DecomposedRs tryToFindDecomposed(Map<String, DecomposedRs> decomposeClassMap, DecomposedObject localRoot) {
        NamedObject namedObject = localRoot.getNamedObject();
        if(namedObject == null) return null;

        //поиск по имени поля
        DecomposedRs byName = decomposeClassMap.get(namedObject.getName());
        if(byName != null) return byName;

        //поиск по псевдонимам
        List<String> aliases = namedObject.getAliases();
        for (String alias : aliases) {
            DecomposedRs byAlias = decomposeClassMap.get(alias);
            if(byAlias != null) return byAlias;
        }
        return null;
    }

    private DecomposedObject outerArrayDecompose(DecomposedObject localRoot, Object o, Field field, DecomposedRs decomposed) {
        return new DecomposedObject().setVo(o).setRoot(localRoot).setField(field);
    }

    private DecomposedObject innerArrayDecompose(DecomposedObject localRoot, Object o, Object declaredObject, Field field, DecomposedRs decomposed) {
        DecomposedObject decomposedObject = outerArrayDecompose(localRoot, o, field, decomposed);
        Object[] objects = castToArray(declaredObject);
        if (objects != null) {
            Class<? extends Object[]> ocls = objects.getClass();
            Class<?> componentType = ocls.getComponentType();
            Map<String, DecomposedRs> decomposeMap = decomposedClassService.decompose(componentType);
            for (Object innerObj : objects) {
                DecomposedObject decomposedObjectBranch = new DecomposedObject().setRoot(decomposedObject).setVo(innerObj).setField(field).setArrayElement(true);
                if (innerObj != null) {
                    decomposedObject.addBranch(recursiveTreeDecompose(innerObj, decomposedObjectBranch, decomposeMap));
                } else {
                    decomposedObject.addBranch(decomposedObjectBranch);
                }
            }
        }
        return decomposedObject;
    }


}
