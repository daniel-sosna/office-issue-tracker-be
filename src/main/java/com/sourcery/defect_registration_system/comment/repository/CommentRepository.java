package com.sourcery.defect_registration_system.comment.repository;

import com.sourcery.defect_registration_system.comment.entity.Comment;
import java.util.List;
import java.util.UUID;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface CommentRepository {
  @Insert("""
        INSERT INTO comment (id, user_id, issue_id, comment_text, date_created)
        VALUES (#{id}, #{userId}, #{issueId}, #{commentText}, #{dateCreated})
        """)
  @Options(useGeneratedKeys = false)
  void insertComment(Comment comment);

  @Select("""
        SELECT id, user_id, issue_id, comment_text, date_created
        FROM comment
        WHERE issue_id = #{issueId}
        ORDER BY date_created ASC
        """)
  List<Comment> findByIssueId(UUID issueId);

  @Select("""
    SELECT COUNT(*)
    FROM comment
    WHERE issue_id = #{issueId}
    """)
  int countByIssueId(UUID issueId);

}
