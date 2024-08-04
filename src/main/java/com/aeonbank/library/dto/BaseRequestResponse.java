package com.aeonbank.library.dto;

import com.aeonbank.library.common.Enums;
import lombok.Data;

// use as common request/response object between services

@Data
public class BaseRequestResponse<REQUEST> {

    // request/response object
    private REQUEST request;
    private Object data;

    // status code, status message, status detail
    private String code;
    private String message;
    private String detail;

    // pagination
    private BasePagination pagination;

    // default status
    public BaseRequestResponse(){
        this.setCode(Enums.Status.SOMETHING_WENT_WRONG.getCode());
        this.setMessage(Enums.Status.SOMETHING_WENT_WRONG.getMessage());
        this.setPagination(new BasePagination());
    }

    public void setStatus(Enums.Status status){
        this.setCode(status.getCode());
        this.setMessage(status.getMessage());
    }

}
