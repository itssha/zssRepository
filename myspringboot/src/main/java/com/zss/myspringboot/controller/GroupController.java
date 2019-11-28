package com.zss.myspringboot.controller;

import com.zss.myspringboot.entity.Code;
import com.zss.myspringboot.entity.Group;
import com.zss.myspringboot.model.Message;
import com.zss.myspringboot.module.condition.ui.ConditionModel;
import com.zss.myspringboot.service.GroupService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping(value="/group")
public class GroupController {
    @Resource
    private GroupService groupService;
    @RequestMapping(value="/queryPage", method= RequestMethod.POST)
    public Message<Page<Group>>    listGroup( @RequestBody ConditionModel cm){

        return  groupService.queryPage(cm);
    }
    @RequestMapping(value="/add", method=RequestMethod.POST)
    public Message<Group>postGroup(@RequestBody Group group) {
        // 处理"/users/"的POST请求，用来创建User
        // 除了@ModelAttribute绑定参数之外，还可以通过@RequestParam从页面中传递参数
       /* System.out.println("group");
        System.out.println(group.getId());
        System.out.println(group.getName());*/
        return groupService.add(group);
    }
    @RequestMapping(value="/update", method=RequestMethod.POST)
    public Message<Group> putGroup(@RequestBody Group group) {
        // 处理"/users/"的POST请求，用来创建User
        // 除了@ModelAttribute绑定参数之外，还可以通过@RequestParam从页面中传递参数
        /*System.out.println("group");
        System.out.println(group.getId());
        System.out.println(group.getName());*/
        return groupService.update(group);


    }

    @RequestMapping(value="/{id}", method=RequestMethod.GET)
    public Group getUser(@PathVariable Long id) {
        // 处理"/users/{id}"的GET请求，用来获取url中id值的User信息
        // url中的id可通过@PathVariable绑定到函数的参数中
        return null;
    }

    @RequestMapping(value="/{id}", method=RequestMethod.PUT)
    public String putUser(@PathVariable Long id, @ModelAttribute Group group) {
        // 处理"/users/{id}"的PUT请求，用来更新User信息

        return "success";
    }
}
