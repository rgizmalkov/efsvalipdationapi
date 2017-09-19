package efs.validation.machine.vo;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DecomposedMap {
    Map<Class<? extends Annotation>, List<Annotation>>  validationMarkedAnnotations;
    Map<Class<? extends Annotation>, List<Annotation>>  dmvMarkedAnnotations;
    Map<Class<? extends Annotation>, List<Annotation>>  behaviourAnnotations;

    public DecomposedMap() {
    }

    public Map<Class<? extends Annotation>, List<Annotation>> getValidationMarkedAnnotations() {
        return validationMarkedAnnotations;
    }

    public DecomposedMap setValidationMarkedAnnotations(Map<Class<? extends Annotation>, List<Annotation>> validationMarkedAnnotations) {
        this.validationMarkedAnnotations = validationMarkedAnnotations;
        return this;
    }

    public Map<Class<? extends Annotation>, List<Annotation>> getDmvMarkedAnnotations() {
        return dmvMarkedAnnotations;
    }

    public DecomposedMap setDmvMarkedAnnotations(Map<Class<? extends Annotation>, List<Annotation>> dmvMarkedAnnotations) {
        this.dmvMarkedAnnotations = dmvMarkedAnnotations;
        return this;
    }

    public Map<Class<? extends Annotation>, List<Annotation>> getBehaviourAnnotations() {
        return behaviourAnnotations;
    }

    public DecomposedMap setBehaviourAnnotations(Map<Class<? extends Annotation>, List<Annotation>> behaviourAnnotations) {
        this.behaviourAnnotations = behaviourAnnotations;
        return this;
    }

    @SuppressWarnings("Duplicates")
    public void validation(Class<? extends Annotation> acls, Annotation annotation) {
        if(validationMarkedAnnotations == null) validationMarkedAnnotations = new HashMap<>();
        boolean baccsl = validationMarkedAnnotations.containsKey(acls);
        if(baccsl){
            validationMarkedAnnotations.get(acls).add(annotation);
        }else {
            List<Annotation> annotations = new ArrayList<>();
            annotations.add(annotation);
            validationMarkedAnnotations.put(acls, annotations);
        }
    }

    @SuppressWarnings("Duplicates")
    public void dmv(Class<? extends Annotation> acls, Annotation annotation) {
        if(dmvMarkedAnnotations == null) dmvMarkedAnnotations = new HashMap<>();
        boolean baccsl = behaviourAnnotations.containsKey(acls);
        if(baccsl){
            dmvMarkedAnnotations.get(acls).add(annotation);
        }else {
            List<Annotation> annotations = new ArrayList<>();
            annotations.add(annotation);
            dmvMarkedAnnotations.put(acls, annotations);
        }
    }

    @SuppressWarnings("Duplicates")
    public void behaviour(Class<? extends Annotation> acls, Annotation annotation) {
        if(behaviourAnnotations == null) behaviourAnnotations = new HashMap<>();
        boolean baccsl = behaviourAnnotations.containsKey(acls);
        if(baccsl){
            behaviourAnnotations.get(acls).add(annotation);
        }else {
            List<Annotation> annotations = new ArrayList<>();
            annotations.add(annotation);
            behaviourAnnotations.put(acls, annotations);
        }
    }
}