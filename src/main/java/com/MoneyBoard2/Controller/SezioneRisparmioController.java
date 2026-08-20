package com.MoneyBoard2.Controller;

import com.MoneyBoard2.Exception.CreationException;
import com.MoneyBoard2.Exception.DeleteException;
import com.MoneyBoard2.Model.SezioneRisparmio;
import com.MoneyBoard2.Repository.SezioneRisparmioRepository;
import com.MoneyBoard2.Request.DeleteSezioneRequest;
import com.MoneyBoard2.Request.SectionCreateRequest;
import com.MoneyBoard2.Service.ServiceSezione;

import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity.BodyBuilder;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/sezioni")
@CrossOrigin(origins = "*")
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
    	
    	repository.save(nuova); 
    	
    	return ResponseEntity.ok(nuova); 
    }
    
    @DeleteMapping
    public ResponseEntity<Void> delete(@RequestBody DeleteSezioneRequest request) throws DeleteException
    {

        if (request == null || request.getNome() == null || request.getNome().isBlank()) {
            return ResponseEntity.badRequest().build();
        }

        SezioneRisparmio sezione = repository.findBynome(request.getNome());

        if(!ServiceSezione.validateDelete(sezione))
        {
        	throw new DeleteException("Impossibile eliminare la sezione!");
        }
        
        if (sezione == null) {
            return ResponseEntity.notFound().build();
        }

        repository.delete(sezione);
        return ResponseEntity.noContent().build();
    }
    
 
}
