package com.projam.projambackend.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.projam.projambackend.dto.MemberRoleResponse;
import com.projam.projambackend.models.MemberRole;

@Repository
public interface MemberRoleRepository extends JpaRepository<MemberRole, String> {

	@Query("SELECT new com.projam.projambackend.dto.MemberRoleResponse( " + "mr.memberRoleId, " + "mr.roleName, "
			+ "mr.roleColor, " + "mr.canCreateTask, " + "mr.canEditTask, " + "mr.canDeleteTask, " + "mr.canAssignTask, "
			+ "mr.canManageMembers, " + "mr.canEditProject, " + "mr.canDeleteProject, " + "mr.canCreateColumn, "
			+ "mr.canDeleteColumn, mr.canManageRolesAndPermission, mr.canManageGithub) " + "FROM MemberRole mr " + "WHERE mr.project.projectId = :projectId")
	List<MemberRoleResponse> findAllByProject_ProjectId(@Param("projectId") String projectId);
	
	
	@Query("SELECT new com.projam.projambackend.dto.MemberRoleResponse( " + "mr.memberRoleId, " + "mr.roleName, "
			+ "mr.roleColor, " + "mr.canCreateTask, " + "mr.canEditTask, " + "mr.canDeleteTask, " + "mr.canAssignTask, "
			+ "mr.canManageMembers, " + "mr.canEditProject, " + "mr.canDeleteProject, " + "mr.canCreateColumn, "
			+ "mr.canDeleteColumn, mr.canManageRolesAndPermission, mr.canManageGithub) " + "FROM MemberRole mr " + "JOIN mr.members m WHERE mr.project.projectId = :projectId AND m.memberGmail = :memberGmail")
	List<MemberRoleResponse> getMemberRoleByProjectIdAndMemberGmail(@Param("projectId") String projectId, @Param("memberGmail") String memberGmail);
	
	Optional<MemberRole> findByRoleNameAndProject_ProjectId(String roleName, String projectId);
	

	Optional<MemberRole> findByMemberRoleIdAndProject_ProjectId(String memberRoleId, String projectId);
	
}
