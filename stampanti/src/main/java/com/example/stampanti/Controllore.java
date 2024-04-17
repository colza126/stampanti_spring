package com.example.stampanti;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class Controllore {

    @GetMapping("/")
    public String root() {
        return "index";
    }


    //controllo lato server che l'utente sia autenticato
    @GetMapping("/home")
    public String home() {
        if(restControllore.s != null){
            return "pages/homepage";
        }else{
            return "index";
        }
    }

    @GetMapping("/inserisci")
    public String pagina(){
        if(restControllore.s != null && restControllore.s.ruolo.equals("admin")){
            return "pages/inserisci_user";
        }else{
            return home();
        }
    }

    @GetMapping("/contabile")
    public String contabile(){
        if(restControllore.s != null && restControllore.s.ruolo.equals("contabile")){
            return "pages/paginaContabile";
        }else{
            return home();
        }
    }
}
