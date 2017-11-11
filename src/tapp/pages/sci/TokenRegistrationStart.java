package tapp.pages.sci;

import org.apache.tapestry5.annotations.SessionAttribute;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.BeanModelSource;

import com.appspot.cloudserviceapi.sci.dao.FiOSTokenDAO;
import com.appspot.cloudserviceapi.sci.services.manager.FiOSTokenManager;

import tapp.model.sci.FiOSToken;

public class TokenRegistrationStart {

	@Inject
	private FiOSTokenManager beanManager;

	private FiOSToken myBean;

    @Inject
    private BeanModelSource beanModelSource;

    @SessionAttribute
    private String email;
    
	public FiOSToken getFiOSToken() {
		if(email == null) {
			myBean = new FiOSToken();
		} else
		if(myBean == null) {
		    myBean = (new FiOSTokenDAO()).findFiOSTokenByLoginId(email);
		    if(myBean == null) {
		    	myBean = new FiOSToken();
		    }
		}
		
		return myBean;
	}

	public void setFiOSToken(FiOSToken FiOSToken) {
		this.myBean = FiOSToken;
	}

/*	public List<FiOSToken> getFiOSTokens() {
		return beanManager.getFiOSTokens();
	}

	public boolean isEmptyList() {
		return beanManager.getFiOSTokens().isEmpty();
	}
*/
	public void onActionFromDelete(Long id) {
		beanManager.delete(id);
	}
}
