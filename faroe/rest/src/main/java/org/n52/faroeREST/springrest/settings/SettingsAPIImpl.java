/*
 * Copyright (C) 2015-2022 52°North Spatial Information Research GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.n52.faroeREST.springrest.settings;


import java.io.File;
import java.io.IOException;
import java.util.*;
import javax.inject.Inject;
import javax.inject.Qualifier;

import org.n52.faroe.SettingDefinition;
import org.n52.faroe.SettingValue;
import org.n52.faroe.SettingsDefinitionDao;
import org.n52.faroe.SettingsService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;


@Service
public class SettingsAPIImpl implements InitializingBean,SettingsAPI {

	@Inject
	private SettingsService service;
	
	@Inject
	private SettingsDefinitionDao dao;
	
	
	private Collection<SettingDefinition<?>> titles = new ArrayList<SettingDefinition<?>>();
	//private Collection<SettingDefinition<?>> assignData = new ArrayList<SettingDefinition<?>>();
	private Collection<SettingAPIDao> settings = new LinkedList<SettingAPIDao>();
	private Set<String> groups =  new HashSet<String>();

	
	public void afterPropertiesSet() {
		service.addSettings(dao.getAllSettings());
	}
	
	@Override
	public Collection<SettingDefinition<?>> setSettings() {
		return null;
		
	}
	
	@Override
	public Collection<SettingAPIDao> getSettings() {
		
		service.getSettingDefinitions().forEach(definition -> {
			   SettingAPIDao  setting =  new SettingAPIDao(definition.getGroup().getTitle(), definition.getGroup().getDescription(), "Sujit");
			   if(this.settings.toArray().length == 0) {
					this.settings.add(setting);   				
				}
//			   System.out.println(setting);
			   
			this.settings.forEach(definitions -> {
//				System.out.println(this.service.getSettingDefinitions());
			if(definitions.getTitle()!=(definition.getGroup().getTitle())) {
					this.settings.add(setting);   
			}
		});
	          
   	
});

//		System.out.println(this.settings.size());
		return this.settings;
	}


	@Override
	public Collection<SettingDefinition<?>> getSettingsbyTitle(String groupTitle) {
		titles.clear();
	
		service.getSettingDefinitions().forEach(definition -> {
           if(definition.getGroup().getTitle().equalsIgnoreCase(groupTitle))
        		   {
        	   
        	   			this.titles.add(definition);
        		   }
		});
		
		return this.titles;
	}
	
	@Override
	public Set<String> getGroups() {
		service.getSettingDefinitions().forEach(definition -> {
           
        	   			this.groups.add(definition.getGroup().getTitle());   
		});
		
		return this.groups;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public String addSettings(Collection<SettingDefinition<?>> group) {
		dao.saveSettings(group);
		service.addSettings(group);
		return "Settings Added Successfully";
	}
	

	@Override
	public String updateSettings(SettingValue<?> group) {
		service.changeSetting(group);
		return "Settings updated";
	}


	@Override
	public String deleteSettings(String setting) {
		
	 service.deleteSetting(setting);
	 
	 return "Deleted Successfully";
	}


	@Override
	public String deleteAllSettings() {

		service.deleteAll();
		return "Everything deleted Successfully";
	}

}
