package com.zss.myspringboot.util;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

//泛型相关的工具
public class TUtil {
    public static <T> Class<T> getClassT(Object obj){
        Class<T> entityClass=null;
        Type t=obj.getClass().getGenericSuperclass();
        if(t instanceof ParameterizedType){
            Type[] p=((ParameterizedType)t).getActualTypeArguments();
            entityClass=(Class <T>)p[0];
        }
        return entityClass;
    }
}
