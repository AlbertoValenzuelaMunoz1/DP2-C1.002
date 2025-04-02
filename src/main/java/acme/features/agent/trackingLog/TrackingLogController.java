
package acme.features.agent.trackingLog;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.entities.student4.tranckingLog.TrackingLog;
import acme.realms.AssistanceAgent;

@GuiController
public class TrackingLogController extends AbstractGuiController<AssistanceAgent, TrackingLog> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private TrackingLogListService		listService;

	@Autowired
	private TrackingLogShowService		showService;

	@Autowired
	private TrackingLogCreateService	createService;

	@Autowired
	private TrackingLogUpdateService	updateService;

	@Autowired
	private TrackingLogDeleteService	deleteService;

	@Autowired
	private TrackingLogPublishService	publishService;
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
