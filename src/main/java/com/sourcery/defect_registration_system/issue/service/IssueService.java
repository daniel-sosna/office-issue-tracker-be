package com.sourcery.defect_registration_system.issue.service;

import com.sourcery.defect_registration_system.attachment.dto.IssueAttachmentResponse;
import com.sourcery.defect_registration_system.attachment.service.IssueAttachmentService;
import com.sourcery.defect_registration_system.exception.UnauthorizedException;
import com.sourcery.defect_registration_system.issue.dto.ChangeIssueStatusRequest;
import com.sourcery.defect_registration_system.issue.dto.CreateIssueRequest;
import com.sourcery.defect_registration_system.issue.dto.IssueDetailsResponseDto;
import com.sourcery.defect_registration_system.issue.dto.IssueResponseDto;
import com.sourcery.defect_registration_system.issue.dto.PageIssueResponseDto;
import com.sourcery.defect_registration_system.issue.dto.PageResponseDto;
import com.sourcery.defect_registration_system.issue.dto.UpdateIssueRequest;
import com.sourcery.defect_registration_system.issue.entity.Issue;
import com.sourcery.defect_registration_system.issue.enums.IssueStatus;
import com.sourcery.defect_registration_system.issue.exceptions.IssueNotFoundException;
import com.sourcery.defect_registration_system.issue.repository.IssueRepository;
import com.sourcery.defect_registration_system.issue_vote.dto.VoteInfoDto;
import com.sourcery.defect_registration_system.issue_vote.service.VoteService;
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
import org.springframework.web.multipart.MultipartFile;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class IssueService {
    private final IssueRepository issueRepository;
    private final AuthService authService;
    private final VoteService voteService;
    private final OfficeService officeService;
    private final UserService userService;
    private final IssueAttachmentService issueAttachmentService;


    public PageResponseDto<PageIssueResponseDto> getAllIssues(
            String status,
            UUID office,
            UUID reportedBy,
            String sort,
            int page,
            int size,
            OAuth2User principal
    ) {
        boolean isAdmin = Role.ADMIN.equals(authService.getCurrentUserInfo(principal).role());
        int offset = (page - 1) * size;

        String orderBy;
        if (sort == null) {
            orderBy = "date_created DESC";
        } else {
            switch (sort.toLowerCase()) {
                case "dateasc": orderBy = "date_created ASC"; break;
                case "votesdesc": orderBy = "votes DESC"; break;
                case "commentsdesc": orderBy = "comments DESC"; break;
                default: orderBy = "date_created DESC";
            }
        }
        List<Issue> issues = issueRepository.getAllIssues(status, office, reportedBy, orderBy, size, offset, isAdmin);
        List<UUID> ids = issues.stream().map(Issue::getId).toList();
        UUID userId = authService.getCurrentUserId(principal);
        Map<UUID, VoteInfoDto> votesInfo = voteService.getVoteInfoForIssues(ids, userId);
        List<PageIssueResponseDto> content = issues
                .stream()
                .map(issue -> {
                    VoteInfoDto voteInfoDto = votesInfo.get(issue.getId());
                    return PageIssueResponseDto.from(
                            issue,
                            voteInfoDto.userVoted(),
                            voteInfoDto.voteCount()
                    );
                })
                .toList();
        long totalElements = issueRepository.countAllIssues(status, office, reportedBy, isAdmin);
        int totalPages = (int) Math.ceil(totalElements / (double) size);

        return new PageResponseDto<>(content, totalElements, totalPages, page, size);
    }

    public PageResponseDto<PageIssueResponseDto> getAllIssues(int page, int size, OAuth2User principal) {
        return getAllIssues(null, null, null, "dateDesc", page, size, principal);
    }

    @Transactional
    public IssueResponseDto createIssue(CreateIssueRequest request, List<MultipartFile> files, OAuth2User principal) {

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

        if (files != null && !files.isEmpty()) {
            issueAttachmentService.uploadAttachments(issue.getId(), createdBy, files);
        }

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
        List<IssueAttachmentResponse> attachments = issueAttachmentService.getAttachmentsByIssueId(issue.getId());

        return new IssueDetailsResponseDto(
                IssueResponseDto.from(issue),
                officeName,
                user.name(),
                user.picture(),
                user.email(),
                issue.getDateModified(),
                attachments
        );
    }

    @Transactional
    public IssueResponseDto updateIssue(UUID id, UpdateIssueRequest request, List<MultipartFile> newFiles,
                                        List<UUID> deleteAttachmentIds, OAuth2User principal) {

        UUID currentUserId = authService.getCurrentUserId(principal);
        Role role = authService.getCurrentUserInfo(principal).role();

        Issue existingIssue = issueRepository.getIssueById(id)
                .orElseThrow(() -> new IssueNotFoundException("Issue with " + id + " id not found"));

        boolean isOwner = existingIssue.getCreatedBy().equals(currentUserId);
        boolean isAdmin = role == Role.ADMIN;

        if (!isOwner && !isAdmin) {
            throw new UnauthorizedException("You are not allowed to update this issue");
        }

        if (isAdmin && !isOwner) {
            if (request.summary() != null || request.description() != null) {
                throw new UnauthorizedException("Admin can only change issue office/status");
            }
            if ((newFiles != null && !newFiles.isEmpty()) ||
                    (deleteAttachmentIds != null && !deleteAttachmentIds.isEmpty())) {
                throw new UnauthorizedException("Admin cannot modify attachments");
            }
            if (request.officeId() == null) {
                throw new UnauthorizedException("Admin update requires officeId");
            }

            int updated = issueRepository.updateIssueOffice(id, request.officeId());
            if (updated == 0) throw new IssueNotFoundException("Failed to update issue office");

            Issue updatedIssue = issueRepository.getIssueById(id)
                    .orElseThrow(() -> new IllegalStateException("Issue missing after update"));
            return IssueResponseDto.from(updatedIssue);
        }

        String summary = request.summary() != null ? request.summary() : existingIssue.getSummary();
        String description = request.description() != null ? request.description() : existingIssue.getDescription();
        UUID officeId = request.officeId() != null ? request.officeId() : existingIssue.getOfficeId();

        int updatedRows = issueRepository.updateIssue(id, summary, description, officeId);
        if (updatedRows == 0) throw new IssueNotFoundException("Failed to update issue");

        if (deleteAttachmentIds != null && !deleteAttachmentIds.isEmpty()) {
            for (UUID attachmentId : deleteAttachmentIds) {
                issueAttachmentService.deleteAttachment(attachmentId);
            }
        }

        if (newFiles != null && !newFiles.isEmpty()) {
            issueAttachmentService.uploadAttachments(id, currentUserId, newFiles);
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
    public void softDeleteIssue(UUID id, OAuth2User principal) {
        UUID currentUserId = authService.getCurrentUserId(principal);

        Issue existingIssue = issueRepository.getIssueById(id)
                .orElseThrow(() -> new IssueNotFoundException("Issue with id " + id + " not found"));

        if (!existingIssue.getCreatedBy().equals(currentUserId) && !Role.ADMIN.equals(authService.getCurrentUserInfo(principal).role())) {
            throw new UnauthorizedException("You are not allowed to delete this issue.");
        }
        issueRepository.updateIssueStatus(id, IssueStatus.DELETED);
    }
}
