package com.sourcery.defect_registration_system.user.controller;

import com.sourcery.defect_registration_system.user.dto.AllUserDto;
import com.sourcery.defect_registration_system.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/getAllUsers")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public List<AllUserDto> getAllUsers(){
        return userService.getAllUsersForDropdown();
    }
}
