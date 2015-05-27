package org.n52.iceland.coding;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import org.n52.iceland.encode.Encoder;
import org.n52.iceland.encode.EncoderKey;
import org.n52.iceland.encode.XmlEncoderKey;
import org.n52.iceland.lifecycle.Constructable;
import org.n52.iceland.w3c.SchemaLocation;

import com.google.common.collect.Maps;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class SchemaRepository implements Constructable {
    @Deprecated
    private static SchemaRepository instance;

    private final Map<String, Set<SchemaLocation>> schemaLocations = Maps.newHashMap();
    private EncoderRepository encoderRepository;

    @Inject
    public void setEncoderRepository(EncoderRepository encoderRepository) {
        this.encoderRepository = encoderRepository;
    }

    @Deprecated
    public static SchemaRepository getInstance() {
        return instance;
    }

    @Override
    public void init() {
        SchemaRepository.instance = this;
        this.schemaLocations.clear();
        for (Encoder<?, ?> encoder : this.encoderRepository.getEncoders()) {
            for (EncoderKey key : encoder.getKeys()) {
                if (key instanceof XmlEncoderKey) {
                    Set<SchemaLocation> locations = encoder.getSchemaLocations();
                    if (locations != null && !locations.isEmpty()) {
                        String namespace = ((XmlEncoderKey) key).getNamespace();
                        this.schemaLocations.put(namespace, locations);
                    }
                }
            }
        }
    }

     public Set<SchemaLocation> getSchemaLocation(String namespace) {
        if (this.schemaLocations.containsKey(namespace)) {
            return this.schemaLocations.get(namespace);
        }
        return Collections.emptySet();
    }

    public String getNamespaceFor(String prefix) {
        Map<String, String> prefixNamspaceMap = getPrefixNamspaceMap();
        for (String namespace : prefixNamspaceMap.keySet()) {
            if (prefix.equals(prefixNamspaceMap.get(prefix))) {
                return namespace;
            }
        }
        return null;
    }

    public String getPrefixFor(String namespace) {
        return getPrefixNamspaceMap().get(namespace);
    }

    private Map<String, String> getPrefixNamspaceMap() {
        Map<String, String> prefixMap = Maps.newHashMap();
        for (Encoder<?, ?> encoder : this.encoderRepository.getEncoders()) {
            encoder.addNamespacePrefixToMap(prefixMap);
        }
        return prefixMap;
    }

}
