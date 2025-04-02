
package acme.features.agent.claim;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.entities.student4.claim.Claim;
import acme.realms.AssistanceAgent;

@GuiController
public class ClaimController extends AbstractGuiController<AssistanceAgent, Claim> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private ClaimListService	listService;

	@Autowired
	private ClaimShowService	showService;

	@Autowired
	private ClaimCreateService	createService;

	@Autowired
	private ClaimUpdateService	updateService;

	@Autowired
	private ClaimPublishService	publishService;

	@Autowired
	private ClaimDeleteService	deleteService;

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
