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
package org.n52.iceland.binding.exi;

/**
 * Configuration settings for the EXI binding.
 *
 * @author <a href="mailto:e.h.juerrens@52north.org">Eike Hinderk J&uuml;rrens</a>
 *
 * @since 2.0.0
 *
 */
public interface EXISettings {
    String EXI_ALIGNMENT = "exi.alignment";
    String EXI_FIDELITY = "exi.fidelity";
    String EXI_FIDELITY_STRICT = "exi.fidelity.strict";
    String EXI_FIDELITY_DEFAULT = "exi.fidelity.default";
    String EXI_FIDELITY_SPECIFIC = "exi.fidelity.specific";
    String EXI_FIDELITY_COMMENTS = "exi.fidelity.comments";
    String EXI_FIDELITY_PROCESSING_INSTRUCTIONS = "exi.fidelity.processing.instructions";
    String EXI_FIDELITY_DTD = "exi.fidelity.dtd";
    String EXI_FIDELITY_PREFIXES = "exi.fidelity.prefixes";
    String EXI_FIDELITY_LEXICAL_VALUE = "exi.fidelity.lexical.value";
    String EXI_GRAMMAR = "exi.grammar";
    String EXI_GRAMMAR_SCHEMALESS = "exi.grammar.schemaless";
    String EXI_GRAMMAR_BASETYPES = "exi.grammar.basetypes";
    String EXI_GRAMMAR_SCHEMABASED = "exi.grammar.schemabased";
    String EXI_GRAMMAR_SCHEMA = "exi.grammar.schema";
    String EXI_GRAMMAR_SCHEMA_SOS_10 = "exi.grammar.schema.sos.10";
    String EXI_GRAMMAR_SCHEMA_SOS_20 = "exi.grammar.schema.sos.20";
}
