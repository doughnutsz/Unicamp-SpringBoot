package com.perfectmatch.unicampspringboot.utils;

import java.lang.reflect.Field;
import java.util.*;

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

    public static LinkedHashMap<Long, Integer> sortMap(HashMap<Long, Integer> oldMap) {
        Set<Map.Entry<Long, Integer>> set = oldMap.entrySet();
        ArrayList<Map.Entry<Long, Integer>> arrayList = new ArrayList<>(set);
        Collections.sort(arrayList, (arg0, arg1) -> arg1.getValue() - arg0.getValue());
        LinkedHashMap<Long, Integer> map = new LinkedHashMap<>();
        for (int i = 0; i < arrayList.size(); i++) {
            Map.Entry<Long, Integer> entry = arrayList.get(i);
            map.put(entry.getKey(), entry.getValue());
        }
        return map;
    }

}
