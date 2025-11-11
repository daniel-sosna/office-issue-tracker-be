package com.sourcery.defect_registration_system.repository;

import com.sourcery.defect_registration_system.entity.Issue;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface IssueRepository {

  @Select(
      """
    Select * from issue
    ORDER BY date_created DESC
    LIMIT #{limit} OFFSET #{offset}
"""
  )
  List<Issue> getAllIssuesPaged(int limit, int offset);

  @Select(
      """
    Select count(*) from issue
"""
  )
long countAllIssues();
}
