package com.projam.projambackend.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.projam.projambackend.models.JoinProjectToken;

@Repository
public interface JoinProjectTokenRepository extends JpaRepository<JoinProjectToken, String> {

	Optional<JoinProjectToken> findByToken(String token);
}
