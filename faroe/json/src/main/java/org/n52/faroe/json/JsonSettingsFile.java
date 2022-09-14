package org.n52.faroe.json;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.n52.faroe.ConfigurationError;
import org.n52.faroe.FileSettingsConfiguration;
import org.n52.janmayen.ConfigLocationProvider;
import org.n52.janmayen.Debouncer;
import org.n52.janmayen.Json;
import org.n52.janmayen.Producer;
import org.n52.janmayen.lifecycle.Destroyable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

public class JsonSettingsFile implements Destroyable,
Producer<ObjectNode>,
FileSettingsConfiguration{
	
	   private static final Logger LOG = LoggerFactory.getLogger(JsonSettingsFile.class);
	    private static final String DEFAULT_FILE_NAME = "settings.json";
	    private static final int DEFAULT_WRITE_TIMEOUT = 1000;
	    private static final String CONFIG_PATH = "config";
	    private static final String WEB_INF_PATH = "WEB-INF";
	    private String fileName = DEFAULT_FILE_NAME;
	    private int writeTimeout = DEFAULT_WRITE_TIMEOUT;
	   private Debouncer debouncer;
	   private ObjectNode settings;
	   private final ReadWriteLock lock = new ReentrantReadWriteLock();
	   private File file;
	   private boolean readonly;
	    private ConfigLocationProvider configLocationProvider;
	   private final JsonNodeFactory nodeFactory = Json.nodeFactory();
	
	   public void init() {
	        writeLock().lock();
	        try {
	            this.debouncer = new Debouncer(this.writeTimeout, this::persist);
	            Path path = buildPath();
	            Path parent = path.getParent();
	            if (parent != null) {
	                if (!Files.isSymbolicLink(parent)) {
	                    Files.createDirectories(parent);
	                }
	            } else {
	                throw new RuntimeException("Error while creating config file path.");
	            }
	            this.file = path.toFile();
	            this.refresh();
	        } catch (IOException e) {
	            throw new RuntimeException(e);
	        } finally {
	            writeLock().unlock();
	        }
	    }
	   
	   private Path buildPath() {
	        if (configLocationProvider != null && configLocationProvider.get() != null) {
	            return Paths.get(configLocationProvider.get(), WEB_INF_PATH, CONFIG_PATH, this.fileName);
	        }
	        return Paths.get(WEB_INF_PATH, CONFIG_PATH, this.fileName);
	    }
	   
	    private synchronized void persist() {
	        if (!readonly) {
	            readLock().lock();
	            try {
	                LOG.debug("Writing configuration file");
	                try (FileOutputStream fos = new FileOutputStream(this.file)) {
	                    Json.print(fos, this.settings);
	                }
	            } catch (IOException e) {
	                throw new ConfigurationError("Could not persist configuration", e);
	            } finally {
	                readLock().unlock();
	            }
	        }
	    }

	    public void delete() {
	        writeLock().lock();
	        try {
	            if (this.file.exists() && this.file.isFile()) {
	                if (!this.file.delete()) {
	                    throw new ConfigurationError("Can not delete configuration file %s", file
	                                                 .getAbsolutePath());
	                }
	            }
	        } finally {
	            writeLock().unlock();
	        }
	    }
	    
	    public void setWriteTimeout(int writeTimeout) {
	        this.writeTimeout = writeTimeout;
	    }

	    
	    public synchronized void setReadonly(boolean readonly) {
	        this.readonly = readonly;
	    }

	    public void setFileName(String fileName) {
	        this.fileName = fileName;
	    }

	    public void setConfigLocationProvider(ConfigLocationProvider configLocationProvider) {
	        this.configLocationProvider = configLocationProvider;
	    }

	    
	@Override
	public Path getPath() {
		 return this.file.toPath();
	}

	  public Lock readLock() {
	        return this.lock.readLock();
	    }

	    public Lock writeLock() {
	        return this.lock.writeLock();
	    }
	    
	    public void scheduleWrite() {
	        this.debouncer.call();
	    }


	@Override
	public void refresh() {
		 writeLock().lock();
	        try {
	            this.settings = readSettings(this.file)
	                    .orElseGet(nodeFactory::objectNode);
	        } finally {
	            writeLock().unlock();
	        }
	}
	
   private Optional<ObjectNode> readSettings(File f) {
       if (!f.exists()) {
           return Optional.empty();
       }
       if (!f.isFile()) {
           throw new ConfigurationError("%s is not a file", f.getAbsolutePath());
       }
       if (!f.canRead()) {
           throw new ConfigurationError("%s is not a readable file", f.getAbsolutePath());
       }
       try {
           JsonNode node = Json.loadFile(f);
           if (!node.isObject()) {
               throw new ConfigurationError("%s does not contain a JSON object", f.getAbsolutePath());
           }
           return Optional.of((ObjectNode) node);
       } catch (IOException ex) {
           throw new ConfigurationError("Could not read " + f.getAbsolutePath(), ex);
       }
   }

	 @SuppressFBWarnings({"EI_EXPOSE_REP2"})
	    public void set(ObjectNode settings) {
	        this.settings = settings;
	    }


	@Override
	public ObjectNode get() {
		return this.settings;
	}


	@Override
	public void destroy() {
		LOG.info("Destroying {}", System.identityHashCode(this));
        this.debouncer.finish();
	}
	  public String toString() {
	        return "JsonConfiguration{" + "file=" + file + '}';
	    }

}