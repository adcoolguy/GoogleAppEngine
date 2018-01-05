package com.appspot.cloudserviceapi.security.spring.model;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;

public class GaeDaoAuthenticationProvider extends DaoAuthenticationProvider {

//	private void runAuthenticationChecks(GaeUserDetails user) throws Exception {
//		if (!user.isAccountNonLocked()) {
//			throw new LockedException("User account is locked");
//		}
//
//		if (!user.isEnabled()) {
//			throw new Exception("User account is disabled");
//		}
//
//		if (!user.isAccountNonExpired()) {
//			throw new Exception("User account is expired");
//		}
//
//		if (!user.isCredentialsNonExpired()) {
//			throw new CredentialsExpiredException("User credentials have expired");
//		}
//	}

	// @Autowired
	UserDetailsDAO userDetailsDAO;

	public void setUserDetailsDAO(UserDetailsDAO userDetailsDAO) {
		this.userDetailsDAO = userDetailsDAO;
	}

	// @Autowired
	// @Qualifier("userDetailsService")
	@Override
	public void setUserDetailsService(UserDetailsService userDetailsService) {
		super.setUserDetailsService(userDetailsService);
	}

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {

		try {

			Authentication auth = super.authenticate(authentication);

			String username = authentication.getName();
			// if reach here, means login success, else an exception will be
			// thrown
			// reset the user_attempts only if it is not previously locked!
//			GaeUserDetails user = userDetailsDAO.loadUserByUsername(username);
//			if(user != null && user.getAccountNonLocked())  {
				userDetailsDAO.resetFailAttempts(username);
				System.out.println("User account [" + username + "] attempts reset");
//			} else {
//				System.out.println("User account [" + username + "] is still locked!");
//			}

			return auth;

		} catch (BadCredentialsException e) {

			// invalid login, update to user_attempts
			userDetailsDAO.trackFailAttempts(authentication.getName());
			throw e;

		} catch (LockedException e) {

			// this user is locked!
			String error = "";
//			UserAttempts userAttempts = userDetailsDAO.getUserAttempts(authentication.getName());
//
//			if (userAttempts != null) {
//				Date lastAttempts = userAttempts.getLastModified();
				error = "User account [" + authentication.getName() + "] is locked!"
//						+ "<br>Last Attempts : " + lastAttempts
				;
//			} else {
//				error = e.getMessage();
//			}

			throw new LockedException(error);
		}

	}

}