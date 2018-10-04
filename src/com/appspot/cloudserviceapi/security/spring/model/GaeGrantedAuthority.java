package com.appspot.cloudserviceapi.security.spring.model;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.springframework.security.core.GrantedAuthority;

import com.google.appengine.api.datastore.Key;

@Entity
public class GaeGrantedAuthority implements GrantedAuthority {

	public static final String ROLE_USER = "ROLE_USER";
	public static final String ROLE_ANONYMOUS = "ROLE_ANONYMOUS";
	public static final String ROLE_ADMIN = "ROLE_ADMIN";

	@Basic	
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private Key id;

	@Enumerated(EnumType.STRING)
	private UserRole role = UserRole.ROLE_USER;

	@Basic
	private GaeUserDetails gaeUserDetails;

	public UserRole getRole() {
		return role;
	}

	public void setRole(UserRole role) {
		this.role = role;
	}

	public GaeUserDetails getGaeUserDetails() {
		return gaeUserDetails;
	}

	public void setGaeUserDetails(GaeUserDetails gaeUserDetails) {
		this.gaeUserDetails = gaeUserDetails;
	}

	
	public String getAuthority() {
		return role!=null?role.toString():null;
	}

	public int compareTo(Object o) {
		// TODO Auto-generated method stub
		return 0;
	}

}