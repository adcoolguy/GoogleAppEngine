package tapp.pages;

import java.util.List;

import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.BeanModelSource;

import com.appspot.cloudserviceapi.security.spring.GaeUserDetails;
import com.appspot.cloudserviceapi.services.manager.UserManager;

public class UserStart {

	@Inject
	private UserManager beanManager;

	private GaeUserDetails myBean;

    @Inject
    private BeanModelSource beanModelSource;
    
	public GaeUserDetails getUser() {
		return myBean;
	}

	public void setUser(GaeUserDetails User) {
		this.myBean = User;
	}

	public List<GaeUserDetails> getUsers() {
		return beanManager.getUsers();
	}

	public boolean isEmptyList() {
		return beanManager.getUsers().isEmpty();
	}

	public void onActionFromDelete(Long id) {
		beanManager.delete(id);
	}
}
