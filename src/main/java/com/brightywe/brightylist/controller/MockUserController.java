package com.brightywe.brightylist.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mockuser")
@PreAuthorize("hasAuthority('ROLE_USER')")
public class MockUserController {

    @GetMapping
    public String info() {
        return "User page - created by Zan";
    }
}
