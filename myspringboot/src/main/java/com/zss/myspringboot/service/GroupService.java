package com.zss.myspringboot.service;

import com.zss.myspringboot.entity.Group;

import java.util.Optional;

public interface GroupService extends EntityService<Group,Integer> {
    public Optional<Group> findById(Integer id);


}
