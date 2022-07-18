package org.n52.faroeREST.springrest.controller;

import java.util.List;

import org.n52.faroeREST.springrest.entities.groups;
import org.n52.faroeREST.springrest.settings.SettingsAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class APIController {
	
	@Autowired
	private SettingsAPI API;

	@GetMapping(path = "/groups")
	public List<groups> getGroup(){
		
		return this.API.getGroup();
		
	}
	
	@GetMapping(path = "/groups/{groupTitle}")
	public groups getGroupbyTitle(@PathVariable String groupTitle) {
		return this.API.getGroupbyTitle(groupTitle);
	}
	
	@PostMapping(path = "/groups", consumes = "application/json")
	public groups addGroup(@RequestBody groups group) {
		
		return this.API.addGroup(group);
		
	}
	
	@PutMapping(path = "/groups", consumes = "application/json")
	public groups updateGroup(@RequestBody groups group) {
		
		return this.API.updateGroup(group);
		
	}
	
	@DeleteMapping(path = "/groups/{groupTitle}")
	public ResponseEntity<HttpStatus> deleteGroup(@PathVariable String groupTitle){
		try {
			this.API.deleteGroup(groupTitle);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
}
