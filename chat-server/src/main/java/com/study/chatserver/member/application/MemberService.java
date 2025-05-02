package com.study.chatserver.member.application;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.study.chatserver.global.jwt.TokenProvider;
import com.study.chatserver.global.jwt.dto.TokenDto;
import com.study.chatserver.member.api.dto.request.MemberLoginReqDto;
import com.study.chatserver.member.api.dto.request.MemberSaveReqDto;
import com.study.chatserver.member.api.dto.response.MemberInfoResDto;
import com.study.chatserver.member.api.dto.response.MemberLoginResDto;
import com.study.chatserver.member.domain.Member;
import com.study.chatserver.member.domain.Role;
import com.study.chatserver.member.domain.repository.MemberRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

	private final MemberRepository memberRepository;
	private final PasswordEncoder passwordEncoder;
	private final TokenProvider tokenProvider;

	@Transactional
	public Long save(MemberSaveReqDto memberSaveReqDto) {
		if (memberRepository.findByEmail(memberSaveReqDto.email()).isPresent()) {
			throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
		}

		Member member = memberRepository.save(Member.builder()
			.name(memberSaveReqDto.name())
			.email(memberSaveReqDto.email())
			.password(passwordEncoder.encode(memberSaveReqDto.password()))
			.role(Role.USER)
			.build());

		return member.getId();
	}

	public MemberLoginResDto login(MemberLoginReqDto memberLoginReqDto) {
		Member member = memberRepository.findByEmail(memberLoginReqDto.email())
			.orElseThrow(() -> new EntityNotFoundException("존재하지 않는 이메일입니다."));

		if (!passwordEncoder.matches(memberLoginReqDto.password(), member.getPassword())) {
			throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
		}

		TokenDto tokenDto = tokenProvider.generateToken(member.getEmail(), String.valueOf(member.getRole()));

		return MemberLoginResDto.of(member.getId(), tokenDto.accessToken());
	}

	public List<MemberInfoResDto> findAll() {
		return memberRepository.findAll().stream()
			.map(m -> (MemberInfoResDto.of(m.getId(), m.getName(), m.getEmail())))
			.toList();
	}
}
