package tapp.pages.sci;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.tapestry5.Asset;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.SessionState;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.corelib.components.ActionLink;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.corelib.components.Grid;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.AssetSource;
import org.apache.tapestry5.services.RequestGlobals;
import org.apache.tapestry5.services.ajax.AjaxResponseRenderer;
import org.ocpsoft.pretty.time.PrettyTime;
import org.springframework.security.core.Authentication;

import com.appspot.cloudserviceapi.common.HTMLUtil;
import com.appspot.cloudserviceapi.common.TimeUtil;
import com.appspot.cloudserviceapi.data.AppEngine;
import com.appspot.cloudserviceapi.sci.dao.ServiceRegistryDAO;
import com.appspot.cloudserviceapi.sci.services.manager.ServiceRegistryManager;
import com.appspot.cloudserviceapi.security.spring.UserRole;
import com.appspot.cloudserviceapi.util.Compass;

import app.common.SecurityUtils;
import tapp.model.ServiceRegistry;

public class ServiceRegistryStart {

//	private String TCOUNT_UUID = "service.registry.count";
    private int MB = 1024*1024;

//	@Inject
//    private static AlertManager alertManager;	//didn't work on GAEJ!!! :(
	@Inject 
	private AjaxResponseRenderer ajaxResponseRenderer;

	@SessionState
	private Authentication auth;
	
	@Component(id="landscapeSrGrid")
    private Grid _landscapeGrid;

	@Component(id="portraitSrGrid")
    private Grid _portraitGrid;

	@Inject
	private ServiceRegistryManager beanManager;

	private ServiceRegistry myBean;

	@Inject 
	private Messages messages;
	
	@Inject
	private RequestGlobals requestGlobals;
	
	//=== http://tapestry.apache.org/session-storage.html
	//@SessionState(create=true)	//didn't work
    private Boolean justSaved;
    
    private String currentExcerpt;
	
    private static String cachedHostName;
    
    @Component
    private Form form1;
    @Component
    private Form form2;
//    @Inject 
//    private AlertManager alertManager;

    private long totalRows = 0;
    
//    @Inject
//    private Request request;
    @InjectComponent
    private Zone myZone;
    private String indexStatusMessage;


    public String getIndexStatusMessage() {
		return indexStatusMessage;
	}

	public String getCustomErrorMessage() {
    	return "Error";
    }
    
	public ServiceRegistry getServiceRegistry() {
		return myBean;
	}

	public void setServiceRegistry(ServiceRegistry ServiceRegistry) {
		this.myBean = ServiceRegistry;
	}

	public List<ServiceRegistry> getServiceRegistrys() {
		return beanManager.getServiceRegistrys();
	}

	public boolean isEmptyList() {
		return beanManager.getServiceRegistrys().isEmpty();
	}

	/** cost of this call has not been evaluated!!! */
	public long getTotalRows() {
		long totalRows = 0;
//		String t = SettingsDBUtils.getSettings(TCOUNT_UUID);
//		if(!StringUtils.isEmpty(t)) {
			try {
				ServiceRegistryDAO dao = new ServiceRegistryDAO();
				List l = dao.getCloneList();
				if(l != null) {
					totalRows = l.size();
				}
//				long count = Integer.valueOf(t);
//				totalRows = count;
//			} catch (NumberFormatException e) {
//				// TODO Auto-generated catch block
//				//just ignore it
			} catch (Exception e) {
				e.printStackTrace();
			}
		return totalRows;
	}
	
	/**
	 * https://tapestry.apache.org/component-rendering.html
	 * 
	 * @return
	 */
//	@AfterRender
//	public void initTotalRows() {
//		long retVal = -1;
//		if(_portraitGrid != null && _portraitGrid.getDataSource() != null)
//			retVal = _portraitGrid.getDataSource().getAvailableRows();
//		else if(_landscapeGrid != null && _landscapeGrid.getDataSource() != null)
//			retVal = _landscapeGrid.getDataSource().getAvailableRows();
//		
//		totalRows = retVal;	//beanManager.getServiceRegistrys().size(); //TBD PERF expensive operation!
//	}
	
	/**
	 * Thanks to http://apache-tapestry-mailing-list-archives.1045711.n5.nabble.com/delete-confirm-td2421285.html#a2421287
	 */
	@Component(parameters = {"onClick=literal:return confirm(\"OK to delete this item?\");"})
	private ActionLink delete;
			
	public void onActionFromDelete(Long id) throws Exception {
		if(ServiceRegistryHelper.isUpdateAllowed(id)) {
			try {
				beanManager.delete(id);
//				decrementTotalCount();
			} catch (Exception e) {
				//e.printStackTrace();
				// TODO is this a real issue or due to improper implementation?
				if(e.getMessage() != null && !e.getMessage().equals("Cannot read fields from a deleted object")) {
					form1.recordError("Sorry, not able to delete Service Id '" + id + "' due to an error [" + e.getMessage() + "].");
				} else {
//					decrementTotalCount();
				}
			}
		} else {
            form1.recordError("Sorry, but delete to Service Id '" + id + "' is not permitted.");
//			AlertManager alertManager = TapestryUtil.getAlertManager();
//			if(alertManager != null) {
//				alertManager.alert(Duration.SINGLE, Severity.INFO, "Sorry, but delete to Service Id '" + id + "' is not permitted.");
//			}
		}
	}

//	private void decrementTotalCount() {
//		totalRows = totalRows-1;
//		SettingsDBUtils.updateSettings(TCOUNT_UUID, String.valueOf(totalRows));
//		initTotalRows();
//	}

	public String getHostName() {
		if(cachedHostName == null) {
			cachedHostName = requestGlobals.getHTTPServletRequest().getRemoteHost();
			if(cachedHostName != null && !cachedHostName.equals("localhost") && !cachedHostName.equals("127.0.0.1")) {	//TODO future enhancement should avoid this check on PROD
				cachedHostName = AppEngine.getHostName();
			} else {
				cachedHostName += ":8888";
			}
//			cachedHostName = AppUtils.getAppHost(cachedHostName);
		}
		
		System.out.println("cachedHostName set to [" + cachedHostName + "]");
		
		return cachedHostName;
	}

	public String getHostNameWithoutProtocol() {
		return removeProtocol(cachedHostName);
	}

	private String removeProtocol(String cachedHostName) {
		String ret = cachedHostName;
		if(ret != null) {
			ret = ret.replaceAll("https://", "");
			ret = ret.replaceAll("http://", "");
			ret = ret.trim();
		}
		
		return ret;
	}

	/*
	 * Used for sorting the grid after each new entry is inserted.
	 * This method will be called during the Grid rendering.
	 * 
	 * source: http://tapestry.1045711.n5.nabble.com/T5-disabling-column-sorting-programmatically-in-Grid-td2427489.html
	 */
//	public BeanModel getBeanModel() {
//		BeanModel model = beanModelSource.createDisplayModel(ServiceRegistry.class,
//				messages);
//		PropertyModel nameColumn = model.getById("lastUpdated");
//		nameColumn.sortable(true);
//		((Grid)model).getSortModel().updateSort("lastUpdated");
//		return model;
//	}
	
	@SetupRender
	void setupRender() {
		currentExcerpt = "";
		/*
        BeanModel model = _grid.getDataModel(); 
        List<String> propertyNames = model.getPropertyNames(); 
        for (String propName : propertyNames) { 
            PropertyModel propModel = model.get(propName); 
            propModel.sortable(false);
        }
        */
		justSaved = (Boolean)requestGlobals.getHTTPServletRequest().getSession().getAttribute("justSavedServiceRegistry");
		//System.out.println("sr: justSaved " + justSaved);
		if(justSaved != null && !justSaved) {
//	        _grid.getSortModel().updateSort("lastAccessed");
//	        _grid.getSortModel().updateSort("lastAccessed");
		} else {
			//=== these allow a descending sort based on column lastUpdatedDate (I know it seems like it is a hack, but it is not!)
			_portraitGrid.getSortModel().updateSort("lastAccessed");
			_portraitGrid.getSortModel().updateSort("lastAccessed");
			_landscapeGrid.getSortModel().updateSort("lastUpdated");
			_landscapeGrid.getSortModel().updateSort("lastUpdated");
	        requestGlobals.getHTTPServletRequest().getSession().setAttribute("justSavedServiceRegistry", false);
		}

//		if(_landscapeGrid != null && _landscapeGrid.getDataSource() != null) {
//			totalRows = _landscapeGrid.getDataSource().getAvailableRows();
//		} else
//		if(_portraitGrid != null && _portraitGrid.getDataSource() != null) {
//			totalRows = _portraitGrid.getDataSource().getAvailableRows();
//		}

//		initTotalRows();
    }

/*	
	public String getDescriptionExcerpt() {
		String retVal = myBean.getDescription();
//		if(myBean.getUseHtml()) {
			retVal = HTMLUtil.removeTags(myBean.getDescription());	//cause slow startup on the GAEJ cloud
//		}
		retVal = StringUtils.abbreviate(retVal, 100);
		return retVal;
	}
*/

	public String getCurrentExcerpt() {
		return currentExcerpt;
	}

	public String getDescriptionDetails() {
		String retVal = myBean.getDescription();
		try {
			if(!myBean.getUseHtml()) {
				retVal = HTMLUtil.removeTags(myBean.getDescription());	//#PERF1
				retVal = retVal.trim();
			}
				
			currentExcerpt = StringUtils.abbreviate(retVal, 100);
		} catch(Exception e) {
		}
		return retVal;
	}
	
	public String getFriendlyLastUpdatedTime() {
		PrettyTime p = new PrettyTime();
		
		return p.format(myBean.getLastUpdated());
	}	

	public String getFriendlyLastAccessedLocalTime() {
		PrettyTime p = new PrettyTime();
		String ret = "N/A";

//		Date lastRead = TimeUtil.getHQDate(myBean.getLastAccessed());
//		if(lastRead != null) {
//			ret = p.format(lastRead);
//		}
		ret = TimeUtil.getHQDateTime(myBean.getLastAccessed());
		
		return ret;
	}	
			
	/**
	 * http://blog.snugsound.com/2008/08/implementing-logout-function-in.html
	 */
	public Object onActionFromLogout() {
		requestGlobals.getRequest().getSession(false).invalidate();

		return Index.class;
	}

	public Object onActionFromLogout1() {
		requestGlobals.getRequest().getSession(false).invalidate();

		return Index.class;
	}

	/** Loggedin User's ID */
	public String getLoggedInUser() {
		String retVal = SecurityUtils.getLoggedInUser();

		return retVal;
	}
	
	/** Loggedin User's Role(s) */
	public String getUserAssignedRole() {
		String retVal = SecurityUtils.getLoggedInUserRole();

		return retVal;
	}
	
	//TODO to replace with Spring Security's security annotation/tag
	/** Running lucene index */
	public String getRunIndexDisplay() {
		String retVal = "none";
		
		if(SecurityUtils.isAdmin(UserRole.ROLE_ADMIN_SCI.toString()) || SecurityUtils.isAdmin(UserRole.ROLE_ADMIN.toString())) {
			retVal = "inline";
		}
		return retVal;
	}

	//TODO to replace with Spring Security's security annotation/tag
	/** Delete action link */
	public String getDeleteActionDisplay() {
		String retVal = "none";
//		
//		if(SecurityUtils.isAdmin(UserRole.ROLE_ADMIN_SCI.toString()) || SecurityUtils.isAdmin(UserRole.ROLE_ADMIN.toString())) {
			retVal = "inline";
//		}
		return retVal;
	}

	//TODO to replace with Spring Security's security annotation/tag
	/** Genius start link */
	public String getGeniusStartDisplay() {
		String retVal = "none";
		
//		if(SecurityUtils.isAdmin(UserRole.ROLE_ADMIN_SCI.toString()) || SecurityUtils.isAdmin(UserRole.ROLE_ADMIN.toString())) {
			retVal = "inline";
//		}
		return retVal;
	}
	
	//TODO to replace with Spring Security's security annotation/tag
	/** Running lucene index */
	public String getSearchBoxDisplay() {
		String retVal = "none";
		
		if(SecurityUtils.isAdmin(UserRole.ROLE_ADMIN_SCI.toString()) || SecurityUtils.isAdmin(UserRole.ROLE_ADMIN.toString())) {
			retVal = "inline";
		}
		return retVal;
	}

	/** t-data-grid-pager */
	public String getDataGridPagerDisplay() {
		String retVal = "none";
		
		if(SecurityUtils.isAdmin(UserRole.ROLE_ADMIN_SCI.toString()) || SecurityUtils.isAdmin(UserRole.ROLE_ADMIN.toString())) {
			retVal = "inline";
		}
		return retVal;
	}
	
    Object onActionFromRunIndexLink() {
    	try {
    		indexStatusMessage = "processing ...";
    		Compass.index();
    		Date updatedDate = new Date();	//DateTimeFormat.forPattern("MM/dd/yyyy hh:mm:ss").parseDateTime(dtStr).toDate();
    		indexStatusMessage = updatedDate.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return myZone.getBody(); // AJAX request, return zone's own body
    }

    @Deprecated
	public void onSubmit() {
		try {
//			ajaxResponseRenderer.addCallback(new JavaScriptCallback() { 
//	            @Override 
//	            public void run(JavaScriptSupport javaScriptSupport) { 
//	                System.out.println("works?");
//	            } 
//		    }); 
			Compass.index();
			//alertManager.alert( Duration.TRANSIENT, Severity.INFO, "Search index was processed successfully" );
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

    @Deprecated		//cost one more server side call!
    @Inject
    private AssetSource assetSource;

    //http://stackoverflow.com/questions/16996219/unable-to-locate-asset-the-file-does-not-exist
    @Deprecated		//cost one more server side call!
    public Asset getSearchIcon() {
        final String path = "images/" + "google-web-search-m" + ".png";
        return assetSource.getContextAsset(path, null);
    }

    public long getFreeMemory() {
    	return Runtime.getRuntime().freeMemory() / MB;
    }

    public long getTotalMemory() {
    	return Runtime.getRuntime().totalMemory() / MB;
    }

}