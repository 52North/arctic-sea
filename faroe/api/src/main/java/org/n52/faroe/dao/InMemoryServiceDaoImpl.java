package org.n52.faroe.dao;

import org.n52.faroe.service.Service;

import java.util.ArrayList;
import java.util.List;

public class InMemoryServiceDaoImpl implements ServicesDao {

	List<Service> services = new ArrayList<>();

	@Override
	public List<Service> getServices() {
		return services;
	}

	@Override
	public void createService(Service s) {
		services.add(s);
	}

	public Service getServiceByName(final String name) {
		return services.stream().filter(service -> service.getName().equals(name)).findFirst()
				.orElseGet(null);
	}
}
