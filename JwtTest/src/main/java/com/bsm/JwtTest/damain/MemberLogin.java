package com.bsm.JwtTest.damain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MemberLogin {
	private String memberId;
	private String password;
}
