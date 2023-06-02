package com.pullanner.global.auth.oauth2.handler;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DefaultOAuth2LoginPageHandler {

    @GetMapping("/login/default")
    public String defaultOAuth2LoginPage() {
        return "redirect:/";
    }
}
