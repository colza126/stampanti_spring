package com.example.stampanti;

public class Utente {
    public String nome;
    public String cognome;
    public String id;
    public String codice;
    public String ruolo;
    public int fondi;
    
    public Utente(String nome, String cognome, String id, String codice, String ruolo, int fondi) {
        this.nome = nome;
        this.cognome = cognome;
        this.id = id;
        this.codice = codice;
        this.ruolo = ruolo;
        this.fondi = fondi;
    }
}
