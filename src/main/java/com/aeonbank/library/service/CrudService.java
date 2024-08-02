package com.aeonbank.library.service;

import com.aeonbank.library.dto.common.RequestResponse;

public interface CrudService<REQUEST,RESPONSE> {

    void list(RequestResponse<REQUEST, RESPONSE> rr);

    void get(RequestResponse<REQUEST, RESPONSE> rr);

    void add(RequestResponse<REQUEST, RESPONSE> rr);

    void update(RequestResponse<REQUEST, RESPONSE> rr);

    void delete(RequestResponse<REQUEST, RESPONSE> rr);

}
