package tapp.pages.g.social;

import java.util.Date;

import javax.jdo.PersistenceManager;
import javax.servlet.http.HttpServletRequest;

import org.apache.tapestry5.alerts.AlertManager;
import org.apache.tapestry5.alerts.Duration;
import org.apache.tapestry5.alerts.Severity;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.SessionAttribute;
import org.apache.tapestry5.corelib.components.BeanEditForm;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.RequestGlobals;

import com.appspot.cloudserviceapi.common.TapestryUtil;
import com.appspot.cloudserviceapi.data.Datastore;
import com.appspot.cloudserviceapi.data.Persistence;
import com.appspot.cloudserviceapi.dto.Huma;
import com.appspot.cloudserviceapi.service.manager.SocialManager;
import com.appspot.cloudserviceapi.util.Compass;

public class SocialSave {

	private Huma myBean;

	private Long id;

	@Inject
	private SocialManager beanManager;

	@InjectPage
	private SocialStart start;

	@SessionAttribute
	private String email;

	@Component
	private BeanEditForm form;
	
	@InjectPage
	private SocialActionUpdate edit;

	@Inject 
	private RequestGlobals requestGlobals;	

	@Inject
    private AlertManager alertManager; 

	private Boolean justSaved;
	
	public Object onActivate(Long id) {
		Object retVal = null;
		
		if(!edit.isAuthorized()) /** hackable? */ {
	        HttpServletRequest request = requestGlobals.getHTTPServletRequest(); 
			edit.setUri(request.getRequestURL().toString());
			edit.setAction("template:update:social");
			edit.setMagicKey(":magickey");
			retVal = edit;
		} else
		if(edit.isAuthorized()) {
			System.out.println("Social id '" + id + "'");
			if (id.equals(0L)) {
				myBean = new Huma();
			} else {
				myBean = (Huma) beanManager.getHuma(id);
			}
			this.id = id;
		}
		
		return retVal;
	}

	public Long onPassivate() {
		return id;
	}

	public Object onSubmit() {
		Object retVal = null;

		try {
			String what = myBean.getWhat();
			Huma g = beanManager.getDao().findByWhat(what);
			if (g != null) {
				id = g.getId();
			}
			Long bid = myBean.getId();
			if (bid == null) {
				bid = Datastore.getId(myBean, "Social");
				myBean.setId(bid);
			}

			if (myBean != null && id > 0 && !id.equals(bid)) {
				// as workaround as GAE4J does not support composite primary key
				// i.e.
				// beanManager.delete(id);
				form.recordError("Social '" + myBean.getWhat()
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

	public Huma getSocial() {
		return myBean;
	}

	public void setSocial(Huma myBean) {
		this.myBean = myBean;
	}

}