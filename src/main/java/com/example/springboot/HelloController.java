package com.example.springboot;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
	public static final String login = "AKIAJXBOVX5Q2EULDUIA";
	@PostMapping ("/greeting")
	public String greetingSubmit(@ModelAttribute Greeting greeting, Model model){
		model.addAttribute("greeting", greeting);
		return "Hello";
	}
}
