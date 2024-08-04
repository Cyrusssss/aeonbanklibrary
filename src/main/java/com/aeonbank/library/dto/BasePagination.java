package com.aeonbank.library.dto;

import lombok.Data;

@Data
public class BasePagination {

    private Integer pageNo;
    private Integer pageSize;
    private Long totalCount;
    private Long totalPages;

}
