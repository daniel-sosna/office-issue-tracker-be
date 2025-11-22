package com.sourcery.defect_registration_system.issue.repository;

import com.sourcery.defect_registration_system.issue.entity.Issue;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

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
}
