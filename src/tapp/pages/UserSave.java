package tapp.pages;

import java.util.ArrayList;
import java.util.List;

import org.apache.tapestry5.OptionModel;
import org.apache.tapestry5.SelectModel;
import org.apache.tapestry5.ValidationException;
import org.apache.tapestry5.ValueEncoder;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.internal.OptionModelImpl;
import org.apache.tapestry5.internal.SelectModelImpl;
//import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.internal.services.MapMessages;
import org.apache.tapestry5.ioc.annotations.Inject;
//import org.apache.tapestry5.ioc.services.TypeCoercer;
import org.apache.tapestry5.util.EnumSelectModel;
import org.apache.tapestry5.util.EnumValueEncoder;

import com.appspot.cloudserviceapi.security.spring.GaeUserDetails;
import com.appspot.cloudserviceapi.security.spring.UserRole;
import com.appspot.cloudserviceapi.security.spring.UserSecurityDAO;
import com.appspot.cloudserviceapi.services.manager.UserManager;

public class UserSave {

	@Inject
	private MapMessages messages; //source: Tapestry 5 Building Web Applications page 121

	private GaeUserDetails myBean;

	private Long id;
 
	@Inject
	private UserManager beanManager;

	@InjectPage
	private UserStart start;

	@Property
	private List<UserRole> selectedList; 
	//List to supply data from 
	@Property
	private List<UserRole> myList; 
	
//	@Inject
//	private TypeCoercer typeCoercer;
	
	public void onActivate(Long id) {
		if (id.equals(0L)) {
			myBean = new GaeUserDetails();
		} else {
			myBean = beanManager.getUser(id);
		}
		this.id = id;
		
		//user roles stuff - to support multiple selections of roles
		//Initialize the selected list 
		if(selectedList == null){ 
			if(myBean.getUserRoles() == null) {
				selectedList = new ArrayList<UserRole>();
			} else {
				selectedList = myBean.getUserRoles();
			}
		}

		//Fill dummy data. In your case you have to load data 
		if(myList == null){ 
			//Dummy Data 
			myList = new ArrayList<UserRole>(); 
			UserRole[] roles = UserRole.values();
	
			for(int i = 0; i < roles.length; ++i){ 
				myList.add(roles[i]);
			} 
		}		
	}

	public Long onPassivate() {
		return id;
	}

	public Object onSubmit() throws ValidationException {
		Long id = -1L;
		
		id = (new UserSecurityDAO()).exist(myBean);
		Long bid = myBean.getId();
		
		if(myBean != null && id > 0 && !id.equals(myBean.getId())) {
			//as workaround as GAE4J does not support composite primary key i.e.
			//beanManager.delete(id);
			throw new ValidationException("User with user id '" + myBean.getUserId() + "' already exists!");
		} else {
			if(selectedList != null && selectedList.size() > 0) {
				//change only if there is a selection, if not do not modify existing role(s)
				myBean.setUserRoles(selectedList);
			}
		}

		beanManager.save(myBean);
		
		return start;
	}

	public GaeUserDetails getUser() {
		return myBean;
	}

	public void setUser(GaeUserDetails myBean) {
		this.myBean = myBean;
	}

	//Encoder 
//	public MultipleValueEncoder<UserRole> getEncoder(){ 
//	   return new MultipleValueEncoder<UserRole>(){ 
//
//		   public String toClient(UserRole object) { 
//			   return object.toString();
//			} 
//
//			public List<UserRole> toValue(String[] values) { 
//			   List<UserRole> objects = new ArrayList<UserRole>(); 
//			   for(String value: values){ 
//			      for(UserRole myObject: myList) 
//				  if(myObject.toString().equals(value)){
//				     objects.add(myObject); 
//				  } 
//			      } 
//
//			   return objects; 
//			} 
//		   
//	   }; 
//	} 

	//Model 
//	public SelectModel getModel(){ 
//	   return new AbstractSelectModel(){
//		   
//		   public List<OptionGroupModel> getOptionGroups() { 
//			   return null; 
//			} 
//
//			public List<OptionModel> getOptions() { 
//			   final List<OptionModel> options = new ArrayList<OptionModel>(); 
//			   for(final UserRole myObject: myList){ 
//			      options.add(new OptionModelImpl(myObject.toString(), myObject)); 
//			   } 
//			   return options; 
//			} 
//	   }; 
//	} 

	//source: https://builds.apache.org/job/tapestry-trunk-freestyle/javadoc/org/apache/tapestry5/corelib/components/Checklist.html
//	public ValueEncoder<UserRole> getEncoder() {
//        return new EnumValueEncoder<UserRole>(typeCoercer, UserRole.class);
//    }

    public SelectModel getModel() {
//        return new EnumSelectModel(UserRole.class, messages);	//TODO 5.4
    	//=== http://apache-tapestry-mailing-list-archives.1045711.n5.nabble.com/Renaming-options-in-select-menu-using-properties-td5723516.html#a5723527    	List<OptionModel> tardies = new ArrayList<OptionModel>(); 
        List<OptionModel> tardies = new ArrayList<OptionModel>(); 
        tardies.add(new OptionModelImpl("Y", true)); 
        tardies.add(new OptionModelImpl("N", false)); 
        return new SelectModelImpl(null, tardies);
    }
    
}