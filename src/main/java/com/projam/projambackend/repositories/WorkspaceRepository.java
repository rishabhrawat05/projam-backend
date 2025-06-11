package com.projam.projambackend.repositories;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.projam.projambackend.dto.WorkspaceSummaryDto;
import com.projam.projambackend.models.User;
import com.projam.projambackend.models.Workspace;

@Repository
public interface WorkspaceRepository extends JpaRepository<Workspace, Long> {

	Optional<Workspace> findByWorkspaceName(String workspaceName);

	Optional<Workspace> findByWorkspaceSlug(String workspaceSlug);

	Optional<Workspace> findByJoinCode(String joinCode);

	@Query("SELECT new com.projam.projambackend.dto.WorkspaceSummaryDto(" + "w.workspaceId, " + "w.workspaceName, "
			+ "w.isPrivate, " + "w.organizationName, " + "w.workspaceSlug, " + "w.workspaceType) " + "FROM Workspace w " + "JOIN w.users u "
			+ "WHERE u.gmail = :gmail "
			+ "GROUP BY w.workspaceId, w.workspaceName, w.isPrivate, w.organizationName, w.workspaceSlug, w.workspaceType")
	Page<WorkspaceSummaryDto> findAllWorkspaceSummariesByUserGmail(@Param("gmail") String gmail, Pageable pageable);

	@Query("SELECT new com.projam.projambackend.dto.WorkspaceSummaryDto(" + "w.workspaceId, " + "w.workspaceName, "
			+ "w.isPrivate, " + "w.organizationName, " + "w.workspaceSlug, " + "w.workspaceType) " + "FROM Workspace w " + "WHERE w.workspaceName LIKE CONCAT(:keyword, '%') "
			+ "OR w.organizationName LIKE CONCAT(:keyword, '%') " + "OR w.workspaceSlug LIKE CONCAT(:keyword, '%')")
	Page<WorkspaceSummaryDto> findAllWorkspaceSummaryByKeyword(@Param("keyword") String keyword, Pageable pageable);

}
