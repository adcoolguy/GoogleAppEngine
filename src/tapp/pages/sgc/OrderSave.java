package tapp.pages.sgc;

import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.ioc.annotations.Inject;

import com.appspot.cloudserviceapi.sgc.services.manager.OrderManager;

import tapp.model.sgc.WorkOrder;

public class OrderSave {

	private WorkOrder myBean;

	private Long id;

	@Inject
	private OrderManager beanManager;

	@InjectPage
	private OrderStart start;

	public void onActivate(Long id) {
		if (id.equals(0L)) {
			myBean = new WorkOrder();
		} else {
			myBean = beanManager.getWorkOrder(id);
		}
		this.id = id;
	}

	public Long onPassivate() {
		return id;
	}

	public Object onSubmit() {
		beanManager.save(myBean);
		return start;
	}

	public WorkOrder getWorkOrder() {
		return myBean;
	}

	public void setWorkOrder(WorkOrder myBean) {
		this.myBean = myBean;
	}

}