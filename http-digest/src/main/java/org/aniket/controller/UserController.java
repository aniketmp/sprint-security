package org.aniket.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class UserController {

	@GetMapping("/hello")
	public Object healthCheckHello() {
		return "Hello World!";
	}
	
	@GetMapping("/digest")
	public Object healthCheckToken() {
		return SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}
	
	
}
