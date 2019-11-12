package com.zss.myspringboot.model;

import java.util.ArrayList;
import java.util.List;

public class ListParam<T> {
    private List<T> list=new ArrayList<>();
    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public ListParam(List<T> list) {
        super();
        this.list = list;
    }

    public ListParam() {
        super();
    }

    @Override
    public String toString() {
        return "ListParam{" +
                "list=" + list +
                '}';
    }
}
