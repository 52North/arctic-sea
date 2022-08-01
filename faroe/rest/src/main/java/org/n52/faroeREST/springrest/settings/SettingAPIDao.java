package org.n52.faroeREST.springrest.settings;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize
public class SettingAPIDao {
	
	String title,description,href;

	public SettingAPIDao(String title, String description, String href) {
		super();
		this.title = title;
		this.description = description;
		this.href = href;
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

	public String getHref() {
		return href;
	}

	public void setHref(String href) {
		this.href = href;
	}

	@Override
	public String toString() {
		return "SettingAPIDao [title=" + title + ", description=" + description + ", href=" + href + "]";
	}

	
}
