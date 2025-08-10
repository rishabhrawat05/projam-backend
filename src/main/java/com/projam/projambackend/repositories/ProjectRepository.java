package com.projam.projambackend.repositories;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.projam.projambackend.dto.EditProjectResponse;
import com.projam.projambackend.dto.ProjectResponse;
import com.projam.projambackend.enums.ProjectStatus;
import com.projam.projambackend.models.Project;

@Repository
public interface ProjectRepository extends JpaRepository<Project, String> {

	Optional<Project> findByProjectName(String projectName);

	@Query("SELECT new com.projam.projambackend.dto.ProjectResponse(" + "p.projectId, " + "p.projectName, "
			+ "p.isPrivate, " + "p.startDate, " + "p.endDate, " + "p.projectStatus, p.deletionMarkedAt) " + "FROM Project p " + "JOIN p.workspace w JOIN p.members m " + "WHERE w.workspaceId = :workspaceId AND m.memberGmail = :email AND p.projectStatus NOT LIKE 'DELETION%' "
			+ "GROUP BY p.projectId, p.projectName, p.isPrivate, p.startDate, p.endDate, p.projectStatus, p.deletionMarkedAt")
	Page<ProjectResponse> findAllProjectResponseByWorkspaceAndEmail(@Param("workspaceId") String workspaceId, @Param("email") String email, Pageable pageable);
	
	@Query(value = "SELECT COUNT(*) FROM project_members WHERE project_id = :projectId", nativeQuery = true)
	int countTotalMembersByProjectId(@Param("projectId") String projectId);

	@Query("SELECT new com.projam.projambackend.dto.EditProjectResponse(p.projectName, p.projectDescription) FROM Project p WHERE p.projectId = :projectId")
	Optional<EditProjectResponse> findProjectResponseByProjectId(@Param("projectId") String projectId);
	
	@Query("""
		    SELECT DISTINCT p FROM Project p
		    LEFT JOIN FETCH p.tasks t
		    LEFT JOIN FETCH t.comments
		    LEFT JOIN FETCH t.tags
		    LEFT JOIN FETCH t.activities
		    LEFT JOIN FETCH p.activities
		    LEFT JOIN FETCH p.memberRoles r
		    LEFT JOIN FETCH r.members
		    LEFT JOIN FETCH p.edges
		    LEFT JOIN FETCH p.members m
		    LEFT JOIN FETCH m.activities
		    LEFT JOIN FETCH m.memberRoles
		    LEFT JOIN FETCH p.tags tag
		    LEFT JOIN FETCH tag.memberRole
		    LEFT JOIN FETCH tag.project
		    WHERE p.projectId = :projectId
		""")
	Optional<Project> findProjectWithAllRelations(@Param("projectId") String projectId);
	
	@Query("SELECT p.projectId FROM Project p WHERE p.projectStatus = :projectStatus AND p.deletionMarkedAt <= :thresholdDate")
	List<String> findAllByProjectStatusAndThresholdTime(@Param("projectStatus") ProjectStatus projectStatus, @Param("thresholdTime") Date thresholdTime);
	

	@Query("SELECT new com.projam.projambackend.dto.ProjectResponse(" + "p.projectId, " + "p.projectName, "
			+ "p.isPrivate, " + "p.startDate, " + "p.endDate, " + "p.projectStatus, p.deletionMarkedAt) " + "FROM Project p " + "JOIN p.workspace w WHERE p.projectStatus = :projectStatus AND w.workspaceId = :workspaceId "
			+ "GROUP BY p.projectId, p.projectName, p.isPrivate, p.startDate, p.endDate, p.projectStatus, p.deletionMarkedAt")
	List<ProjectResponse> findAllByProjectStatusAndWorkspaceId(@Param("projectStatus") ProjectStatus projectStatus, @Param("workspaceId") String workspaceId);
	
	@Query("SELECT new com.projam.projambackend.dto.ProjectResponse(" +
		       "p.projectId, p.projectName, p.isPrivate, p.startDate, " +
		       "p.endDate, p.projectStatus, p.deletionMarkedAt) " +
		       "FROM Project p " +
		       "JOIN p.workspace w " +
		       "JOIN p.members m " +
		       "WHERE w.workspaceId = :workspaceId " +
		       "AND m.memberGmail = :email " +
		       "AND p.projectStatus NOT LIKE 'DELETION%' " +
		       "AND (LOWER(p.projectName) LIKE LOWER(CONCAT('%', :keyword, '%')))")
		List<ProjectResponse> findProjectNameByWorkspaceIdAndEmailAndKeyword(
		    @Param("workspaceId") String workspaceId,
		    @Param("email") String email,
		    @Param("keyword") String keyword
		);

}

