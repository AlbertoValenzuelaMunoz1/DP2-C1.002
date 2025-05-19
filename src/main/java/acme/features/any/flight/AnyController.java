
package acme.features.any.flight;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.principals.Any;
import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.entities.student1.flight.Flight;

@GuiController
public class AnyController extends AbstractGuiController<Any, Flight> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AnyListService	listService;

	@Autowired
	private AnyShowService	showService;

	// Constructors -----------------------------------------------------------


	@PostConstruct
	protected void initialise() {

		super.addBasicCommand("list", this.listService);
		super.addBasicCommand("show", this.showService);

	}
}
