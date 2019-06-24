package org.n52.faroe.utils;

import org.n52.faroe.SettingDefinition;
import org.n52.faroe.SettingDefinitionGroup;
import org.n52.faroe.settings.BooleanSettingDefinition;

public class TestUtils {

	public static SettingDefinitionGroup getSettingDefinitionGroup() {
		return getSettingDefinitionGroup("test setting group title", "test setting group description");
	}

	public static SettingDefinitionGroup getSettingDefinitionGroup(final String title,
																   final String description) {
		SettingDefinitionGroup settingDefinitionGroup = new SettingDefinitionGroup();
		settingDefinitionGroup.setTitle(title);
		settingDefinitionGroup.setDescription(description);
		settingDefinitionGroup.setShowInDefaultSettings(true);
		return settingDefinitionGroup;
	}

	public static SettingDefinition<Boolean> getBooleanSettingDefinition() {
		return getBooleanSettingDefinition("test boolean setting title", "test boolean setting key",
				"test boolean setting description");
	}

	public static SettingDefinition<Boolean> getBooleanSettingDefinition(final String title,
																		 final String key, final String description) {
		BooleanSettingDefinition booleanSettingDefinition = new BooleanSettingDefinition();
		booleanSettingDefinition.setDefaultValue(true);
		booleanSettingDefinition.setDescription(description);
		booleanSettingDefinition.setKey(key);
		booleanSettingDefinition.setTitle(title);
		booleanSettingDefinition.setOptional(false);
		return booleanSettingDefinition;
	}
}
