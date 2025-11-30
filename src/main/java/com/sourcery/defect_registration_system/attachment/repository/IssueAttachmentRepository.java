package com.sourcery.defect_registration_system.attachment.repository;

import com.sourcery.defect_registration_system.attachment.entity.IssueAttachment;
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
public interface IssueAttachmentRepository {

    @Select("""
            SELECT *
            FROM issue_attachments
            WHERE issue_id = #{issueId}
            """)
    List<IssueAttachment> getAttachmentsByIssueId(@Param("issueId") UUID issueId);

    @Insert("""
            INSERT INTO issue_attachments(id, issue_id, uploaded_by, public_id, url, format, original_filename, file_size, date_created)
            VALUES (#{id}, #{issueId}, #{uploadedBy}, #{publicId}, #{url}, #{format}, #{originalFilename}, #{fileSize}, #{dateCreated})
            """)
    void insertAttachment(IssueAttachment attachment);

    @Delete("""
            DELETE FROM issue_attachments
            WHERE id = #{id}
            """)
    void deleteAttachment(@Param("id") UUID id);

    @Delete("""
            DELETE FROM issue_attachments
            WHERE issue_id = #{issueId}
            """)
    void deleteAttachmentByIssueId(@Param("issueId") UUID issueId);
}
