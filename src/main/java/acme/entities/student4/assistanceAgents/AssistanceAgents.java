
package acme.entities.student4.assistanceAgents;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import Validators.ValidIdentifier;
import acme.client.components.basis.AbstractEntity;
import acme.client.components.datatypes.Money;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidMoney;
import acme.client.components.validation.ValidString;
import acme.client.components.validation.ValidUrl;
import acme.entities.group.airline.Airline;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class AssistanceAgents extends AbstractEntity {

	//Serialisation -------------------------------

	private static final long	serialVersionUID	= 1L;

	//Attributes ---------------------------------

	@Mandatory
	@Column(unique = true)
	@ValidIdentifier
	private String				employeeCode;

	@Mandatory
	@NotBlank
	@ValidString(min = 1)
	private String				spokenLanguages;

	//	@Mandatory
	//	@NotEmpty
	//	@ElementCollection
	//	@CollectionTable(name = "assistance_agent_languages", joinColumns = @JoinColumn(name = "agent_id"))
	//	@Column(name = "language", length = 255)
	//	@Automapped
	//	private List<String>		spokenLanguages;

	@Mandatory
	@ManyToOne(optional = false)
	@Valid
	private Airline				airline;

	@Mandatory
	@Temporal(TemporalType.TIMESTAMP)
	@ValidMoment(past = true)
	private Date				employmentStartDate;

	@Optional
	@ValidString
	@Automapped
	private String				bio;

	@Optional
	@ValidMoney
	@Automapped
	private Money				salary;

	@Optional
	@ValidUrl
	@Automapped
	private String				photoUrl;
}
