package com.projam.projambackend.repositories;



import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.projam.projambackend.models.JoinWorkspaceRequest;
import com.projam.projambackend.models.User;

@Repository
public interface JoinWorkspaceRequestRepository extends JpaRepository<JoinWorkspaceRequest, Long> {
	

	Optional<JoinWorkspaceRequest> findByUser(User user);
}
