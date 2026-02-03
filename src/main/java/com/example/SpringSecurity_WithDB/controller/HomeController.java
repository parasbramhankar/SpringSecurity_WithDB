package com.example.SpringSecurity_WithDB.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {
    @GetMapping("/")
    public String welcome(){
        return "hello sir, welcome to our office";
    }

    @GetMapping("/user")
    public String user(){
        return "hello user...";
    }

    @GetMapping("/admin")
    public String admin(){
        return "hello admin...";
    }
}
