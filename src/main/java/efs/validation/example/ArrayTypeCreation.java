package efs.validation.example;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ArrayTypeCreation {


    public static void main(String[] args) {
        Object[] list = new IdentityDocument[0];
        Map map = new HashMap();

        System.out.println(map.values());
    }
}
