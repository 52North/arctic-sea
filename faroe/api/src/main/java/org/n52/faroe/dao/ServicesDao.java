package org.n52.faroe.dao;

import org.n52.faroe.service.Service;

import java.util.List;

/**
 *
 */
public interface ServicesDao {

    List getServices();

    void createService(Service s);

    Service getServiceByName(String name);
}