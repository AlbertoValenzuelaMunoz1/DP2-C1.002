
package acme.entities;

import java.util.Date;
import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import acme.client.components.basis.AbstractRole;
import acme.client.components.datatypes.Money;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidMoney;
import acme.client.components.validation.ValidString;
import acme.client.components.validation.ValidUrl;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class AssistanceAgents extends AbstractRole {

	//Serialisation -------------------------------

	private static final long	serialVersionUID	= 1L;

	//Attributes ---------------------------------

	@Mandatory
	@NotBlank
	@Column(unique = true, length = 9)
	@ValidString(pattern = "^[A-Z]{2,3}\\d{6}$", message = "Employee code must start with 2-3 uppercase letters followed by 6 digits")
	private String				employeeCode;

	//@NotBlank
	//@Size(max = 255)
	//private String	spokenLanguages;

	@Mandatory
	@NotEmpty
	@ElementCollection
	@CollectionTable(name = "assistance_agent_languages", joinColumns = @JoinColumn(name = "agent_id"))
	@Column(name = "language", length = 255)
	@Automapped
	private List<String>		spokenLanguages;

	@Mandatory
	@NotBlank
	@ValidString
	@Automapped
	private String				airline;

	// @Mandatory
	//	@NotNull
	//	@ManyToOne
	//	@JoinColumn(name = "airline_id", nullable = false)
	//  @Valid
	//	private Airline airline;
	//Esta es la correcta implementacion pero en la rama no esta Airline

	@Mandatory
	@Temporal(TemporalType.DATE)
	@NotNull
	@ValidMoment(past = true)
	private Date				employmentStartDate;

	@Optional
	@Size(max = 255)
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
