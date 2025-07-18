package com.projam.projambackend.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.projam.projambackend.models.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, String> {

	Optional<Role> findByRoleName(String roleName);
}
