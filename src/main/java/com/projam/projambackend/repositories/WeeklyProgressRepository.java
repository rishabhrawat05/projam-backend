package com.projam.projambackend.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.projam.projambackend.dto.WeeklyProgressResponse;
import com.projam.projambackend.models.WeeklyProgress;

@Repository
public interface WeeklyProgressRepository extends JpaRepository<WeeklyProgress, String> {

	@Query("SELECT new com.projam.projambackend.dto.WeeklyProgressResponse(wp.month, wp.weekOfMonth, wp.year, wp.weeklyProgressPercent, wp.totalWeeksOfMonth) FROM WeeklyProgress wp WHERE wp.project.projectId = :projectId AND wp.member.memberGmail = :email")
	List<WeeklyProgressResponse> findByProjectIdAndEmail(@Param("projectId") String projectId, @Param("email") String email);
	
	@Query("SELECT new com.projam.projambackend.dto.WeeklyProgressResponse(wp.month, wp.weekOfMonth, wp.year, wp.weeklyProgressPercent, wp.totalWeeksOfMonth) FROM WeeklyProgress wp WHERE wp.project.projectId = :projectId")
	List<WeeklyProgressResponse> findByProjectId(@Param("projectId") String projectId);
}
