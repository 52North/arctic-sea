package org.n52.faroe;

import java.util.Collection;

public interface SettingsDefinitionDao extends SettingsDao {
	
	Collection<SettingDefinition<?>> getAllSettings();
	
	void saveSettings(Collection<SettingDefinition<?>> settings);

}
