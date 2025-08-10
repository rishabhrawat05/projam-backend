package com.projam.projambackend.repositories;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.projam.projambackend.dto.MemberResponse;
import com.projam.projambackend.dto.TaskAssignmentSummaryDto;
import com.projam.projambackend.dto.TaskResponse;
import com.projam.projambackend.models.Project;
import com.projam.projambackend.models.Task;

@Repository
public interface TaskRepository extends JpaRepository<Task, String> {

	@Query("SELECT t FROM Task t JOIN t.project p WHERE p.projectId = :projectId AND t.isDeleted = false")
	Page<Task> findAllByProjectId(@Param("projectId") String projectId, Pageable pageable);
	
	@Query("SELECT t FROM Task t JOIN t.project p WHERE p.projectId = :projectId")
	List<Task> findAllByProjectId(@Param("projectId") String projectId);
	
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
	
	@Query("SELECT new com.projam.projambackend.dto.TaskAssignmentSummaryDto(t.taskId, t.title, t.status, t.taskKey, t.taskColumn.taskColumnColor) FROM Task t WHERE t.project.projectId = :projectId AND t.assignedTo.memberGmail = :email AND t.isDeleted = false")
	List<TaskAssignmentSummaryDto> getTaskByProjectAndUser(@Param("projectId") String projectId, @Param("email") String email);
	
	@Query("SELECT new com.projam.projambackend.dto.TaskAssignmentSummaryDto(t.taskId, t.title, t.status, t.taskKey, t.taskColumn.taskColumnColor) FROM Task t WHERE t.project.projectId = :projectId AND t.assignee.memberGmail = :email AND t.isDeleted = false")
	List<TaskAssignmentSummaryDto> getTaskByProjectAndAdmin(@Param("projectId") String projectId, @Param("email") String email);
	
	@Query("SELECT COUNT(DISTINCT t.assignedTo.id) FROM Task t WHERE t.project.projectId = :projectId AND t.assignedAt >= :startOfDay AND t.assignedAt < :endOfDay AND t.assignedTo IS NOT NULL")
	int countActiveMembersByProjectIdAndToday(@Param("projectId") String projectId, @Param("startOfDay") LocalDateTime startOfDay, @Param("endOfDay") LocalDateTime endOfDay);
	
	@Query("SELECT DISTINCT new com.projam.projambackend.dto.MemberResponse(ao.memberId, ao.memberName, ao.memberGmail, ao.memberJoinDate) FROM Task t JOIN t.assignedTo ao WHERE t.project.projectId = :projectId AND t.assignedAt >= :startOfDay AND t.assignedAt < :endOfDay")
	List<MemberResponse> getActiveMembersByToday(@Param("projectId") String projectId, @Param("startOfDay") LocalDateTime startOfDay, @Param("endOfDay") LocalDateTime endOfDay);
	
	@Query("SELECT COUNT(DISTINCT t.taskId) FROM Task t WHERE t.project.projectId = :projectId AND t.assignedTo.memberGmail = :email AND t.assignedAt BETWEEN :start AND :end")
	int countByProjectIdAndEmailAndAssignedAtBetween(@Param("projectId") String projectId, @Param("email") String email, @Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
	
	@Query("SELECT COUNT(DISTINCT t.taskId) FROM Task t WHERE t.project.projectId = :projectId AND t.status = 'completed' AND t.assignedAt BETWEEN :start AND :end AND t.assignedTo.memberGmail = :email AND t.completedAt BETWEEN :start AND :end")
	int countByProjectIdAndEmailAndCompletedAtBetween(@Param("projectId") String projectId, @Param("email") String email, @Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
	
	@Query("SELECT COUNT(DISTINCT t.taskId) FROM Task t WHERE t.project.projectId = :projectId AND t.assignedAt BETWEEN :start AND :end")
	int countByProjectIdAndAssignedAtBetween(@Param("projectId") String projectId, @Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
	
	@Query("SELECT COUNT(DISTINCT t.taskId) FROM Task t WHERE t.project.projectId = :projectId AND t.status = 'completed' AND t.assignedAt BETWEEN :start AND :end AND t.completedAt BETWEEN :start AND :end")
	int countByProjectIdAndCompletedAtBetween(@Param("projectId") String projectId, @Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
	
	@Query("""
		    SELECT t FROM Task t
		    WHERE t.taskColumn.taskColumnId = :columnId
		      AND t.taskColumn.project.projectId = :projectId
		      AND t.isDeleted = false
		""")
	Page<Task> findAllByTaskColumn_TaskColumnIdAndTaskColumn_Project_ProjectId(@Param("columnId") String columnId, @Param("projectId") String projectId, Pageable pageable);
	
	@Query("""
			SELECT t.title FROM Task t WHERE t.project.projectId = :projectId
			""")
	List<String> findTitleByProjectId(@Param("projectId") String projectId);
	
	@Query("""
			SELECT t.title FROM Task t WHERE t.project.projectId = :projectId AND LOWER(t.title) LIKE (:query)
			""")
	List<String> findTitleByProjectIdAndQuery(@Param("projectId") String projectId, @Param("query") String query);
	
	@Query("""
		    SELECT t
		    FROM Task t
		    LEFT JOIN t.tags tag
		    WHERE (:assignedTo IS NULL OR t.assignedTo.memberName = :assignedTo)
		      AND (:status IS NULL OR t.status = :status)
		      AND ((:due IS NULL AND :dueStart IS NULL AND :dueEnd IS NULL) OR t.endDate = :due OR t.endDate BETWEEN :dueStart AND :dueEnd)
		      AND (:tags IS NULL OR tag.title IN :tags)
		      AND (:title IS NULL OR t.title = :title)
		      AND (:priority IS NULL OR t.priority = :priority)
		      AND t.project.projectId = :projectId
		      AND t.isDeleted = false
		""")
		List<Task> findTaskCardsByQuery(
		    @Param("projectId") String projectId,
		    @Param("assignedTo") String assignedTo,
		    @Param("due") LocalDate due,
		    @Param("dueStart") LocalDate dueStart,
		    @Param("dueEnd") LocalDate dueEnd,
		    @Param("title") String title,
		    @Param("priority") Integer priority,
		    @Param("status") String status,
		    @Param("tags") List<String> tags
		);

	@Query("SELECT COUNT(DISTINCT t.taskId) FROM Task t WHERE t.project.projectId = :projectId AND t.taskColumn.taskColumnId = :taskColumnId AND t.isDeleted = false")
	Long countByProjectIdAndTaskColumnId(@Param("projectId") String projectId, @Param("taskColumnId") String taskColumnId);

	
	
}
