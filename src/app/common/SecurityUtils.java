package app.common;

import java.util.Collection;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
//import org.datanucleus.util.StringUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import com.appspot.cloudserviceapi.security.spring.model.GaeGrantedAuthority;
import com.appspot.cloudserviceapi.security.spring.model.GaeUserDetails;

import app.model.Movie;

public class SecurityUtils {

	public static HttpServletResponse handleOWASPSession(HttpServletRequest req, HttpServletResponse res) {
		HttpServletResponse retVal = res;

		String sessionid = req.getSession().getId();
		if(!StringUtils.isEmpty(sessionid)) {
			/** secured session cookie */
			//=== https://www.owasp.org/index.php/HttpOnly
			// be careful overwriting with HttpOnly: JSESSIONID may have been set with other flags + secure: https://blog.whitehatsec.com/session-cookie-httponly-flag-java/
			//retVal.setHeader("SET-COOKIE", "JSESSIONID=" + sessionid + "; HttpOnly; secure");
			/** frame busting (https://www.owasp.org/index.php/Clickjacking_Protection_for_Java_EE) - just for the IE browsers */
	        // to prevent all framing of this content
			retVal.addHeader( "X-FRAME-OPTIONS", "DENY" );
	        // to allow framing of this content only by this site
			retVal.addHeader( "X-FRAME-OPTIONS", "SAMEORIGIN" );
		}
		
        return retVal;
	}
	
	public static boolean isAuthenticated(HttpServletRequest request) {
		boolean retVal = false;

		//TODO buggy - check disabled!
		retVal = true;
		
//		String uid = (String) request.getSession().getAttribute(Constants.UNIVERSAL_ID);
//		if(!StringUtils.isEmpty(uid)) {
//			retVal = true;
//		}
		
        return retVal;
	}
	
	public static boolean isOwner(Movie item, String oid) throws Exception {
		boolean retVal = false;
		
		if(item == null) {
			throw new Exception(Constants.ERR_NO_ITEM_FOR_OWNERSHIP);
		}
		String owner = oid;	//AppUtils.getOwnerPart(oid);
		System.out.println("SecurityUtils.java isOwner checking owner " + owner + " with " + item.getOid() + " ...");
		if(oid != null && owner != null && owner.equals(item.getOid())) {
			retVal = true;
		} else 
		//handles migrated entities too
		if(oid != null && owner != null && "undefined".equals(item.getOid())) {
			retVal = true;
		}

		return retVal;
	}

	/** Loggedin User's ID */
	public static String getLoggedInUser() {
		GaeUserDetails user = null;
	    String name = "anonymous";
		
		if(SecurityContextHolder.getContext().getAuthentication() != null) {
			try {
				name = (String)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			} catch (Exception e) {
				System.out.println("ServiceRegistryStart.java 1 Not anonymous login, trying the authenticated credential ...");
				user = (GaeUserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
				name = user.getUsername(); //get logged in username
			}
		}
		
		return name;
	}

	public static String getLoggedInUserRole() {
		String retVal = "";
		
		Collection<GaeGrantedAuthority> authorities = null;
		if(SecurityContextHolder.getContext().getAuthentication() != null) {
			authorities = (Collection<GaeGrantedAuthority>) SecurityContextHolder.getContext().getAuthentication().getAuthorities();
		}
		try {
			SimpleGrantedAuthority auth = null;
			
			if(authorities != null) {
				Iterator it = authorities.iterator();
				while(it.hasNext()) {
					auth = (SimpleGrantedAuthority) it.next();
					retVal += "_" + auth.toString();
				}
			} else {
				retVal = "No Role Assigned";
			}
		} catch (Exception e) {
			//TODO should not need to depend on exception to perform authentication check!!!
			//System.out.println("ServiceRegistryStart.java 2 Not anonymous login, trying the authenticated credential ...");
			GaeGrantedAuthority auth = null;
			
			if(authorities != null) {
				Iterator it = authorities.iterator();
				while(it.hasNext()) {
					auth = (GaeGrantedAuthority) it.next();
					retVal += "_" + auth.getRole().toString();
				}
			} else {
				retVal = "No Role Assigned";
			}
		}

		return retVal;
	}
	
	/**
	 * targetSecurityToken is the string/text to compare against the role's string acquired by the user.
	 *
	 * @param targetSecurityToken
	 * @return
	 */
	public static boolean isAdmin(String targetSecurityToken) {
		boolean retVal = false;

		if(!StringUtils.isEmpty(targetSecurityToken) && !StringUtils.isEmpty(getLoggedInUserRole()) && getLoggedInUserRole().indexOf(targetSecurityToken) > -1) {
			retVal = true;
		}
		
        return retVal;
	}

	public static boolean isParseLogin(String targetSecurityToken) {
		boolean retVal = false;

		if(!StringUtils.isEmpty(targetSecurityToken) && targetSecurityToken.trim().startsWith("r:")) {
			retVal = true;
		}
		
        return retVal;
	}
}