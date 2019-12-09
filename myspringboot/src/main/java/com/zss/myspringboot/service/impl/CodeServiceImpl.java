package com.zss.myspringboot.service.impl;

import com.zss.myspringboot.entity.BaseEntity;
import com.zss.myspringboot.entity.Code;
import com.zss.myspringboot.model.ListParam;
import com.zss.myspringboot.model.Message;
import com.zss.myspringboot.module.condition.ConditionQuery;
import com.zss.myspringboot.module.condition.ui.ConditionExpression;
import com.zss.myspringboot.module.condition.ui.ConditionModel;

import com.zss.myspringboot.module.condition.ui.OrderBy;
import com.zss.myspringboot.module.condition.util.BeanUtil;
import com.zss.myspringboot.repository.CodeRepository;
import com.zss.myspringboot.repository.EntityRepository;
import com.zss.myspringboot.service.CodeService;

import com.zss.myspringboot.util.PaginationSupport;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.Table;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.SingularAttribute;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

@Service
public   class CodeServiceImpl extends EntityServiceImpl<Code> implements CodeService  {
    @Resource
    public CodeRepository codeRepository;



    public Page<Code> findAll(ConditionModel condition){

        return  this.findAll(condition.getPage(),condition.getLimit());
    }




    @Override
    public Page<Code> queryPage1(ConditionModel conditionModel) {
        return null;
    }

    @Override
    public Page<Code> findAll(int pageNumble ,int pageSize) {

            Pageable pageable = PageRequest.of(pageNumble, pageSize /*Sort.Direction.ASC, "id"*/);
            return codeRepository.findAll(pageable);

    }


    @Override
    public Page<Code> findByCode() {

        return null;
    }

    @Override
    public Page<Code> findByName() {
        return null;
    }

    @Override
    public Page<Code> findBylabel() {
        return null;
    }

    @Override
    public Optional<Code> findById(Integer codeId) {

        return codeRepository.findById(codeId);
    }

    @Override
    public int selected(int id,boolean selected) {
        return codeRepository.updateSelected(id,selected);
    }
    @Override
    public int level(int id,int level) {
        return codeRepository.updateLevel(id,level);
    }

    @Override
    public int setLabels(int id, String labels)  {
        return codeRepository.updateLabels(id,labels);
    }

    @Override
    public int setRemarks(int id, String labels) {
        return codeRepository.updateRemarks(id,labels);
    }


    /**
     * 属性名转换为字段名格式(userName -> user_name)
     *
     * @param propertyName
     * @return
     */
    protected String propertyToFieldName(String propertyName) {

        if (StringUtils.isBlank(propertyName)) {
            return null;
        }

        StringBuilder fieldName = new StringBuilder();
        char[] chars = propertyName.toCharArray();
        for (char c : chars) {
            if (CharUtils.isAsciiAlphaUpper(c)) {
                fieldName.append("_").append(StringUtils.lowerCase(CharUtils.toString(c)));

            } else {
                fieldName.append(c);
            }
        }

        return fieldName.toString();
    }


    @Override
    public EntityRepository<Code, Integer> getRepository() {
        return codeRepository;
    }
}
