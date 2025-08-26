package com.project.back_end.mvc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import com.project.back_end.services.ServiceClass;

import java.util.Map;

@Controller
public class DashboardController {

    @Autowired
    private ServiceClass service;

    // === ADMIN DASHBOARD ===
    @GetMapping("/adminDashboard/{token}")
    public String adminDashboard(@PathVariable String token) {
        ResponseEntity<Map<String, String>> response = service.validateToken(token, "admin");

        if (response.getStatusCode().is2xxSuccessful()) {
            return "admin/adminDashboard"; // vue Thymeleaf
        } else {
            return "redirect:/"; // renvoie vers login
        }
    }

    // === DOCTOR DASHBOARD ===
    @GetMapping("/doctorDashboard/{token}")
    public String doctorDashboard(@PathVariable String token) {
        ResponseEntity<Map<String, String>> response = service.validateToken(token, "doctor");

        if (response.getStatusCode().is2xxSuccessful()) {
            return "doctor/doctorDashboard"; // vue Thymeleaf
        } else {
            return "redirect:/"; // renvoie vers login
        }
    }
}
