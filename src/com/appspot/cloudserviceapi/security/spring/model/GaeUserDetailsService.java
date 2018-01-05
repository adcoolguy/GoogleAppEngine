package com.appspot.cloudserviceapi.security.spring.model;

//import org.springframework.dao.DataAccessException;
import javax.jdo.annotations.Transactional;

//import org.apache.shiro.dao.DataAccessException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.transaction.annotation.Transactional;

public class GaeUserDetailsService implements UserDetailsService {

	private UserSecurityDAO userSecurityDAO;

	public UserSecurityDAO getUserSecurityDAO() {
		return userSecurityDAO;
	}

	public void setUserSecurityDAO(UserSecurityDAO userSecurityDao) {
		this.userSecurityDAO = userSecurityDao;
	}

	@Transactional//(readOnly = true)
	public GaeUserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException {
		GaeUserDetails d = userSecurityDAO.findUserDetailsByLoginId(username);
		if (d == null) {
			throw new UsernameNotFoundException("Invalid user credentials");
		}
		return d;
	}

}