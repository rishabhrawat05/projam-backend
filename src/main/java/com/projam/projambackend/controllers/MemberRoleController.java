package com.projam.projambackend.controllers;

import java.security.Principal;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.projam.projambackend.dto.MemberRoleRequest;
import com.projam.projambackend.dto.MemberRoleResponse;
import com.projam.projambackend.services.MemberRoleService;

@RestController
@RequestMapping("/projam/member-role")
public class MemberRoleController {

	private final MemberRoleService memberRoleService;
	
	public MemberRoleController(MemberRoleService memberRoleService) {
		this.memberRoleService = memberRoleService;
	}
	
	@GetMapping("/get-all-member-role")
	public List<MemberRoleResponse> getAllMemberRoleByProjectId(@RequestParam String projectId){
		return memberRoleService.getAllMemberRoleByProjectId(projectId);
	}
	
	@PostMapping("/add/member-role")
	public ResponseEntity<?> createNewMemberRole(@RequestBody MemberRoleRequest memberRoleRequest, @RequestParam String projectId){
		return ResponseEntity.ok(memberRoleService.createNewMemberRole(memberRoleRequest, projectId));
	}
	
	@GetMapping("/get")
	public List<MemberRoleResponse> getMemberRolebyProjectIdAndMemberGmail(@RequestParam String memberGmail, @RequestParam String projectId){
		return memberRoleService.getMemberRoleByProjectIdAndMemberGmail(memberGmail, projectId);
	}
	
	@PutMapping("/update")
	public ResponseEntity<MemberRoleResponse> updateMemberRole(@RequestBody MemberRoleRequest memberRoleRequest, @RequestParam String projectId, Principal principal){
		return ResponseEntity.ok(memberRoleService.updateMemberRolePermissions(memberRoleRequest, projectId, principal.getName()));
	}
	
	@DeleteMapping("/delete")
	public ResponseEntity<String> deleteMemberRole(@RequestParam String projectId, @RequestParam String memberRoleId, Principal principal){
		return ResponseEntity.ok(memberRoleService.deleteMemberRole(memberRoleId, projectId, principal.getName()));
	}
}
