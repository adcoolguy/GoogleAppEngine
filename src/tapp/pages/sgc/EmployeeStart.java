package tapp.pages.sgc;

import java.util.List;

import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.BeanModelSource;

import com.appspot.cloudserviceapi.sgc.services.manager.EmployeeManager;

import tapp.model.sgc.Employee;

public class EmployeeStart {

	@Inject
	private EmployeeManager beanManager;

	private Employee myBean;

    @Inject
    private BeanModelSource beanModelSource;
    
	public Employee getEmployee() {
		return myBean;
	}

	public void setEmployee(Employee Employee) {
		this.myBean = Employee;
	}

	public List<Employee> getEmployees() {
		return beanManager.getEmployees();
	}

	public boolean isEmptyList() {
		return beanManager.getEmployees().isEmpty();
	}

	public void onActionFromDelete(Long id) {
		beanManager.delete(id);
	}
}
