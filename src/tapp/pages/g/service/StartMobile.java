package tapp.pages.g.service;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.RequestGlobals;
import org.ocpsoft.pretty.time.PrettyTime;

import com.appspot.cloudserviceapi.common.model.HTMLUtil;
import com.appspot.cloudserviceapi.data.AppEngine;
import com.appspot.cloudserviceapi.sci.services.manager.ServiceRegistryManager;

import tapp.model.ServiceRegistry;

public class StartMobile {

//	@Component(id="portraitSrGrid")
//    private Grid _portraitGrid;

	@Inject
	private ServiceRegistryManager beanManager;

	private ServiceRegistry myBean;

//	@Inject
//	private BeanModelSource beanModelSource;

	@InjectPage
	private StartMobile start;
	
	@Inject 
	private Messages messages;
	
//	@Inject 
//	private Grid grid;

	@Inject
	private RequestGlobals requestGlobals;
	
	//=== http://tapestry.apache.org/session-storage.html
	//@SessionState(create=true)	//didn't work
    private boolean justSaved;
    
    private String currentExcerpt;
	
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

//	public long getTotalRows() {
//		return -1;	//beanManager.getServiceRegistrys().size();	//TBD PERF expensive operation!
//	}
	
	public long getTotal() {
		return -1;	//TBD PERF expensive operation!
	}

//	public void onActionFromDelete(Long id) {
//		beanManager.delete(id);
//	}

	public String getHostName() {
		return AppEngine.getHostName();
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
	
	private void setupRender() {
		currentExcerpt = "";
		/*
        BeanModel model = _grid.getDataModel(); 
        List<String> propertyNames = model.getPropertyNames(); 
        for (String propName : propertyNames) { 
            PropertyModel propModel = model.get(propName); 
            propModel.sortable(false);
        }
        */
		justSaved = (boolean)requestGlobals.getHTTPServletRequest().getSession().getAttribute("justSavedServiceRegistry");
		//System.out.println("sr: justSaved " + justSaved);
		if(!justSaved) {
//		if(justSaved != null && !justSaved) {
//	        _grid.getSortModel().updateSort("lastAccessed");
//	        _grid.getSortModel().updateSort("lastAccessed");
		} else {
			//=== these allow a descending sort based on column lastUpdatedDate (I know it seems like it is a hack, but it is not!)
//			_portraitGrid.getSortModel().updateSort("lastAccessed");
//			_portraitGrid.getSortModel().updateSort("lastAccessed");
	        requestGlobals.getHTTPServletRequest().getSession().setAttribute("justSavedServiceRegistry", false);
		}
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
			retVal = HTMLUtil.removeTags(myBean.getDescription());	//cause slow startup on the GAEJ cloud
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
}
