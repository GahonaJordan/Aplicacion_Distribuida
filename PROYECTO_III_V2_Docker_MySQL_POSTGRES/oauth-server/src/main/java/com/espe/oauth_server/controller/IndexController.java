package com.espe.oauth_server.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Collections;
import java.util.Map;

@RestController
public class IndexController {

    @GetMapping("/")
    public RedirectView home() {
        String authorizeUrl = "http://127.0.0.1:9000/oauth2/authorize?response_type=code&client_id=app-category&scope=openid%20profile%20read%20write&redirect_uri=http://127.0.0.1:9000/authorized";
        return new RedirectView(authorizeUrl);
    }

    @GetMapping("/authorized")
    public Map<String, String> authorize(@RequestParam String code) {
        return Collections.singletonMap("authorizationCode", code);
    }
}