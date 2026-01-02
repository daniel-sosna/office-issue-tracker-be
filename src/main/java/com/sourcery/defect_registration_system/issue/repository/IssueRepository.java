package com.sourcery.defect_registration_system.issue.repository;

import com.sourcery.defect_registration_system.issue.entity.Issue;
import com.sourcery.defect_registration_system.issue.enums.IssueStatus;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.Delete;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@Mapper
public interface IssueRepository {

    @Select("""
            <script>
            SELECT *
            FROM issue
            WHERE status != 'DELETED'
              <if test="isAdmin == false">
                AND status != 'BLOCKED'
              </if>
              <if test='status != null'>
                AND status = #{status}
              </if>
              <if test='office != null'>
                AND office_id = #{office}
              </if>
              <if test='reportedBy != null'>
                AND created_by = #{reportedBy}
              </if>
            ORDER BY ${orderBy}
            LIMIT #{size} OFFSET #{offset}
            </script>
            """)
    List<Issue> getAllIssues(
            @Param("status") String status,
            @Param("office") UUID office,
            @Param("reportedBy") UUID reportedBy,
            @Param("orderBy") String orderBy,
            @Param("size") int size,
            @Param("offset") int offset,
            @Param("isAdmin") boolean isAdmin
    );

    @Select("""
            <script>
            SELECT COUNT(*)
            FROM issue
            WHERE status != 'DELETED'
              <if test="isAdmin == false">
                AND status != 'BLOCKED'
              </if>
              <if test='status != null'>
                AND status = #{status}
              </if>
              <if test='office != null'>
                AND office_id = #{office}
              </if>
              <if test='reportedBy != null'>
                AND created_by = #{reportedBy}
              </if>
            </script>
            """)
    long countAllIssues(
            @Param("status") String status,
            @Param("office") UUID office,
            @Param("reportedBy") UUID reportedBy,
            @Param("isAdmin") boolean isAdmin
    );

    @Insert("""
            INSERT INTO issue (id, summary, description, office_id, status, created_by, date_created, date_modified)
            VALUES (#{id}, #{summary}, #{description}, #{officeId}, #{status}, #{createdBy}, #{dateCreated}, #{dateModified})
            """)
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    void insertIssue(Issue issue);

    @Select("""
    SELECT
      id,
      summary,
      description,
      office_id    AS officeId,
      status,
      created_by  AS createdBy,
      date_created AS dateCreated,
      date_modified AS dateModified
    FROM issue
    WHERE id = #{id}
    """)
    Optional<Issue> getIssueById(@Param("id") UUID id);

    @Update("""
    UPDATE issue
    SET summary = #{summary},
        description = #{description},
        office_id = #{officeId},
        date_modified = now()
    WHERE id = #{id}
""")
    int updateIssue(@Param("id") UUID id,
                          @Param("summary") String summary,
                          @Param("description") String description,
                          @Param("officeId") UUID officeId);


    @Update("""
            UPDATE issue
            SET status = #{status}, date_modified = now()
            WHERE id = #{id}
            """)
    int updateIssueStatus(@Param("id") UUID id, @Param("status") IssueStatus status);

    @Update("""
    UPDATE issue
    SET office_id = #{officeId}, date_modified = now()
    WHERE id = #{id}
""")
    int updateIssueOffice(@Param("id") UUID id, @Param("officeId") UUID officeId);

    @Delete("""
            DELETE FROM issue
            WHERE id = #{id}
            """)
    void deleteIssue(UUID id);
}
