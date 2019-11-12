package com.zss.myspringboot.service;

import com.zss.myspringboot.entity.BaseEntity;
import com.zss.myspringboot.entity.Code;
import com.zss.myspringboot.model.Message;
import com.zss.myspringboot.module.condition.ui.ConditionModel;
import com.zss.myspringboot.util.PaginationSupport;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Map;



public interface CodeService  extends EntityService<Code,Integer>{
    public /*Message<Page<Code>> */  Page<Code>  queryPage1(ConditionModel conditionModel);
    public Page<Code> findAll(int pageNumble ,int pageSize/* Sort.Direction.ASC, "id"*/);
    public Page<Code> findByCode();
    public Page<Code> findByName();
    public Page<Code> findBylabel();
    public int selected(int id,boolean selected);

}
