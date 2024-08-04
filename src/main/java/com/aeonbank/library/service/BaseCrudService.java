package com.aeonbank.library.service;

public interface BaseCrudService<REQUEST> {

    void list(REQUEST request);
    void get(REQUEST request);
    void add(REQUEST request);
    void update(REQUEST request);
    void delete(REQUEST request);

}
