package tapp.pages.sci;

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

import com.appspot.cloudserviceapi.common.HTMLUtil;
import com.appspot.cloudserviceapi.common.SettingsDBUtils;
import com.appspot.cloudserviceapi.sci.dao.ServiceRegistryDAO;
import com.appspot.cloudserviceapi.sci.services.ServiceRegistryServlet;
import com.appspot.cloudserviceapi.sci.services.manager.ServiceRegistryManager;

import app.common.SecurityUtils;
import tapp.model.ServiceRegistry;

public class ServiceRegistrySave {

//	private String TCOUNT_UUID = "service.registry.count";

	private ServiceRegistry myBean;

	private Long id;

	@Inject
	private ServiceRegistryManager beanManager;

	@InjectPage
	private ServiceRegistryStart start;

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

	//=== for dirty check comparison
//	private ServiceRegistry originalBean;	//TODO cause duplicate entry in Start! commented out

    private long totalRows = -1;

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

//	public void initTotalRows() {
//		String t = SettingsDBUtils.getSettings(TCOUNT_UUID);
//		if(!StringUtils.isEmpty(t)) {
//			try {
//				long count = Integer.valueOf(t);
//				totalRows = count;
//			} catch (NumberFormatException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//	}

//	private void incrementTotalCount() {
//		totalRows = totalRows+1;
//		SettingsDBUtils.updateSettings(TCOUNT_UUID, String.valueOf(totalRows));
//		initTotalRows();
//	}

    public void onActivate(Long id) {
    	System.out.println("ServiceRegistry id '" + id + "'");
		if (id.equals(0L)) {
			myBean = new ServiceRegistry();
			myBean.setOid(SecurityUtils.getLoggedInUser());
		} else {
			myBean = beanManager.getServiceRegistry(id);
			if(myBean != null) {
				//=== saved for dirty check
//				try {
//					originalBean = (ServiceRegistry) myBean.clone();	//TODO cause duplicate entry in start page!??
//				} catch (CloneNotSupportedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
			}
		}
		
		try {
			if(myBean != null && !myBean.getUseHtml()) {
				myBean.setDescription(HTMLUtil.handleText(myBean.getDescription()));	//so that ckEditor display the new line properly
			}
		} catch (Exception e) {
			System.out.println("ServiceRegistrySave:onActivate legacy entity without getUseHtml flag: " + e);
		}
		this.id = id;
		
//		initTotalRows();
	}

	public Long onPassivate() {
		return id;
	}

	public Object onSubmit() throws ValidationException {
		Object retVal = null;
		ServiceRegistry oldServiceRegistry = null;

		ServiceRegistryDAO dao = new ServiceRegistryDAO();
		oldServiceRegistry = dao.findServiceRegistryByService(myBean.getService());
		if(oldServiceRegistry != null) {
			id = oldServiceRegistry.getId();
		}
		
		if(id > 0 && !id.equals(myBean.getId())) {
			//as workaround as GAE4J does not support composite primary key i.e.
			//beanManager.delete(id);
            form.recordError("Service '" + myBean.getService() + "' already exists!");
		} else {
			if(id == 0 || ServiceRegistryHelper.isUpdateAllowed(id)) {	//anyone can create a new SR (id == 0)
				//=== is it dirty?
//				if (originalBean == null || !originalBean.equals(myBean)) {
					myBean.setLastUpdated(new Date());
					/*
					if(myBean.getUseHtml()) {
						myBean.setDescription(myBean.getDescription().replaceAll("<br>", "<p/>"));
					} else {
						myBean.setDescription(HTMLUtil.removeTags(myBean.getDescription()));
					}
					*/
					if(myBean.getDescription() != null) {
						myBean.setDescription(myBean.getDescription().trim());
					}
					
					//System.out.println("desc " + myBean.getDescription());
					if(myBean.getService() != null) myBean.setService(myBean.getService().trim());

					beanManager.save(myBean);
//					incrementTotalCount();

					System.out.println("service [" + myBean.getService() + "] saved");
//				} else {
//					System.out.println("service [" + myBean.getService() + "] not saved (not changed)");
//				}
				//Compass.runIndex();
				
				//=== "remove" the bean so that it would not become a shadow bean in the start!!!
//				originalBean = null;
				
				retVal = start;
				justSaved = true;
				requestGlobals.getHTTPServletRequest().getSession().setAttribute("justSavedServiceRegistry", justSaved);
			} else {
	            form.recordError("Sorry, but update to Service '" + myBean.getService() + "' is not permitted.");
			}
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
	
	public String getServiceRegistryService() {
		return "unknown";
	}
	
//	@Inject
//	private Response _response;
//	
//	public void onActionFromLogout(String service) {
//		requestGlobals.getRequest().getSession(false).invalidate();
//
//		String url = Constants.LAUNCHER_URI + "/" + service;
//		
//		try {
//			beanManager.save(myBean);
//			
//			_response.sendRedirect(url);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}	
    
	//TODO to replace with Spring Security's security annotation/tag
	/** Backup Service On/Off Switched */
	public String getBackupServiceDisplay() {
		String retVal = "none";
		if(ServiceRegistryServlet.isBackupServiceEnabled()) {
			retVal = "inline";
		}
		return retVal;
	}

	//TODO to replace with Spring Security's security annotation/tag
	/** Advanced UI On/Off Switch */
	public String getAdvancedUIDisplay() {
		String retVal = "none";
		
//		String temp = SettingsDBUtils.getSettings("advanced.ui.active");
//		if(temp != null && temp.equals("true")) {
//			retVal = "inline";
//		}
		return retVal;
	}

	public String getCancelButton() {
		String value = "<input id='buttonCancel' type='button' value='Cancel' onclick='javascript:alert('1');history.go(-1);'/>";
		
		return value;
	}
}