package com.sourcery.defect_registration_system.comment.service;

import com.sourcery.defect_registration_system.comment.dto.CommentRequestDto;
import com.sourcery.defect_registration_system.comment.dto.CommentResponseDto;
import com.sourcery.defect_registration_system.comment.dto.CommentWebSocketDto;
import com.sourcery.defect_registration_system.comment.entity.Comment;
import com.sourcery.defect_registration_system.comment.repository.CommentRepository;
import com.sourcery.defect_registration_system.issue.dto.IssueResponseDto;
import com.sourcery.defect_registration_system.issue.service.IssueService;
import com.sourcery.defect_registration_system.user.dto.UserDto;
import com.sourcery.defect_registration_system.user.service.AuthService;
import com.sourcery.defect_registration_system.user.service.UserService;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.messaging.simp.SimpMessagingTemplate;

@Service
@RequiredArgsConstructor
public class CommentService { private final CommentRepository commentRepository;
  private final UserService userService;
  private final AuthService authService;
  private final IssueService issueService;
  private final SimpMessagingTemplate messagingTemplate;

  @Transactional
  public CommentResponseDto postComment(OAuth2User principal,CommentRequestDto commentDto, UUID issueId) {

    issueService.getIssueById(issueId);
    UUID userId = authService.getCurrentUserId(principal);

    Comment comment = Comment.builder()
        .id(UUID.randomUUID())
        .issueId(issueId)
        .userId(userId)
        .commentText(commentDto.commentText())
        .dateCreated(OffsetDateTime.now())
        .build();

    commentRepository.insertComment(comment);

    UserDto user = userService.getUserById(comment.getUserId());

    IssueResponseDto issue = issueService.getIssueById(issueId);

      CommentWebSocketDto commentWebSocketDto = new CommentWebSocketDto(
        comment.getId(),
        issueId,
        user.name(),
        user.picture(),
        comment.getCommentText(),
        comment.getDateCreated()
      );

    messagingTemplate.convertAndSend(
        "/topic/issues/" + issueId + "/comments",
        commentWebSocketDto
    );

    return new CommentResponseDto(
        user.name(),
        user.picture(),
        comment.getCommentText(),
        comment.getDateCreated()
    );
  }

  @Transactional(readOnly = true)
  public List<CommentResponseDto> getCommentsByIssueId(
      OAuth2User principal,
      UUID issueId) {

    issueService.getIssueById(issueId);
    authService.getCurrentUserId(principal);

    return commentRepository.findByIssueId(issueId)
        .stream()
        .map(comment -> {
          UserDto user = userService.getUserById(comment.getUserId());

          return new CommentResponseDto(
              user.name(),
              user.picture(),
              comment.getCommentText(),
              comment.getDateCreated()
          );
        })
        .toList();
  }
}
