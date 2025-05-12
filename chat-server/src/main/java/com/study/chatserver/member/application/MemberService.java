package com.study.chatserver.member.application;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.study.chatserver.member.api.dto.request.MemberSaveReqDto;
import com.study.chatserver.member.api.dto.response.MemberInfoResDto;
import com.study.chatserver.member.domain.Member;
import com.study.chatserver.member.domain.Role;
import com.study.chatserver.member.domain.repository.MemberRepository;
import com.study.chatserver.member.exception.ExistsEmailException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

	private final MemberRepository memberRepository;
	private final PasswordEncoder passwordEncoder;

	@Transactional
	public Long save(MemberSaveReqDto memberSaveReqDto) {
		if (memberRepository.findByEmail(memberSaveReqDto.email()).isPresent()) {
			throw new ExistsEmailException();
		}

		Member member = memberRepository.save(Member.builder()
			.name(memberSaveReqDto.name())
			.email(memberSaveReqDto.email())
			.password(passwordEncoder.encode(memberSaveReqDto.password()))
			.role(Role.USER)
			.build());

		return member.getId();
	}

	public List<MemberInfoResDto> findAll() {
		return memberRepository.findAll().stream()
			.map(m -> (MemberInfoResDto.of(m.getId(), m.getName(), m.getEmail())))
			.toList();
	}
}
