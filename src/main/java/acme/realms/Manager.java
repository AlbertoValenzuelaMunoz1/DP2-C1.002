
package acme.realms;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;

import acme.Validators.ValidIdentifier;
import acme.client.components.basis.AbstractRole;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidNumber;
import acme.client.components.validation.ValidUrl;
import acme.entities.group.airline.Airline;
import lombok.Getter;
import lombok.Setter;

/*
 * No es necesario crear el indice para la consulta findManagerByIdentifier
 * ya que la propiedad identifier tiene la anotacion de @Column(unique = true),
 * por tanto se genera de forma automatica el indice
 */
@Entity
@Getter
@Setter
public class Manager extends AbstractRole {

	private static final long	serialVersionUID	= 1L;

	@Mandatory
	@ValidIdentifier
	@Column(unique = true)
	private String				identifier;

	@Mandatory
	@ValidNumber(min = 0, max = 150, integer = 3)
	@Automapped
	private int					yearsOfExperience;

	@Mandatory
	@ValidMoment(past = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Date				birthDate;

	@Optional
	@ValidUrl
	private String				link;

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Airline				airline;

}
