package com.brightywe.brightylist.email;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/send")
public class EmailController {

    @Autowired
    private EmailService emailService;

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public EmailDto send(@RequestBody EmailDto mailDto) throws IOException {
        return emailService.send(mailDto);
    }
}
