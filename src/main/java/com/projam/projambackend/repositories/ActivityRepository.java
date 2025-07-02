package com.projam.projambackend.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.projam.projambackend.dto.ActivityResponse;
import com.projam.projambackend.models.Activity;

@Repository
public interface ActivityRepository extends JpaRepository<Activity, String> {

	@Query("SELECT new com.projam.projambackend.dto.ActivityResponse( a.activityId, a.description, a.timeStamp) FROM Activity a WHERE a.project.projectId = :projectId")
	List<ActivityResponse> findAllByProject_ProjectId(String projectId);
}
