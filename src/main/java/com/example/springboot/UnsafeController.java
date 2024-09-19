package com.example.springboot;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class UnsafeController {
    public static final String login = "AKIAJXBOVX5Q2EULDUIA";
    public static final String pwd ="SqcyDpetv+pCsbNYWHDLE8yR5mJ13MI+4d8NOwtM";
    @PostMapping ("/greeting")
    public String greetingSubmit(@ModelAttribute Greeting greeting, Model model){
       model.addAttribute("greeting", greeting);
       return "Hello, I'm a vulnerability";
    }
}
