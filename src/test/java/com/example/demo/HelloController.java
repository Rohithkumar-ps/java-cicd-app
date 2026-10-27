cat > src/main/java/com/example/demo/HelloController.java <<'EOF'
package com.example.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/")
    public String home() {
        return "Hello! Java CI/CD application is running.";
    }

    @GetMapping("/health")
    public String health() {
        return "UP";
    }
}
EOF
