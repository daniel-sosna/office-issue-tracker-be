package com.sourcery.defect_registration_system.issue_vote.repository;

import com.sourcery.defect_registration_system.issue_vote.entity.Vote;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
@Mapper
public interface VoteRepository {

    @Select("""
            SELECT 1
            FROM issue_vote
            WHERE issue_id = #{issueId} AND user_id = #{userId}
            """)
    boolean isVoteExists(@Param("issueId") UUID issueId, @Param("userId") UUID userId);

    @Select("""
            SELECT *
            FROM issue_vote
            """)
    List<Vote> getAllVotes();

    @Insert("""
            INSERT INTO issue_vote (issue_id, user_id)
            VALUES (#{issueId}, #{userId})
            """)
    void insertVote(Vote vote);

    @Delete("""
            DELETE from issue
            WHERE issue_id = #{issueId} AND user_id = #{userId}
        """)
    void deleteVote(@Param("issueId") UUID issueId, @Param("userId") UUID userId);
}
