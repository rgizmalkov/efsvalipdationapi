package efs.validation.machine.vo;

import com.google.common.base.Objects;

import java.util.ArrayList;
import java.util.List;

public class NamedObject {

    String name;
    List<String> alias = new ArrayList<>();

    public NamedObject(String name) {
        this.name = name;
    }

    public NamedObject addAlias(String s){
        if(s == null) return this;
        alias.add(s);
        return this;
    }

    public String getName() {
        return name;
    }

    public List<String> getAliases() {
        return alias;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NamedObject that = (NamedObject) o;
        return Objects.equal(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }
}
