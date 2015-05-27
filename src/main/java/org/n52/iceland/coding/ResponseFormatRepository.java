package org.n52.iceland.coding;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import org.n52.iceland.config.SettingsManager;
import org.n52.iceland.ds.ConnectionProviderException;
import org.n52.iceland.encode.Encoder;
import org.n52.iceland.encode.ObservationEncoder;
import org.n52.iceland.encode.ResponseFormatKey;
import org.n52.iceland.exception.ConfigurationException;
import org.n52.iceland.lifecycle.Constructable;
import org.n52.iceland.service.operator.ServiceOperatorKey;
import org.n52.iceland.service.operator.ServiceOperatorRepository;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class ResponseFormatRepository implements Constructable {
    @Deprecated
    private static ResponseFormatRepository instance;
    private final Map<String, Map<String, Set<String>>> responseFormats = Maps.newHashMap();
    private final Map<ResponseFormatKey, Boolean> responseFormatStatus = Maps.newHashMap();

    @Inject
    private ServiceOperatorRepository serviceOperatorRepository;

    @Inject
    private SettingsManager settingsManager;

    @Inject
    private CodingRepository codingRepository;

    @Override
    public void init() {
        ResponseFormatRepository.instance = this;
        generateResponseFormatMaps();
    }

    private void generateResponseFormatMaps() {
        this.responseFormatStatus.clear();
        this.responseFormats.clear();
        Set<ServiceOperatorKey> serviceOperatorKeyTypes
                = getServiceOperatorKeys();

        for (Encoder<?, ?> encoder : this.codingRepository.getEncoders()) {
            if (encoder instanceof ObservationEncoder) {
                ObservationEncoder<?, ?> observationEncoder = (ObservationEncoder<?, ?>) encoder;
                for (ServiceOperatorKey key : serviceOperatorKeyTypes) {
                    Set<String> responseFormats = observationEncoder.getSupportedResponseFormats(key.getService(), key.getVersion());
                    if (responseFormats != null) {
                        for (String responseFormat : responseFormats) {
                            addResponseFormat(new ResponseFormatKey(key, responseFormat));
                        }
                    }
                }
            }
        }
    }

    protected void addResponseFormat(ResponseFormatKey key) {
        try {
            this.responseFormatStatus.put(key, this.settingsManager.isResponseFormatActive(key));
        } catch (ConnectionProviderException ex) {
            throw new ConfigurationException(ex);
        }
        Map<String, Set<String>> byService = this.responseFormats.get(key.getService());
        if (byService == null) {
            this.responseFormats.put(key.getService(), byService = Maps.newHashMap());
        }
        Set<String> byVersion = byService.get(key.getVersion());
        if (byVersion == null) {
            byService.put(key.getVersion(), byVersion = Sets.newHashSet());
        }
        byVersion.add(key.getResponseFormat());
    }

    public Set<String> getSupportedResponseFormats(ServiceOperatorKey sokt) {
        return getSupportedResponseFormats(sokt.getService(), sokt.getVersion());
    }

    public Set<String> getSupportedResponseFormats(String service,
                                                   String version) {
        Map<String, Set<String>> byService = responseFormats.get(service);
        if (byService == null) {
            return Collections.emptySet();
        }
        Set<String> responseFormats = byService.get(version);
        if (responseFormats == null) {
            return Collections.emptySet();
        }

        ServiceOperatorKey sokt = new ServiceOperatorKey(service, version);
        Set<String> result = Sets.newHashSet();
        for (String responseFormat : responseFormats) {
            ResponseFormatKey rfkt = new ResponseFormatKey(sokt, responseFormat);
            Boolean status = responseFormatStatus.get(rfkt);
            if (status != null && status) {
                result.add(responseFormat);
            }
        }
        return result;
    }

    public Set<String> getAllSupportedResponseFormats(String service,
                                                      String version) {
        Map<String, Set<String>> byService = this.responseFormats.get(service);
        if (byService == null) {
            return Collections.emptySet();
        }
        Set<String> rfs = byService.get(version);
        if (rfs == null) {
            return Collections.emptySet();
        }
        return Collections.unmodifiableSet(rfs);
    }

    public Map<ServiceOperatorKey, Set<String>> getAllSupportedResponseFormats() {
        Map<ServiceOperatorKey, Set<String>> map = Maps.newHashMap();
        for (ServiceOperatorKey sokt : getServiceOperatorKeys()) {
            map.put(sokt, getAllSupportedResponseFormats(sokt));
        }
        return map;
    }

    private Set<ServiceOperatorKey> getServiceOperatorKeys() {
        return this.serviceOperatorRepository
                .getServiceOperatorKeys();
    }

    public Set<String> getAllSupportedResponseFormats(ServiceOperatorKey sokt) {
        return getAllSupportedResponseFormats(sokt.getService(),
                                              sokt.getVersion());
    }

     public Map<ServiceOperatorKey, Set<String>> getSupportedResponseFormats() {
        Map<ServiceOperatorKey, Set<String>> map = Maps.newHashMap();
        for (ServiceOperatorKey sokt : getServiceOperatorKeys()) {
            map.put(sokt, getSupportedResponseFormats(sokt));
        }
        return map;
    }

    public void setActive(ResponseFormatKey rfkt, boolean active) {
        if (responseFormatStatus.containsKey(rfkt)) {
            responseFormatStatus.put(rfkt, active);
        }
    }


    @Deprecated
    public static ResponseFormatRepository getInstance() {
        return instance;
    }

}
