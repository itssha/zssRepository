package com.zss.myspringboot.controller;

import com.zss.myspringboot.entity.Code;
import com.zss.myspringboot.entity.Group;
import com.zss.myspringboot.model.Message;
import com.zss.myspringboot.module.condition.ui.ConditionModel;
import com.zss.myspringboot.service.CodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;


@RestController
@RequestMapping(value="/code")
public class CodeController {
   @Resource
    private CodeService codeService;
    @RequestMapping(value="/queryPage", method=RequestMethod.POST)
    public Message<Page<Code>>   /*Page<Code>*/  listCode(
            /* @RequestParam("condition")*/
            @RequestBody ConditionModel<Code> cm
            /* ,
      Integer start, Integer limit*/){

     /*   return   codeService.findAll(start,limit);*/

      /*  System.out.println(cm);*/
        System.out.println(cm.getPage());
        System.out.println(cm.getLimit());

//        Iterator<Map.Entry<Integer, Integer>> it = cm.getMap().entrySet().iterator();
//        while (it.hasNext()) {
//            Map.Entry<Integer, Integer> entry = it.next();
//            System.out.println("key = " + entry.getKey() + ", value = " + entry.getValue());
//        }
        return codeService.queryPage(cm);
    }
    @RequestMapping(value="/queryPageTest", method=RequestMethod.POST)
    public Message<Page<Code>>    listCodeTest(

            @RequestBody ConditionModel<Code> cm){

        System.out.println(cm.getPage());
        System.out.println(cm.getLimit());


        return codeService.queryPageTest(cm);
    }
    @RequestMapping("/select")
    //事务配置
    @Transactional
    public int select(int id,boolean selected){
        /*return 1;*/
        return codeService.selected(id,selected);
    }
    @RequestMapping("/level")
    //事务配置
    @Transactional
    public int setLevel(@RequestParam int id,int level){
        /*return 1;*/
        return codeService.level(id,level);
    }

    @RequestMapping("/labels")
    //事务配置
    @Transactional
    public int setLabels(@RequestParam int id,String labels){
      /*  return 1;*/
        System.out.println(labels);
        System.out.println(id);
        return codeService.setLabels(id,labels);
    }
    @RequestMapping("/remarks")
    //事务配置
    @Transactional
    public int setRemarks(@RequestParam int id,String remarks){
        /*  return 1;*/
        System.out.println(remarks);
        System.out.println(id);
        return codeService.setRemarks(id,remarks);
    }
    @RequestMapping(value="/add", method=RequestMethod.POST)
    public Message<Code>postGroup(@RequestBody Code code) {
        // 处理"/users/"的POST请求，用来创建User
        // 除了@ModelAttribute绑定参数之外，还可以通过@RequestParam从页面中传递参数
        System.out.println("code");
        code.setNameCapital( code.getNameCapital().toUpperCase());
/*
        System.out.println(code.getNameCapital());
        System.out.println(code.getId());
        System.out.println(code.getName());
        System.out.println(code.getUrl());*/
        /*return null;*/
      return codeService.add(code);
    }
    @RequestMapping("/test")
    public Page<Code> login() {
        System.out.println("test ...");
//        if(codeService==null){
//            codeService=new com.zss.myspringboot.service.impl.CodeServiceImpl();
//        }
        return codeService.findAll(0,15);
    }
}
