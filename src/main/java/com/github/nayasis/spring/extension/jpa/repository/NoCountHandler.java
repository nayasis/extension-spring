package com.github.nayasis.spring.extension.jpa.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.data.repository.support.PageableExecutionUtils;
import org.springframework.lang.Nullable;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.io.Serializable;

public class NoCountHandler<T,ID extends Serializable> extends SimpleJpaRepository<T,ID> {

    public NoCountHandler( Class<T> domainClass, EntityManager em ) {
        super( domainClass, em );
    }

    protected <S extends T> Page<S> readPage( TypedQuery<S> query, final Class<S> domainClass, Pageable pageable, @Nullable Specification<S> spec) {
        if( pageable.isPaged() ) {
            query.setFirstResult( (int) pageable.getOffset() );
            query.setMaxResults(pageable.getPageSize());
        }
        return PageableExecutionUtils.getPage(query.getResultList(), pageable, () -> { return 0L; } );
    }

}
