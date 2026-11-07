package com.MoneyBoard2.NEW_CONTROLLER_JWT;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.http.ResponseEntity;

@RestController 
@RequestMapping("/api2/ping")
public class Ping 
{
    @GetMapping
    public ResponseEntity<String> ping() 
    {
        return ResponseEntity.ok("pong");
    }
    
}
