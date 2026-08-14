package com.akashf.springv4.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebViewController {

    @GetMapping("/notify-push")
    public String showTestPanel(Model model) {
        // You can inject backend data properties into the UI view context here if
        // needed
        model.addAttribute("pageTitle", "FCM Integration Diagnostic Panel");
        return "index"; // Maps directly to src/main/resources/templates/index.html
    }
}
