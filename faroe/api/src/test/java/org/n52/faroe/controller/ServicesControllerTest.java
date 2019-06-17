package org.n52.faroe.controller;

import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.n52.faroe.dao.ServicesDao;
import org.n52.faroe.service.Service;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ContextConfiguration(locations = "classpath:testApplicationContext.xml")
public class ServicesControllerTest {

  private MockMvc mvc;
  @Mock
  private ServicesDao servicesDao;
  @Mock
  private Service mockService;
  @InjectMocks
  private ServicesController servicesController;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    mvc = MockMvcBuilders.standaloneSetup(servicesController).build();
  }

  @Test
  public void testGetServices() {
    List<Service> serviceList = new ArrayList<>();
    Service service1 = Mockito.mock(Service.class);
    Mockito.when(service1.getName()).thenReturn("service1");
    Service service2 = Mockito.mock(Service.class);
    Mockito.when(service2.getName()).thenReturn("service2");
    serviceList.add(service1);
    serviceList.add(service2);
    Mockito.when(servicesDao.getServices()).thenReturn(serviceList);

  }
}
