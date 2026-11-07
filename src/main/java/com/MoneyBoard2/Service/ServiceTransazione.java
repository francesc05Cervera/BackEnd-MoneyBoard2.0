package com.MoneyBoard2.Service;

import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.MoneyBoard2.Exception.CreationException;
import com.MoneyBoard2.Exception.DeleteException;
import com.MoneyBoard2.Exception.InfoNotFound;
import com.MoneyBoard2.Model.SezioneRisparmio;
import com.MoneyBoard2.Model.Transazione;
import com.MoneyBoard2.Model.Utente;
import com.MoneyBoard2.Repository.SezioneRisparmioRepository;
import com.MoneyBoard2.Repository.TransazioneRepository;
import com.MoneyBoard2.Repository.UtenteRepository;
import com.MoneyBoard2.Request.CreateTransactionRequest;
import com.MoneyBoard2.security.JwtService;

@Service
public class ServiceTransazione {

    private final TransazioneRepository transazioneRepository;
    private final SezioneRisparmioRepository sezioneRepository;
    private final UtenteRepository utenteRepository;
    private final JwtService jwtService;

    public ServiceTransazione(
            TransazioneRepository transazioneRepository,
            SezioneRisparmioRepository sezioneRepository,
            UtenteRepository utenteRepository,
            JwtService jwtService) {
        this.transazioneRepository = transazioneRepository;
        this.sezioneRepository = sezioneRepository;
        this.utenteRepository = utenteRepository;
        this.jwtService = jwtService;
    }

    public List<Transazione> getTransazioniUser(String token) throws InfoNotFound {
        Long userId = getAuthenticatedUserId(token);
        return transazioneRepository.findByUtenteId(userId);
    }

    public List<Transazione> getTransazioniSezione(String token, Long sezioneId) throws InfoNotFound {
        Long userId = getAuthenticatedUserId(token);
        if (sezioneId == null) {
            throw new InfoNotFound("ID della sezione non trovato!");
        }
        return transazioneRepository.findByUtenteIdAndSezioneId(userId, sezioneId);
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean create(CreateTransactionRequest request, String token)
            throws CreationException, InfoNotFound {
        Long userId = getAuthenticatedUserId(token);

        if (request == null
                || request.getTitolo() == null
                || request.getTitolo().isBlank()
                || request.getImporto() == null
                || !Double.isFinite(request.getImporto())
                || request.getImporto() <= 0
                || request.getData() == null
                || request.getSezione() == null
                || request.getSezione().getNome() == null
                || request.getSezione().getNome().isBlank()) {
            throw new InfoNotFound("Titolo, importo, data o sezione non validi nella richiesta!");
        }

        SezioneRisparmio sezione = sezioneRepository.findBynome(request.getSezione().getNome());
        if (sezione == null) {
            throw new InfoNotFound("Sezione non trovata!");
        }
        if (sezione.getUtente() == null || !Objects.equals(sezione.getUtente().getId(), userId)) {
            throw new InfoNotFound("La sezione non appartiene all'utente autenticato!");
        }
        if (sezione.getSaldo() == null || !validateCreation(sezione, request)) {
            throw new CreationException("Saldo in sezione insufficiente o non valido!");
        }

        Utente utente = utenteRepository.findById(userId)
                .orElseThrow(() -> new InfoNotFound("Utente non trovato!"));

        Transazione transazione = new Transazione();
        transazione.setTitolo(request.getTitolo());
        transazione.setDescrizione(request.getDescrizione());
        transazione.setData(request.getData());
        transazione.setImporto(request.getImporto());
        transazione.setIsEntrata(request.isEntrata());
        transazione.setUtente(utente);
        transazione.setSezione(sezione);

        Double nuovoSaldo = ServiceSezione.aggiornaSaldoEntrata(
                transazione.getIsEntrata(), transazione.getImporto(), sezione.getSaldo());
        if (nuovoSaldo < 0) {
            throw new CreationException("Saldo in sezione insufficiente!");
        }

        sezione.setSaldo(nuovoSaldo);
        sezioneRepository.save(sezione);
        transazioneRepository.save(transazione);
        return true;
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean delete(Long id, String token) throws DeleteException, InfoNotFound {
        Long userId = getAuthenticatedUserId(token);
        if (id == null) {
            throw new InfoNotFound("ID della transazione non trovato!");
        }

        Transazione transazione = transazioneRepository.findById(id)
                .orElseThrow(() -> new InfoNotFound("Transazione non trovata!"));
        SezioneRisparmio sezione = transazione.getSezione();

        if (transazione.getUtente() == null
                || !Objects.equals(transazione.getUtente().getId(), userId)
                || sezione == null
                || sezione.getUtente() == null
                || !Objects.equals(sezione.getUtente().getId(), userId)) {
            throw new InfoNotFound("La transazione non appartiene all'utente autenticato!");
        }
        Double saldo = sezione.getSaldo();
        Double importo = transazione.getImporto();
        if (saldo == null || !Double.isFinite(saldo) || saldo < 0
                || importo == null || !Double.isFinite(importo) || importo <= 0) {
            throw new DeleteException("Saldo o importo della transazione non valido!");
        }

        Double nuovoSaldo = ServiceSezione.aggiornaSaldoUscita(
                transazione.getIsEntrata(), importo, saldo);
        if (!Double.isFinite(nuovoSaldo) || nuovoSaldo < 0) {
            throw new DeleteException("La cancellazione porterebbe il saldo a un valore non valido!");
        }
        if (Double.compare(nuovoSaldo, saldo) == 0) {
            throw new DeleteException("Errore durante l'aggiornamento del saldo!");
        }

        sezione.setSaldo(nuovoSaldo);
        sezioneRepository.save(sezione);
        transazioneRepository.delete(transazione);
        return true;
    }

    private Long getAuthenticatedUserId(String token) throws InfoNotFound {
        if (token == null || token.isBlank() || !jwtService.isTokenValid(token)) {
            throw new InfoNotFound("Token non valido!");
        }
        return jwtService.getUserIdFromToken(token);
    }

    public static boolean validateCreation(SezioneRisparmio sezione, CreateTransactionRequest transazione) {
        if (sezione == null
                || transazione == null
                || sezione.getSaldo() == null
                || transazione.getImporto() == null
                || !Double.isFinite(transazione.getImporto())
                || transazione.getImporto() <= 0) {
            return false;
        }
        return transazione.isEntrata() || transazione.getImporto() <= sezione.getSaldo();
    }
}
