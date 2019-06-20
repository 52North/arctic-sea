package org.n52.faroe.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.HashMap;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.n52.faroe.SettingDefinition;
import org.n52.faroe.SettingValue;
import org.n52.faroe.SettingValueFactory;
import org.n52.faroe.SettingsService;
import org.n52.faroe.dao.ServicesDao;
import org.n52.faroe.json.JsonSettingValueFactory;
import org.n52.faroe.service.Service;
import org.n52.faroe.utils.TestUtils;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ContextConfiguration(locations = "classpath:testApplicationContext.xml")
public class SettingsControllerTest {

  private MockMvc mvc;
  @Mock
  private ServicesDao servicesDao;
  @Mock
  private Service mockService;
  @Mock
  private SettingsService settingsService;
  @Mock
  private SettingValueFactory settingValueFactory;
  @InjectMocks
  private SettingsController settingsController;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    mvc = MockMvcBuilders.standaloneSetup(settingsController).build();
    Mockito.when(mockService.getName()).thenReturn("dummy_service");
    servicesDao.createService(mockService);
    Mockito.when(servicesDao.getServiceByName("dummy_service")).thenReturn(mockService);
    Mockito.when(mockService.getSettingsService()).thenReturn(settingsService);
    Mockito.when(settingsService.getSettingFactory()).thenReturn(settingValueFactory);
  }

  @Test
  public void testGetSettings() throws Exception {
    createSettings();
    mvc.perform(MockMvcRequestBuilders.get("/services/dummy_service/settings").accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
  }

  @Test
  public void testGetSettings_badRequest() throws Exception {
    Mockito.when(settingsService.getSettings()).thenThrow(new RuntimeException());
    mvc.perform(MockMvcRequestBuilders.get("/services/dummy_service/settings").accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest());
  }

  @Test
  public void testAddNewSettings() throws Exception {
    final Map<SettingDefinition<?>, SettingValue<?>> settingValueMap = new HashMap<>();
    SettingValue<Boolean> settingValue = Mockito.mock(SettingValue.class);
    Mockito.when(settingValueFactory.newBooleanSettingValue(Mockito.anyString(), Mockito.anyString())).thenReturn(settingValue);
    mvc.perform(MockMvcRequestBuilders.post("/services/dummy_service/settings").content("{\"Type\":\"Boolean\", \"Key\":\"new-boolean-setting\", \"Value\":\"false\"}").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
  }

  @Test
  public void testAddNewSettings_badRequest() throws Exception {
    Mockito.when(settingsService.getSettingFactory()).thenThrow(new RuntimeException());
    mvc.perform(MockMvcRequestBuilders.post("/services/dummy_service/settings").content("{\"Type\":\"Boolean\", \"Key\":\"new-boolean-setting\", \"Value\":\"false\"}").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest());
  }

  @Test
  public void testDeleteSettings() throws Exception {
    createSettings();
    mvc.perform(MockMvcRequestBuilders.delete("/services/dummy_service/settings/key setting 1"));
  }

  @Test
  public void testUpdateSettings() throws Exception {
    createSettings();
    SettingValue<Boolean> settingValue = Mockito.mock(SettingValue.class);
    Mockito.when(settingValueFactory.newBooleanSettingValue(Mockito.anyString(), Mockito.anyString())).thenReturn(settingValue);
    mvc.perform(MockMvcRequestBuilders.put("/services/dummy_service/settings/key setting 1").content("{\"Type\":\"Boolean\", \"Key\":\"key setting 1\", \"Value\":\"false\"}").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
  }

  private void createSettings() {
    final Map<SettingDefinition<?>, SettingValue<?>> settingValueMap = new HashMap<>();
    final SettingDefinition<?> settingDefinition1 = TestUtils.getBooleanSettingDefinition("title def 1", "key def 1", "title def desc1");
    final SettingDefinition<?> settingDefinition2 = TestUtils.getBooleanSettingDefinition("title def 2", "key def 2", "title def desc2");
    final SettingValue<?> settingValue1 = new JsonSettingValueFactory().newBooleanSettingValue("key setting 2", "false");
    final SettingValue<?> settingValue2 = new JsonSettingValueFactory().newBooleanSettingValue("key setting 1", "true");
    settingValueMap.put(settingDefinition1, settingValue1);
    settingValueMap.put(settingDefinition2, settingValue2);
    Mockito.when(settingsService.getSettings()).thenReturn(settingValueMap);
  }
}
