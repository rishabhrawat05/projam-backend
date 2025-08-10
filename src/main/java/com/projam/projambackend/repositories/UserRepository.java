package com.projam.projambackend.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.projam.projambackend.dto.UserProfileResponse;
import com.projam.projambackend.dto.UserResponse;
import com.projam.projambackend.models.User;

@Repository
public interface UserRepository extends JpaRepository<User, String>{

	Optional<User> findByGmail(String gmail);
	
	Optional<User> findByRefreshToken(String refreshToken);
	
	@Query("SELECT new com.projam.projambackend.dto.UserProfileResponse( u.username, u.gmail) FROM User u WHERE u.gmail = :gmail")
	Optional<UserProfileResponse> findUserProfileResponseByGmail(@Param("gmail") String gmail);

	
}
