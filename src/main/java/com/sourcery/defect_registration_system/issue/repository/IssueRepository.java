package com.sourcery.defect_registration_system.issue.repository;

import com.sourcery.defect_registration_system.issue.dto.UpdateIssueRequest;
import com.sourcery.defect_registration_system.issue.entity.Issue;
import com.sourcery.defect_registration_system.issue.enums.IssueStatus;
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
public interface IssueRepository {

    @Select("""
            SELECT *
            FROM issue
            ORDER BY date_created DESC
            LIMIT #{limit} OFFSET #{offset}
            """)
    List<Issue> getAllIssuesPaged(int limit, int offset);

    @Select("""
            SELECT COUNT(*)
            FROM issue
            """)
    long countAllIssues();

    @Insert("""
            INSERT INTO issue (id, summary, description, office_id, status, created_by, date_created, date_modified)
            VALUES (#{id}, #{summary}, #{description}, #{officeId}, #{status}, #{createdBy}, #{dateCreated}, #{dateModified})
            """)
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    void insertIssue(Issue issue);

    @Select("""
            SELECT * 
            FROM issue
            WHERE id = #{id}
            """)
    Optional<Issue> getIssueById(@Param("id") UUID id);

    @Update("""
            UPDATE issue 
            SET summary = #{request.summary}, description = #{request.description}, office_id = #{request.officeId}, date_modified = now() 
            WHERE id = #{id}
            """)
    int updateIssue(@Param("id") UUID id, @Param("request") UpdateIssueRequest request);

    @Update("""
            UPDATE issue
            SET status = #{status}, date_modified = now()
            WHERE id = #{id}
            """)
    int updateIssueStatus(@Param("id") UUID id, @Param("status") IssueStatus status);
}
