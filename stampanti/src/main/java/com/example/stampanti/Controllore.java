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
        System.out.println("save");
        return "pages/inserisci_user";
        
        
    }

    
}
