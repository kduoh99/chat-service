package com.study.chatserver.chat.exception;

import com.study.chatserver.global.error.exception.NotFoundGroupException;

public class ChatParticipantNotFoundException extends NotFoundGroupException {
	public ChatParticipantNotFoundException(String message) {
		super(message);
	}

	public ChatParticipantNotFoundException() {
		this("참여자를 찾을 수 없습니다.");
	}
}
