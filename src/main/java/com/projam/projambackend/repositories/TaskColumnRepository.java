package com.projam.projambackend.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.projam.projambackend.models.TaskColumn;

@Repository
public interface TaskColumnRepository extends JpaRepository<TaskColumn, String>{

	Optional<TaskColumn> findByTaskColumnSlugAndProject_ProjectId(String taskColumnSlug, String projectId);
	
	@Query("SELECT tc FROM TaskColumn tc WHERE tc.project.projectId = :projectId ORDER BY tc.taskColumnIndex")
	List<TaskColumn> findAllByProject_ProjectId(String projectId);
	
	int countByProject_ProjectId(String projectId);
	
	
	
}
