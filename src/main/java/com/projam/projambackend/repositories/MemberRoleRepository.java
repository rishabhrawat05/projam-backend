package com.projam.projambackend.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.projam.projambackend.dto.MemberRoleResponse;
import com.projam.projambackend.models.MemberRole;

@Repository
public interface MemberRoleRepository extends JpaRepository<MemberRole, String> {

	@Query("SELECT new com.projam.projambackend.dto.MemberRoleResponse( " + "mr.memberRoleId, " + "mr.roleName, "
			+ "mr.canCreateTask, " + "mr.canEditTask, " + "mr.canDeleteTask, " + "mr.canAssignTask, "
			+ "mr.canManageMembers, " + "mr.canEditProject, " + "mr.canDeleteProject, " + "mr.canCreateColumn, "
			+ "mr.canDeleteColumn) " + "FROM MemberRole mr " + "WHERE mr.project.projectId = :projectId")
	List<MemberRoleResponse> findAllByProject_ProjectId(String projectId);

}
