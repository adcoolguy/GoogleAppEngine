package tapp.pages.sgc;

import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.ioc.annotations.Inject;

import com.appspot.cloudserviceapi.sgc.dao.EmployeeDAO;
import com.appspot.cloudserviceapi.sgc.services.manager.EmployeeManager;

import tapp.model.sgc.Employee;

public class EmployeeSave {

	private Employee myBean;

	private Long id;

	@Inject
	private EmployeeManager beanManager;

	@InjectPage
	private EmployeeStart start;

	public void onActivate(Long id) {
		if (id.equals(0L)) {
			myBean = new Employee();
		} else {
			myBean = beanManager.getEmployee(id);
		}
		this.id = id;
	}

	public Long onPassivate() {
		return id;
	}

	public Object onSubmit() {
		Long id = -1L;
		
		id = (new EmployeeDAO()).exist(myBean);
		Long bid = myBean.getId();
		
		if(myBean != null && id > 0 && !id.equals(myBean.getId())) {
			//as workaround as GAE4J does not support composite primary key i.e.
			//beanManager.delete(id);
			throw new RuntimeException("Employee with user id '" + myBean.getUserId() + "' already exists!");
		}

		beanManager.save(myBean);
		return start;
	}

	public Employee getEmployee() {
		return myBean;
	}

	public void setEmployee(Employee myBean) {
		this.myBean = myBean;
	}

}