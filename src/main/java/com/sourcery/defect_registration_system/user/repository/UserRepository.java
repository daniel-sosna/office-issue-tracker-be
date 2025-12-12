package com.sourcery.defect_registration_system.user.repository;

import com.sourcery.defect_registration_system.user.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@Mapper
public interface UserRepository {

    @Select("""
              SELECT *
              FROM users
              WHERE id = #{id}
            """)
    Optional<User> findById(@Param("id") UUID id);

    @Select("""
              SELECT *
              FROM users
              WHERE email = #{email}
            """)
    Optional<User> findByEmail(@Param("email") String email);

    @Insert("""
              INSERT INTO users (id, name, email, image_url, role)
              VALUES (#{id}, #{name}, #{email}, #{imageUrl}, #{role})
            """)
    void insert(User user);
}