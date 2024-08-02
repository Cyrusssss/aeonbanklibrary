package com.aeonbank.library.dto.common;

import com.aeonbank.library.common.constant.Enums;
import lombok.Data;

import java.io.Serializable;

// as the class name say, this class is used for returning controller response

@Data
public class ControllerResponse implements Serializable {

    private String code;
    private String message;
    private String detail;
    private Object data;

    // default error
    public ControllerResponse(){
        this.setCode(Enums.Status.SOMETHING_WENT_WRONG.getCode());
        this.setMessage(Enums.Status.SOMETHING_WENT_WRONG.getMessage());
    }

}
