package com.MoneyBoard2.NEW_CONTROLLER_JWT;

import com.MoneyBoard2.Response.LoginResponse;
import com.MoneyBoard2.Request.LoginRequest;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.ResponseEntity;

import com.MoneyBoard2.Exception.*;
import com.MoneyBoard2.Request.UserCreateRequest;
import com.MoneyBoard2.Response.RegisterResp;


import com.MoneyBoard2.Service.ServiceUtente;

@RestController 
@RequestMapping("/api2/user")
@CrossOrigin(origins = {"https://moneyboard-klhv.onrender.com", "http://localhost:4200"})
public class UsrCtrl 
{
   private ServiceUtente service; 
   
   public UsrCtrl(ServiceUtente service) 
   {
       this.service = service; 
   }

   @PostMapping("/login")
   public ResponseEntity<LoginResponse> loginUser(@RequestBody LoginRequest request)
   {
        try
        {
            LoginResponse response = service.login(request);
            return ResponseEntity.ok(response);
        }
        catch(InfoNotFound ex)
        {
            return ResponseEntity.badRequest().build(); 
        }
        catch(BadLoginException ex)
        {
            return ResponseEntity.status(401).build(); 
        }
   }

   @PostMapping ("/register")
   public ResponseEntity<RegisterResp> registerUser(@RequestBody UserCreateRequest request)
   {
        try
        {
            RegisterResp response = service.register(request);
            return ResponseEntity.ok(response);
        }
        catch(CreationException ex)
        {
            return ResponseEntity.status(400).build();
        }
        catch(InfoNotFound ex)
        {
            return ResponseEntity.badRequest().build();
        }
   }

}

