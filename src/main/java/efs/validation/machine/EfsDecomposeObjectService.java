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
import static java.lang.String.format;

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
        if (localRoot == null) localRoot = new DecomposedObject().setVo(o).setValidationObjectClass(o.getClass());

        Class<?> objectClass = o.getClass();
        //Поле - валидаторы + условия + исключения + стратегии валидирования поля
        if(decomposeClassMap == null) decomposeClassMap = decomposedClassService.decompose(objectClass);

        Field[] declaredFields = objectClass.getDeclaredFields();
        Object[] declaredObjects = ObjectUtil.getObjectsByFiled(declaredFields, o);

        for (int f = 0; f < declaredFields.length; f++) {
            DecomposedObject decomposedObject = null;

            Field declaredField = declaredFields[f];
            Object declaredObject = declaredObjects[f];

            DecomposedObject localDecomposedObject = new DecomposedObject().setVo(declaredObject).setRoot(localRoot).setField(declaredField).setValidationObjectClass(declaredField.getType());
            DecomposedRs decomposed = tryToFindDecomposed(decomposeClassMap, localDecomposedObject);
            if(decomposed != null){
                localDecomposedObject.setDecomposed(decomposed.getDecomposed()).setDefaultValue(decomposed.getDefaultValue());
            }
            boolean primitive = isPrimitive(declaredField.getType());
            boolean isArray = isArray(declaredObject, declaredField);
            if (!primitive && !isArray) {
                decomposedObject = recursiveTreeDecompose(declaredObject, localDecomposedObject, null);
            } else if (isArray) {
                switch (arrayPrincipe) {
                    case INNER:
                        decomposedObject = innerArrayDecompose(localDecomposedObject, declaredObject, declaredField);
                        break;
                }
            }else {
                decomposedObject = localDecomposedObject;
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

    /**
     *
     * @param localRoot - корневой объект (сам массив)
     * @param declaredObject - объект массива
     * @param field - LClass
     * @return - рутовый объект с N ветвями, где N - кол-во объектов в массиве
     */
    private DecomposedObject innerArrayDecompose(DecomposedObject localRoot, Object declaredObject, Field field) {
        localRoot.setArray(true);
        //Кастим любой вектор данных в массив
        Object[] objects = castToArray(declaredObject);
        if (objects != null) {
            Class<? extends Object[]> ocls = objects.getClass();
            //Определяем тип элементов массива
            Class<?> componentType = ocls.getComponentType();
            //Определяем для данного типа элемента массива ее классовые аннотации (одинаковые для всех элементов массива)
            //Работа массивов с разными типами элементов (не полиморфными) не поддерживается
            Map<String, DecomposedRs> decomposeMap = decomposedClassService.decompose(componentType);
            int i = 0;
            for (Object innerObj : objects) {
                // innerObj - элемент массива
                DecomposedObject decomposedObjectBranch = new DecomposedObject().setRoot(localRoot).setVo(innerObj).setField(field).setValidationObjectClass(componentType);
                NamedObject beforeNamedObject = decomposedObjectBranch.getNamedObject();
                NamedObject namedObject = new NamedObject(format("%s[%s]", beforeNamedObject.getName(), i++));
                namedObject.addAlias(beforeNamedObject.getName());
                decomposedObjectBranch.setNamedObject(namedObject);
                if (innerObj != null) {
                    localRoot.addBranch(recursiveTreeDecompose(innerObj, decomposedObjectBranch, decomposeMap));
                } else {
                    localRoot.addBranch(decomposedObjectBranch);
                }
            }
        }
        return localRoot;
    }


}
