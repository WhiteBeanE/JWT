package com.bsm.JwtTest.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bsm.JwtTest.damain.MemberLoginRequestDto;
import com.bsm.JwtTest.damain.TokenInfo;
import com.bsm.JwtTest.service.MemberService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {
    private final MemberService memberService;
 
    @PostMapping("/login")
    public TokenInfo login(@RequestBody MemberLoginRequestDto memberLoginRequestDto) {
    	log.info("[MemberService login] memberLoginRequestDto : {}", memberLoginRequestDto);
        String memberId = memberLoginRequestDto.getMemberId();
        String password = memberLoginRequestDto.getPassword();
        TokenInfo tokenInfo = memberService.login(memberId, password);
        log.info("[MemberService login] tokenInfo : {}", tokenInfo);
        return tokenInfo;
    }
}