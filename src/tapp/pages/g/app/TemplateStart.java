package tapp.pages.g.app;

import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.tapestry5.EventConstants;
import org.apache.tapestry5.alerts.AlertManager;
import org.apache.tapestry5.alerts.Duration;
import org.apache.tapestry5.alerts.Severity;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Grid;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.BeanModelSource;
import org.apache.tapestry5.services.RequestGlobals;
import org.ocpsoft.pretty.time.PrettyTime;

import com.appspot.cloudserviceapi.common.BackupService;
import com.appspot.cloudserviceapi.common.TapestryUtil;
import com.appspot.cloudserviceapi.data.AppEngine;
import com.appspot.cloudserviceapi.dto.Geniu;
import com.appspot.cloudserviceapi.security.spring.model.UserRole;
import com.appspot.cloudserviceapi.util.Compass;
//import org.apache.shiro.authz.annotation.RequiresUser;

import app.common.SecurityUtils;
import cloudserviceapi.service.manager.GeniusManager;
import tapp.pages.sci.Index;

public class TemplateStart {

	private static boolean initFreqDone = false;
	
	@Component(id="genius1Grid")
    private Grid _grid;
	
	@Inject
	private GeniusManager beanManager;

//	private Template myBean;
    @Property 
	private Geniu myBean;

    @Inject
    private BeanModelSource beanModelSource;
    
	@InjectPage
	private TemplateStart start;
    
//	@Inject
//	private PageRenderLinkSource linkSource;
	
	@InjectPage
	private TemplateActionDelete delete;

	@Inject
	private RequestGlobals requestGlobals;
	 
	//=== http://tapestry.apache.org/session-storage.html
	//@SessionState(create=true)	//didn't work
    private Boolean justSaved;

	@Inject
    private AlertManager alertManager;
    
	public Geniu getTemplate() {
		return myBean;
	}

	public void setTemplate(Geniu Template) {
		this.myBean = Template;
	}

	public List<Geniu> getTemplates() throws Exception {
		List<Geniu> retVal = beanManager.getGenius();
		//=== do only once
		if(!initFreqDone) {
			initFrequency(retVal);
		}
		System.out.println("TemplateStart:getTemplates size " + retVal.size());
		return retVal;
	}

	private void initFrequency(List<Geniu> l) {
		Enumeration<Geniu> e = Collections.enumeration(l);
    	Geniu o = null;
	    for (; e.hasMoreElements();) {
	    	o = e.nextElement();
	    	//System.out.println(o);
	    	o.countFrequency();
	    }
	    initFreqDone = true;
	}

	public boolean isEmptyList() throws Exception {
		return beanManager.getGenius().isEmpty();
	}

	public long getTotalRows() {
		long totalRows = 0;
		List l = beanManager.getClonedList();
		if(l != null) {
			totalRows = l.size();
		}
		return totalRows;
	}

	public Object onActionFromDelete(Long id) {
		Object retVal = null;
		
//		if(!delete.isAuthorized()) /** hackable? */ {
//	        HttpServletRequest request = requestGlobals.getHTTPServletRequest(); 
//			delete.setUri(request.getRequestURL().toString());
//			delete.setAction("template:delete:genius");
//			delete.setMagicKey(":magickey");
//			retVal = delete;
//		} else
//		if(delete.isAuthorized()) {
			beanManager.delete(id);
			delete.setAuthorized(false);	/** TBD - the caller does not set this but rather the security service */
//		}
		
		return retVal;
	}
	
	public String getDetailsExcerpt() {
		return StringUtils.abbreviate(myBean.getDetails(), 150);
	}
	
//	public String getPageURL() {
//		//source: http://blog.markwshead.com/825/get-the-url-of-page-in-tapestry-5/
//	    Link link = linkSource.createPageRenderLinkWithContext(this.toString(), 5);
//	    return link.toAbsoluteURI();
//	}
	
	public String getHostName() {
		return AppEngine.getHostName();
	}
	
	private void setupRender() {
		/*
        BeanModel model = _grid.getDataModel(); 
        List<String> propertyNames = model.getPropertyNames(); 
        for (String propName : propertyNames) { 
            PropertyModel propModel = model.get(propName); 
            propModel.sortable(false);
        }
		model.get("first_name").sortable(false).label("First"); 
        */
		
		justSaved = (Boolean)requestGlobals.getHTTPServletRequest().getSession().getAttribute("justSavedTemplate");
		//System.out.println("similarity: justSaved " + justSaved);
		if(justSaved != null && !justSaved) {
//	        _grid.getSortModel().updateSort("id");
//	        _grid.getSortModel().updateSort("id");
		} else {
			//=== these allow a descending sort based on column lastUpdatedDate (I know it seems like it is a hack, but it is not!)
	        _grid.getSortModel().updateSort("lastUpdatedDate");
	        _grid.getSortModel().updateSort("lastUpdatedDate");
	        requestGlobals.getHTTPServletRequest().getSession().setAttribute("justSavedTemplate", false);
		}
    }
	
	public String getSimilarity() {
		return myBean.getSimilarity();
	}
	
	@OnEvent(component = "runIndex", value = EventConstants.SELECTED)
	//@Secured("ROLE_ADMIN")	//didn't work
	//http://tapestryjava.blogspot.com/2009/12/securing-tapestry-pages-with.html
//	@RequiresLogin	//should be page?!
	void runIndexClicked() {
		TapestryUtil.setAlertManager(alertManager);
		Compass.runIndex(Geniu.class);
        //alertManager.info("Search engine is indexing the data entered ...");
		alertManager.alert(Duration.TRANSIENT, Severity.INFO, "Indexing the data (it will not be searchable until the process is done) ...");
	}
	
	public String getFriendlyLastUpdatedTime() {
		PrettyTime p = new PrettyTime();
		
		return p.format(myBean.getLastUpdatedDate());
	}
	
	public String getUniqueWhat() {
		return BackupService.getUniqueWhat(myBean);
	}	

	public String getUniqueCategory() {
		return BackupService.getUniqueCategory(myBean);
	}	
	
	/**
	 * http://blog.snugsound.com/2008/08/implementing-logout-function-in.html
	 */
	public Object onActionFromLogout() {
//		requestGlobals.getHTTPServletRequest().getSession().invalidate();
		requestGlobals.getRequest().getSession(false).invalidate();

		return Index.class;
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
		
		if(SecurityUtils.isAdmin(UserRole.ROLE_ADMIN_SCI.toString()) || SecurityUtils.isAdmin(UserRole.ROLE_ADMIN.toString())) {
			retVal = "inline";
		}
		return retVal;
	}
}