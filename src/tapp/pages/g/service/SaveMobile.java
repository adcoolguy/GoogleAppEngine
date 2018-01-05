package tapp.pages.g.service;

import java.util.Date;

import org.apache.tapestry5.Asset;
import org.apache.tapestry5.ValidationException;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Path;
import org.apache.tapestry5.annotations.SessionAttribute;
import org.apache.tapestry5.corelib.components.BeanEditForm;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.RequestGlobals;

import com.appspot.cloudserviceapi.common.model.HTMLUtil;
import com.appspot.cloudserviceapi.common.model.SettingsDBUtils;
import com.appspot.cloudserviceapi.sci.dao.ServiceRegistryDAO;
import com.appspot.cloudserviceapi.sci.services.manager.ServiceRegistryManager;

import tapp.model.ServiceRegistry;

public class SaveMobile {

	private ServiceRegistry myBean;

	private Long id;

	@Inject
	private ServiceRegistryManager beanManager;

	@InjectPage
	private StartMobile start;

    @SessionAttribute
    private String email;
    
    @Component
    private BeanEditForm form;
    
	@Inject
	private RequestGlobals requestGlobals;
    
	//=== http://tapestry.apache.org/session-storage.html
	//@SessionState	//didn't work
	private Boolean justSaved;

	//=== ckeditor's config file (http://tynamo.org/tapestry-ckeditor+guide)
	@Inject
	@Path("config.js")
	private Asset config;

//	@InjectComponent
//	private Zone mainZone;
//
//	Object onSuccess() {
//		return mainZone.getBody();
//	}
	
    //private boolean backToStart = true;
    
//    void onSelectedFromSubmit() {
//    	backToStart = true;
//    }     
//    void onSelectedFromSave() {
//    	backToStart = false;
//    }
     
    public void onActivate(Long id) {
    	System.out.println("ServiceRegistry id '" + id + "'");
		if (id.equals(0L)) {
			myBean = new ServiceRegistry();
		} else {
			myBean = beanManager.getServiceRegistry(id);
		}
		
		try {
			if(!myBean.getUseHtml()) {
				myBean.setDescription(HTMLUtil.handleText(myBean.getDescription()));	//so that ckEditor display the new line properly
			}
		} catch (Exception e) {
			System.out.println("ServiceRegistrySave:onActivate legacy entity without getUseHtml flag: " + e);
		}
		this.id = id;
	}

	public Long onPassivate() {
		return id;
	}

	public Object onSubmit() throws ValidationException {
		Object retVal = null;
		
		id = (new ServiceRegistryDAO()).exist(myBean);
		Long bid = myBean.getId();
		
		if(myBean != null && id > 0 && !id.equals(myBean.getId())) {
			//as workaround as GAE4J does not support composite primary key i.e.
			//beanManager.delete(id);
            form.recordError("Service '" + myBean.getService() + "' already exists!");
		} else {
			myBean.setLastUpdated(new Date());
			if(myBean.getUseHtml()) {
//				myBean.setDescription(myBean.getDescription().replaceAll("<br>", "<p/>"));
			} else {
//				myBean.setDescription(HTMLUtil.removeTags(myBean.getDescription()));
			}
			if(myBean.getDescription() != null) {
				myBean.setDescription(myBean.getDescription().trim());
			}
			
			System.out.println("desc " + myBean.getDescription());
			beanManager.save(myBean);
//			Compass.runIndex();
			retVal = start;
			justSaved = true;
			requestGlobals.getHTTPServletRequest().getSession().setAttribute("justSavedServiceRegistry", justSaved);
		}
		if(myBean.getSaveOnly()) {
			retVal = null;
		}
		
		return retVal;
	}

	public ServiceRegistry getServiceRegistry() {
		return myBean;
	}

	public void setServiceRegistry(ServiceRegistry myBean) {
		this.myBean = myBean;
	}

	/** Backup Service Host */
	public String getBackupServiceHost() {
		return SettingsDBUtils.getSettings("backup.service.ip");
	}
	
	/** Service ID for Backup Service */
	public String getUniqueSID() {
		return com.appspot.cloudserviceapi.data.AppEngine.getName();
	}

}