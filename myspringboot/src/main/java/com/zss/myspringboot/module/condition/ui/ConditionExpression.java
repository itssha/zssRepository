package com.zss.myspringboot.module.condition.ui;

import org.apache.commons.beanutils.ConvertUtils;
import org.springframework.util.StringUtils;

public class ConditionExpression {
    public static final String EQ = "eq";            //相等
    public static final String GT = "gt";            //大于
    public static final String GE = "ge";            //大于等于
    public static final String LT = "lt";            //小于
    public static final String LE = "le";            //小于等于
    public static final String LK = "lk";             // like  模糊查询
    public static final String LKS = "lks";           // likestart    like 'aa%'
    public static final String LKE = "lke";           // likeend      like '%aa'
    public static final String IN = "in";             // in            where id  in (..)
    public static final String NEQ = "nq";             //           where id <> 不等于
    public static final String NULL = "nul";          // null         where  id=null
    public static final String NOT_NULL = "nnl";      // not null     where   id is not null
    public static final String START_MATCH = "startMatch";
    public static final String END_MATCH = "endMatch";
    public static final String ANY_MATCH = "anyMatch";
    private String column;
    private String op;
    private String matchMode;
    private Object value;

    public ConditionExpression(String column, String op, Object value) {
        this.column = column;
        this.op = op;
        this.value = value;
    }

    public ConditionExpression(String column, String op, Object value, String matchMode) {
        this.column = column;
        this.op = op;
        this.matchMode = matchMode;
        this.value = value;
    }

    public String getColumn() {
        return this.column;
    }

    public String getOp() {
        return this.op;
    }

    public Object getValue() {
        return this.value;
    }

    public void setColumn(String column) {
        this.column = column;
    }

    public void setOp(String op) {
        this.op = op;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public String getMatchMode() {
        return this.matchMode;
    }

    public void setMatchMode(String matchMode) {
        this.matchMode = matchMode;
    }

    public boolean isEmpty() {
        return !StringUtils.hasText(this.column) || this.value == null;
    }

    public <T> Object getValue(Class<T> valueClazz) {
        return this.value != null && !"".equals(this.value) ? ConvertUtils.convert(this.value, valueClazz) : null;
    }
}
