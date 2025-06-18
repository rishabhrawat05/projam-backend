package com.projam.projambackend.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.projam.projambackend.models.Tag;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {

	Optional<Tag> findByTitle(String title);
}
