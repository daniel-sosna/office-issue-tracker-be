package com.sourcery.defect_registration_system.issue.service;

import com.sourcery.defect_registration_system.issue.dto.IssueResponseDto;
import com.sourcery.defect_registration_system.issue.dto.PageResponseDto;
import com.sourcery.defect_registration_system.issue.repository.IssueRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class IssueService {
  private final IssueRepository issueRepository;

  public PageResponseDto<IssueResponseDto> getAllIssues(int page, int size) {
    int offset = (page - 1) * size;
    List<IssueResponseDto> content = issueRepository.getAllIssuesPaged(size,offset)
        .stream()
        .map(IssueResponseDto::from)
        .toList();

    long totalElements = issueRepository.countAllIssues();
    int totalPages = (int) Math.ceil(totalElements / (double) size);

    return new PageResponseDto<>(content,totalElements,totalPages,page,size);
  }
}
