package com.MoneyBoard2.Controller;

import com.MoneyBoard2.Model.Utente;
import com.MoneyBoard2.Repository.UtenteRepository;
import com.MoneyBoard2.Request.LoginRequest;
import com.MoneyBoard2.Request.UserCreateRequest;
import com.MoneyBoard2.Service.ServiceUtente;
import com.MoneyBoard2.Exception.BadLoginException;
import com.MoneyBoard2.Exception.CreationException;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Deprecated 
@RestController
@RequestMapping("/api/utenti")
@CrossOrigin(origins = "*") 
public class UtenteController {

    private final UtenteRepository repository;
    private final PasswordEncoder passwordEncoder; // Strumento per cifrare

    // Spring inietterà automaticamente il passwordEncoder che abbiamo definito in SecurityConfig
    public UtenteController(UtenteRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    public List<Utente> getAll() {
        return repository.findAll();
    }

    @PostMapping
    public ResponseEntity<Object> create(@RequestBody UserCreateRequest request) throws CreationException
    {
    	if(!ServiceUtente.validateInputCreate(request))
    	{
    		throw new CreationException("Problema nella creazione!");
    	}
    	
        String passwordCifrata = passwordEncoder.encode(request.getPassword());
        Utente utente2 = repository.findByemail(request.getEmail());
        if(utente2 != null) { throw new CreationException("Utente già esistente");  } 
        
        repository.save(new Utente(request.getUsername(), passwordCifrata, request.getEmail()));
        
        return ResponseEntity.ok().build();
    }
    
    @PostMapping("/login")
    public Utente login(@RequestBody LoginRequest login) throws BadLoginException 
    {
    	if(!ServiceUtente.validateInputLogin(login))
    	{
    		throw new BadLoginException("Problema nei dati inseriti, i campi Email e Password risultano vuoti");
    		
    	}
    	Utente utente = repository.findByemail(login.getEmail());

        if (utente != null) {
            if (passwordEncoder.matches(login.getPassword(), utente.getPassword())) 
            {
            	utente.setPassword(null); 
                return utente; 
            }
        }
        
        throw new BadLoginException("Email o password errati");
    }
}