package com.projam.projambackend.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.projam.projambackend.models.ProjectDeletionStatus;

@Repository
public interface ProjectDeletionStatusRepository extends JpaRepository<ProjectDeletionStatus, String> {

	Optional<ProjectDeletionStatus> findByProjectId(String projectId);
}
