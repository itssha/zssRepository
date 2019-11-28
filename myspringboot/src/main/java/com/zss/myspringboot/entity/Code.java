package com.zss.myspringboot.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.stereotype.Controller;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "code")
public class Code extends BaseEntity {

    @Column(name = "code")
    private String code;
//    @Column(name = "name")
//    private String name;
    @Column(name = "name_capital")
    private String nameCapital;
    @Column(name = "url")
    private String url;
    @Column(name = "label")
    private String label;
    @Column(name = "level")
    private Integer level;
    @Column(name = "remarks")
    private String remarks;
    @Column(name = "selected",columnDefinition = " bit default false")
    private boolean selected = false;


   // @JsonIgnore
    @ManyToMany(mappedBy = "codes")
    private List<Group> groups;



    public String getCode() {
        return code;
    }



    public String getUrl() {
        return url;
    }

    public String getLabel() {
        return label;
    }

    public Integer getLevel() {
        return level;
    }

    public String getRemarks() {
        return remarks;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setCode(String code) {
        this.code = code;
    }



    public void setUrl(String url) {
        this.url = url;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }


    public String getNameCapital() {
        return nameCapital;
    }

    public void setNameCapital(String nameCapital) {
        this.nameCapital = nameCapital;
    }


    public List<Group> getGroups() {
        return groups;
    }

    public void setGroups(List<Group> groups) {


        this.groups = groups;
    }


}
