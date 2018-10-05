package tapp.pages.g.owasp;

import java.util.Date;

import javax.jdo.PersistenceManager;

import org.apache.tapestry5.alerts.AlertManager;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.SessionAttribute;
import org.apache.tapestry5.corelib.components.BeanEditForm;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.RequestGlobals;

import com.appspot.cloudserviceapi.common.model.BackupService;
import com.appspot.cloudserviceapi.common.model.SettingsDBUtils;
import com.appspot.cloudserviceapi.common.model.StringUtil;
import com.appspot.cloudserviceapi.data.Datastore;
import com.appspot.cloudserviceapi.data.Persistence;
import com.appspot.cloudserviceapi.dto.Secure;
import com.appspot.cloudserviceapi.sci.services.ServiceRegistryServlet;

import cloudserviceapi.service.manager.SecuredManager;

public class SecuredSave {

	// private Template myBean;
	private Secure myBean;

	private Long id;

	@Inject
	private SecuredManager beanManager;

	@InjectPage
	private SecuredStart start;

	@SessionAttribute
	private String email;

	@Component
	private BeanEditForm form;
	
	@InjectPage
	private SecuredActionUpdate edit;

	@Inject 
	private RequestGlobals requestGlobals;	

	@Inject
    private AlertManager alertManager;
	
	//=== http://tapestry.apache.org/session-storage.html
	//@SessionState	//didn't work
	private boolean justSaved;
	
	/** Backup Service Host */
	public String getBackupServiceHost() {
		return SettingsDBUtils.getSettings("backup.service.ip");
	}

	/** Service ID for Backup Service */
	public String getUniqueSID() {
		return com.appspot.cloudserviceapi.data.AppEngine.getName();
	}

	public String getUniqueWhat() {
		return BackupService.getUniqueWhat(myBean);
	}	

	public String getUniqueCategory() {
		return BackupService.getUniqueCategory(myBean);
	}	

	public String getDetailsInASCII() {
		String retVal = "";
		try {
			retVal = myBean.getDetails()!=null?StringUtil.toASCIICode(myBean.getDetails()):"";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return retVal;
	}

	public Object onActivate(Long id) {
		Object retVal = null;
		
////		if(CommonService.isAuthenticated()) {
//			//TBD - currently the same as authorized
//			System.out.println("authenticated:Template id '" + id + "'");
//			if (id.equals(0L)) {
//				myBean = new Secure();
//			} else {
//				myBean = (Secure) beanManager.getSecure(id);
//			}
//			this.id = id;
//		} else
//		if(edit.isAuthorized()) {
			System.out.println("authorized:Template id '" + id + "'");
			if (id.equals(0L)) {
				myBean = new Secure();
			} else {
				myBean = (Secure) beanManager.getSecure(id);
			}
			this.id = id;
//		} else {
//	        HttpServletRequest request = requestGlobals.getHTTPServletRequest(); 
//			edit.setUri(request.getRequestURL().toString());
//			edit.setAction("template:update:secured");
//			edit.setMagicKey(":magickey");
//			retVal = edit;
//		}
//		

		return retVal;
	}

	public Long onPassivate() {
		return id;
	}

	public Object onSubmit() {
		Object retVal = null;

		try {
			String what = myBean.getWhat();
			Secure g = beanManager.getDao().findByWhat(what);
			if (g != null) {
				id = g.getId();
			}
			Long bid = myBean.getId();
			if (bid == null) {
				bid = Datastore.getId(myBean, "template");
				myBean.setId(bid);
			}

			if (myBean != null && id > 0 && !id.equals(bid)) {
				// as workaround as GAE4J does not support composite primary key
				// i.e.
				// beanManager.delete(id);
				form.recordError("Template '" + myBean.getWhat()
						+ "' already exists!");
			} else {
				myBean.setLastUpdatedDate(new Date());
				//beanManager.save(myBean);	//need jdo to make compass works! :(
				PersistenceManager pm = Persistence.getManager();
				try {
					pm.makePersistent(myBean);
				} catch(Exception e) {
					throw e;
				} finally {
					pm.close();
				}
				edit.setAuthorized(false);	/** TBD - the caller does not set this but rather the security service */

//				TapestryUtil.setAlertManager(alertManager);
//				Compass.runIndex();
//		        //alertManager.info("Search engine is indexing the data entered ...");
//				alertManager.alert(Duration.TRANSIENT, Severity.INFO, "Indexing the data (it will not be searchable until the process is done) ...");

				retVal = start;
				justSaved = true;
				requestGlobals.getHTTPServletRequest().getSession().setAttribute("justSavedTemplate", justSaved);
			}
		} catch (Exception e) {
			form.recordError("Error: " + e);
			e.printStackTrace();
		}
		
		return retVal;
	}

	public Secure getTemplate() {
		return myBean;
	}

	public void setTemplate(Secure myBean) {
		this.myBean = myBean;
	}

	/** Backup Service On/Off Switched */
	public String getBackupServiceDisplay() {
		String retVal = "none";
		if(ServiceRegistryServlet.isBackupServiceEnabled()) {
			retVal = "inline";
		}
		return retVal;
	}
}