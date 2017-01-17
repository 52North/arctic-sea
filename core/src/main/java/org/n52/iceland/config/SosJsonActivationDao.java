package org.n52.iceland.config;

import java.util.Set;
import java.util.function.Function;

import org.n52.iceland.config.json.JsonActivationDao;
import org.n52.iceland.ogc.sos.SosOfferingExtensionKey;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class SosJsonActivationDao extends JsonActivationDao implements SosActivationDao {

    private static final String OFFERING_EXTENSIONS = "offeringExtensions";

    @Override
    public boolean isOfferingExtensionActive(SosOfferingExtensionKey key) {
        return isActive(OFFERING_EXTENSIONS, matches(key), true);
    }

    @Override
    public void setOfferingExtensionStatus(SosOfferingExtensionKey key, boolean active) {
        setStatus(OFFERING_EXTENSIONS, matches(key), s -> encode(s, key), active);
    }

    @Override
    public Set<SosOfferingExtensionKey> getOfferingExtensionKeys() {
        Function<JsonNode, SosOfferingExtensionKey> fun = createDomainDecoder(SosOfferingExtensionKey::new);
        return getKeys(OFFERING_EXTENSIONS, fun);
    }

}
