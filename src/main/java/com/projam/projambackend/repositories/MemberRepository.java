package com.projam.projambackend.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.projam.projambackend.dto.MemberResponse;
import com.projam.projambackend.models.Member;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

	@Query("SELECT new com.projam.projambackend.dto.MemberResponse(" + "m.memberName, " + "m.memberGmail, "
			+ "m.memberJoinDate) " + "FROM Member m " + "JOIN m.workspace w " + "WHERE w.workspaceId = :workspaceId "
			+ "GROUP BY m.memberName, m.memberGmail, m.memberJoinDate")
	Page<MemberResponse> findAllMemberByWorkspaceId(@Param("workspaceId") Long workspaceId, Pageable pageable);
}
