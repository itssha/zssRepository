package com.zss.myspringboot.controller;

import com.zss.myspringboot.config.AppConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@Controller
public class MyController {

    @Value("${app.name}")
    private String name;
    @Resource
    private AppConfig appConfig;
    @RequestMapping("/out")
    @ResponseBody
    public String out(){
        return "success";
    }

    @RequestMapping("/")

    public String index(){
        return "AshockTable";
    }

    @RequestMapping("/AshockTable.html")
    public String dynamic_table(){
        return "AshockTable";
    }


    @RequestMapping("/groupTable")

    public String groupTable(){
        return "groupTable";
    }



    @RequestMapping("/login.html")
    public String login(){
        return "login";
    }
}
