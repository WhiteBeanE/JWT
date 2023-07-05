package com.bsm.JwtTest.service;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.bsm.JwtTest.damain.JwtUtil;
import com.bsm.JwtTest.damain.MemberDto;
import com.bsm.JwtTest.damain.MemberLogin;
import com.bsm.JwtTest.damain.UserDetailDto;
import com.bsm.JwtTest.dao.JwtDao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
	
	private final JwtUtil jwtUtil;
	private final JwtDao jwtDao;
	//private final AuthenticationManager authenticationManager;
	private final AuthenticationManagerBuilder authenticationManagerBuilder;
	private final PasswordEncoder passwordEncoder;
	
	public String login(MemberLogin member) {
		log.info("[UserService login] member : {}", member);
		
		// 사용자 인증을 위해 Authentication 객체 생성
		UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(member.getMemberId(), member.getPassword());
		log.info("[UserService login] authenticationToken : {}", authenticationToken);

		// 인증 매니저를 사용하여 인증 처리(loadUserByUsername 호출), 인증 실패 -> AuthenticationException
		//Authentication authentication = authenticationManager.authenticate(authenticationToken);
		Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
		log.info("[UserService login] authentication : {}", authentication);

		MemberDto memberDto = jwtDao.selectUserByUserName(member.getMemberId());
		log.info("[UserService login] memberDto : {}", memberDto);

		String jwt = jwtUtil.createJwt(memberDto);
		log.info("[UserService login] jwt : {}", jwt);

		// 인증 성공한 경우, SecurityContext 인증 객체를 설정
		SecurityContextHolder.getContext().setAuthentication(authentication);
				
		return jwt;
	}
	
	public UserDetails loadUserByUsername(String memberId) {
		MemberDto member =  jwtDao.selectUserByUserName(memberId);
		if(member != null) {
			UserDetailDto userDetailDto = new UserDetailDto(member, getRoles(member));
			log.info("[loadUserByUsername] userDetailsDto : {}", userDetailDto);
			
			return userDetailDto;
		}
		throw new BadCredentialsException("No such id or wrong password");
	}
	
	private Collection<GrantedAuthority> getRoles(MemberDto memberDto) {
		Collection<GrantedAuthority> roles = new ArrayList<>();
		roles.add(new SimpleGrantedAuthority("ROLE_" + memberDto.getAuthorities()));
		
		return roles;
	}

}
