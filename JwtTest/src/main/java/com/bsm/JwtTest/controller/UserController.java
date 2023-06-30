package com.bsm.JwtTest.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bsm.JwtTest.damain.MemberLogin;
import com.bsm.JwtTest.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {
	
	private final UserService userService;
	
	@PostMapping("/login")
	public ResponseEntity<String> login(@RequestBody MemberLogin member){
		log.info("[UserController login] member : {}", member);
		return ResponseEntity.ok().body(userService.login(member));
	}
}
