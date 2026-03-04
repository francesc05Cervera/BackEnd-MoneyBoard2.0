package com.MoneyBoard2.Controller;

import com.MoneyBoard2.Model.SezioneRisparmio;
import com.MoneyBoard2.Model.Transazione;
import com.MoneyBoard2.Repository.SezioneRisparmioRepository;
import com.MoneyBoard2.Repository.TransazioneRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/transazioni")
public class TransazioneController {

    private final TransazioneRepository transazioneRepository;
    private final SezioneRisparmioRepository sezioneRepository; // Aggiunto!

    // Il costruttore ora deve avere ENTRAMBI i repository
    public TransazioneController(TransazioneRepository transazioneRepository, 
                                 SezioneRisparmioRepository sezioneRepository) {
        this.transazioneRepository = transazioneRepository;
        this.sezioneRepository = sezioneRepository;
    }

    @GetMapping("/utente/{utenteId}")
    public List<Transazione> getPerUtente(@PathVariable Long utenteId) {
        return transazioneRepository.findByUtenteId(utenteId);
    }

    @GetMapping("/utente/{utenteId}/sezione/{sezioneId}")
    public List<Transazione> getPerUtenteESezione(@PathVariable Long utenteId, @PathVariable Long sezioneId) {
        return transazioneRepository.findByUtenteIdAndSezioneId(utenteId, sezioneId);
    }

    @PostMapping
    public Transazione create(@RequestBody Transazione transazione) {
        // 1. Salviamo la transazione
        Transazione salvata = transazioneRepository.save(transazione);

        // 2. Aggiorniamo il saldo della sezione
        if (salvata.getSezione() != null) {
            // Recuperiamo la sezione aggiornata dal database per sicurezza
            SezioneRisparmio sezione = sezioneRepository.findById(salvata.getSezione().getId()).orElse(null);
            
            if (sezione != null) {
                // Nuovo Saldo = Saldo Attuale + Importo Transazione
                sezione.setSaldo(sezione.getSaldo() + salvata.getImporto());
                sezioneRepository.save(sezione);
            }
        }
        return salvata;
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        Optional<Transazione> transOpt = transazioneRepository.findById(id);
        
        if (transOpt.isPresent()) {
            Transazione trans = transOpt.get();
            SezioneRisparmio sezione = trans.getSezione();

            if (sezione != null) {
                // Sottraiamo l'importo prima di eliminare la transazione
                sezione.setSaldo(sezione.getSaldo() - trans.getImporto());
                sezioneRepository.save(sezione);
            }
            transazioneRepository.deleteById(id);
        }
    }
}