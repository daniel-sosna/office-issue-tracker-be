package com.sourcery.defect_registration_system.issue.service;

import com.sourcery.defect_registration_system.exception.UnauthorizedException;
import com.sourcery.defect_registration_system.issue.dto.ChangeIssueStatusRequest;
import com.sourcery.defect_registration_system.issue.dto.CreateIssueRequest;
import com.sourcery.defect_registration_system.issue.dto.IssueDetailsResponseDto;
import com.sourcery.defect_registration_system.issue.dto.IssueResponseDto;
import com.sourcery.defect_registration_system.issue.dto.PageResponseDto;
import com.sourcery.defect_registration_system.issue.dto.UpdateIssueRequest;
import com.sourcery.defect_registration_system.issue.entity.Issue;
import com.sourcery.defect_registration_system.issue.enums.IssueStatus;
import com.sourcery.defect_registration_system.issue.exceptions.IssueNotFoundException;
import com.sourcery.defect_registration_system.issue.repository.IssueRepository;
import com.sourcery.defect_registration_system.office.service.OfficeService;
import com.sourcery.defect_registration_system.user.dto.UserDto;
import com.sourcery.defect_registration_system.user.enums.Role;
import com.sourcery.defect_registration_system.user.service.AuthService;
import com.sourcery.defect_registration_system.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class IssueService {
    private final IssueRepository issueRepository;
    private final AuthService authService;
    private final OfficeService officeService;
    private final UserService userService;

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

    public IssueDetailsResponseDto getIssueDetailsById(UUID id) {
        Issue issue = issueRepository.getIssueById(id)
                .orElseThrow(() -> new IssueNotFoundException("Issue with " + id + " id not found"));

        String officeName = officeService.getOfficeDisplayNameById(issue.getOfficeId());
        UserDto user = userService.getUserById(issue.getCreatedBy());

        return new IssueDetailsResponseDto(
                IssueResponseDto.from(issue),
                officeName,
                issue.getOfficeId().toString(),
                user.name(),
                user.picture(),
                user.email()
        );
    }

    @Transactional
    public IssueResponseDto updateIssue(UUID id, UpdateIssueRequest request, OAuth2User principal) {

        UUID currentUserId = authService.getCurrentUserId(principal);

        Issue existingIssue = issueRepository.getIssueById(id)
                .orElseThrow(() -> new IssueNotFoundException("Issue with " + id + " id not found"));

        if (!existingIssue.getCreatedBy().equals(currentUserId)) {
            throw new UnauthorizedException("You are not allowed to update this issue");
        }

        int updatedRows = issueRepository.updateIssue(id, request);
        if (updatedRows == 0) {
            throw new IssueNotFoundException("Failed to update issue");
        }

        Issue updatedIssue = issueRepository.getIssueById(id)
                .orElseThrow(() -> new IllegalStateException("Issue missing after update"));

        return IssueResponseDto.from(updatedIssue);
    }

    @Transactional
    public IssueResponseDto updateIssueStatus(UUID id, ChangeIssueStatusRequest request, OAuth2User principal) {

        if (!Role.ADMIN.equals(authService.getCurrentUserInfo(principal).role())) {
            throw new AccessDeniedException("You do not have permission to change issue status");
        }

        issueRepository.getIssueById(id)
                .orElseThrow(() -> new IssueNotFoundException("Issue with " + id + " id not found"));

        int updatedRows = issueRepository.updateIssueStatus(id, request.status());

        if (updatedRows == 0) {
            throw new IssueNotFoundException("Failed to update status");
        }

        Issue updatedIssue = issueRepository.getIssueById(id)
                .orElseThrow(() -> new IllegalStateException("Issue missing after update"));

        return IssueResponseDto.from(updatedIssue);
    }
    @Transactional
    public void deleteIssue(UUID id, OAuth2User principal) {

        UUID currentUserId = authService.getCurrentUserId(principal);

        Issue existingIssue = issueRepository.getIssueById(id)
                .orElseThrow(() -> new IssueNotFoundException("Issue with id " + id + " not found"));

        if (!existingIssue.getCreatedBy().equals(currentUserId) && !Role.ADMIN.equals(authService.getCurrentUserInfo(principal).role())) {
            throw new UnauthorizedException("You are not allowed to delete this issue.");
        }

        issueRepository.deleteIssue(id);
    }

}
