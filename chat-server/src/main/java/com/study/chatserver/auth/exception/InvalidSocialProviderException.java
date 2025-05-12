package com.study.chatserver.auth.exception;

import com.study.chatserver.global.error.exception.InvalidGroupException;

public class InvalidSocialProviderException extends InvalidGroupException {
	public InvalidSocialProviderException(String message) {
		super(message);
	}

	public InvalidSocialProviderException() {
		this("지원하지 않는 소셜 로그인입니다.");
	}
}
