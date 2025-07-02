package com.projam.projambackend.repositories;



import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import com.projam.projambackend.dto.JoinWorkspaceRequestResponseSummary;
import com.projam.projambackend.models.JoinWorkspaceRequest;
import com.projam.projambackend.models.User;

@Repository
public interface JoinWorkspaceRequestRepository extends JpaRepository<JoinWorkspaceRequest, String> {
	

	Optional<JoinWorkspaceRequest> findByUser(User user);
	
	@Query("SELECT new com.projam.projambackend.dto.JoinWorkspaceRequestResponseSummary(wr.requestId, wr.workspace.workspaceId, wr.user.userId, wr.user.gmail, wr.requestTime, wr.status, wr.user.username) FROM JoinWorkspaceRequest wr WHERE wr.workspace.workspaceId = :workspaceId AND wr.status = 'PENDING'")
	List<JoinWorkspaceRequestResponseSummary> getAllRequestByWorkspaceIdAndPendingStatus(@Param("workspaceId") String workspaceId);
}
