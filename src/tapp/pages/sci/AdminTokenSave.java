package tapp.pages.sci;

import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.SessionAttribute;
import org.apache.tapestry5.ioc.annotations.Inject;

import com.appspot.cloudserviceapi.sci.services.manager.FiOSTokenManager;

import tapp.model.sci.FiOSToken;

public class AdminTokenSave {

	private FiOSToken myBean;

	private Long id;

	@Inject
	private FiOSTokenManager beanManager;

	@InjectPage
	private AdminTokenStart start;

    @SessionAttribute
    private String email;
    
    public void onActivate(Long id) {
		if (id.equals(0L)) {
			myBean = new FiOSToken();
			myBean.setUserId(email);
		} else {
			myBean = beanManager.getFiOSToken(id);
		}
		this.id = id;
	}

	public Long onPassivate() {
		return id;
	}

	public Object onSubmit() {
//		Long id = -1L;
//		
//		id = (new FiOSTokenDAO()).exist(myBean);
//		Long bid = myBean.getId();
//		
//		if(myBean != null && id > 0 && !id.equals(myBean.getId())) {
//			throw new RuntimeException("FiOSToken with FiOSToken id '" + myBean.getUserId() + "' already exists!");
//		}
		beanManager.save(myBean);
		
		return start;
	}

	public FiOSToken getFiOSToken() {
		return myBean;
	}

	public void setFiOSToken(FiOSToken myBean) {
		this.myBean = myBean;
	}

}