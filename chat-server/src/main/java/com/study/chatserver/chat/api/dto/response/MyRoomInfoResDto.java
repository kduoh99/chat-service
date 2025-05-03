package com.study.chatserver.chat.api.dto.response;

import lombok.Builder;

@Builder
public record MyRoomInfoResDto(
	Long roomId,
	String roomName,
	String isGroupChat,
	Long unReadCount
) {
	public static MyRoomInfoResDto of(Long roomId, String roomName, String isGroupChat, Long unReadCount) {
		return MyRoomInfoResDto.builder()
			.roomId(roomId)
			.roomName(roomName)
			.isGroupChat(isGroupChat)
			.unReadCount(unReadCount)
			.build();
	}
}
