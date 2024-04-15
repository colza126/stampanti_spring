package com.example.stampanti;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class Controllore {

    @GetMapping("/")
    public String root() {
        return "index";
    }

    @GetMapping("/home")
    public String home() {
        return "homepage";
    }
    
}
