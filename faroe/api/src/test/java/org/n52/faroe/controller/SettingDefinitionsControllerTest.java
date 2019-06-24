package org.n52.faroe.controller;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.n52.faroe.SettingDefinition;
import org.n52.faroe.SettingDefinitionGroup;
import org.n52.faroe.SettingsService;
import org.n52.faroe.dao.ServicesDao;
import org.n52.faroe.service.Service;
import org.n52.faroe.utils.TestUtils;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.HashSet;
import java.util.Set;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ContextConfiguration(locations = "classpath:testApplicationContext.xml")
public class SettingDefinitionsControllerTest {

	private MockMvc mvc;
	@Mock
	private ServicesDao servicesDao;
	@Mock
	private Service mockService;
	@Mock
	private SettingsService settingsService;
	@InjectMocks
	private SettingDefinitionsController settingDefinitionsController;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		mvc = MockMvcBuilders.standaloneSetup(settingDefinitionsController).build();
		Mockito.when(mockService.getName()).thenReturn("dummy_service");
		Mockito.when(servicesDao.getServiceByName("dummy_service")).thenReturn(mockService);
		Mockito.when(mockService.getSettingsService()).thenReturn(settingsService);

	}

	@Test
	public void testGetSettingDefinitions() throws Exception {
		Set<SettingDefinition<?>> settingDefinitions = new HashSet<>();
		final SettingDefinitionGroup settingDefinitionGroup1 = TestUtils
				.getSettingDefinitionGroup("setting def group title 1", "setting def group description 1");
		final SettingDefinitionGroup settingDefinitionGroup2 = TestUtils
				.getSettingDefinitionGroup("setting def group title 2", "setting def group description 2");
		final SettingDefinition<?> booleanSettingDefinition1 = TestUtils.getBooleanSettingDefinition();
		booleanSettingDefinition1.setGroup(settingDefinitionGroup1);
		final SettingDefinition<?> booleanSettingDefinition2 = TestUtils.getBooleanSettingDefinition();
		booleanSettingDefinition2.setGroup(settingDefinitionGroup2);
		settingDefinitions.add(booleanSettingDefinition1);
		settingDefinitions.add(booleanSettingDefinition2);
		servicesDao.createService(mockService);
		Mockito.when(settingsService.getSettingDefinitions()).thenReturn(settingDefinitions);
		mvc.perform(MockMvcRequestBuilders.get("/services/dummy_service/definitions").accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
	}

	@Test
	public void testGetSettingDefinition_badRequest() throws Exception {
		Mockito.when(settingsService.getSettingDefinitions()).thenThrow(new RuntimeException());
		mvc.perform(MockMvcRequestBuilders.get("/services/dummy_service/definitions").accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest());
	}

	@Test
	public void testGetSettingDefinitionById() throws Exception {
		final Set<SettingDefinition<?>> settingDefinitions = new HashSet<>();
		final SettingDefinitionGroup settingDefinitionGroup = TestUtils.getSettingDefinitionGroup();
		final SettingDefinition booleanSettingDefinition = TestUtils
				.getBooleanSettingDefinition("a boolean def", "bool-key", "test definition");
		booleanSettingDefinition.setGroup(settingDefinitionGroup);
		settingDefinitions.add(booleanSettingDefinition);
		servicesDao.createService(mockService);
		Mockito.when(settingsService.getDefinitionByKey(Mockito.anyString())).thenReturn(booleanSettingDefinition);
		mvc.perform(MockMvcRequestBuilders.get("/services/dummy_service/definitions/bool-key/").accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
	}

	@Test
	public void testUpdateSettingDefinition() throws Exception {
		final Set<SettingDefinition<?>> settingDefinitions = new HashSet<>();
		final SettingDefinitionGroup settingDefinitionGroup = TestUtils.getSettingDefinitionGroup();
		final SettingDefinition booleanSettingDefinition = TestUtils
				.getBooleanSettingDefinition("a boolean def", "bool-key", "test definition");
		booleanSettingDefinition.setGroup(settingDefinitionGroup);
		settingDefinitions.add(booleanSettingDefinition);
		servicesDao.createService(mockService);
		Mockito.when(settingsService.getDefinitionByKey(Mockito.anyString())).thenReturn(booleanSettingDefinition);
		mvc.perform(MockMvcRequestBuilders.put("/services/dummy_service/definitions/bool-key").content("{\"SettingDefinitionGroup\":{\"Title\":\"new Title for group\", \"Description\":\"new description for group\"}, \"Title\":\"new setting title\", \"Description\":\"test desc\"}").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
	}

	@Test
	public void testUpdateSettingDefinition_badRequest() throws Exception {
		Mockito.when(settingsService.getDefinitionByKey(Mockito.anyString())).thenThrow(new RuntimeException());
		mvc.perform(MockMvcRequestBuilders.put("/services/dummy_service/definitions/bool-key").content("{\"SettingDefinitionGroup\":{\"Title\":\"new Title for group\", \"Description\":\"new description for group\"}, \"Title\":\"new setting title\", \"Description\":\"test desc\"}").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest());
	}
}
