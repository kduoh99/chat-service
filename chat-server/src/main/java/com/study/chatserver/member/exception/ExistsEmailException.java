package com.study.chatserver.member.exception;

import com.study.chatserver.global.error.exception.ConflictGroupException;

public class ExistsEmailException extends ConflictGroupException {
	public ExistsEmailException(String message) {
		super(message);
	}

	public ExistsEmailException() {
		this("이미 존재하는 이메일입니다.");
	}
}
