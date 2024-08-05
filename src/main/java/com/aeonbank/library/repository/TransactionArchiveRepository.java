package com.aeonbank.library.repository;

import com.aeonbank.library.model.Book;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;

import java.util.List;

@Mapper
public interface TransactionArchiveRepository {

    @Insert("""
            insert into transaction_archive (
            select *
             from `transaction`
             where id in
             <foreach item="id" collection="idList" open="(" close=")" separator=",">
            #{id}
            </foreach>
            )
            """)
    int insertTrans(List<Long> idList);

}
