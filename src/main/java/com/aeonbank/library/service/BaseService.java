package com.aeonbank.library.service;

import com.aeonbank.library.common.util.ObjectMapperUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class BaseService {

    protected ObjectMapper getObjectMapper(){
        return ObjectMapperUtil.getObjectMapper();
    }
    protected ObjectMapper getObjectMapper(boolean excludeNull, boolean failUnknown){
        return ObjectMapperUtil.getObjectMapper(excludeNull, failUnknown);
    }

}
