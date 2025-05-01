
package acme.features.customer.dashboard;

import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.datatypes.CustomerDashboard;
import acme.datatypes.TravelClass;
import acme.entities.student2.customer.Customer;

@GuiService
public class CustomerDashboardShowService extends AbstractGuiService<Customer, CustomerDashboard> {

	@Autowired
	private CustomerDashboardRepository repository;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}
	@Override
	public void load() {
		int customerId = super.getRequest().getPrincipal().getActiveRealm().getId();
		CustomerDashboard dashboard = new CustomerDashboard(customerId, this.repository);
		super.getBuffer().addData(dashboard);
	}

	@Override
	public void unbind(final CustomerDashboard dashboard) {
		Dataset dataset = super.unbindObject(dashboard, "moneySpent", "costStatistics.average", "costStatistics.min", "costStatistics.max", "costStatistics.standardDesviation", "costStatistics.count", "passengerStatistics.average",
			"passengerStatistics.min", "passengerStatistics.max", "passengerStatistics.standardDesviation", "passengerStatistics.count");
		dataset.put("economy", dashboard.getNumberBookings().getOrDefault(TravelClass.ECONOMY, 0L));
		dataset.put("business", dashboard.getNumberBookings().getOrDefault(TravelClass.BUSINESS, 0L));
		IntStream.range(0, Integer.min(5, dashboard.getLastDestinations().size())).forEach(i -> dataset.put(Integer.toString(i), dashboard.getLastDestinations().get(i)));
		super.getResponse().addData(dataset);

	}
}
