package com.marcosbarbero.lab.sec.oauth.jwt.web;

import java.security.Principal;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {

    @GetMapping("/me")
    public ResponseEntity<Principal> get(final Principal principal) {
        return ResponseEntity.ok(principal);
    }

    @GetMapping("test")
    public ResponseEntity<String> getTesting(){
       return new ResponseEntity<String>("This is Testing", HttpStatus.OK);
    }
}

