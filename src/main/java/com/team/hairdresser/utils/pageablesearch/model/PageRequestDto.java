package com.team.hairdresser.utils.pageablesearch.model;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;

public class PageRequestDto {

    private int page = 0;
    private int size = Integer.MAX_VALUE;
    private List<BaseSortRequestDto> sort;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public List<BaseSortRequestDto> getSort() {
        return sort;
    }

    public void setSort(List<BaseSortRequestDto> sort) {
        this.sort = sort;
    }

    public void addSort(BaseSortRequestDto baseSortRequestDto) {
        if (this.getSort() == null) {
            this.setSort(new ArrayList<BaseSortRequestDto>());
        }

        this.sort.add(baseSortRequestDto);
    }

    private Sort getSortProperties() {
        Sort sort;
        if (this.getSort() != null && this.getSort().size() > 0) {
            Sort.Order order = new Sort.Order(Sort.Direction.fromString(this.getSort().get(0).getDir()), this.getSort().get(0).getField());
            order = applyNullHandling(order, this.getSort().get(0).getNullHandling());
            sort = Sort.by(order);

            if (this.getSort().size() > 1) {
                for (int i = 1; i < this.getSort().size(); i++) {
                    BaseSortRequestDto newSort = this.getSort().get(i);
                    Sort.Order newOrder = new Sort.Order(Sort.Direction.fromString(newSort.getDir()), newSort.getField());
                    newOrder = applyNullHandling(newOrder, newSort.getNullHandling());

                    sort = sort.and(Sort.by(newOrder));
                }
            }
            return sort;
        } else {
            return null;
        }
    }

    private Sort.Order applyNullHandling(Sort.Order order, Sort.NullHandling nullHandling) {
        if (nullHandling == null) {
            return order;
        }

        if (Sort.NullHandling.NULLS_FIRST.equals(nullHandling)) {
            return order.nullsFirst();
        } else if (Sort.NullHandling.NULLS_LAST.equals(nullHandling)) {
            return order.nullsLast();
        } else if (Sort.NullHandling.NATIVE.equals(nullHandling)) {
            return order.nullsNative();
        }

        return order;
    }

    public PageRequest getSpringPageRequest() {
        Sort sort = getSortProperties();
        if (sort != null) {
            return PageRequest.of(page, size, getSortProperties());
        } else {
            return PageRequest.of(page, size);
        }
    }
}

