
package acme.features.manager.flight;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.entities.student1.flight.Flight;
import acme.realms.Manager;

@GuiController
public class ManagerController extends AbstractGuiController<Manager, Flight> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private ManagerListService		listService;

	@Autowired
	private ManagerShowService		showService;

	@Autowired
	private ManagerCreateService	createService;

	@Autowired
	private ManagerUpdateService	updateService;

	@Autowired
	private ManagerDeleteService	deleteService;

	@Autowired
	private ManagerPublishService	publishService;

	// Constructors -----------------------------------------------------------


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("list", this.listService);
		super.addBasicCommand("show", this.showService);
		super.addBasicCommand("create", this.createService);
		super.addBasicCommand("update", this.updateService);
		super.addBasicCommand("delete", this.deleteService);

		super.addCustomCommand("publish", "update", this.publishService);
	}

}
