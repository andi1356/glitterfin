package com.ndr.glitterfin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@SpringBootApplication
@Controller
public class GlitterfinApplication {

	public static void main(String[] args) {
		SpringApplication.run(GlitterfinApplication.class, args);
	}

	@GetMapping("/")
	public String index(Model model) {
		model.addAttribute("message", "Welcome to my Thymeleaf app!");
		return "index";
	}

}
