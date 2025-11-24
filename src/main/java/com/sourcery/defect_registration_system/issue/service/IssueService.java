package com.sourcery.defect_registration_system.issue.service;

import com.sourcery.defect_registration_system.exception.UnauthorizedException;
import com.sourcery.defect_registration_system.issue.dto.*;
import com.sourcery.defect_registration_system.issue.entity.Issue;
import com.sourcery.defect_registration_system.issue.enums.IssueStatus;
import com.sourcery.defect_registration_system.issue.exceptions.IssueNotFoundException;
import com.sourcery.defect_registration_system.issue.repository.IssueRepository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import com.sourcery.defect_registration_system.user.dto.UserDto;
import com.sourcery.defect_registration_system.user.enums.Role;
import com.sourcery.defect_registration_system.user.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class IssueService {
    private final IssueRepository issueRepository;
    private final AuthService authService;

    public PageResponseDto<IssueResponseDto> getAllIssues(int page, int size) {
        int offset = (page - 1) * size;
        List<IssueResponseDto> content = issueRepository.getAllIssuesPaged(size, offset)
                .stream()
                .map(IssueResponseDto::from)
                .toList();

        long totalElements = issueRepository.countAllIssues();
        int totalPages = (int) Math.ceil(totalElements / (double) size);

        return new PageResponseDto<>(content, totalElements, totalPages, page, size);
    }

    @Transactional
    public IssueResponseDto createIssue(CreateIssueRequest request, OAuth2User principal) {

        UUID createdBy = authService.getCurrentUserId(principal);

        Issue issue = Issue.builder()
                .id(UUID.randomUUID())
                .summary(request.summary())
                .description(request.description())
                .officeId(request.officeId())
                .status(IssueStatus.OPEN)
                .createdBy(createdBy)
                .dateCreated(OffsetDateTime.now())
                .dateModified(null)
                .build();

        issueRepository.insertIssue(issue);

        return IssueResponseDto.from(issue);
    }

    public IssueResponseDto getIssueById(UUID id) {
        Issue issue = issueRepository.getIssueById(id)
                .orElseThrow(() -> new IssueNotFoundException("Issue with " + id + " id not found"));

        return IssueResponseDto.from(issue);
    }

    @Transactional
    public IssueResponseDto updateIssue(UUID id, UpdateIssueRequest request, OAuth2User principal) {

        UUID currentUserId = authService.getCurrentUserId(principal);

        Issue existingIssue = issueRepository.getIssueById(id)
                .orElseThrow(() -> new IssueNotFoundException("Issue with " + id + " id not found"));

        if (!existingIssue.getCreatedBy().equals(currentUserId)) {
            throw new UnauthorizedException("You are not allowed to update this issue");
        }

        issueRepository.updateIssue(id, request);

        Issue updatedIssue = issueRepository.getIssueById(id)
                .orElseThrow(() -> new IssueNotFoundException("Issue with " + id + " id not found"));

        return IssueResponseDto.from(updatedIssue);
    }

    @Transactional
    public IssueResponseDto updateIssueStatus(UUID id, ChangeIssueStatusRequest request, OAuth2User principal) {

        UserDto currentUser = authService.getCurrentUserInfo(principal);

        if (currentUser.role() != Role.ADMIN) {
            throw new UnauthorizedException("You do not have permission to change issue status");
        }

        issueRepository.getIssueById(id)
                .orElseThrow(() -> new IssueNotFoundException("Issue with " + id + " id not found"));

        issueRepository.updateIssueStatus(id, request.status());
        Issue updatedIssue = issueRepository.getIssueById(id)
                .orElseThrow(() -> new IssueNotFoundException("Issue with " + id + " id not found"));

        return IssueResponseDto.from(updatedIssue);
    }
}
