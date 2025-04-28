package com.study.chatserver.member.api;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.study.chatserver.member.api.dto.request.MemberLoginReqDto;
import com.study.chatserver.member.api.dto.request.MemberSaveReqDto;
import com.study.chatserver.member.api.dto.response.MemberInfoResDto;
import com.study.chatserver.member.api.dto.response.MemberLoginResDto;
import com.study.chatserver.member.application.MemberService;

@RestController
@RequestMapping("/api/member")
public class MemberController {

	private final MemberService memberService;

	public MemberController(MemberService memberService) {
		this.memberService = memberService;
	}

	@PostMapping("/save")
	public ResponseEntity<Long> save(@RequestBody MemberSaveReqDto memberSaveReqDto) {
		return ResponseEntity.status(HttpStatus.CREATED).body(memberService.save(memberSaveReqDto));
	}

	@PostMapping("/login")
	public ResponseEntity<MemberLoginResDto> login(@RequestBody MemberLoginReqDto memberLoginReqDto) {
		return ResponseEntity.ok(memberService.login(memberLoginReqDto));
	}

	@GetMapping("/list")
	public ResponseEntity<List<MemberInfoResDto>> findAll() {
		return ResponseEntity.ok(memberService.findAll());
	}
}
