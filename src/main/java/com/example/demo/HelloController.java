package com.example.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/")
    public String home() {
        return "Hello from Java CI/CD with Jenkins, Helm and Argo CD!";
    }

    @GetMapping("/health")
    public String health() {
        return "UP";
    }

    @GetMapping("/version")
    public String version() {
        return "1.0.0";
    }
}
