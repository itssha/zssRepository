package com.zss.myspringboot.repository;

import com.zss.myspringboot.entity.Code;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface CodeRepository extends EntityRepository <Code,Integer> {
   /* @Query(value = "select * from code",
            countQuery = "select count(*) from code",
            nativeQuery = true)
    public Page<Code> findAll(Pageable pageable);*/
    @Modifying
    @Query(
            "update Code c set selected= ?2 where c.id= ?1 "

    )
   public int updateSelected(int id,boolean isSelected);
}
