package org.n52.faroe.json;

import java.util.Map.Entry;
import java.util.Spliterators;
import java.util.concurrent.locks.Lock;
import java.util.function.Predicate;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import javax.inject.Inject;

import org.n52.janmayen.Json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

public abstract class AbstractJsonSettingsDao {

	 private final JsonNodeFactory nodeFactory = Json.nodeFactory();
	    private JsonSettingsFile settings;

	    @Inject
	    @SuppressFBWarnings({"EI_EXPOSE_REP2"})
	    public void setConfiguration(JsonSettingsFile settings) {
	        this.settings = settings;
	    }

	    protected JsonNodeFactory nodeFactory() {
	        return nodeFactory;
	    }

	    protected Lock writeLock() {
	        return settings().writeLock();
	    }

	    protected Lock readLock() {
	        return settings().readLock();
	    }

	    protected JsonSettingsFile settings() {
	        return this.settings;
	    }

	    protected ObjectNode getSettings() {
	        return settings().get();
	    }

	    protected Stream<JsonNode> createStream(JsonNode node) {
	        return StreamSupport.stream(node.spliterator(), false);
	    }

	    protected Stream<Entry<String, JsonNode>> createEntryStream(JsonNode node) {
	        return StreamSupport.stream(Spliterators.spliteratorUnknownSize(node.fields(), 0), true);
	    }

	    protected Predicate<JsonNode> isNullOrMissing(String key) {
	        return n -> n.path(key).isNull() || n.path(key).isMissingNode();
	    }
}
