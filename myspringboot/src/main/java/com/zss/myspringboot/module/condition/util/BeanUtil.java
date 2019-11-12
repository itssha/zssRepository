package com.zss.myspringboot.module.condition.util;

import org.springframework.beans.BeanUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;

public class BeanUtil extends BeanUtils {
    public BeanUtil() {
    }

    public static Field findDeclaredField(Class<?> clazz, String name) {
        try {
            return clazz.getDeclaredField(name);
        } catch (NoSuchFieldException var3) {
            return clazz.getSuperclass() != null ? findDeclaredField(clazz.getSuperclass(), name) : null;
        }
    }

    public static Class<?> findFieldType(Class<?> clazz, String name) {
        if (!StringUtils.hasText(name)) {
            return null;
        } else {
            String property = name;
            String nativeProperty = null;
            int pos = name.indexOf(".");
            if (pos > -1) {
                property = name.substring(0, pos);
                nativeProperty = name.substring(pos + 1);
            }

            Class<?> typeClazz = null;
            Field fld = findDeclaredField(clazz, property);
            if (fld != null) {
                typeClazz = fld.getType();
                if (nativeProperty != null) {
                    if (ParameterizedType.class.isAssignableFrom(fld.getGenericType().getClass())) {
                        typeClazz = (Class)((ParameterizedType)fld.getGenericType()).getActualTypeArguments()[0];
                    }

                    return findFieldType(typeClazz, nativeProperty);
                }
            }

            return typeClazz;
        }
    }

    public static String convertPropertyName(String name) {
        if (!StringUtils.hasText(name)) {
            return null;
        } else {
            String firstLetter = String.valueOf(name.charAt(0)).toLowerCase();
            return firstLetter + name.substring(1);
        }
    }
}
