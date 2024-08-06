package com.aeonbank.library.repository;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface TransactionArchiveRepository {

    @Insert("""
            <script>
            insert into transaction_archive (
            select *
             from `transaction`
             where id in
             <foreach item='item' collection='list' open='(' separator=',' close=')'>#{item}</foreach>
            )
            </script>
            """)
    int insertTrans(@Param("list") List<Long> ids);

}
