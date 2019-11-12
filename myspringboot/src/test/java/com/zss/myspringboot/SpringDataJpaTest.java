package com.zss.myspringboot;

import com.zss.myspringboot.repository.CodeRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import javax.transaction.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringDataJpaTest {
    @Resource
    private CodeRepository codeRepository;
    @Test
    //事务配置
    @Transactional
    public void select(){
        System.out.println(codeRepository.updateSelected(1,true));
        System.out.println(codeRepository.findById(1).get().isSelected());
    }


}
