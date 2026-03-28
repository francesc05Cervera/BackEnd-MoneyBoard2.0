package com.MoneyBoard2.Request;

public class DeleteSezioneRequest {
    private String nome;

    public DeleteSezioneRequest() {
    }

    public DeleteSezioneRequest(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}