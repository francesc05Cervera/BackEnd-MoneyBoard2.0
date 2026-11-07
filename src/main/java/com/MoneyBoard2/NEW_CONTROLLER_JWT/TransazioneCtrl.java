package com.MoneyBoard2.NEW_CONTROLLER_JWT;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.MoneyBoard2.Exception.CreationException;
import com.MoneyBoard2.Exception.DeleteException;
import com.MoneyBoard2.Exception.InfoNotFound;
import com.MoneyBoard2.Model.Transazione;
import com.MoneyBoard2.Request.CreateTransactionRequest;
import com.MoneyBoard2.Service.ServiceTransazione;

@RestController
@RequestMapping("/api2/transazione")
@CrossOrigin(origins = "*")
public class TransazioneCtrl {

    private final ServiceTransazione service;

    public TransazioneCtrl(ServiceTransazione service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<Transazione>> getTransazioniUser(
            @RequestHeader("Authorization") String token) {
        try {
            return ResponseEntity.ok(service.getTransazioniUser(token));
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping("/sezione/{sezioneId}")
    public ResponseEntity<List<Transazione>> getTransazioniSezione(
            @RequestHeader("Authorization") String token,
            @PathVariable Long sezioneId) {
        try {
            return ResponseEntity.ok(service.getTransazioniSezione(token, sezioneId));
        } catch (InfoNotFound e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    @PostMapping("/create")
    public ResponseEntity<Object> create(
            @RequestHeader("Authorization") String token,
            @RequestBody CreateTransactionRequest request) {
        try {
            boolean created = service.create(request, token);
            if (created) {
                return ResponseEntity.ok("Transazione creata con successo!");
            }
            return ResponseEntity.status(500).body("Errore server sconosciuto");
        } catch (CreationException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (InfoNotFound e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(
            @RequestHeader("Authorization") String token,
            @PathVariable Long id) {
        try {
            boolean deleted = service.delete(id, token);
            if (deleted) {
                return ResponseEntity.ok("Transazione eliminata con successo!");
            }
            return ResponseEntity.status(500).body("Errore server sconosciuto");
        } catch (DeleteException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (InfoNotFound e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }
}
