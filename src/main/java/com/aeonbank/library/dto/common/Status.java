package com.aeonbank.library.dto.common;

import com.aeonbank.library.common.constant.Enums;
import lombok.Data;

@Data
public class Status {

    private String code;
    private String message;
    private String detail;

    public Status(){
        this.code = Enums.Status.SOMETHING_WENT_WRONG.getCode();
        this.message = Enums.Status.SOMETHING_WENT_WRONG.getMessage();
    }

    public Status(Enums.Status status){
        this.code = status.getCode();
        this.message = status.getMessage();
    }

    public Status setDetail(String detail){
        this.detail = detail;
        return this;
    }

}
