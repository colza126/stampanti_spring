package com.example.stampanti;

import java.security.NoSuchAlgorithmException;
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

    @GetMapping("/logout")
    public boolean logout(HttpServletResponse response) throws NoSuchAlgorithmException{
        
        sessionManager.session_destroy(response);
        s = null;
        return true;
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
