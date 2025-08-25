package com.project.back_end.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;

@Service
public class CommonService {

    @Autowired
    private TokenService tokenService;

    public Map<String, Object> validateToken(String token, String role) {
        Map<String, Object> errors = new HashMap<>();
        
        try {
            boolean isValid = tokenService.validateToken(token, role);
            if (!isValid) {
                errors.put("error", "Invalid or expired token");
                errors.put("status", 401);
            }
        } catch (Exception e) {
            errors.put("error", "Token validation failed");
            errors.put("status", 500);
        }
        
        return errors;
    }
}
