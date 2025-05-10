
package acme.datatypes;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import acme.client.components.basis.AbstractObject;
import acme.entities.group.airport.Airport;
import acme.entities.student1.flight.Flight;
import acme.features.manager.dashboard.ManagerDashboardRepository;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ManagerDashboard extends AbstractObject {

	private static final long	serialVersionUID	= 1L;
	private Integer				ranking;
	private Integer				yearsToRetire;
	private Double				ratio;
	private List<String>		mostPopularAirports;
	private List<String>		lessPopularAirports;
	private Integer				landedLegs;
	private Integer				onTimeLegs;
	private Integer				delayedLegs;
	private Integer				canceledLegs;
	private Double				average;
	private Double				max;
	private Double				min;
	private Double				standardDesviation;


	public ManagerDashboard(final int managerId, final ManagerDashboardRepository repository) {

		this.ranking = repository.managersRanking().indexOf(managerId) + 1;
		this.yearsToRetire = 65 - repository.findManagerById(managerId).getYearsOfExperience();
		this.ratio = (double) repository.onTimeLegs(managerId).size() / (double) repository.delayedLegs(managerId).size();

		List<Flight> flights = repository.flightsByManager(managerId);
		List<Airport> destAirports = flights.stream().map(Flight::destinationCity).toList();
		List<Airport> orgAirports = flights.stream().map(Flight::originCity).toList();
		List<Airport> allAirports = Stream.concat(destAirports.stream(), orgAirports.stream()).filter(Objects::nonNull).toList();

		Map<Airport, Long> airportCounts = allAirports.stream().collect(Collectors.groupingBy(f -> f, Collectors.counting()));

		List<Map.Entry<Airport, Long>> mostPopularAirports = airportCounts.entrySet().stream().sorted((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue())).collect(Collectors.toList());

		List<Map.Entry<Airport, Long>> lessPopularAirports = airportCounts.entrySet().stream().sorted((entry1, entry2) -> entry1.getValue().compareTo(entry2.getValue())).collect(Collectors.toList());

		List<String> resultMostAirports = mostPopularAirports.stream().map(air -> air.getKey().getName()).toList();

		List<String> resultLessAirports = lessPopularAirports.stream().map(air -> air.getKey().getName()).toList();

		this.mostPopularAirports = resultMostAirports;

		this.lessPopularAirports = resultLessAirports;

		this.landedLegs = repository.landedLegs(managerId).size();
		this.onTimeLegs = repository.onTimeLegs(managerId).size();
		this.delayedLegs = repository.delayedLegs(managerId).size();
		this.canceledLegs = repository.canceledLegs(managerId).size();
		this.average = repository.averageFlightsCost(managerId);
		this.max = repository.maxFlightsCost(managerId);
		this.min = repository.minFlightsCost(managerId);
		this.standardDesviation = repository.desvFlightsCost(managerId);
	}

}
