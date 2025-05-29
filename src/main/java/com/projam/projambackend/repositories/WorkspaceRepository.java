package com.projam.projambackend.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.projam.projambackend.models.Workspace;

@Repository
public interface WorkspaceRepository extends JpaRepository<Workspace, Long> {

	Optional<Workspace> findByWorkspaceName(String workspaceName);
	
	Optional<Workspace> findByWorkspaceSlug(String workspaceSlug);
	
	
}
