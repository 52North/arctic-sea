/*
 * Copyright 2015-2018 52°North Initiative for Geospatial Open Source
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
package org.n52.shetland.inspire.ompr;

import java.util.List;

import org.n52.shetland.inspire.base.Identifier;
import org.n52.shetland.inspire.base2.DocumentCitation;
import org.n52.shetland.inspire.base2.RelatedParty;
import org.n52.shetland.ogc.gml.AbstractFeature;
import org.n52.shetland.ogc.gml.CodeType;
import org.n52.shetland.util.CollectionHelper;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;

public class Process extends AbstractFeature {

    /**
     * 0..1 name
     */

    /**
     * 1..1
     */
    private String type;

    /**
     * 0..*
     */
    private List<DocumentCitation> documentation = Lists.newArrayList();

    /**
     * 0..*
     */
    private List<ProcessParameter> processParameter = Lists.newArrayList();

    /**
     * 1..*
     */
    private List<RelatedParty> responsibleParty = Lists.newArrayList();

    public Process() {
        super("");
        setDefaultElementEncoding(InspireOMPRConstants.NS_OMPR_30);
    }

    public Identifier getInspireId() {
        return new Identifier(getIdentifierCodeWithAuthority());
    }

    /**
     * @param name the name to set
     */
    public Process setName(String name) {
        super.setName(new CodeType(name));
        return this;
    }

    /**
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public Process setType(String type) {
        this.type = type;
        return this;
    }

    public boolean isSetType() {
        return !Strings.isNullOrEmpty(getType());
    }

    /**
     * @return the documentation
     */
    public List<DocumentCitation> getDocumentation() {
        return documentation;
    }

    /**
     * @param documentation the documentation to set
     */
    public Process setDocumentation(List<DocumentCitation> documentation) {
        this.documentation.clear();
        if (documentation != null) {
            this.documentation.addAll(documentation);
        }
        return this;
    }

    /**
     * @param documentation the documentation to add
     */
    public Process addDocumentation(DocumentCitation documentation) {
        if (documentation != null) {
            this.documentation.add(documentation);
        }
        return this;
    }

    public boolean isSetDocumentation() {
        return CollectionHelper.isNotEmpty(getDocumentation());
    }

    /**
     * @return the processParameter
     */
    public List<ProcessParameter> getProcessParameter() {
        return processParameter;
    }

    /**
     * @param processParameter the processParameter to set
     */
    public Process setProcessParameter(List<ProcessParameter> processParameter) {
        this.processParameter.clear();
        if (processParameter != null) {
            this.processParameter.addAll(processParameter);
        }
        return this;
    }

    /**
     * @param processParameter the processParameter to add
     */
    public Process addProcessParameter(ProcessParameter processParameter) {
        if (processParameter != null) {
            this.processParameter.add(processParameter);
        }
        return this;
    }

    public boolean isSetProcessParameter() {
        return CollectionHelper.isNotEmpty(getProcessParameter());
    }

    /**
     * @return the responsibleParty
     */
    public List<RelatedParty> getResponsibleParty() {
        return responsibleParty;
    }

    /**
     * @param responsibleParty the responsibleParty to set
     */
    public Process setResponsibleParty(List<RelatedParty> responsibleParty) {
        this.responsibleParty.clear();
        if (responsibleParty != null) {
            this.responsibleParty.addAll(responsibleParty);
        }
        return this;
    }

    /**
     * @param responsibleParty the responsibleParty to add
     */
    public Process addResponsibleParty(RelatedParty responsibleParty) {
        if (responsibleParty != null) {
            this.responsibleParty.add(responsibleParty);
        }
        return this;
    }


    public boolean isSetResponsibleParty() {
        return CollectionHelper.isNotEmpty(getResponsibleParty());
    }

}
