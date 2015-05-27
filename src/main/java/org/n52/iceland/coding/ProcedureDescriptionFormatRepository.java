package org.n52.iceland.coding;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import javax.inject.Inject;

import org.n52.iceland.config.SettingsManager;
import org.n52.iceland.encode.Encoder;
import org.n52.iceland.encode.ProcedureDescriptionFormatKey;
import org.n52.iceland.encode.ProcedureEncoder;
import org.n52.iceland.lifecycle.Constructable;
import org.n52.iceland.service.operator.ServiceOperatorKey;
import org.n52.iceland.service.operator.ServiceOperatorRepository;
import org.n52.iceland.util.activation.ActivationListener;
import org.n52.iceland.util.activation.ActivationListeners;
import org.n52.iceland.util.activation.ActivationManager;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class ProcedureDescriptionFormatRepository implements Constructable,
                                                             ActivationManager<ProcedureDescriptionFormatKey> {

    @Deprecated
    private static ProcedureDescriptionFormatRepository instance;
    private final ActivationListeners<ProcedureDescriptionFormatKey> activation = new ActivationListeners<>(true);
    private final Map<String, Map<String, Set<String>>> procedureDescriptionFormats = Maps.newHashMap();

    private SettingsManager settingsManager;
    private EncoderRepository encoderRepository;
    private ServiceOperatorRepository serviceOperatorRepository;

    @Override
    public void init() {
        ProcedureDescriptionFormatRepository.instance = this;

        Objects.requireNonNull(getEncoderRepository());
        Objects.requireNonNull(getSettingsManager());
        Objects.requireNonNull(getServiceOperatorRepository());

        generateProcedureDescriptionFormatMaps();
    }

    private SettingsManager getSettingsManager() {
        return this.settingsManager;
    }

    @Inject
    public void setSettingsManager(SettingsManager settingsManager) {
        this.settingsManager = settingsManager;
    }

    private EncoderRepository getEncoderRepository() {
        return this.encoderRepository;
    }

    @Inject
    public void setEncoderRepository(EncoderRepository repository) {
        this.encoderRepository = repository;
    }

    private Set<ServiceOperatorKey> getServiceOperatorKeys() {
        return getServiceOperatorRepository().getServiceOperatorKeys();
    }

    private ServiceOperatorRepository getServiceOperatorRepository() {
        return this.serviceOperatorRepository;
    }

    @Inject
    public void setServiceOperatorRepository(ServiceOperatorRepository repository) {
        this.serviceOperatorRepository = repository;
    }

    private void generateProcedureDescriptionFormatMaps() {
        this.procedureDescriptionFormats.clear();
        Set<ServiceOperatorKey> serviceOperatorKeyTypes
                = getServiceOperatorKeys();
        for (Encoder<?, ?> e : getEncoderRepository().getEncoders()) {
            if (e instanceof ProcedureEncoder) {
                ProcedureEncoder<?, ?> oe = (ProcedureEncoder<?, ?>) e;
                for (ServiceOperatorKey sokt : serviceOperatorKeyTypes) {
                    Set<String> rfs = oe.getSupportedProcedureDescriptionFormats(sokt.getService(), sokt.getVersion());
                    if (rfs != null) {
                        for (String rf : rfs) {
                            addProcedureDescriptionFormat(new ProcedureDescriptionFormatKey(sokt, rf));
                        }
                    }
                }
            }
        }
    }

    protected void addProcedureDescriptionFormat(ProcedureDescriptionFormatKey key) {
        Map<String, Set<String>> byService = this.procedureDescriptionFormats.get(key.getService());
        if (byService == null) {
            this.procedureDescriptionFormats.put(key.getService(), byService = Maps.newHashMap());
        }
        Set<String> byVersion = byService.get(key.getVersion());
        if (byVersion == null) {
            byService.put(key.getVersion(), byVersion = Sets.newHashSet());
        }
        byVersion.add(key.getProcedureDescriptionFormat());
    }

    public Map<ServiceOperatorKey, Set<String>> getSupportedProcedureDescriptionFormats() {
        Map<ServiceOperatorKey, Set<String>> map = Maps.newHashMap();
        for (ServiceOperatorKey sokt : getServiceOperatorKeys()) {
            map.put(sokt, getSupportedProcedureDescriptionFormats(sokt));
        }
        return map;
    }

    public Set<String> getSupportedProcedureDescriptionFormats(ServiceOperatorKey sokt) {
        return getSupportedProcedureDescriptionFormats(sokt.getService(), sokt.getVersion());
    }

    public Set<String> getSupportedProcedureDescriptionFormats(String service, String version) {
        Map<String, Set<String>> byService = procedureDescriptionFormats.get(service);
        if (byService == null) {
            return Collections.emptySet();
        }
        Set<String> rfs = byService.get(version);
        if (rfs == null) {
            return Collections.emptySet();
        }

        ServiceOperatorKey sokt = new ServiceOperatorKey(service, version);
        Set<String> result = Sets.newHashSet();
        for (String a : rfs) {
            ProcedureDescriptionFormatKey pdfkt
                    = new ProcedureDescriptionFormatKey(sokt, a);
            if (this.activation.isActive(pdfkt)) {
                result.add(a);
            }
        }
        return result;
    }

    public Map<ServiceOperatorKey, Set<String>> getAllProcedureDescriptionFormats() {
        Map<ServiceOperatorKey, Set<String>> map = Maps.newHashMap();
        for (ServiceOperatorKey sokt : getServiceOperatorKeys()) {
            map.put(sokt, getAllSupportedProcedureDescriptionFormats(sokt));
        }
        return map;
    }

    public Set<String> getAllSupportedProcedureDescriptionFormats(String service, String version) {
        Map<String, Set<String>> byService = procedureDescriptionFormats.get(service);
        if (byService == null) {
            return Collections.emptySet();
        }
        Set<String> rfs = byService.get(version);
        if (rfs == null) {
            return Collections.emptySet();
        }
        return Collections.unmodifiableSet(rfs);
    }

    public Set<String> getAllSupportedProcedureDescriptionFormats(ServiceOperatorKey sokt) {
        return getAllSupportedProcedureDescriptionFormats(sokt.getService(), sokt.getVersion());
    }

    @Override
    public void activate(ProcedureDescriptionFormatKey key) {
        this.activation.activate(key);
    }

    @Override
    public void deactivate(ProcedureDescriptionFormatKey key) {
        this.activation.deactivate(key);
    }

    @Override
    public boolean isActive(ProcedureDescriptionFormatKey key) {
        return this.activation.isActive(key);
    }

    @Override
    public void registerListener(ActivationListener<ProcedureDescriptionFormatKey> listener) {
        this.activation.registerListener(listener);
    }

    @Override
    public void deregisterListener(ActivationListener<ProcedureDescriptionFormatKey> listener) {
        this.activation.deregisterListener(listener);
    }

    @Deprecated
    public static ProcedureDescriptionFormatRepository getInstance() {
        return instance;
    }
}
