package com.github.nayasis.spring.extension.jpa.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.Serializable;
import java.util.List;

/**
 * Pageable JPA repository which does not count total rows so increases performance
 *
 * @author nayasis@gmail.com
 * @since 2020-04-22
 *
 */
@Repository
public class NoCountRepository {

    @PersistenceContext
    private EntityManager em;

    public <T> List<T> findAll( Specification<T> specification, Sort sort, Class<T> domainClass ) {
        PageRequest pageable = PageRequest.of( 0, Integer.MAX_VALUE, sort );
        return readPage( specification, pageable, domainClass ).getContent();
    }

    public <T> List<T> findAll( Specification<T> specification, Pageable pageable, Class<T> domainClass ) {
        return readPage( specification, pageable, domainClass ).getContent();
    }

    public <T,ID extends Serializable> Page<T> readPage( Specification<T> specification, Pageable pageable, Class<T> domainClass ) {
        NoCountHandler<T,ID> noCountDao = new NoCountHandler<>( domainClass, em );
        return noCountDao.findAll( specification, pageable );
    }

}