package com.aeonbank.library.dto.common;

import lombok.Data;

// use as common request/response object between services

@Data
public class RequestResponse<REQUEST, RESPONSE> {

    private REQUEST request;
    private RESPONSE response;
    private Status status;

    public RequestResponse(){
        this.setStatus(new Status());
    }

}
