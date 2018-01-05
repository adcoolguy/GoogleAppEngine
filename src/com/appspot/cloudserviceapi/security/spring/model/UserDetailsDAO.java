package com.appspot.cloudserviceapi.security.spring.model;

public interface UserDetailsDAO {

	public void trackFailAttempts(String username);

	public void resetFailAttempts(String username);

//	UserAttempts getUserAttempts(String username);
	
	GaeCache getUserAttempts();

	GaeUserDetails loadUserByUsername(String username);

}