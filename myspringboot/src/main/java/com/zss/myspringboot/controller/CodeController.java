package com.zss.myspringboot.controller;

import com.zss.myspringboot.entity.Code;
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
    public Message<Page<Code>>   /*Page<Code>*/  listcode(
            /* @RequestParam("condition")*/
            @RequestBody ConditionModel cm
            /* ,
      Integer start, Integer limit*/){

     /*   return   codeService.findAll(start,limit);*/

      /*  System.out.println(cm);*/
        System.out.println(cm.getPage());
        System.out.println(cm.getLimit());

        Iterator<Map.Entry<Integer, Integer>> it = cm.getMap().entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<Integer, Integer> entry = it.next();
            System.out.println("key = " + entry.getKey() + ", value = " + entry.getValue());
        }


        return codeService.queryPage(cm);
    }
    @RequestMapping("/select")
    //事务配置
    @Transactional
    public int select(@RequestParam int id,boolean selected){
        /*return 1;*/
        System.out.println(selected);
        return codeService.selected(id,selected);
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
