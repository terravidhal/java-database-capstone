package com.project.back_end.mvc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import com.project.back_end.services.Service;

import java.util.Map;

@Controller
public class DashboardController {

    @Autowired
    private Service service;

    // === ADMIN DASHBOARD ===
    @GetMapping("/adminDashboard/{token}")
    public String adminDashboard(@PathVariable String token) {
        Map<String, Object> errors = service.validateToken(token, "admin");

        if (errors.isEmpty()) {
            return "admin/adminDashboard"; // vue Thymeleaf
        } else {
            return "redirect:/"; // renvoie vers login
        }
    }

    // === DOCTOR DASHBOARD ===
    @GetMapping("/doctorDashboard/{token}")
    public String doctorDashboard(@PathVariable String token) {
        Map<String, Object> errors = service.validateToken(token, "doctor");

        if (errors.isEmpty()) {
            return "doctor/doctorDashboard"; // vue Thymeleaf
        } else {
            return "redirect:/"; // renvoie vers login
        }
    }
}
