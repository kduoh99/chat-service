package com.study.chatserver.member.exception;

import com.study.chatserver.global.error.exception.NotFoundGroupException;

public class MemberNotFoundException extends NotFoundGroupException {
	public MemberNotFoundException(String message) {
		super(message);
	}

	public MemberNotFoundException() {
		this("존재하지 않는 사용자입니다.");
	}
}
