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
import com.projam.projambackend.dto.MemberSummary;
import com.projam.projambackend.models.Member;

@Repository
public interface MemberRepository extends JpaRepository<Member, String> {

	@Query("SELECT new com.projam.projambackend.dto.MemberResponse(" + "m.memberId, " + "m.memberName, "
			+ "m.memberGmail, " + "m.memberJoinDate) " + "FROM Member m " + "JOIN m.workspace w "
			+ "WHERE w.workspaceId = :workspaceId "
			+ "GROUP BY m.memberId, m.memberName, m.memberGmail, m.memberJoinDate")
	Page<MemberResponse> findAllMemberByWorkspaceId(@Param("workspaceId") String workspaceId, Pageable pageable);

	@Query("SELECT new com.projam.projambackend.dto.MemberResponse(" + "m.memberId, " + "m.memberName, "
			+ "m.memberGmail, " + "m.memberJoinDate) " + "FROM Member m " + "JOIN m.projects p "
			+ "WHERE p.projectId = :projectId " + "GROUP BY  m.memberId, m.memberName, m.memberGmail, m.memberJoinDate")
	Page<MemberResponse> findAllMemberByProjectId(@Param("projectId") String projectId, Pageable pageable);

	@Query("SELECT m FROM Member m JOIN m.projects p WHERE m.memberGmail = :memberGmail AND p.projectId = :projectId")
	Optional<Member> findByMemberGmailAndProjectId(@Param("memberGmail") String memberGmail,
			@Param("projectId") String projectId);

	@Query("SELECT m FROM Member m JOIN m.workspace w WHERE w.workspaceId = :workspaceId")
	List<Member> findAllByWorkspaceId(@Param("workspaceId") String workspaceId);

	@Query("SELECT m FROM Member m WHERE m.memberGmail = :memberGmail AND m.workspace.workspaceId = :workspaceId")
	Optional<Member> findByMemberGmailAndWorkspaceId(@Param("memberGmail") String memberGmail,
			@Param("workspaceId") String workspaceId);

	@Query("SELECT m FROM Member m JOIN m.projects p WHERE p.projectId = :projectId")
	List<Member> findAllByProjectId(@Param("projectId") String projectId);

	@Query("SELECT new com.projam.projambackend.dto.MemberResponse(m.memberId, m.memberName, m.memberGmail, m.memberJoinDate) "
			+ "FROM Member m JOIN m.projects p "
			+ "WHERE p.projectId = :projectId AND (LOWER(m.memberName) LIKE LOWER(CONCAT('%', :keyword, '%')) "
			+ "OR LOWER(m.memberGmail) LIKE LOWER(CONCAT('%', :keyword, '%')))")
	List<MemberResponse> getAllMembersByKeyword(@Param("keyword") String keyword, @Param("projectId") String projectId);

	@Query("""
			SELECT m.memberName FROM Member m JOIN m.projects p WHERE p.projectId = :projectId AND LOWER(m.memberName) LIKE LOWER(:query)
			""")
	List<String> findAllMemberNameByProjectIdAndQuery(@Param("projectId") String projectId,
			@Param("query") String query);

	@Query("""
			SELECT m.memberName FROM Member m JOIN m.projects p WHERE p.projectId = :projectId
			""")
	List<String> findAllMemberNameByProjectId(@Param("projectId") String projectId);

	@Query("""
			SELECT m FROM Member m
			JOIN m.memberRoles mr
			WHERE mr.memberRoleId = :memberRoleId
			""")
	List<Member> findByMemberRole_MemberRoleId(@Param("memberRoleId") String memberId);

	@Query("""
			    SELECT new com.projam.projambackend.dto.MemberSummary( m.memberName, m.memberGmail)
			    FROM Member m
			    JOIN m.workspace w
			    JOIN m.projects p
			    WHERE w.workspaceId = :workspaceId
				AND p.projectId <> :projectId
			      AND m.requestStatus = 'WORKSPACE'
			""")
	List<MemberSummary> findAllMemberNameByWorkspaceId(@Param("workspaceId") String workspaceId,
			@Param("projectId") String projectId);
	
	@Query("SELECT new com.projam.projambackend.dto.MemberResponse(m.memberId, m.memberName, m.memberGmail, m.memberJoinDate) "
			+ "FROM Member m "
			+ "WHERE m.workspace.workspaceId = :workspaceId AND (LOWER(m.memberName) LIKE LOWER(CONCAT('%', :keyword, '%')) "
			+ "OR LOWER(m.memberGmail) LIKE LOWER(CONCAT('%', :keyword, '%')))")
	List<MemberResponse> getAllMembersByKeywordAndWorkspaceId(@Param("keyword") String keyword, @Param("workspaceId") String workspaceId);

}
