package com.sourcery.defect_registration_system.comment.service;

import com.sourcery.defect_registration_system.comment.dto.CommentRequestDto;
import com.sourcery.defect_registration_system.comment.dto.CommentResponseDto;
import com.sourcery.defect_registration_system.comment.entity.Comment;
import com.sourcery.defect_registration_system.comment.repository.CommentRepository;
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

@Service
@RequiredArgsConstructor
public class CommentService {

  private final CommentRepository commentRepository;
  private final UserService userService;
  private final AuthService authService;

  @Transactional
  public CommentResponseDto postComment(OAuth2User principal,CommentRequestDto commentDto, UUID issueId) {

    UUID userId = authService.getCurrentUserId(principal);
    Comment comment = Comment.builder()
        .id(UUID.randomUUID())
        .issueId(issueId)
        .userId(userId)
        .commentText(commentDto.getCommentText())
        .dateCreated(OffsetDateTime.now())
        .build();

    commentRepository.insertComment(comment);

    UserDto user = userService.getUserById(comment.getUserId());

    CommentResponseDto response = new CommentResponseDto();
    response.setUserName(user.name());
    response.setImageUrl(user.picture());
    response.setCommentText(comment.getCommentText());
    response.setCreationDateTime(comment.getDateCreated());

    return response;
  }

  @Transactional(readOnly = true)
  public List<CommentResponseDto> getCommentsByIssueId(
      OAuth2User principal,
      UUID issueId) {

    authService.getCurrentUserId(principal);

    return commentRepository.findByIssueId(issueId)
        .stream()
        .map(comment -> {
          UserDto user = userService.getUserById(comment.getUserId());

          CommentResponseDto commentDto = new CommentResponseDto();
          commentDto.setUserName(user.name());
          commentDto.setImageUrl(user.picture());
          commentDto.setCommentText(comment.getCommentText());
          commentDto.setCreationDateTime(comment.getDateCreated());

          return commentDto;
        })
        .toList();
  }
}
