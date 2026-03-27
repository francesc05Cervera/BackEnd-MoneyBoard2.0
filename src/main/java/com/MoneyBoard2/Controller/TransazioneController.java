package com.MoneyBoard2.Controller;

import com.MoneyBoard2.Exception.NotFoundException;
import com.MoneyBoard2.Model.SezioneRisparmio;
import com.MoneyBoard2.Model.Transazione;
import com.MoneyBoard2.Model.Utente;
import com.MoneyBoard2.Repository.SezioneRisparmioRepository;
import com.MoneyBoard2.Repository.TransazioneRepository;
import com.MoneyBoard2.Repository.UtenteRepository;
import com.MoneyBoard2.Request.CreateTransactionRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/transazioni")
public class TransazioneController {

    private final TransazioneRepository transazioneRepository;
    private final SezioneRisparmioRepository sezioneRepository; 
    private final UtenteRepository utenteRepository;
  
    public TransazioneController(TransazioneRepository transazioneRepository, 
                                 SezioneRisparmioRepository sezioneRepository,
                                 UtenteRepository  utenteRepository) 
    {
        this.transazioneRepository = transazioneRepository;
        this.sezioneRepository = sezioneRepository;
        this.utenteRepository = utenteRepository;
    }

    @GetMapping("/utente/{utenteId}")
    public List<Transazione> getPerUtente(@PathVariable Long utenteId) {
        return transazioneRepository.findByUtenteId(utenteId);
    }

    @GetMapping("/utente/{utenteId}/sezione/{sezioneId}")
    public List<Transazione> getPerUtenteESezione(@PathVariable Long utenteId, @PathVariable Long sezioneId) 
    {
        return transazioneRepository.findByUtenteIdAndSezioneId(utenteId, sezioneId);
    }

    @PostMapping
    public ResponseEntity<Transazione> create(@RequestBody CreateTransactionRequest request) throws NotFoundException
    {
    	SezioneRisparmio section = sezioneRepository.findBynome(request.getSezione().getNome()); 
    	if(section == null)
    	{ 
    		throw new NotFoundException("Sezione "+ request.getSezione().getNome() + " inesistente!"); 
    	}
    	
    	Utente utente = utenteRepository.findByemail(request.getUtente().getEmail()); 
    	
    	if(utente == null)
    	{
    		throw new NotFoundException("Utente con mail " + request.getUtente().getEmail() + " inesistente!");
    	}
    	
    	Transazione transazione = createItem(request, section); 
    	
    	if(!transazione.getIsEntrata())
    	{
    		Double saldo = section.getSaldo() - transazione.getImporto(); 
    		section.setSaldo(saldo);
    		transazioneRepository.save(transazione); 
    		sezioneRepository.save(section);
    	}
    	else
    	{
    		Double saldo = section.getSaldo() + transazione.getImporto(); 
    		section.setSaldo(saldo);
    		transazioneRepository.save(transazione); 
    		sezioneRepository.save(section);
    	}
    	
    	return ResponseEntity.ok(transazione);
    	
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        Optional<Transazione> transOpt = transazioneRepository.findById(id);
        
        if (transOpt.isPresent()) {
            Transazione trans = transOpt.get();
            SezioneRisparmio sezione = trans.getSezione();

            if (sezione != null) {
                if (trans.getIsEntrata()) {
                    sezione.setSaldo(sezione.getSaldo() - trans.getImporto());  // Entrata → sottrai dal saldo
                } else {
                    sezione.setSaldo(sezione.getSaldo() + trans.getImporto());  // Uscita → aggiungi al saldo (annulla)
                }
               
             sezioneRepository.save(sezione);
             transazioneRepository.delete(trans);
            
            }
        }
        
    }
    
    public Transazione createItem(CreateTransactionRequest request, SezioneRisparmio section)
    {
    	Transazione transazione = new Transazione();
    	transazione.setTitolo(request.getTitolo());
    	transazione.setDescrizione(request.getDescrizione());
    	transazione.setData(request.getData());
    	transazione.setImporto(request.getImporto());
    	transazione.setIsEntrata(request.isEntrata());
    	transazione.setUtente(request.getUtente());
    	transazione.setSezione(section);
    	return transazione;
    }
}