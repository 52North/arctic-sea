package org.n52.faroe.dao;

import java.util.List;
import org.n52.faroe.service.Service;

/**
 *
 */
public interface ServicesDao {

  List getServices();

  void createService(Service s);

  Service getServiceByName(String name);
}