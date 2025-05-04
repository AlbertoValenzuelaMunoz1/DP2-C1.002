
package acme.datatypes;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;

import acme.client.components.basis.AbstractObject;
import acme.client.helpers.MomentHelper;
import acme.features.customer.dashboard.CustomerDashboardRepository;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerDashboard extends AbstractObject {

	/**
	 * 
	 */
	private static final long		serialVersionUID	= 1L;
	private List<String>			lastDestinations;
	private Double					moneySpent;
	private Map<TravelClass, Long>	numberBookings;
	private Statistics<Double>		costStatistics;
	private Statistics<Integer>		passengerStatistics;


	@SuppressWarnings("deprecation")
	public CustomerDashboard(final int customerId, final CustomerDashboardRepository repository) {
		this.lastDestinations = repository.findLastFlightsByCustomerId(customerId, PageRequest.of(0, 5)).stream().map(f -> f.destinationCity().getCity()).toList();
		Date maximumDateSumCostStatistics = (Date) MomentHelper.getCurrentMoment().clone();
		maximumDateSumCostStatistics.setYear(maximumDateSumCostStatistics.getYear() - 1);
		this.moneySpent = repository.findSumPriceByCustomerId(customerId, maximumDateSumCostStatistics);
		this.costStatistics = new Statistics<>();
		Date maximumDateCostStatistics = (Date) MomentHelper.getCurrentMoment().clone();
		maximumDateCostStatistics.setYear(maximumDateCostStatistics.getYear() - 5);
		this.costStatistics.setAverage(repository.findAveragePriceByCustomerId(customerId, maximumDateCostStatistics));
		this.costStatistics.setMax(repository.findMaxPriceByCustomerId(customerId, maximumDateCostStatistics));
		this.costStatistics.setMin(repository.findMinPriceByCustomerId(customerId, maximumDateCostStatistics));
		Collection<Double> prices = repository.findBookingCostsByCustomerId(customerId, maximumDateCostStatistics);
		this.costStatistics.setCount((long) prices.size());
		this.costStatistics.setStandardDesviation(this.calculateStandardDesviation(prices, this.costStatistics.getAverage()));
		this.passengerStatistics = new Statistics<>();
		this.passengerStatistics.setAverage(repository.findAvgBookingPassenger(customerId));
		this.passengerStatistics.setMin(repository.findMinBookingPassenger(customerId));
		this.passengerStatistics.setMax(repository.findMaxBookingPassenger(customerId));
		Collection<Integer> passengers = repository.findCountBookingPassenger(customerId);
		this.passengerStatistics.setCount((long) passengers.size());
		this.passengerStatistics.setStandardDesviation(this.calculateStandardDesviation(passengers, this.passengerStatistics.getAverage()));
		this.numberBookings = repository.findBookingsPerTravelClass(customerId).stream().collect(Collectors.toMap(x -> (TravelClass) x[1], x -> (Long) x[0]));

	}
	private Double calculateStandardDesviation(final Collection<? extends Number> collection, final Double average) {

		if (collection.isEmpty())
			return null;
		return Math.sqrt(collection.stream().mapToDouble(p -> Math.pow(average - p.doubleValue(), 2)).sum() / collection.size());
	}
}
