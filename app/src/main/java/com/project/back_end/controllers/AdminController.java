package com.project.back_end.controllers;

import com.project.back_end.models.Admin;
import com.project.back_end.services.ServiceClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("${api.path}admin")
public class AdminController {

    private final ServiceClass service;

    @Autowired
    public AdminController(ServiceClass service) {
        this.service = service;
    }

    // -------------------------
    // Admin login
    // -------------------------
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> adminLogin(@RequestBody Admin admin) {
        return service.validateAdmin(admin);
    }
}
