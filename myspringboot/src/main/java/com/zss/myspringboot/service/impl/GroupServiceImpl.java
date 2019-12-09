package com.zss.myspringboot.service.impl;

import com.zss.myspringboot.entity.Group;
import com.zss.myspringboot.repository.EntityRepository;
import com.zss.myspringboot.repository.GroupRepository;
import com.zss.myspringboot.service.CodeService;
import com.zss.myspringboot.service.GroupService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Optional;

@Service
public class GroupServiceImpl extends EntityServiceImpl<Group> implements GroupService {
    @Resource
    public GroupRepository groupRepository;
    @Override
    public EntityRepository<Group, Integer> getRepository() {
        return groupRepository;
    }

    @Override
    public Optional<Group> findById(Integer id) {
        return groupRepository.findById(id);
    }
}
