
package acme.entities;

import java.util.Date;

import javax.persistence.Entity;

import acme.client.components.basis.AbstractEntity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Airline extends AbstractEntity {

	private String	name;
	private String	iataCode;
	private String	website;

	private Date	foundationMoment;
	private Type	type;
	private String	email;
	private String	phoneNumber;
}
