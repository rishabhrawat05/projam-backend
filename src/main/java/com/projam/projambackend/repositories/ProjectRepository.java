package com.projam.projambackend.repositories;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.projam.projambackend.dto.ProjectResponse;
import com.projam.projambackend.models.Project;

@Repository
public interface ProjectRepository extends JpaRepository<Project, String> {

	Optional<Project> findByProjectName(String projectName);

	@Query("SELECT new com.projam.projambackend.dto.ProjectResponse(" + "p.projectId, " + "p.projectName, "
			+ "p.isPrivate, " + "p.startDate, " + "p.endDate, " + "p.projectStatus) " + "FROM Project p " + "JOIN p.workspace w " + "WHERE w.workspaceId = :workspaceId "
			+ "GROUP BY p.projectId, p.projectName, p.isPrivate, p.startDate, p.endDate, p.projectStatus")
	Page<ProjectResponse> findAllProjectResponseByWorkspace(@Param("workspaceId") String workspaceId, Pageable pageable);
	
	@Query(value = "SELECT COUNT(*) FROM project_members WHERE project_id = :projectId", nativeQuery = true)
	int countTotalMembersByProjectId(@Param("projectId") String projectId);

}
