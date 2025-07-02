package com.projam.projambackend.controllers;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.projam.projambackend.dto.MemberRequest;
import com.projam.projambackend.dto.MemberResponse;
import com.projam.projambackend.services.MemberService;

@RestController
@RequestMapping("/projam/member")
public class MemberController {

	private final MemberService memberService;
	
	public MemberController(MemberService memberService) {
		this.memberService = memberService;
	}
	
	@PostMapping("/add")
	public ResponseEntity<?> addMemberToWorkspace(@RequestBody MemberRequest memberRequest){
		return ResponseEntity.ok(memberService.addMemberToWorkspace(memberRequest));
	}
	
	@GetMapping("/get/by-workspace")
	public Page<MemberResponse> getAllMembersByWorkspaceId(@RequestParam int page, @RequestParam int size, @RequestParam String workspaceId){
		return memberService.getAllMembersByWorkspaceId(workspaceId, page, size);
	}
	
	@GetMapping("/get/by-project")
	public Page<MemberResponse> getAllMembersByProjectId(@RequestParam int page, @RequestParam int size, @RequestParam String projectId){
		return memberService.getAllMembersByProjectId(projectId, page, size);
	}
	
	@GetMapping("/get-all/keyword")
	public List<MemberResponse> getAllMembersByKeyword(@RequestParam String keyword, @RequestParam String projectId){
		return memberService.getAllMembersByKeyword(keyword, projectId);
	}
}
