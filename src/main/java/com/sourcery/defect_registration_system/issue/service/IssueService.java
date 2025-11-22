package com.sourcery.defect_registration_system.issue.service;

import com.sourcery.defect_registration_system.issue.dto.CreateIssueRequest;
import com.sourcery.defect_registration_system.issue.dto.IssueResponseDto;
import com.sourcery.defect_registration_system.issue.dto.PageResponseDto;
import com.sourcery.defect_registration_system.issue.entity.Issue;
import com.sourcery.defect_registration_system.issue.enums.IssueStatus;
import com.sourcery.defect_registration_system.issue.exceptions.IssueNotFoundException;
import com.sourcery.defect_registration_system.issue.repository.IssueRepository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import com.sourcery.defect_registration_system.user.service.AuthService;
import lombok.RequiredArgsConstructor;
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
}
