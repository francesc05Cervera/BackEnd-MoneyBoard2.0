package com.MoneyBoard2.Controller;

import com.MoneyBoard2.Model.SezioneRisparmio;
import com.MoneyBoard2.Repository.SezioneRisparmioRepository;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/sezioni")
public class SezioneRisparmioController {

    private final SezioneRisparmioRepository repository;

    public SezioneRisparmioController(SezioneRisparmioRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/utente/{utenteId}")
    public List<SezioneRisparmio> getPerUtente(@PathVariable Long utenteId) {
        return repository.findByUtenteId(utenteId);
    }

    @PostMapping
    public SezioneRisparmio create(@RequestBody SezioneRisparmio sezione) {
        return repository.save(sezione);
    }
}