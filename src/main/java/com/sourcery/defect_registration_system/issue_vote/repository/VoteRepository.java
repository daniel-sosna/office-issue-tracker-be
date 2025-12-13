package com.sourcery.defect_registration_system.issue_vote.repository;

import com.sourcery.defect_registration_system.issue_vote.dto.IssueVoteCountDto;
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
    boolean isVoteExist(@Param("issueId") UUID issueId, @Param("userId") UUID userId);

    @Select("""
            <script>
            SELECT issue_id
            FROM issue_vote
            WHERE issue_id IN
                <foreach collection="issueIds" item="id" open="(" separator="," close=")">
                  #{id}
                </foreach>
            AND user_id = #{userId}
            </script>
    """)
    List<UUID> findIssuesVotedByUser(@Param("issueIds") List<UUID> issueIds, @Param("userId") UUID userId);

    @Select("""
            SELECT COUNT(*)
            FROM issue_vote
            WHERE issue_id = #{id}
    """)
    int countVotesOnIssue(@Param("id") UUID issueId);

    @Select("""
            <script>
            SELECT issue_id AS issueId, COUNT(*) AS voteCount
            FROM issue_vote
            WHERE issue_id IN
                <foreach collection="ids" item="id" open="(" separator="," close=")">
                  #{id}
                </foreach>
            GROUP BY issue_id
            </script>
    """)
    List<IssueVoteCountDto> countVotesOnIssues(@Param("ids") List<UUID> issueIds);

    @Select("""
            SELECT *
            FROM issue_vote
            """)
    List<Vote> findAllVotes();

    @Insert("""
            INSERT INTO issue_vote (issue_id, user_id)
            VALUES (#{issueId}, #{userId})
            """)
    void insertVote(Vote vote);

    @Delete("""
            DELETE from issue_vote
            WHERE issue_id = #{issueId} AND user_id = #{userId}
            """)
    void deleteVote(@Param("issueId") UUID issueId, @Param("userId") UUID userId);
}
