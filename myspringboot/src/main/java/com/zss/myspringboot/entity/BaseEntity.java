package com.zss.myspringboot.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Date;


@MappedSuperclass //这个MappedSuperclass注解没有的话会有问题
public abstract class BaseEntity  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    protected Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "active",columnDefinition = " bit default true")
    protected Boolean active=true;

    //@JsonIgnore
    @Column(name = "create_time",nullable = false)
    protected Date createTime;

    //@JsonIgnore
    @Column(name = "modify_time")
    protected Date modifyTime;

    //@JsonIgnore
    @Column(name = "delete_time")
    protected Date deleteTime;

    //@Override
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    public Date getDeleteTime() {
        return deleteTime;
    }

    public void setDeleteTime(Date deleteTime) {
        this.deleteTime = deleteTime;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
