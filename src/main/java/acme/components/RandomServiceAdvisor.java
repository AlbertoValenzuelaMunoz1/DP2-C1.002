
package acme.components;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import acme.entities.group.service.Service;
import acme.features.any.service.RandomServiceService;

@ControllerAdvice
public class RandomServiceAdvisor {

	@Autowired
	protected RandomServiceService service;


	@ModelAttribute("randomService")
	public Service injectRandomService() {
		try {
			return this.service.findRandomService();
		} catch (Throwable oops) {
			return null;
		}
	}

}
