package com.sourcery.defect_registration_system.profile.repository;

import com.sourcery.defect_registration_system.profile.entity.Profile;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@Mapper
public interface ProfileRepository {

    @Select("SELECT id, user_id, department, role, street_address, city, state_province, postcode, country, updated_at " +
            "FROM user_profile " +
            "WHERE user_id = #{userId}")
    Optional<Profile> findProfileByUserId(@Param("userId") UUID userId);

    @Insert("INSERT INTO user_profile (id, user_id, department, role, street_address, city, state_province, postcode, country, updated_at) " +
            "VALUES (#{profile.id}, #{profile.userId}, #{profile.department}, #{profile.role}, #{profile.streetAddress}, #{profile.city}, " +
            "#{profile.stateProvince}, #{profile.postcode}, #{profile.country}, NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "profile.id", keyColumn = "id")
    void insertProfile(@Param("profile") Profile profile);

    @Update("UPDATE user_profile SET department = #{profile.department}, role = #{profile.role}, street_address = #{profile.streetAddress}, " +
            "city = #{profile.city}, state_province = #{profile.stateProvince}, postcode = #{profile.postcode}, country = #{profile.country}, " +
            "updated_at = NOW() WHERE user_id = #{profile.userId}")
    void updateProfile(@Param("profile") Profile profile);

    @Select("SELECT COUNT(*) > 0 FROM user_profile WHERE user_id = #{userId}")
    boolean existsByUserId(@Param("userId") UUID userId);
}
