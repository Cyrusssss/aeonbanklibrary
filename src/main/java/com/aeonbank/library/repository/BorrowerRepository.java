package com.aeonbank.library.repository;

import com.aeonbank.library.model.Borrower;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface BorrowerRepository {

    @Select("""
            select *
             from borrower
            """)
    List<Borrower> list();

    @Select("""
            select *
             from borrower
             where id = #{id}
            """)
    Borrower get(Long id);

    @Insert("""
            insert into borrower(name, email)
             values(#{name}, #{email})
            """)
    @Options(useGeneratedKeys = true, keyProperty="id", keyColumn="id")
    int add(Borrower borrower);

    @Update("""
            update borrower set
             name = #{name},
             email = #{email}
             where id = #{id}
            """)
    int update(Borrower borrower);

    @Delete("""
            delete from borrower
             where id = #{id}
            """)
    int delete(Long id);

    @Select("""
            select *
             from borrower
             where id = #{id}
             for update
            """)
    Borrower getForUpdate(Long id);

}
