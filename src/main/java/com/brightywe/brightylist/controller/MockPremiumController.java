package com.brightywe.brightylist.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mockpremium")
public class MockPremiumController {
    
    @GetMapping
    public String info() {
        return "Premium - created by Zan";
    }
}
