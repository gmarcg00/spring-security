package dev.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping(path = "/account")
public class AccountsController {
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public Map<String,String> accounts(){
        return Collections.singletonMap("msj","accounts");
    }
}
