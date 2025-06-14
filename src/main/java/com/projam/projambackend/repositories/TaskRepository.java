package com.projam.projambackend.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.projam.projambackend.models.Task;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

	@Query("SELECT t FROM Task t JOIN t.project p WHERE p.projectId = :projectId")
	Page<Task> findAllByProjectId(@Param("projectId") Long projectId, Pageable pageable);
}
