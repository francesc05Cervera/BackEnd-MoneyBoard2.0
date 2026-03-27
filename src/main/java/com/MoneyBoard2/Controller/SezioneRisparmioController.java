package com.MoneyBoard2.Controller;

import com.MoneyBoard2.Exception.CreationException;
import com.MoneyBoard2.Model.SezioneRisparmio;
import com.MoneyBoard2.Repository.SezioneRisparmioRepository;
import com.MoneyBoard2.Request.SectionCreateRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity.BodyBuilder;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/sezioni")
public class SezioneRisparmioController 
{

    private final SezioneRisparmioRepository repository;

    public SezioneRisparmioController(SezioneRisparmioRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/utente/{utenteId}")
    public List<SezioneRisparmio> getPerUtente(@PathVariable Long utenteId) {
        return repository.findByUtenteId(utenteId);
    }

    @PostMapping
    public ResponseEntity<Object> create(@RequestBody SectionCreateRequest request) throws CreationException
    {
    	SezioneRisparmio exist = repository.findBynome(request.getNome()); 
    	if(exist != null)
    	{
    		throw new CreationException("Sezione già presente con il nome: " + request.getNome()); 
    	}
    	
    	SezioneRisparmio nuova = new SezioneRisparmio(); 
    	nuova.setNome(request.getNome());
    	nuova.setObiettivo(request.getObbiettivo()); 
    	nuova.setSaldo(0.00d);
    	nuova.setUtente(request.getUtente());
    	
    	return ResponseEntity.ok(nuova); 
    }
    
    @DeleteMapping
    public ResponseEntity<Void> delete(@RequestBody SezioneRisparmio sezione) {

        if (sezione == null || sezione.getNome() == null) {
            return ResponseEntity.badRequest().build();
        }

        boolean deleted = repository.deleteByNome(sezione.getNome());

        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}