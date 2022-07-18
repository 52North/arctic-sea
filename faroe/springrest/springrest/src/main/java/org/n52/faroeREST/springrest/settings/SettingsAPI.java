package org.n52.faroeREST.springrest.settings;

import java.util.List;

import org.n52.faroeREST.springrest.entities.groups;

public interface SettingsAPI {

	public List<groups> getGroup();
	
	public groups getGroupbyTitle(String groupTitle);
	
	public groups addGroup(groups group);
	
	public groups updateGroup(groups group);
	
	public groups deleteGroup(String groupTitle);
}
