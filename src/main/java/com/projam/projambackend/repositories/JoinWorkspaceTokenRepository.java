package com.projam.projambackend.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.projam.projambackend.models.JoinWorkspaceToken;

@Repository
public interface JoinWorkspaceTokenRepository extends JpaRepository<JoinWorkspaceToken, Long> {

	Optional<JoinWorkspaceToken> findByToken(String token);
}
