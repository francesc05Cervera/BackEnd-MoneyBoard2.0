package com.MoneyBoard2.Model;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.List;

@Entity
@Table(name = "utenti")
public class Utente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    private String password; 
    private String email;

    @OneToMany(mappedBy = "utente")
    @JsonIgnore
    private List<SezioneRisparmio> sezioni;

    @OneToMany(mappedBy = "utente")
    @JsonIgnore
    private List<Transazione> transazioni;

    // --- COSTRUTTORI ---

    // Obbligatorio per JPA/Hibernate
    public Utente() {
    }

    // Comodo per creare un utente al volo
    public Utente(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }

    // --- GETTER E SETTER (Quelli che Eclipse non vedeva) ---

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<SezioneRisparmio> getSezioni() {
        return sezioni;
    }

    public void setSezioni(List<SezioneRisparmio> sezioni) {
        this.sezioni = sezioni;
    }

    public List<Transazione> getTransazioni() {
        return transazioni;
    }

    public void setTransazioni(List<Transazione> transazioni) {
        this.transazioni = transazioni;
    }
}