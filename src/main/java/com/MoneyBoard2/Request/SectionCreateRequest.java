package com.MoneyBoard2.Request;

import com.MoneyBoard2.Model.Utente;


public class SectionCreateRequest 
{
	
	private String Nome; 
	private Double Obbiettivo;
	private Utente Utente; 
	
	public SectionCreateRequest(String Nome, Double Obbiettivo, Utente Utente) 
	{
		this.Nome=Nome; 
		this.Obbiettivo = Obbiettivo;
		this.Utente=Utente;
	}
	
	public SectionCreateRequest() { }

	public String getNome() {
		return Nome;
	}

	public void setNome(String nome) {
		Nome = nome;
	}

	public Double getObbiettivo() {
		return Obbiettivo;
	}

	public void setObbiettivo(Double obbiettivo) {
		Obbiettivo = obbiettivo;
	}

	public Utente getUtente() {
		return Utente;
	}

	public void setUtente(Utente utente) {
		Utente = utente;
	}
	
	

}
