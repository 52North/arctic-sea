/*
 * Copyright 2015-2022 52°North Spatial Information Research GmbH
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
package org.n52.shetland.w3c.wsdl;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeSet;

import javax.xml.namespace.QName;

public class Definitions extends AbstractDocumentedWsdl {

    private String targetNamespace;
    private Map<String, String> namespaces = new LinkedHashMap<>();
    private Collection<Message> messages = new TreeSet<>();
    private Collection<Service> services = new TreeSet<>();
    private Collection<Import> imports = new TreeSet<>();
    private Collection<PortType> portTypes = new TreeSet<>();
    private Collection<Types> types = new TreeSet<>();
    private Collection<Binding> bindings = new TreeSet<>();

    @Override
    public QName getQName() {
        return WSDLConstants.WSDLQNames.QN_WSDL_DEFINITIONS;
    }

    public Definitions setTargetNamespace(String targetNamespace) {
        this.targetNamespace = targetNamespace;
        return this;
    }

    public String getTargetNamespace() {
        return targetNamespace;
    }

    public boolean isSetTargetNamespace() {
        return getTargetNamespace() != null && !getTargetNamespace().isEmpty();
    }

    public Definitions addNamespace(String prefix, String namespace) {
        if (prefix != null && !prefix.isEmpty() && namespace != null && !namespace.isEmpty()) {
            this.namespaces.put(prefix, namespace);
        }
        return this;
    }

    public Definitions addNamespaces(Map<String, String> namespaces) {
        if (namespaces != null && !namespaces.isEmpty()) {
            namespaces.entrySet().forEach(n -> {
                addNamespace(n.getKey(), n.getValue());
            });
        }
        return this;
    }

    public Definitions setNamespaces(Map<String, String> namespaces) {
        this.namespaces.clear();
        return addNamespaces(namespaces);
    }

    public Map<String, String> getNamespaces() {
        return namespaces;
    }

    public boolean isSetNamespaces() {
        return !getNamespaces().isEmpty();
    }

    public Definitions addMessage(Message message) {
        if (message != null) {
            this.messages.add(message);
        }
        return this;
    }

    public Definitions addMessages(Collection<Message> messages) {
        if (messages != null) {
            messages.forEach(p -> {
                addMessage(p);
            });
        }
        return this;
    }

    public Definitions setMessages(Collection<Message> messages) {
        this.messages.clear();
        return addMessages(messages);
    }

    public Collection<Message> getMessages() {
        return messages;
    }

    public boolean isSetMessages() {
        return !getMessages().isEmpty();
    }

    public Definitions addService(Service service) {
        if (service != null) {
            this.services.add(service);
        }
        return this;
    }

    public Definitions addServices(Collection<Service> services) {
        if (services != null) {
            services.forEach(p -> {
                addService(p);
            });
        }
        return this;
    }

    public Definitions setServices(Collection<Service> services) {
        this.services.clear();
        return addServices(services);
    }

    public Collection<Service> getServices() {
        return services;
    }

    public boolean isSetServices() {
        return !getServices().isEmpty();
    }

    public Definitions addImport(Import imp) {
        if (imp != null) {
            this.imports.add(imp);
        }
        return this;
    }

    public Definitions addImports(Collection<Import> imports) {
        if (imports != null) {
            imports.forEach(p -> {
                addImport(p);
            });
        }
        return this;
    }

    public Definitions setImports(Collection<Import> imports) {
        this.imports.clear();
        return addImports(imports);
    }

    public Collection<Import> getImports() {
        return imports;
    }

    public boolean isSetImports() {
        return !getImports().isEmpty();
    }

    public Definitions addPortType(PortType portType) {
        if (portType != null) {
            this.portTypes.add(portType);
        }
        return this;
    }

    public Definitions addPortTypes(Collection<PortType> portTypes) {
        if (portTypes != null) {
            portTypes.forEach(p -> {
                addPortType(p);
            });
        }
        return this;
    }

    public Definitions setPortTypes(Collection<PortType> portTypes) {
        this.portTypes.clear();
        return addPortTypes(portTypes);
    }

    public Collection<PortType> getPortTypes() {
        return portTypes;
    }

    public boolean isSetPortTypes() {
        return !getPortTypes().isEmpty();
    }

    public Definitions addType(Types type) {
        if (type != null) {
            this.types.add(type);
        }
        return this;
    }

    public Definitions addTypes(Collection<Types> types) {
        if (types != null) {
            types.forEach(p -> {
                addType(p);
            });
        }
        return this;
    }

    public Definitions setTypes(Collection<Types> types) {
        this.types.clear();
        return addTypes(types);
    }

    public Collection<Types> getTypes() {
        return types;
    }

    public boolean isSetTypes() {
        return !getTypes().isEmpty();
    }

    public Definitions addBinding(Binding binding) {
        if (binding != null) {
            this.bindings.add(binding);
        }
        return this;
    }

    public Definitions addBindings(Collection<Binding> bindings) {
        if (bindings != null) {
            bindings.forEach(p -> {
                addBinding(p);
            });
        }
        return this;
    }

    public Definitions setBindings(Collection<Binding> bindings) {
        this.bindings.clear();
        return addBindings(bindings);
    }

    public Collection<Binding> getBindings() {
        return bindings;
    }

    public boolean isSetBindings() {
        return !getBindings().isEmpty();
    }
}
