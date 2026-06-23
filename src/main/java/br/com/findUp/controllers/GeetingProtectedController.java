package br.com.findUp.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ws/greeting")
public class GeetingProtectedController {
    @GetMapping
    public ResponseEntity<String> getGreeting() {
        return ResponseEntity.ok("Hello World");
    }
}
