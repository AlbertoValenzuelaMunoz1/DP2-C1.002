
package acme.datatypes;

import java.util.Map;
import java.util.stream.Collectors;

import acme.client.helpers.MomentHelper;
import acme.client.helpers.SpringHelper;
import acme.entities.student2.customer.Customer;
import acme.entities.student2.customer.CustomerRepository;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerDashboard {

	private Double					moneySpent;
	private Map<TravelClass, Long>	bookings;
	private Long					countBookingsLastFiveYears;
	private Double					averageBookingsCost;
	private Double					minBookingsCost;
	private Double					maxBookingsCost;
	private Double					standardDesviationBookingsCost;
	private Long					countBookings;

	private Double					averagePassengers;
	private Integer					minPassengers;
	private Integer					maxPassengers;

	private Double					standardDesviationPassengers;


	public CustomerDashboard(final Customer customer) {
		CustomerRepository repository = SpringHelper.getBean(CustomerRepository.class);
		this.moneySpent = repository.findMoneySpent(customer);
		this.bookings = repository.findBookingsPerTravelClass(customer).stream().collect(Collectors.toMap(x -> (TravelClass) x[1], x -> (Long) x[0]));
		this.updateCostStatistics(repository, customer);
		this.countBookings = repository.findCountBookingPassenger(customer);
		this.averagePassengers = repository.findAverageBookingPassenger(customer);
		this.minPassengers = repository.findMinBookingPassenger(customer);
		this.maxPassengers = repository.findMaxBookingPassenger(customer);
	}
	private void updateCostStatistics(final CustomerRepository repository, final Customer customer) {
		@SuppressWarnings("deprecation")
		Object[] statistics = repository.findCostStatistics(customer, MomentHelper.getCurrentMoment().getYear() - 5);
		this.countBookingsLastFiveYears = (Long) statistics[0];
		this.averageBookingsCost = (Double) statistics[1];
		this.minBookingsCost = (Double) statistics[2];
		this.maxBookingsCost = (Double) statistics[3];
		this.standardDesviationBookingsCost = (Double) statistics[4];
	}

}
