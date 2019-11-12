package com.zss.myspringboot.module.condition.util;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.ArrayUtils;

import java.lang.reflect.Field;
import java.util.*;

public class MapUtil {

    /**
     * map转化为对象
     * @param map
     * @param beanClass
     * @return
     * @throws Exception
     */
    public static Object mapToObject(Map<String, String> map, Class<?> beanClass) throws Exception {
        if (map == null)
            return null;
        Object obj = beanClass.newInstance();
        BeanUtils.populate(obj, map);
        return obj;
    }

    //对象转换成map
    public static Map<String, String> objectToMap(Object obj){
        Map<String, String> map = new HashMap<String, String>();
        Field[] fields = obj.getClass().getDeclaredFields();
        for(int i = 0, len = fields.length; i < len ; i++){
            String varName = fields[i].getName();
            varName = varName.toLowerCase();
            try {
                boolean accessFlag = fields[i].isAccessible();
                fields[i].setAccessible(true);
                Object o = fields[i].get(obj);
                if(o != null){
                    map.put(varName, o.toString());
                }
                fields[i].setAccessible(accessFlag);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return map;
    }

    /**
     * 将map转换为object，转换全部属性（按照key和对象属性之间的关系进行转换）
     * @param map
     * @param t
     * @param <T>
     * @return
     */
    public static <T> T  mapToObject(Map<String, Object> map, T t) {
        return mapToObject(map, t, null, 0);
    }

    /**
     * 将map转换为object，排除指定key
     * @param map
     * @param t
     * @param keys
     * @param <T>
     * @return
     */
    public static <T, K, V> T  mapToObjectExclude(Map<K, V> map, T t, K[] keys) {
        return mapToObject(map, t, keys, 0);
    }

    /**
     * 将map转换为object，转换指定key
     * @param map
     * @param t
     * @param keys
     * @param <T>
     * @return
     */
    public static <T, K, V> T  mapToObjectInclude(Map<K, V> map, T t, K[] keys) {
        return mapToObject(map, t, keys, 1);
    }

    /**
     * Map中根据key批量删除键值对
     * @param map
     * @param excludeKeys
     * @param <K>
     * @param <V>
     * @return
     */
    public static <K, V> Map removeEntries(Map<K, V> map, K[] excludeKeys) {
        Iterator<K> iterator = map.keySet().iterator();
        while (iterator.hasNext()) {
            K k = iterator.next();
            // 如果k刚好在要排除的key的范围中
            if (ArrayUtils.contains(excludeKeys, k)) {
                iterator.remove();
                map.remove(k);
            }
        }
        return map;
    }

    /**
     * 将map转换为object
     * @param map
     * @param t
     * @param keys
     * @param option 0 需要排除的key；1 需要包含的key
     * @param <T>
     * @return
     */
    public static <T, K, V> T  mapToObject(Map<K, V> map, T t, K[] keys, int option) {
        Class beanClass = t.getClass();
        String[] declaredFieldsName = getDeclaredFieldsName(beanClass);
        Set<K> keySet = new HashSet<K>();
        switch (option) {
            case 0: // 需要排除的key
                if (ArrayUtils.isNotEmpty(keys)) {
                    MapUtil.removeEntries(map, keys);
                }
                keySet = map.keySet();
                break;
            case 1: // 需要包含的key
                keySet = new HashSet<K>(Arrays.asList(keys));
                break;
        }

        for (Object k : keySet) {
            V v = map.get(k);
            if (ArrayUtils.contains(declaredFieldsName, k.toString())) {
                try {
                    Field field = beanClass.getDeclaredField(k.toString());
                    field.setAccessible(true);
                    field.set(t, v);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return t;
    }

    /**
     * 获取转换后的对象的所有属性名称，以字符串数组形式返回
     * @param beanClass
     * @return
     */
    public static String[] getDeclaredFieldsName(Class beanClass) {
        Field[] fields = beanClass.getDeclaredFields();
        int size = fields.length;
        String[] fieldsName = new String[size];
        for (int i = 0; i < size; i++) {
            fieldsName[i] = fields[i].getName();
        }
        return fieldsName;
    }
}

