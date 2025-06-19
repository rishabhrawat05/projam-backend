package com.projam.projambackend.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.projam.projambackend.models.TaskColumn;

@Repository
public interface TaskColumnRepository extends JpaRepository<TaskColumn, Long>{

	Optional<TaskColumn> findByTaskColumnSlug(String taskColumnSlug);
	
}
