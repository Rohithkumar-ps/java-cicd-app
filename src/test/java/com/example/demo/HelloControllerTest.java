package com.example.demo;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class HelloControllerTest {

    private final HelloController controller = new HelloController();

    @Test
    void homeReturnsWelcomeMessage() {
        assertTrue(controller.home().contains("Jenkins"));
    }

    @Test
    void healthReturnsUp() {
        assertEquals("UP", controller.health());
    }

    @Test
    void versionReturnsVersion() {
        assertEquals("1.0.0", controller.version());
    }
}
