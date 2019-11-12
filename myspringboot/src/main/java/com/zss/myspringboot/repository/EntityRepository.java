package com.zss.myspringboot.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.Collection;
import java.util.List;
import java.util.Optional;


@NoRepositoryBean
public interface EntityRepository<T,ID> extends JpaRepository<T,ID>, JpaSpecificationExecutor<T> {

    List<T> findAllByActive(Boolean active);

    Page<T> findAllByActive(Pageable page, Boolean active);

    Optional<T> findByIdAndActive(Integer id, boolean active);

    public long countByActive(Boolean active);

    List<T> findAllByIdIn(Collection<ID> ids);

}
