package com.study.chatserver.chat.api.dto.response;

import lombok.Builder;

@Builder
public record GroupRoomInfoResDto(
	Long roomId,
	String roomName
) {
	public static GroupRoomInfoResDto of(Long roomId, String roomName) {
		return GroupRoomInfoResDto.builder()
			.roomId(roomId)
			.roomName(roomName)
			.build();
	}
}
