
package acme.features.manager.dashboard;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.datatypes.ManagerDashboard;
import acme.realms.Manager;

@GuiService
public class ManagerDashboardShowService extends AbstractGuiService<Manager, ManagerDashboard> {

	@Autowired
	private ManagerDashboardRepository repository;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}
	@Override
	public void load() {
		int managerId = super.getRequest().getPrincipal().getActiveRealm().getId();
		ManagerDashboard dashboard = new ManagerDashboard(managerId, this.repository);
		super.getBuffer().addData(dashboard);
	}

	@Override
	public void unbind(final ManagerDashboard dashboard) {
		Dataset dataset = super.unbindObject(dashboard, "ranking", "yearsToRetire", "ratio", "mostPopularAirports", "lessPopularAirports", "landedLegs", "onTimeLegs", "delayedLegs", "canceledLegs", "average", "max", "min", "standardDesviation");
		super.getResponse().addData(dataset);

	}
}
