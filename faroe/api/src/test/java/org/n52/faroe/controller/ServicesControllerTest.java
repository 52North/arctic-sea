package org.n52.faroe.controller;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.n52.faroe.dao.ServicesDao;
import org.n52.faroe.service.DummyService;
import org.n52.faroe.service.Service;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
    public void testGetServices() throws Exception {
        List<Service> serviceList = new ArrayList<>();
        Service service1 = new DummyService();
        Service service2 = new DummyService();
        serviceList.add(service1);
        serviceList.add(service2);
        Mockito.when(servicesDao.getServices()).thenReturn(serviceList);
        mvc.perform(MockMvcRequestBuilders.get("/services").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetServiceByName() throws Exception {
        Service service = new DummyService();
        Mockito.when(servicesDao.getServiceByName(Mockito.anyString())).thenReturn(service);
        mvc.perform(MockMvcRequestBuilders.get("/services/test service").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
