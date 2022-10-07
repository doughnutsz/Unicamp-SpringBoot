package com.perfectmatch.unicampspringboot.utils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class MyUtils {
    public static Map<String, Object> Object2Map(Object x) {
        if (x == null) return null;
        Map<String, Object> res = new HashMap<>();
        Field[] fields = x.getClass().getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                res.put(field.getName(), field.get(x));
            } catch (Exception e) {
                System.out.println("no getter");
            }
        }
        return res;
    }
}
