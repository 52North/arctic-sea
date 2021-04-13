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
package org.n52.iceland.convert;

/**
 * Defines the types of modification the {@link RequestResponseModifier} performs
 *
 * @author <a href="mailto:c.hollmann@52north.org">Carsten Hollmann</a>
 * @since 1.0.0
 *
 */
public class RequestResponseModifierFacilitator {

    private boolean merger;
    private boolean splitter;
    private boolean adderRemover;

    /**
     * @param merger if this is a merger
     *
     * @return this
     */
    public RequestResponseModifierFacilitator setMerger(boolean merger) {
        this.merger = merger;
        return this;
    }

    /**
     * @return <code>true</code>, if the {@link RequestResponseModifier} merges values, e.g. observations
     */
    public boolean isMerger() {
        return merger;
    }

    /**
     * @param splitter if this is a splitter
     *
     * @return this
     */
    public RequestResponseModifierFacilitator setSplitter(boolean splitter) {
        this.splitter = splitter;
        return this;
    }

    /**
     * @return <code>true</code>, if the {@link RequestResponseModifier} splits values, e.g. a SweDataArray observation
     *         into single observations
     */
    public boolean isSplitter() {
        return splitter;
    }

    /**
     * @param adderRemover if this is a adder or remover
     *
     * @return this
     */
    public RequestResponseModifierFacilitator setAdderRemover(boolean adderRemover) {
        this.adderRemover = adderRemover;
        return this;
    }

    /**
     * @return <code>true</code>, if the {@link RequestResponseModifier} adds/removes values, e.g. add/remove prefixes
     *         to identifier
     */
    public boolean isAdderRemover() {
        return adderRemover;
    }

}
