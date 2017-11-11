package tapp.pages.g.app;

import javax.servlet.http.HttpServletResponse;

import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.RequestGlobals;

import com.appspot.cloudserviceapi.data.ServiceRegistryUtil;
import com.appspot.cloudserviceapi.sci.dao.ServiceRegistryDAO;

import tapp.model.ServiceRegistry;

//@Meta("tapestry.persistence-strategy=flash")
public class TemplateAction {

	@InjectPage
	private TemplateStart start;

	@Component(id = "form1")
	private Form form1;

	@Persist
    private String token;
    
    @Persist
    private String action;
    
    @Persist
    private String uri;

    @Persist
    private boolean authorized;
    
    private boolean cancel;
    
    private String magicKey;

	@Inject 
	private RequestGlobals requestGlobals; 
    
	void onSelectedFromUpdate() {
		cancel = false;
	}

	void onSelectedFromCancel() {
		cancel = true;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public boolean isAuthorized() {
		return authorized;
	}

	public void setAuthorized(boolean authorized) {
		this.authorized = authorized;
	}

	public String getMagicKey() {
		return magicKey;
	}

	public void setMagicKey(String magicKey) {
		this.magicKey = magicKey;
	}

	Object onSuccess() {
		Object retVal = null;
		if (cancel) {
			retVal = start;
		} else {
			if(token == null) {
				form1.recordError("Token can not be empty!");
			} else {
				System.out.println("token '" + token + "'");
				System.out.println("action '" + action + "'");
				//=== get the first three parts of the permission only (Apache Shiro style)
//				if(action != null) {
//					int separatorCount = action.replaceAll("[^:]", "").length();
//					if(separatorCount > 2) {
//						int found = action.lastIndexOf(":");
//						action = action.substring(0, found);
//					}
//				}
				ServiceRegistryDAO r = new ServiceRegistryDAO();
				if(action != null) {
					String tokenAssigned;
					try {
						tokenAssigned = ServiceRegistryUtil.handleEndPoint(action, r);
					} catch (Exception e1) {
						e1.printStackTrace();
					} //SE
					ServiceRegistry u = (ServiceRegistry)r.findServiceRegistryByService(action);
					
					if(u != null) {
						tokenAssigned = u.getEndpoint();
						ServiceRegistryUtil.countHit(u, r, requestGlobals.getHTTPServletRequest(), false);
						if(token != null && tokenAssigned != null && (token.trim().equals(tokenAssigned.trim()))) {
							System.out.println("access granted for '" + action + "' on " + uri);
							//HttpServletRequest request = requestGlobals.getHTTPServletRequest();
							HttpServletResponse response = requestGlobals.getHTTPServletResponse();
							try {
								ServiceRegistry mk = (ServiceRegistry)r.findServiceRegistryByService(magicKey); //has to be number
								if(mk == null) {
									response.sendRedirect(uri);
									//RequestDispatcher rd = request.getRequestDispatcher(uri);
									//rd.forward(request, response);						
									reset();
									authorized = true; //so that the caller knows it is done
								} else {
									Long mkCount = null;
									try {
										mkCount = Long.valueOf(mk.getEndpoint());
									} catch (Exception e) {
										System.out.println("Magic key is not a number! Setting it to -1."); //the user will not able to execute the action
										mkCount = -1L;
									}
									String tokenPlusMagicKeyAssigned = tokenAssigned + mk.getEndpoint();
									if(mkCount > 0 && tokenPlusMagicKeyAssigned.equals(token + mk.getEndpoint())) {
										response.sendRedirect(uri);
										//RequestDispatcher rd = request.getRequestDispatcher(uri);
										//rd.forward(request, response);						
										reset();
										authorized = true; //so that the caller knows it is done
										mkCount--;
									} else {
										//TBD - no such action defined, stay put (might be better to warn the user)
										retVal = null;
									}
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					} else {
						//TBD - no such action defined, stay put (might be better to warn the user)
						retVal = null;
					}
				} else {
					//TBD - this might not work generically
					retVal = start;
				}
			}
		}
		return retVal;
	}

	private void reset() {
		token = null;
		action = null;
		uri = null;
		magicKey = null;
	}
	
	public Object onSubmit() {
		return null;
	}

}