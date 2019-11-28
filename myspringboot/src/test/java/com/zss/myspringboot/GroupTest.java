package com.zss.myspringboot;

import com.zss.myspringboot.entity.Code;
import com.zss.myspringboot.entity.Group;
import com.zss.myspringboot.repository.CodeRepository;
import com.zss.myspringboot.repository.GroupRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@SpringBootTest
@RunWith(SpringRunner.class)
public class GroupTest {
    @Resource
    private CodeRepository codeRepository;
    @Resource
    private GroupRepository groupRepository;
    @Test
    //事务配置
    @Transactional
    public void saveCode() {
        Group group=groupRepository.getOne(1);
        Code code=codeRepository.getOne(1);
        List<Group> g= new ArrayList<Group>();
        g.add(group);
        code.setGroups(g);
        codeRepository.save(code);
    }
    @Test
    @Transactional
    public void saveGroup() {
        Group group=groupRepository.getOne(1);
        Code code=codeRepository.getOne(1);
        List<Code> c= new ArrayList<Code>();
        c.add(code);
        group.setCodes(c);
        groupRepository.save(group);

    }
    @Test
    public void deleteGroup() {

    }

}
