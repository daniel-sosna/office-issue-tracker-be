package com.sourcery.defect_registration_system.office.repository;

import com.sourcery.defect_registration_system.office.entity.Office;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@Mapper
public interface OfficeRepository {

    @Select("""
            SELECT * 
            FROM office
            WHERE id = #{id}
            """)
    Optional<Office> getOfficeById(@Param("id") UUID id);

    @Select("""
            SELECT *
            FROM office
            """)
    List<Office> getAllOffices();

    @Insert("""
            INSERT INTO office (id, title, country, created_at)
            VALUES (#{id}, #{title}, #{country}, #{dateCreated})
            """)
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    void insertOffice(Office office);

    @Update("""
            UPDATE office
            SET title = #{title}, country = #{country}
            WHERE id = #{id}
            """)
    int updateOffice(Office office);
}
