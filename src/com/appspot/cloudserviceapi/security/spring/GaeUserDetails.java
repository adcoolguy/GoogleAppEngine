package com.appspot.cloudserviceapi.security.spring;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import org.apache.tapestry5.beaneditor.Validate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
public class GaeUserDetails implements UserDetails, Cloneable, Serializable {

	@Basic @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

	@Basic
    private boolean enabled = true;

	@Basic
//	private Long userId;
	private String userId;

	@Basic
	private String password;

	@Basic
	private String firstName;
	
	@Basic
	private String lastName;

	//=== http://stackoverflow.com/questions/12106124/eclipse-error-on-mapping-with-embeddedid
	//===  turn off validation for this in Eclipse under Preferences -> Java Persistence -> JPA -> Errors/Warnings -> Attributes -> Cannot resolve attribute name
	@OneToMany(cascade=CascadeType.ALL, mappedBy="gaeUserDetails")
	private List<GaeGrantedAuthority> grantedAuthorities;

	@Basic
	private boolean accountNonExpired = true;

	@Basic
	private boolean accountNonLocked = true;
	
	@Basic
	private boolean credentialsNonExpired = true;
	
	/** source:
	 * http://tapestry.1045711.n5.nabble.com/Tapestry-5-2-5-Select-component-s-multiple-property-td4304338.html#a4313085
	 */
	@Transient
	private String userRole;
	@Basic
	private List<UserRole>userRoles;	

	private String userAgent;
	
	private Date lastAccessed;
	
	private Date lastModified;

//	public String getId() {
//		return id;
//	}
//
//	public void setId(String id) {
//		this.id = id;
//	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public String getUserId() {
		return userId;
	}

	@Validate("required")
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
    public boolean getEnabled() {
    	return this.enabled;
    }

    public void setEnabled(boolean enabled) {
    	this.enabled = enabled;
    } 
    
	public boolean getAccountNonExpired() {
		return accountNonExpired;
	}
    
	public void setAccountNonExpired(boolean accountNonExpired) {
		this.accountNonExpired = accountNonExpired;
	}
	
	public boolean getAccountNonLocked() {
		return accountNonLocked;
	}

	public void setAccountNonLocked(boolean accountNonLocked) {
		this.accountNonLocked = accountNonLocked;
	}

	public boolean getCredentialsNonExpired() {
		return credentialsNonExpired;
	}

	public void setCredentialsNonExpired(boolean credentialsNonExpired) {
		this.credentialsNonExpired = credentialsNonExpired;
	}

	public String getPassword() {
		return password;
	}
	
	@Validate("required,minlength=7")
	public void setPassword(String password) {
		this.password = password;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	public String getUserRole() {
		return getUserRoles().toString();
	}

	@Validate("required")
	public void setUserRole(String userRole) {
	}

	public List getUserRoles() {
		//printUserRoles("retrieved role(s) ", userRoles);
		return userRoles;
	}

	@Validate("required")
	public void setUserRoles(List<UserRole> userRoles) {
		this.userRoles = userRoles;
		//printUserRoles("selected role(s) ", userRoles);
	}

	public static void printUserRoles(String msg, List<UserRole>userRoles) {
		for(UserRole u:userRoles) {
			System.out.println(msg + "'" + u + "'");
		}
	}
	
	public Collection<GrantedAuthority> getAuthorities() {
		return toArray();
	}

	private Collection<GrantedAuthority> toArray() {
//		GrantedAuthority[] gArray = null;
		Collection<GrantedAuthority> gArray = new ArrayList();
			
		if(userRoles != null && userRoles.size() > 0) {
			int limit = userRoles.size();
//			gArray = new GrantedAuthority[limit];
			GaeGrantedAuthority ga = new GaeGrantedAuthority();
			for(int i=0; i<limit; i++) {
				ga.setRole(userRoles.get(i));
//				gArray[i] = ga;
				gArray.add(ga);
			}
			//System.out.println("userID '" + userId + "' role is '" + Arrays.asList(gArray).toString() + "'");
		}
		
		return gArray;
	}
	
	public String getUsername() {
		return userId;
	}
	
	public String getUserAgent() {
		return userAgent;
	}

	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}

	public Date getLastAccessed() {
		return lastAccessed;
	}

	public void setLastAccessed(Date lastAccessed) {
		this.lastAccessed = lastAccessed;
	}

	public Date getLastModified() {
		return lastModified;
	}

	public void setLastModified(Date lastModified) {
		this.lastModified = lastModified;
	}

	public boolean isAccountNonExpired() {
		return accountNonExpired;
	}

	public boolean isAccountNonLocked() {
		return accountNonLocked;
	}

	public boolean isCredentialsNonExpired() {
		return credentialsNonExpired;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public Object clone() throws CloneNotSupportedException {
		GaeUserDetails clone = (GaeUserDetails)super.clone();

		return clone;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((userId == null) ? 0 : userId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GaeUserDetails other = (GaeUserDetails) obj;
		if (userId == null) {
			if (other.userId != null)
				return false;
		} else if (!userId.equals(other.userId))
			return false;
		return true;
	}
	
}