package com.example.stampanti;

import java.security.NoSuchAlgorithmException;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletResponse;


@RestController
public class restControllore{
    DbManager db = new DbManager();
    String codiceUsr;
    SessionManager sessionManager = new SessionManager();
    MySession s;


    @GetMapping("/login")
    public boolean logIn(@RequestParam(value = "codice", required = true) String codice,
    @RequestParam(value = "pass", required = true) String pass,HttpServletResponse response) throws NoSuchAlgorithmException{
        if(db.loginUser(codice, pass) != null) {
            s = sessionManager.session_start(response,db.loginUser(codice, pass));
            codiceUsr = codice;
            return true;
        }

        return false;
    }
    
    @GetMapping("/sess_exist")
    public boolean logIn(HttpServletResponse response) throws NoSuchAlgorithmException{
        if(s != null) {
            return true;
        }
        return false;
    }

    @GetMapping("/checkPrivilegi")
    public boolean checkPrivilegi(){
        return s.permessiAdmin;
    }

    @GetMapping("/getCoda")
    public List<stampa> coda(){
        return db.getCoda();
    }

    @GetMapping("/stampa")
    public boolean stampa(){
        return db.stampa(s.idUtente);
    }

    @GetMapping("/inserisci_coda")
    public boolean inserisci_coda(@RequestParam(value = "fronte", required = true) String fronte,
    @RequestParam(value = "retro", required = true) String retro,
    @RequestParam(value = "colorato", required = true) boolean color){
        return db.inserisci_coda(fronte, retro,color,s.idUtente);
    }

    @GetMapping("/logout")
    public boolean logout(HttpServletResponse response) throws NoSuchAlgorithmException{
        
        sessionManager.session_destroy(response);
        s = null;
        return true;
    }

    @GetMapping("/register")
    public boolean register(@RequestParam(value = "codice", required = true) String codice,
    @RequestParam(value = "pass", required = true) String pass,
    @RequestParam(value = "nome", required = true) String nome,
    @RequestParam(value = "cognome", required = true) String cognome,
    @RequestParam(value = "email", required = true) String email,
    @RequestParam(value = "permessi", required = true) String permessi){
        return db.registerUser(codice, pass, nome, cognome, email, permessi);
    }
    
    @GetMapping("/getPermessi")
    public boolean getPermessi(){
        return db.controllaPermessi(codiceUsr);
    }
    

    @GetMapping("/controllaSessione")
    public boolean controllaSessione(){
        if(sessionManager.sessionExist()) {
            return true;
        }
        return false;
    }
    
}
