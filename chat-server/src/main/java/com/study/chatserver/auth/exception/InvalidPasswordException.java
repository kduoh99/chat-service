package com.study.chatserver.auth.exception;

import com.study.chatserver.global.error.exception.InvalidGroupException;

public class InvalidPasswordException extends InvalidGroupException {
	public InvalidPasswordException(String message) {
		super(message);
	}

	public InvalidPasswordException() {
		this("비밀번호가 일치하지 않습니다.");
	}
}
