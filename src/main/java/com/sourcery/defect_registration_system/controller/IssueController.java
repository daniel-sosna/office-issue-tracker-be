package com.sourcery.defect_registration_system.controller;

import com.sourcery.defect_registration_system.dto.IssueResponseDto;
import com.sourcery.defect_registration_system.dto.PageResponseDto;
import com.sourcery.defect_registration_system.service.IssueService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@RequestMapping("/issues")
public class IssueController {
  private final IssueService issueService;

  @GetMapping
  public PageResponseDto<IssueResponseDto> getAllIssuesPaginated(
      @RequestParam(defaultValue = "1") int page,
      @RequestParam(defaultValue = "10") int size) {
    return issueService.getAllIssues(page, size);
  }

}
