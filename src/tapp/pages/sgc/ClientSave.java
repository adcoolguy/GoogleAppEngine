package tapp.pages.sgc;

import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.ioc.annotations.Inject;

import com.appspot.cloudserviceapi.sgc.services.manager.ClientManager;

import tapp.model.sgc.Client;

public class ClientSave {

	private Client myBean;

	private Long id;

	@Inject
	private ClientManager beanManager;

	@InjectPage
	private ClientStart start;

	public void onActivate(Long id) {
		if (id.equals(0L)) {
			myBean = new Client();
		} else {
			myBean = beanManager.getClient(id);
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

	public Client getClient() {
		return myBean;
	}

	public void setClient(Client myBean) {
		this.myBean = myBean;
	}

}