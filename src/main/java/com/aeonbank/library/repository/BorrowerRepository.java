package com.aeonbank.library.repository;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;

@Mapper
public interface BorrowerRepository {

    @Select("select count(*) as CNT from ms_application where ref_id = #{refId}::uuid")
    @SelectKey(statement="", keyProperty="CNT", before=false, resultType=int.class)
    int getCountByRefId(String refId);

}
