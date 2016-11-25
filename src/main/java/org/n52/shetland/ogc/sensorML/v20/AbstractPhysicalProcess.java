/*
 * Copyright 2016 52°North Initiative for Geospatial Open Source
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
package org.n52.shetland.ogc.sensorML.v20;

import org.n52.shetland.ogc.gml.ReferenceType;
import org.n52.shetland.ogc.sensorML.elements.SmlPosition;

/**
 * Class that represents SensorML 2.0 PhysicalProcess.
 *
 * @author <a href="mailto:c.hollmann@52north.org">Carsten Hollmann</a>
 * @since 4.2.0
 *
 */
public class AbstractPhysicalProcess extends DescribedObject {

    private SpatialFrame localReferenceFrame;
    private TemporalFrame localTimeFrame;

    // TODO extend to list and support other type (point, text, datarecord, ...)
    private SmlPosition position;
    private Object timePosition;

    /**
     * @return the attachedTo
     */
    public ReferenceType getAttachedTo() {
        // if parent procedures set
        return this.getParentProcedure();
    }

    /**
     * Set the attachedTo reference. It is automatically added to
     * parentProcedure list. If title is set, the title is used, else the href.
     *
     * @param attachedTo
     *                   the attachedTo to set
     */
    public void setAttachedTo(ReferenceType attachedTo) {
        this.setParentProcedure(attachedTo);
    }

    public boolean isSetAttachedTo() {
        return isSetParentProcedure();
    }

    /**
     * @return the localReferenceFrame
     */
    public SpatialFrame getLocalReferenceFrame() {
        return localReferenceFrame;
    }

    /**
     * @param localReferenceFrame
     *                            the localReferenceFrame to set
     */
    public void setLocalReferenceFrame(SpatialFrame localReferenceFrame) {
        this.localReferenceFrame = localReferenceFrame;
    }

    /**
     * @return the localTimeFrame
     */
    public TemporalFrame getLocalTimeFrame() {
        return localTimeFrame;
    }

    /**
     * @param localTimeFrame
     *                       the localTimeFrame to set
     */
    public void setLocalTimeFrame(TemporalFrame localTimeFrame) {
        this.localTimeFrame = localTimeFrame;
    }

    /**
     * @return the position
     */
    public SmlPosition getPosition() {
        return position;
    }

    /**
     * @param position
     *                 the position to set
     */
    public void setPosition(SmlPosition position) {
        this.position = position;
    }

    /**
     * @return the timePosition
     */
    public Object getTimePosition() {
        return timePosition;
    }

    /**
     * @param timePosition
     *                     the timePosition to set
     */
    public void setTimePosition(Object timePosition) {
        this.timePosition = timePosition;
    }

    public boolean isSetPosition() {
        return getPosition() != null;
    }

}
