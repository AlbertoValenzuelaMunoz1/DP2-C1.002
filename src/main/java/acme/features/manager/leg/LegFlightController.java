
package acme.features.manager.leg;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.entities.student1.leg.Leg;
import acme.entities.student1.manager.Manager;

@GuiController
public class LegFlightController extends AbstractGuiController<Manager, Leg> {

	@Autowired
	private LegFlightListService	listService;

	@Autowired
	private LegFlightShowService	showService;

	@Autowired
	private LegFlightCreateService	createService;

	@Autowired
	private LegFlightUpdateService	updateService;

	@Autowired
	private LegFlightDeleteService	deleteService;

	@Autowired
	private LegFlightPublishService	publishService;

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
