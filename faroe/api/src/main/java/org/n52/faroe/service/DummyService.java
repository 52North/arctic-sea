package org.n52.faroe.service;

import org.n52.faroe.SettingsService;

import javax.inject.Inject;

public class DummyService implements Service {


    @Inject
    private SettingsService settingsService;

    @Override
    public String getName() {
        return "name";
    }

    @Override
    public SettingsService getSettingsService() {
        return this.settingsService;
    }

}
