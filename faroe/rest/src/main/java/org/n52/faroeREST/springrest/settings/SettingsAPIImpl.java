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

import java.util.*;
import javax.inject.Inject;

import org.n52.faroe.SettingDefinition;
import org.n52.faroe.SettingsService;
import org.n52.faroeREST.springrest.entities.Groups;
import org.springframework.stereotype.Service;


@Service
public class SettingsAPIImpl implements SettingsAPI {

	@Inject
	private SettingsService service;
	private Collection<SettingDefinition<?>> titles = new ArrayList<SettingDefinition<?>>();
	private Collection<SettingAPIDao> settings = new LinkedList<SettingAPIDao>();
	private Set<String> groups =  new HashSet<String>();

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
//		System.out.println(group);
		service.addSettings(group);
		return "Added Successfully";
	}

	@Override
	public Collection<SettingDefinition<?>> updateSettings(Collection<SettingDefinition<?>> group) {

		service.getSettingDefinitions().forEach(e -> {
			if(e.getKey() == ((SettingDefinition<?>) group).getKey()) {
				e.setDescription(((SettingDefinition<?>) group).getDescription());
			}

	});

		return /*group*/ null;
	}

	public Set<SettingDefinition<?>> deleteGroup() {

		service.deleteAll();
		return this.service.getSettingDefinitions();
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
