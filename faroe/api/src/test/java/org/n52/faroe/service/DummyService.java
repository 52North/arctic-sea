package org.n52.faroe.service;

import org.mockito.Mockito;
import org.n52.faroe.SettingsService;

public class DummyService implements Service {
	@Override
	public String getName() {
		return "dummy service";
	}

	@Override
	public SettingsService getSettingsService() {
		return Mockito.mock(SettingsService.class);
	}
}
