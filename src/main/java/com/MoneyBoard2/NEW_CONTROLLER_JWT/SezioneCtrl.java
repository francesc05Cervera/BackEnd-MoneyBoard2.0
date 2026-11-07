package com.MoneyBoard2.NEW_CONTROLLER_JWT;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.http.ResponseEntity;
import com.MoneyBoard2.Service.ServiceSezione;
import org.springframework.web.bind.annotation.RequestBody;
import com.MoneyBoard2.Request.SectionCreateRequest;

import com.MoneyBoard2.Exception.CreationException;
import com.MoneyBoard2.Exception.DeleteException;
import com.MoneyBoard2.Exception.InfoNotFound;

import java.util.List;
import com.MoneyBoard2.Model.SezioneRisparmio;

@RestController 
@RequestMapping("/api2/sezione")
@CrossOrigin(origins = "*")
public class SezioneCtrl 
{
    private final ServiceSezione service;

    public SezioneCtrl(ServiceSezione service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<SezioneRisparmio>> getSezioniUser(@RequestHeader("Authorization") String token)
    {
        try
        {
        return ResponseEntity.ok(service.getSezioniUser(token));
        }
        catch (Exception e)
        {
            return ResponseEntity.status(500).build(); 
        }
    }

    @PostMapping("/create")
    public ResponseEntity<Object> create(@RequestHeader("Authorization") String token, 
                                         @RequestBody SectionCreateRequest req)
    {
        try
        {
            boolean created = service.create(req, token);
            if (created) 
            {
                return ResponseEntity.ok("Sezione creata con successo!");
            } 
            else
            {
                return ResponseEntity.status(500).body("Errore server sconosciuto");
            }
        } 
        catch(CreationException e)
        {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        catch(InfoNotFound e)
        {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        catch (Exception e) 
        {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    @DeleteMapping 
    public ResponseEntity<Object> delete(@RequestHeader("Authorization") String token, 
                                         @RequestBody SectionCreateRequest req)
    {
        try
        {
            boolean deleted = service.delete(req, token);
            if (deleted) 
            {
                return ResponseEntity.ok("Sezione eliminata con successo!");
            } 
            else
            {
                return ResponseEntity.status(500).body("Errore server sconosciuto");
            }
        } 
        catch(InfoNotFound e)
        {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        catch(DeleteException e)
        {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        catch (Exception e) 
        {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }


    
}
