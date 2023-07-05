package com.bsm.JwtTest.dao;

import java.util.Optional;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.bsm.JwtTest.damain.MemberDto;
import com.bsm.JwtTest.damain.MemberLogin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
@RequiredArgsConstructor
public class JwtDaoImpl implements JwtDao {
	
	private final SqlSession session;
	
	@Override
	public Optional<MemberDto> findMemberByMemberId(String memberId) {
		log.info("[JwtDaoImpl findMemberByMemberId] memberId : {}", memberId);
		return session.selectOne("findMemberByMemberId", memberId);
	}

	@Override
	public MemberLogin findMemberByMemberIdNoAuthentication(String memberId) {
		log.info("[JwtDaoImpl findMemberByMemberIdNoAuthentication] memberId : {}", memberId);
		return session.selectOne("findMemberByMemberId", memberId);
	}

	@Override
	public MemberDto selectUserByUserName(String memberId) {
		log.info("[JwtDaoImpl selectUserByUserName] memberId : {}", memberId);
		return session.selectOne("selectUserByUserName", memberId);
	}

}
