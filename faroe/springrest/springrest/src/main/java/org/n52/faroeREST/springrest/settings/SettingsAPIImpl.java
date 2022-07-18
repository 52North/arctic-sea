package org.n52.faroeREST.springrest.settings;

import java.util.*;
import java.util.stream.Collectors;

import org.n52.faroeREST.springrest.entities.groups;
import org.springframework.stereotype.Service;

@Service
public class SettingsAPIImpl implements SettingsAPI {

	List<groups> list;
	
	public SettingsAPIImpl() {
		list=new ArrayList<>();
		list.add(new groups("Desc","This is desc"));
		list.add(new groups("Desc2","This is desc2"));
	}
	
	@Override
	public List<groups> getGroup() {
		return list;
	}

	@Override
	public groups getGroupbyTitle(String groupTitle) {
		
		groups g=null;
		for(groups group : list) {
			if(group.getTitle() == groupTitle) {
				g=group;
				break;
			}
		}
		return g;
		
	}

	@Override
	public groups addGroup(groups group) {
		
		list.add(group);
		
		return group;
	}

	@Override
	public groups updateGroup(groups group) {

		list.forEach(e -> {
			if(e.getTitle() == group.getTitle()) {
				e.setDescription(group.getDescription());
			}
				
	});

		return group;
	}

	@Override
	public groups deleteGroup(String groupTitle) {
		list=this.list.stream().filter(e->e.getTitle()!=groupTitle).collect(Collectors.toList());
		return null;
	}

}
