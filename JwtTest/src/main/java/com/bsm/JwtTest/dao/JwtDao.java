package com.bsm.JwtTest.dao;

import java.util.Optional;

import com.bsm.JwtTest.damain.MemberDto;
import com.bsm.JwtTest.damain.MemberLogin;


public interface JwtDao {
	Optional<MemberDto> findMemberByMemberId(String username);
	MemberLogin findMemberByMemberIdNoAuthentication(String username);
	MemberDto selectUserByUserName(String memberId);
}
