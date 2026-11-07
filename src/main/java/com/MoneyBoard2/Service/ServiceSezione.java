package com.MoneyBoard2.Service;

import java.util.*;

import org.springframework.stereotype.Service;
import com.MoneyBoard2.Model.*;
import com.MoneyBoard2.Repository.*;
import com.MoneyBoard2.security.JwtService; 
import com.MoneyBoard2.Exception.*;

import com.MoneyBoard2.Request.*;

@Service 
public class ServiceSezione 
{
	private final JwtService jwtService;
	private final SezioneRisparmioRepository repo;
	private final UtenteRepository utenteRepo;

	public ServiceSezione(
			JwtService jwtService,
			SezioneRisparmioRepository repo,
			UtenteRepository utenteRepo) 
	{
		this.jwtService = jwtService;
		this.repo = repo;
		this.utenteRepo = utenteRepo;
	}

	public List<SezioneRisparmio> getSezioniUser(String token) throws Exception
	{
		List<SezioneRisparmio> sezioni = new ArrayList<SezioneRisparmio>(); 
		
			if(jwtService.isTokenValid(token))
			{
				sezioni = repo.findByUtenteId(jwtService.getUserIdFromToken(token)); 
				return sezioni; 
			}
			else
			{
				throw new Exception("Token non valido!");
			}
	}

	public boolean create(SectionCreateRequest req, String token) throws CreationException, InfoNotFound, Exception
	{
		if (token.isBlank() || req.getNome().isBlank() || req.getObbiettivo() == null)
		{
			throw new InfoNotFound("Token, Nome o Obiettivo non trovati nella richiesta!");
		}

		if(jwtService.isTokenValid(token))
		{
			
			if(repo.existsByNome(req.getNome()))
			{
				throw new CreationException("Sezione( già presente con il nome: " + req.getNome()); 
			}

			Utente utCreator = utenteRepo.findById(jwtService.getUserIdFromToken(token))
					.orElseThrow(() -> new InfoNotFound("Utente non trovato!"));

			SezioneRisparmio sezione = new SezioneRisparmio(); 
			
			sezione.setNome(req.getNome());
			sezione.setObiettivo(req.getObbiettivo());
			sezione.setUtente(utCreator);
			sezione.setSaldo(0.00d);
	
			repo.save(sezione); 
			
			return true; 
		}
		else
		{
			throw new Exception("Token non valido!");
		}

	
		
	}

	public boolean delete(SectionCreateRequest req, String token) throws DeleteException, InfoNotFound, Exception
	{
		if (req == null || req.getNome() == null || req.getNome().isBlank() || token == null || token.isBlank())
		{
			throw new InfoNotFound("Token o nome della sezione non trovato nella richiesta!");
		}

		if (!jwtService.isTokenValid(token))
		{
			throw new Exception("Token non valido!");
		}

		SezioneRisparmio sezione = repo.findBynome(req.getNome());
		if (sezione == null)
		{
			throw new InfoNotFound("Sezione non trovata!");
		}

		Long userId = jwtService.getUserIdFromToken(token);
		if (sezione.getUtente() == null || !Objects.equals(sezione.getUtente().getId(), userId))
		{
			throw new InfoNotFound("La sezione non appartiene all'utente autenticato!");
		}

		if (!validateDelete(sezione))
		{
			throw new DeleteException("Impossibile eliminare una sezione con transazioni!");
		}

		repo.delete(sezione);
		return true;
	}


	public static boolean validateDelete(SezioneRisparmio sezione)
	{
		return sezione != null
				&& (sezione.getTransazioni() == null || sezione.getTransazioni().isEmpty());
	}

	public static Double aggiornaSaldoEntrata(boolean entrata, Double importo, Double saldo)
	{
		if(entrata)
		{
			Double saldo_nuovo = saldo + importo; 
			
			if(saldo_nuovo > saldo)
			{
				return saldo_nuovo;
			}
		}
		else 
		{
				Double saldo_nuovo = saldo - importo;
				
				if(saldo_nuovo < 0)
				{
					return -1.00; 
				}
				
				return saldo_nuovo;
		}
		
		return saldo;
	}
	
	public static Double aggiornaSaldoUscita(boolean entrata, Double importo, Double saldo)
	{
		
		if(entrata)
		{
			Double newSaldo = saldo - importo; 
			
				if(newSaldo < saldo) { return newSaldo; }
		}
		else {
			Double newSaldo = saldo + importo;
			
				if(newSaldo > saldo) { return newSaldo; }
		}
		
		return saldo; 
	
	
	}
}
