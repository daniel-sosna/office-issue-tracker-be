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

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class IssueService {
    private final IssueRepository issueRepository;

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

    public IssueResponseDto createIssue(CreateIssueRequest request) {
        Issue issue = Issue.builder()
                .id(UUID.randomUUID())
                .summary(request.summary())
                .description(request.description())
                .office(request.office())
                .status(IssueStatus.OPEN)
//                TODO: Replace with actual user UUID
                .createdBy(UUID.randomUUID())
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
