package com.sourcery.defect_registration_system.comment.controller;

import com.sourcery.defect_registration_system.comment.dto.CommentRequestDto;
import com.sourcery.defect_registration_system.comment.dto.CommentResponseDto;
import com.sourcery.defect_registration_system.comment.service.CommentService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/issues/{issueId}/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentResponseDto postComment(
        @AuthenticationPrincipal OAuth2User principal,
        @RequestBody CommentRequestDto commentDto,
        @PathVariable("issueId") UUID issueId) {
        return commentService.postComment(principal, commentDto, issueId);
    }

    @GetMapping
    public List<CommentResponseDto> getComments(
        @AuthenticationPrincipal OAuth2User principal,
        @PathVariable UUID issueId) {
        return commentService.getCommentsByIssueId(principal, issueId);
    }
}
