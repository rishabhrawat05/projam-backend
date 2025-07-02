package com.projam.projambackend.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.projam.projambackend.models.User;

@Repository
public interface UserRepository extends JpaRepository<User, String>{

	public Optional<User> findByGmail(String gmail);

	
}
