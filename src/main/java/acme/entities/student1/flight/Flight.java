
package acme.entities.student1.flight;

import java.beans.Transient;

import javax.persistence.Entity;

import org.dom4j.tree.AbstractEntity;

import acme.client.components.datatypes.Moment;
import acme.client.components.datatypes.Money;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoney;
import acme.client.components.validation.ValidString;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Flight extends AbstractEntity {

	private static final long	serialVersionUID	= 1L;

	@Mandatory
	@ValidString(max = 50)
	@Automapped
	private String				tag;

	@Mandatory
	@Automapped
	private boolean				transfer;

	@Mandatory
	@ValidMoney
	private Money				cost;

	@Optional
	@ValidString(max = 255)
	@Automapped
	private String				description;

	//	@Mandatory
	//	@Valid
	//	@OneToOne(optional = false)
	//	private Manager				manager;


	@Transient
	public Moment scheduledDeparture() {
		return null;
	}

	@Transient
	private Moment scheduledArrival() {
		return null;
	}

	@Transient
	public String originCity() {
		return null;
	}

	@Transient
	public String destinationCity() {
		return null;
	}

	@Transient
	public Integer numberOfLayovers() {
		return null;
	}

}
