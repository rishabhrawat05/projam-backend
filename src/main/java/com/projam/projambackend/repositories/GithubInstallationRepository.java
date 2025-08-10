package com.projam.projambackend.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.projam.projambackend.models.GithubInstallation;
import com.projam.projambackend.models.Workspace;


@Repository
public interface GithubInstallationRepository extends JpaRepository<GithubInstallation, String> {


	boolean existsByInstallationId(Long installationId);
	
	Optional<GithubInstallation> findTopBy();
	
	Optional<GithubInstallation> findByInstallationId(Long installationId);
	
	Optional<GithubInstallation> findByGithubUsername(String username);
	
	Optional<GithubInstallation> findByAdminGmail(String adminGmail);
	
	

}
