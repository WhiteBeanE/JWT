package com.bsm.JwtTest.service;

import org.springframework.stereotype.Service;

import com.bsm.JwtTest.damain.JwtUtil;
import com.bsm.JwtTest.damain.MemberLogin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
	
	private final JwtUtil jwtUtil;
	
	public String login(MemberLogin member) {
		String memberId = member.getMemberId();
		log.info("[UserService login] member : {}", member);
		//String password = member.getPassword();
		// 인증과정
		
		return jwtUtil.createJwt(memberId);
	}

}
