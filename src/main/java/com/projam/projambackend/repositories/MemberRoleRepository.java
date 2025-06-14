package com.projam.projambackend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.projam.projambackend.models.MemberRole;

@Repository
public interface MemberRoleRepository extends JpaRepository<MemberRole, Long> {

}
