/*
 * Copyright 2015-${currentYear} 52°North Spatial Information Research GmbH
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

import org.n52.shetland.ogc.swe.SweAbstractDataComponent;
import org.n52.shetland.ogc.swe.SweConstants.SweDataComponentType;
import org.n52.shetland.ogc.swe.SweDataComponentVisitor;
import org.n52.shetland.ogc.swe.SweDataRecord;
import org.n52.shetland.ogc.swe.SweDataStream;
import org.n52.shetland.ogc.swe.VoidSweDataComponentVisitor;

/**
 * @author <a href="mailto:e.h.juerrens@52north.org">Eike Hinderk
 *         J&uuml;rrens</a>
 *
 * @since 1.0.0
 */
public class SmlDataInterface extends SweAbstractDataComponent {

    private SweDataStream sweDataStream;

    private SweDataRecord inputParameters;

    @Override
    public SweDataComponentType getDataComponentType() {
        return null;
    }

    public SweDataStream getData() {
        return sweDataStream;
    }

    public void setData(SweDataStream sweDataStream) {
        this.sweDataStream = sweDataStream;
    }

    public Boolean isSetInterfaceParameters() {
        return inputParameters != null;
    }

    public SweDataRecord getInterfaceParameters() {
        return inputParameters;
    }

    public void setInputParameters(SweDataRecord inputParameters) {
        this.inputParameters = inputParameters;
    }

    @Override
    public <T, X extends Throwable> T accept(SweDataComponentVisitor<T, X> visitor) throws X {
        return visitor.visit(this);
    }

    @Override
    public <X extends Throwable> void accept(VoidSweDataComponentVisitor<X> visitor) throws X {
        visitor.visit(this);
    }

    @Override
    public SmlDataInterface copy() {
        SmlDataInterface copy = new SmlDataInterface();
        copyValueTo(copy);
        copy.setData(getData());
        if (isSetInterfaceParameters()) {
            copy.setInputParameters(getInterfaceParameters().copy());
        }
        return copy;
    }

}
