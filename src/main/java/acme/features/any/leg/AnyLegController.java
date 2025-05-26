
package acme.features.any.leg;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.principals.Any;
import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.entities.student1.leg.Leg;

@GuiController
public class AnyLegController extends AbstractGuiController<Any, Leg> {

	@Autowired
	private AnyLegListService listService;

	// Constructors -----------------------------------------------------------


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("list", this.listService);

	}

}
