package com.projam.projambackend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.projam.projambackend.models.Activity;

@Repository
public interface ActivityRepository extends JpaRepository<Activity, Long> {

}
