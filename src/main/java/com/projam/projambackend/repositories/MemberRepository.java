package com.projam.projambackend.repositories;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.projam.projambackend.dto.MemberResponse;
import com.projam.projambackend.models.Member;

@Repository
public interface MemberRepository extends JpaRepository<Member, String> {

	@Query("SELECT new com.projam.projambackend.dto.MemberResponse(" + "m.memberId, " + "m.memberName, " + "m.memberGmail, "
			+ "m.memberJoinDate) " + "FROM Member m " + "JOIN m.workspace w " + "WHERE w.workspaceId = :workspaceId "
			+ "GROUP BY m.memberId, m.memberName, m.memberGmail, m.memberJoinDate")
	Page<MemberResponse> findAllMemberByWorkspaceId(@Param("workspaceId") String workspaceId, Pageable pageable);
	
	@Query("SELECT new com.projam.projambackend.dto.MemberResponse(" + "m.memberId, " + "m.memberName, " + "m.memberGmail, "
			+ "m.memberJoinDate) " + "FROM Member m " + "JOIN m.projects p " + "WHERE p.projectId = :projectId "
			+ "GROUP BY  m.memberId, m.memberName, m.memberGmail, m.memberJoinDate")
	Page<MemberResponse> findAllMemberByProjectId(@Param("projectId") String projectId, Pageable pageable);
	
	@Query("SELECT m FROM Member m JOIN m.projects p WHERE m.memberGmail = :memberGmail AND p.projectId = :projectId")
	Optional<Member> findByMemberGmailAndProjectId(@Param("memberGmail") String memberGmail, @Param("projectId") String projectId);
	
	@Query("SELECT m FROM Member m JOIN m.workspace w WHERE w.workspaceId = :workspaceId")
	Set<Member> findAllByWorkspaceId(@Param("workspaceId") String workspaceId);
	
	@Query("SELECT m FROM Member m WHERE m.memberGmail = :memberGmail AND m.workspace.workspaceId = :workspaceId")
	Optional<Member> findByMemberGmailAndWorkspaceId(@Param("memberGmail") String memberGmail, @Param("workspaceId") String workspaceId);
	
	@Query("SELECT m FROM Member m JOIN m.projects p WHERE p.projectId = :projectId")
	List<Member> findAllByProjectId(@Param("projectId") String projectId);
	
	@Query("SELECT new com.projam.projambackend.dto.MemberResponse(m.memberId, m.memberName, m.memberGmail, m.memberJoinDate) " +
		       "FROM Member m JOIN m.projects p " +
		       "WHERE p.projectId = :projectId AND (LOWER(m.memberName) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
		       "OR LOWER(m.memberGmail) LIKE LOWER(CONCAT('%', :keyword, '%')))")
	List<MemberResponse> getAllMembersByKeyword(@Param("keyword") String keyword, @Param("projectId") String projectId);


}

