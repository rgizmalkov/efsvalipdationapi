package efs.validation.core;

import efs.validation.decompose.DecomposedMapParser;
import efs.validation.decompose.DecomposedRs;

import java.util.Map;

public interface DecomposedClassService {

    Map<String, DecomposedRs> decompose(Class<?> cls);
}
