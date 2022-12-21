package com.example.auth.controller;

import com.example.auth.domain.RequestData;
import com.example.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthService authService;

    @RequestMapping(method = RequestMethod.POST, value = "/mail/send", produces = "application/json")
    public String mailAuth(@RequestBody RequestData data) {
        return authService.mailAuth(data.getEmail());
    }

}
