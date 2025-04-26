
package acme.datatypes;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Statistics<E> {

	private Long	count;
	private Double	average;
	private Double	standardDesviation;
	private E		min;
	private E		max;
}
