package com.sourcery.defect_registration_system.notification.repository;

import com.sourcery.defect_registration_system.notification.entity.Notification;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.UUID;

@Mapper
public interface NotificationRepository {

    @Select("""
        SELECT id, user_id, issue_id, type, message, read_flag, created_at
        FROM notification
        WHERE user_id = #{userId}
        ORDER BY created_at DESC
    """)
    List<Notification> findByUserId(@Param("userId") UUID userId);

    @Select("""
        SELECT COUNT(id)
        FROM notification
        WHERE user_id = #{userId} AND read_flag = false
    """)
    long countUnreadNotification(@Param("userId") UUID userId);

    @Insert("""
        INSERT INTO notification(id, user_id, issue_id, type, message, read_flag, created_at)
        VALUES (#{id}, #{userId}, #{issueId}, #{type}, #{message}, #{readFlag}, #{createdAt})
    """)
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    void insertNotification(Notification notification);

    @Update("""
        UPDATE notification
        SET read_flag = true
        WHERE user_id = #{userId}
    """)
    void markAllAsRead(@Param("userId") UUID userId);

    @Update("""
        UPDATE notification
        SET read_flag = true
        WHERE id = #{notificationId}
            AND user_id = #{userId}
    """)
    void markAsRead(
            @Param("userId") UUID userId,
            @Param("notificationId") UUID notificationId
    );

    @Select("""
        SELECT id, user_id, issue_id, type, message, read_flag, created_at
        FROM notification
        WHERE issue_id = #{issueId}
        ORDER BY created_at DESC
    """)
    List<Notification> findByIssueId(@Param("issueId") UUID issueId);
}
