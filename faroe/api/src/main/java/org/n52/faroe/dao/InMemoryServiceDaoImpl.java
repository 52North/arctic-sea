package org.n52.faroe.dao;

import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.List;
import org.n52.faroe.service.Service;

public class InMemoryServiceDaoImpl implements ServicesDao {

  List<Service> services = new ArrayList<>();
  Gson gson = new Gson();

  @Override
  public List<Service> getServices() {
    return services;
  }

  @Override
  public void createService(Service s) {
    services.add(s);
  }

  public Service getServiceByName(final String name) {
    return services.stream().filter(service -> service.getName()==name).findFirst()
            .orElseGet(null);
  }
}
