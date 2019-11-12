package com.zss.myspringboot.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

//
@Component
@ConfigurationProperties(prefix = "app")
//@Resource  动态注入
public class AppConfig {
    private String name;
    private Integer pageSize;

    public String getName() {
        return name;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
}
