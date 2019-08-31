package com.team.hairdresser.utils.abstracts;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;


@NoRepositoryBean
public interface BaseRepository<T, ID extends Serializable> extends JpaRepository<T, ID>, JpaSpecificationExecutor<T> {

    @Transactional
    void softDeleteAll();

    @Transactional
    void softDelete(ID id);

    @Transactional
    void softDelete(T entity);

    @Transactional
    void softDelete(Iterable<? extends T> entities);

    List<T> findAll(Boolean baseStatus);

    List<T> findAll(Boolean baseStatus, Sort sort);

    Page<T> findAll(Boolean baseStatus, Pageable pageable);

    Page<T> findAll(Boolean baseStatus, Specification<T> spec, Pageable pageable);

    T getOne(Boolean baseStatus, ID id);

    Optional<T> findOne(Boolean baseStatus, Specification<T> spec);

    boolean existById(Boolean baseStatus, ID id);

    List<T> findAllById(Boolean baseStatus, Iterable<ID> ids);

}
