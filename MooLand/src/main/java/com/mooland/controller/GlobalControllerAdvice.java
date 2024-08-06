package com.mooland.controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.mooland.service.LoginRequest;

import jakarta.servlet.http.HttpSession;



@ControllerAdvice
public class GlobalControllerAdvice {

    @ModelAttribute
    public void addAttributes(Model model, HttpSession session) {
        String user = (String) session.getAttribute("user");
        
        if (user == null || user.isEmpty()) {
            model.addAttribute("loginRequest", new LoginRequest());
        } else {
            model.addAttribute("nickname", user);
        }
    }
}


