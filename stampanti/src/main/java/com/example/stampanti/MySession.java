package com.example.stampanti;

public class MySession {
    
    public int idUtente;
    
    public String ruolo;

    public MySession(int idUtente, String ruolo) {
        this.idUtente = idUtente;
        this.ruolo = ruolo;
    }

    public MySession() {
        this.idUtente = 0;
        this.ruolo = "default";
    }

    
}
