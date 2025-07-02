package com.projam.projambackend.repositories;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.projam.projambackend.dto.TaskAssignmentSummaryDto;
import com.projam.projambackend.models.Project;
import com.projam.projambackend.models.Task;

@Repository
public interface TaskRepository extends JpaRepository<Task, String> {

	@Query("SELECT t FROM Task t JOIN t.project p WHERE p.projectId = :projectId")
	Page<Task> findAllByProjectId(@Param("projectId") String projectId, Pageable pageable);
	
	Integer countByProjectAndAssignedTo_memberGmail(Project project, String gmail);
	
	Integer countByProject(Project project);
	
	@Query("SELECT t FROM Task t JOIN t.project p WHERE t.taskId = :taskId AND p.projectId = :projectId")
	Optional<Task> findByIdAndProjectId(@Param("taskId") String taskId, @Param("projectId") String projectId);
	
	Optional<Task> findByTaskKey(String taskKey);
	
	Integer countByProject_ProjectIdAndStatus(String projectId, String status);
	
	@Query("SELECT COUNT(t) FROM Task t WHERE t.project.projectId = :projectId AND t.status = 'COMPLETED' AND t.assignedTo.memberGmail = :email")
	int countCompletedTasksByProjectAndUser(@Param("projectId") String projectId, @Param("email") String email);
	
	@Query("SELECT t.status, t.taskColumn.taskColumnColor, COUNT(t) FROM Task t WHERE t.project.id = :projectId GROUP BY t.status, t.taskColumn.taskColumnColor")
	List<Object[]> getStatusCountsByProject(@Param("projectId") String projectId);
	
	@Query("SELECT t.status, t.taskColumn.taskColumnColor, COUNT(t) FROM Task t WHERE t.project.id = :projectId AND t.assignedTo.memberGmail = :email GROUP BY t.status, t.taskColumn.taskColumnColor")
	List<Object[]> getStatusCountsByProjectAndUser(@Param("projectId") String projectId, @Param("email") String email);
	
	@Query("SELECT new com.projam.projambackend.dto.TaskAssignmentSummaryDto(t.taskId, t.title, t.status, t.taskKey, t.taskColumn.taskColumnColor) FROM Task t WHERE t.project.projectId = :projectId AND t.assignedTo.memberGmail = :email")
	List<TaskAssignmentSummaryDto> getTaskByProjectAndUser(@Param("projectId") String projectId, @Param("email") String email);
	
	@Query("SELECT new com.projam.projambackend.dto.TaskAssignmentSummaryDto(t.taskId, t.title, t.status, t.taskKey, t.taskColumn.taskColumnColor) FROM Task t WHERE t.project.projectId = :projectId AND t.assignee.memberGmail = :email")
	List<TaskAssignmentSummaryDto> getTaskByProjectAndAdmin(@Param("projectId") String projectId, @Param("email") String email);
	
	@Query("SELECT COUNT(DISTINCT t.assignedTo.id) FROM Task t WHERE t.project.projectId = :projectId AND t.assignedTo IS NOT NULL")
	int countActiveMembersByProjectId(@Param("projectId") String projectId);
	
	@Query("SELECT COUNT(DISTINCT t.taskId) FROM Task t WHERE t.project.projectId = :projectId AND t.assignedTo.memberGmail = :email AND t.assignedAt BETWEEN :start AND :end")
	int countByProjectIdAndEmailAndAssignedAtBetween(@Param("projectId") String projectId, @Param("email") String email, @Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
	
	@Query("SELECT COUNT(DISTINCT t.taskId) FROM Task t WHERE t.project.projectId = :projectId AND t.status = 'completed' AND t.assignedAt BETWEEN :start AND :end AND t.assignedTo.memberGmail = :email AND t.completedAt BETWEEN :start AND :end")
	int countByProjectIdAndEmailAndCompletedAtBetween(@Param("projectId") String projectId, @Param("email") String email, @Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
	
	@Query("SELECT COUNT(DISTINCT t.taskId) FROM Task t WHERE t.project.projectId = :projectId AND t.assignedAt BETWEEN :start AND :end")
	int countByProjectIdAndAssignedAtBetween(@Param("projectId") String projectId, @Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
	
	@Query("SELECT COUNT(DISTINCT t.taskId) FROM Task t WHERE t.project.projectId = :projectId AND t.status = 'completed' AND t.assignedAt BETWEEN :start AND :end AND t.completedAt BETWEEN :start AND :end")
	int countByProjectIdAndCompletedAtBetween(@Param("projectId") String projectId, @Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
	
	
	
	
}
