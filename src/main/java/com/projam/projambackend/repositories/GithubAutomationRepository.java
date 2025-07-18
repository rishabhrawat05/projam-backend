package com.projam.projambackend.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.projam.projambackend.dto.GithubAutomationResponse;
import com.projam.projambackend.models.GithubAutomation;

@Repository
public interface GithubAutomationRepository extends JpaRepository<GithubAutomation, String>{

	@Query("SELECT new com.projam.projambackend.dto.GithubAutomationResponse( ga.edgeId, ga.sourceEvent, ga.targetColumn, ga.color, ga.edgeCondition) FROM GithubAutomation ga WHERE ga.project.projectId = :projectId")
	List<GithubAutomationResponse> getAllGithubAutomationResponseByProjectId(@Param("projectId") String projectId);
}
