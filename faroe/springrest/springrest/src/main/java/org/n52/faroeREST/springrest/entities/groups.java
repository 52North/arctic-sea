package org.n52.faroeREST.springrest.entities;

public class groups {

	private String title;
	private String description;
	public groups(String title, String description) {
		super();
		this.title = title;
		this.description = description;
	}
	public groups() {
		super();
		// TODO Auto-generated constructor stub
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	@Override
	public String toString() {
		return "groups [title=" + title + ", description=" + description + "]";
	}
	
}
