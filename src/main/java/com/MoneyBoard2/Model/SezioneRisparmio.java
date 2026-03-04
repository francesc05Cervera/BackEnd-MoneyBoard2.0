package com.MoneyBoard2.Model;

import jakarta.persistence.*;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "sezioni_risparmio")
public class SezioneRisparmio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome; 
    private Double obiettivo; 
    private Double saldo = 0.0;

    @OneToMany(mappedBy = "sezione")
    @JsonIgnore 
    private List<Transazione> transazioni;
    
    @ManyToOne
    @JoinColumn(name = "utente_id")
    private Utente utente;

    // --- COSTRUTTORI ---

    // Obbligatorio per Hibernate
    public SezioneRisparmio() {
    }

    public SezioneRisparmio(String nome, Double obiettivo, Double saldo, Utente utente) {
        this.nome = nome;
        this.obiettivo = obiettivo;
        this.saldo = saldo;
        this.utente = utente;
    }

    // --- GETTER E SETTER ---

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Double getObiettivo() {
        return obiettivo;
    }

    public void setObiettivo(Double obiettivo) {
        this.obiettivo = obiettivo;
    }

    public Double getSaldo() {
        return saldo;
    }

    public void setSaldo(Double saldo) {
        this.saldo = saldo;
    }

    public List<Transazione> getTransazioni() {
        return transazioni;
    }

    public void setTransazioni(List<Transazione> transazioni) {
        this.transazioni = transazioni;
    }

    public Utente getUtente() {
        return utente;
    }

    public void setUtente(Utente utente) {
        this.utente = utente;
    }
}