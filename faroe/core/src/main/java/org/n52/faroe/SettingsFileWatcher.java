/*
 * Copyright 2015-2021 52°North Initiative for Geospatial Open Source
 * Software GmbH
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
package org.n52.faroe;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.n52.faroe.annotation.Configurable;
import org.n52.faroe.annotation.Setting;
import org.n52.janmayen.lifecycle.Constructable;
import org.n52.janmayen.lifecycle.Destroyable;

import com.google.common.base.MoreObjects;

/**
 * http://andreinc.net/2013/12/06/java-7-nio-2-tutorial-writing-a-simple-filefolder-monitor-using-the-watch-service-api/
 * https://docs.oracle.com/javase/tutorial/essential/io/notification.html
 *
 *
 * Load bean:
 * <pre><bean id="fileWatcher" class="org.n52.iceland.config.SettingsFileWatcher" /></pre>
 *
 * Configure setting:
 * <pre><bean class="org.n52.iceland.config.settings.BooleanSettingDefinition">
 *       <property name="key" value="filewatcher.enabled" />
 *       <property name="title" value="Enable configuration file watcher" />
 *       <property name="description"
 *                 value="If enabled, the configuration file will be reloaded when changed on the disk." />
 *       <property name="order" value="1.0" />
 *       <property name="group" ref="watcherSettingDefinitionGroup" />
 *       <property name="defaultValue" value="false" />
 * </bean></pre>
 *
 * @author <a href="mailto:d.nuest@52north.org">Daniel Nüst</a>
 */
@Configurable
public class SettingsFileWatcher implements Constructable, Destroyable {

    private static final String FILE_WATCHER_ENABLED = "filewatcher.enabled";
    private static final Logger LOG = LoggerFactory.getLogger(SettingsFileWatcher.class);
    private SettingsService settingsService;
    private FileSettingsConfiguration fileConfiguration;
    private boolean enabled;
    private WatchService watchService;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    public SettingsFileWatcher() {
        LOG.debug("NEW {}", this);
    }

    @Inject
    public void setSettingsService(SettingsService settingsService) {
        this.settingsService = settingsService;
    }

    @Inject
    public void setFileConfiguration(FileSettingsConfiguration fileConfiguration) {
        this.fileConfiguration = fileConfiguration;
    }

    @Setting(value = FILE_WATCHER_ENABLED, required = false)
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public void init() {
        if (!enabled) {
            LOG.info("File watcher loaded but not enabled: {}", this.toString());
            return;
        }

        Path configFilePath = this.fileConfiguration.getPath();

        LOG.info("Adding watch on '{}'.", configFilePath);

        try {
            this.watchService = FileSystems.getDefault().newWatchService();
            Path parent = configFilePath.getParent();
            if (parent == null) {
                throw new ConfigurationError("Can not get parent directory of config file");
            }
            parent.register(this.watchService, StandardWatchEventKinds.ENTRY_MODIFY);
        } catch (IOException e) {
            throw new ConfigurationError("Error creating and registering watch service", e);
        }

        executor.submit(new Watcher(this.watchService, this.settingsService, this.fileConfiguration));
    }

    @Override
    public void destroy() {
        LOG.debug("Destroying... {}", this);
        if (this.watchService != null) {
            try {
                this.watchService.close();
            } catch (IOException e) {
                LOG.error("Error closing watch service on file {}", this.fileConfiguration.getPath(), e);
            }
        }

        executor.shutdown();

        LOG.debug("Destroyed {}", this);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).omitNullValues()
                .add("enabled", this.enabled)
                .add("path", this.fileConfiguration)
                .toString();
    }

    private static final class Watcher implements Runnable {

        private final WatchService service;
        private final SettingsService settings;
        private final FileSettingsConfiguration fileConfiguration;

        private Watcher(WatchService watchService, SettingsService settingsService,
                        FileSettingsConfiguration fileConfiguration) {
            this.service = watchService;
            this.settings = settingsService;
            this.fileConfiguration = fileConfiguration;
        }

        @Override
        public void run() {
            LOG.debug("Starting watcher thread.");

            for (;;) {
                // wait for key to be signaled
                WatchKey key;
                try {
                    key = service.take();
                } catch (InterruptedException e) {
                    LOG.error("Could not take key from watcher", e);
                    return;
                }

                key.pollEvents().forEach(event -> {
                    WatchEvent.Kind<?> kind = event.kind();
                    if (kind != StandardWatchEventKinds.OVERFLOW) {
                        LOG.trace("Received {} event for file: {}",
                                  event.kind(), event.context());

                        // check if it is an event on the config file
                        Path eventPath = (Path) event.context();
                        if (this.fileConfiguration.getPath().endsWith(eventPath)) {
                            LOG.debug("File {} changed, updating settings...", eventPath);

                            fileConfiguration.refresh();
                            settings.reconfigure();
                        }
                    }
                });

                boolean valid = key.reset();
                if (!valid) {
                    break;
                }
            }

            LOG.debug("Stopping watcher thread.");
        }
    }

}
