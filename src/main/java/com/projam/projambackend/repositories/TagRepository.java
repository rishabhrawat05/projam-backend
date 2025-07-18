package com.projam.projambackend.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.projam.projambackend.dto.TagResponse;
import com.projam.projambackend.models.Tag;

@Repository
public interface TagRepository extends JpaRepository<Tag, String> {

	Optional<Tag> findByTitle(String title);
	
	@Query("SELECT new com.projam.projambackend.dto.TagResponse(tr.tagId, tr.title) FROM Tag tr JOIN tr.projects p WHERE p.projectId = :projectId")
	List<TagResponse> findAllByProjectId(@Param("projectId") String projectId);
}
