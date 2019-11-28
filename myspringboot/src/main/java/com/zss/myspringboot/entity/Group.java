package com.zss.myspringboot.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.zss.myspringboot.SerializerUtil.CodeSerializer;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "sub_group")
public class Group extends BaseEntity  {



    @ManyToMany
    @JoinTable(name = "group_code",
            joinColumns = @JoinColumn(name = "g_id",referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "c_id", referencedColumnName = "id") )


    //1、关系维护端，负责多对多关系的绑定和解除
    //2、@JoinTable注解的name属性指定关联表的名字，joinColumns指定外键的名字，关联到关系维护端(User)
    //3、inverseJoinColumns指定外键的名字，要关联的关系被维护端(Code)
    //4、其实可以不使用@JoinTable注解，默认生成的关联表名称为主表表名+下划线+从表表名，
    //即表名为group_code
    //关联到主表的外键名：主表名+下划线+主表中的主键列名,即
    //关联到从表的外键名：主表中用于关联的属性名+下划线+从表的主键列名,即
    //主表就是关系维护端对应的表，从表就是关系被维护端对应的表

    @JsonSerialize(using = CodeSerializer.class)
    private List<Code> codes;

    public List<Code> getCodes() {
        return codes;
    }

    public void setCodes(List<Code> codes) {
        this.codes = codes;
    }
}
