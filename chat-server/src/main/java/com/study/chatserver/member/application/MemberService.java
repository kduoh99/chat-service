package com.study.chatserver.member.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.study.chatserver.member.api.dto.request.MemberSaveReqDto;
import com.study.chatserver.member.domain.Member;
import com.study.chatserver.member.domain.Role;
import com.study.chatserver.member.domain.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

	private final MemberRepository memberRepository;

	@Transactional
	public Long save(MemberSaveReqDto memberSaveReqDto) {
		if (memberRepository.findByEmail(memberSaveReqDto.email()).isPresent()) {
			throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
		}

		Member member = memberRepository.save(
			Member.builder()
				.name(memberSaveReqDto.name())
				.email(memberSaveReqDto.email())
				.password(memberSaveReqDto.password())
				.role(Role.ROLE_USER)
				.build()
		);

		return member.getId();
	}
}
