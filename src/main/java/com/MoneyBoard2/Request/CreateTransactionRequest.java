package com.MoneyBoard2.Request;

import java.time.LocalDate;

import com.MoneyBoard2.Model.SezioneRisparmio;
import com.MoneyBoard2.Model.Utente;

public class CreateTransactionRequest 
{
	
	private String titolo;
	private String descrizione; 
	private Double importo; 
	private LocalDate Data; 
	private SezioneRisparmio sezione; 
	private Utente utente; 
	private boolean isEntrata; 
	
	public CreateTransactionRequest(String titolo, String descrizione, Double importo, LocalDate Data, SezioneRisparmio sezione, Utente utente)
	{
		this.titolo = titolo;
	    this.descrizione = descrizione;
	    this.importo = importo;
	    this.Data = Data;
	    this.sezione = sezione;
	    this.utente = utente;
	    this.isEntrata = true; 
	}
		
	public CreateTransactionRequest() { }

	public String getTitolo() {
		return titolo;
	}

	public void setTitolo(String titolo) {
		this.titolo = titolo;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public Double getImporto() {
		return importo;
	}

	public void setImporto(Double importo) {
		this.importo = importo;
	}

	public LocalDate getData() {
		return Data;
	}

	public void setData(LocalDate data) {
		Data = data;
	}

	public SezioneRisparmio getSezione() {
		return sezione;
	}

	public void setSezione(SezioneRisparmio sezione) {
		this.sezione = sezione;
	}

	public Utente getUtente() {
		return utente;
	}

	public void setUtente(Utente utente) {
		this.utente = utente;
	}

	public boolean isEntrata() {
		return isEntrata;
	}

	public void setEntrata(boolean isEntrata) {
		this.isEntrata = isEntrata;
	} 
	
	

}
