package com.github.nayasis.spring.extension.jpa.domain;

import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Data
@Accessors(fluent=true)
public class PageParam {

    private int  page = 0;
    private int  size = 10;
    private Sort sort = Sort.unsorted();

    public PageParam sort( Sort sort ) {
        this.sort = sort;
        return this;
    }

    @JsonSetter
    public PageParam sort( String sort ) {
        this.sort = SortBuilder.toSort(sort);
        return this;
    }

    public Pageable toPageable() {
        return PageRequest.of( page, size, sort );
    }

}
