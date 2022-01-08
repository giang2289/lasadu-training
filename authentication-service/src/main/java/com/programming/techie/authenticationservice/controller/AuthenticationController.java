package com.programming.techie.authenticationservice.controller;

import com.google.common.collect.ImmutableMap;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {
    @GetMapping("/")
    public Map<String, String> home(@AuthenticationPrincipal DefaultOAuth2User user) {
        return ImmutableMap.of("message", "You are logged in, " + user.getName() + "!");
    }

    @GetMapping("/token")
    public OAuth2AccessToken token(@RegisteredOAuth2AuthorizedClient("test-client") OAuth2AuthorizedClient testClient) {
        return testClient.getAccessToken();
    }
}
