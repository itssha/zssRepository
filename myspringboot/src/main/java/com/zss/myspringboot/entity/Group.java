package com.zss.myspringboot.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "group")
public class Group extends BaseEntity  {



    @ManyToMany
    @JoinTable(name = "group_code",
            joinColumns = @JoinColumn(name = "g_id",referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "c_id", referencedColumnName = "id") )
    private Set<Code> codes;

}
