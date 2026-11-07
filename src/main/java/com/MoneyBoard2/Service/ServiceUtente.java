package com.MoneyBoard2.Service;

import com.MoneyBoard2.Request.LoginRequest;
import com.MoneyBoard2.Request.UserCreateRequest;

import com.MoneyBoard2.Response.LoginResponse;
import com.MoneyBoard2.Response.RegisterResp;

import com.MoneyBoard2.Repository.UtenteRepository;
import com.MoneyBoard2.security.JwtService;

import javax.sound.midi.MidiDevice.Info;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.MoneyBoard2.Exception.BadLoginException;
import com.MoneyBoard2.Exception.InfoNotFound;
import com.MoneyBoard2.Exception.CreationException;
import com.MoneyBoard2.Model.Utente;

@Service 
public class ServiceUtente 
{
	
	private final PasswordEncoder passwordEncoder;
	private final JwtService jwtService;
	private final UtenteRepository utenteRepository;

	public ServiceUtente(
			PasswordEncoder passwordEncoder,
			JwtService jwtService,
			UtenteRepository utenteRepository) 
	{
		this.passwordEncoder = passwordEncoder;
		this.jwtService = jwtService;
		this.utenteRepository = utenteRepository;
	}

	public LoginResponse login(LoginRequest req) throws BadLoginException, InfoNotFound 
	{
		if (req.getEmail().isBlank() || req.getPassword().isBlank()) 
		{
			throw new InfoNotFound("Email or Password not found in request!");
		}

		Utente utente = utenteRepository.findByemail(req.getEmail());
		
		if (utente == null)
		{
			throw new BadLoginException("User not found!");
		}

		if (!passwordEncoder.matches(req.getPassword(), utente.getPassword())) 
		{
			throw new BadLoginException("Invalid password!");
		}

		String token = jwtService.generateToken(utente.getId());

		return new LoginResponse(token);
	}
	
	public RegisterResp register(UserCreateRequest req) throws CreationException, InfoNotFound
	{
		if (req.getEmail().isBlank() || req.getPassword().isBlank()) 
		{
			throw new InfoNotFound("Email or Password not found in request!");
		}

		if (utenteRepository.existsByEmail(req.getEmail())) 
		{
			throw new CreationException("User with this email already exists!");
		}

		String encodedPassword = passwordEncoder.encode(req.getPassword());
		Utente newUtente = new Utente();
		newUtente.setEmail(req.getEmail());
		newUtente.setPassword(encodedPassword);
		newUtente.setUsername(req.getUsername());
		utenteRepository.save(newUtente);

		return new RegisterResp("User registered successfully!");
	}
	

	public static boolean validateInputCreate(UserCreateRequest req) 
	{
		return req != null
				&& req.getUsername() != null && !req.getUsername().isBlank()
				&& req.getEmail() != null && !req.getEmail().isBlank()
				&& req.getPassword() != null && !req.getPassword().isBlank();
	}

	public static boolean validateInputLogin(LoginRequest req) 
	{
		return req != null
				&& req.getEmail() != null && !req.getEmail().isBlank()
				&& req.getPassword() != null && !req.getPassword().isBlank();
	}
}
