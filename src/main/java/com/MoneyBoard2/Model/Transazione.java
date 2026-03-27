package com.MoneyBoard2.Model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity 
@Table(name = "transazioni")
public class Transazione {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; 

    private String titolo;
    private String descrizione;
    private Double importo; 
    private boolean isEntrata; 
    private LocalDate data; 

    @ManyToOne 
    @JoinColumn(name = "sezione_id")
    private SezioneRisparmio sezione; 

    @ManyToOne
    @JoinColumn(name = "utente_id")
    private Utente utente;

    // --- COSTRUTTORI ---

    // Obbligatorio per JPA
    public Transazione() {
    }

    public Transazione(String titolo, String descrizione, Double importo, LocalDate data, SezioneRisparmio sezione, Utente utente) {
        this.titolo = titolo;
        this.descrizione = descrizione;
        this.importo = importo;
        this.data = data;
        this.sezione = sezione;
        this.utente = utente;
        this.isEntrata = true; 
    }

    // --- GETTER E SETTER ---

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
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
    
    public boolean getIsEntrata() { return isEntrata; } 
    
    public void setIsEntrata(boolean flag) { this.isEntrata = flag; } 
}