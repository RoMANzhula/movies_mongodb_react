package org.romanzhula.movies_mongodb_react.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/by-role/moderator")
public class ModeratorController {

    @GetMapping
    @PreAuthorize("hasRole('MODERATOR')")
    public ResponseEntity<String> userSuccessAccess() {
        return ResponseEntity.ok("User access success!");
    }
}
