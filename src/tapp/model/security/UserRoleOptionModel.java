package tapp.model.security;

import java.util.Map;

import org.apache.tapestry5.OptionModel;

import com.appspot.cloudserviceapi.security.spring.model.UserRole;

public class UserRoleOptionModel implements OptionModel {
	UserRole userRole;

//	public UserRoleOptionModel(UserRole userRole) {
//		this.userRole = userRole;
//	}

	public String getLabel() {
		return userRole.name();
	}

	public Object getValue() {
		return userRole.toString();
	}

	public Map<String, String> getAttributes() {
		return null;
	}

	public boolean isDisabled() {
		return false;
	}
}
