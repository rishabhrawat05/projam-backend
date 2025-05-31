package com.projam.projambackend.repositories;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.projam.projambackend.models.JoinWorkspaceRequest;

@Repository
public interface JoinWorkspaceRequestRepository extends JpaRepository<JoinWorkspaceRequest, Long> {
	

}
