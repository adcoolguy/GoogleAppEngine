package tapp.pages.sci;

import java.util.List;

import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.BeanModelSource;

import com.appspot.cloudserviceapi.sci.services.manager.FiOSTokenManager;

import tapp.model.sci.FiOSToken;

public class AdminTokenStart {

	@Inject
	private FiOSTokenManager beanManager;

	private FiOSToken myBean;

    @Inject
    private BeanModelSource beanModelSource;
    
	public FiOSToken getFiOSToken() {
		return myBean;
	}

	public void setFiOSToken(FiOSToken FiOSToken) {
		this.myBean = FiOSToken;
	}

	public List<FiOSToken> getFiOSTokens() {
		return beanManager.getFiOSTokens();
	}

	public boolean isEmptyList() {
		return beanManager.getFiOSTokens().isEmpty();
	}

	public void onActionFromDelete(Long id) {
		beanManager.delete(id);
	}
}
