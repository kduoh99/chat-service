package com.study.chatserver.chat.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.study.chatserver.chat.domain.repository.ChatMessageRepository;
import com.study.chatserver.chat.domain.repository.ChatParticipantRepository;
import com.study.chatserver.chat.domain.repository.ChatRoomRepository;
import com.study.chatserver.chat.domain.repository.ReadStatusRepository;
import com.study.chatserver.member.domain.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatService {

	private final ChatRoomRepository chatRoomRepository;
	private final ChatParticipantRepository chatParticipantRepository;
	private final ChatMessageRepository chatMessageRepository;
	private final ReadStatusRepository readStatusRepository;
	private final MemberRepository memberRepository;

}
