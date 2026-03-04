package com.MoneyBoard2.Controller;

import com.MoneyBoard2.Model.Utente;
import com.MoneyBoard2.Repository.UtenteRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.util.List;

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
    public Utente create(@RequestBody Utente utente) {
        String passwordCifrata = passwordEncoder.encode(utente.getPassword());
        utente.setPassword(passwordCifrata);
        
        return repository.save(utente);
    }
    
    @PostMapping("/login")
    public Utente login(@RequestBody Utente loginRequest) {

    	Utente utente = repository.findByUsername(loginRequest.getUsername());

        if (utente != null) {
            // 2. Usiamo BCrypt per confrontare la password inserita con quella cifrata nel DB
            // NON si usa "equals", perché una è in chiaro e l'altra è un hash!
            if (passwordEncoder.matches(loginRequest.getPassword(), utente.getPassword())) {
                return utente; // Login successo! Restituiamo l'utente
            }
        }
        
        // 3. Se arriviamo qui, qualcosa è andato storto
        throw new RuntimeException("Credenziali non valide");
    }
}
