
package acme.features.technician.maintenanceRecord;

import javax.annotation.PostConstruct;

import acme.client.controllers.AbstractGuiController;
import acme.entities.student5.maintenanceRecord.MaintenanceRecord;
import acme.entities.student5.technician.Technician;

public class TechnicianMaintenanceRecordController extends AbstractGuiController<Technician, MaintenanceRecord> {

	private TechnicianMaintenanceRecordListService service;


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("list", this.service);
	}

}
