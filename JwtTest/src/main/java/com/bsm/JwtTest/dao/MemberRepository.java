package com.bsm.JwtTest.dao;

import java.util.Optional;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.bsm.JwtTest.damain.Member;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
@RequiredArgsConstructor
public class MemberRepository {
	
	private final SqlSession session;
	
	public Optional<Member> findByMemberId(String username){
		log.info("[MemberRepository findByMemberId] username : {}", username);
		Optional<Member> member = session.selectOne("findByMemberId", username);
		log.info("[MemberRepository findByMemberId] member : {}", member);
		return member;
	}
}
