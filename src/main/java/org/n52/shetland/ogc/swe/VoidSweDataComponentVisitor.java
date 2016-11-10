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
package org.n52.shetland.ogc.swe;

import org.n52.shetland.ogc.ows.exception.OwsExceptionReport;
import org.n52.shetland.ogc.sensorML.elements.SmlPosition;
import org.n52.shetland.ogc.swe.simpleType.SweBoolean;
import org.n52.shetland.ogc.swe.simpleType.SweCategory;
import org.n52.shetland.ogc.swe.simpleType.SweCount;
import org.n52.shetland.ogc.swe.simpleType.SweCountRange;
import org.n52.shetland.ogc.swe.simpleType.SweObservableProperty;
import org.n52.shetland.ogc.swe.simpleType.SweQuantity;
import org.n52.shetland.ogc.swe.simpleType.SweQuantityRange;
import org.n52.shetland.ogc.swe.simpleType.SweText;
import org.n52.shetland.ogc.swe.simpleType.SweTime;
import org.n52.shetland.ogc.swe.simpleType.SweTimeRange;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public abstract class VoidSweDataComponentVisitor implements SweDataComponentVisitor<Void> {

    @Override
    public Void visit(SweField component)
            throws OwsExceptionReport {
        _visit(component);
        return null;
    }

    @Override
    public Void visit(SweDataRecord component)
            throws OwsExceptionReport {
        _visit(component);
        return null;
    }

    @Override
    public Void visit(SweDataArray component)
            throws OwsExceptionReport {
        _visit(component);
        return null;
    }

    @Override
    public Void visit(SweCount component)
            throws OwsExceptionReport {
        _visit(component);
        return null;
    }

    @Override
    public Void visit(SweCountRange component)
            throws OwsExceptionReport {
        _visit(component);
        return null;
    }

    @Override
    public Void visit(SweBoolean component)
            throws OwsExceptionReport {
        _visit(component);
        return null;
    }

    @Override
    public Void visit(SweCategory component)
            throws OwsExceptionReport {
        _visit(component);
        return null;
    }

    @Override
    public Void visit(SweObservableProperty component)
            throws OwsExceptionReport {
        _visit(component);
        return null;
    }

    @Override
    public Void visit(SweQuantity component)
            throws OwsExceptionReport {
        _visit(component);
        return null;
    }

    @Override
    public Void visit(SweQuantityRange component)
            throws OwsExceptionReport {
        _visit(component);
        return null;
    }

    @Override
    public Void visit(SweText component)
            throws OwsExceptionReport {
        _visit(component);
        return null;
    }

    @Override
    public Void visit(SweTime component)
            throws OwsExceptionReport {
        _visit(component);
        return null;
    }

    @Override
    public Void visit(SweTimeRange component)
            throws OwsExceptionReport {
        _visit(component);
        return null;
    }

    @Override
    public Void visit(SweEnvelope component)
            throws OwsExceptionReport {
        _visit(component);
        return null;
    }

    @Override
    public Void visit(SweVector component)
            throws OwsExceptionReport {
        _visit(component);
        return null;
    }

    @Override
    public Void visit(SweSimpleDataRecord component)
            throws OwsExceptionReport {
        _visit(component);
        return null;
    }

    @Override
    public Void visit(SmlPosition component)
            throws OwsExceptionReport {
        _visit(component);
        return null;
    }

    protected abstract void _visit(SweField component)
            throws OwsExceptionReport;

    protected abstract void _visit(SweDataRecord component)
            throws OwsExceptionReport;

    protected abstract void _visit(SweDataArray component)
            throws OwsExceptionReport;

    protected abstract void _visit(SweCount component)
            throws OwsExceptionReport;

    protected abstract void _visit(SweCountRange component)
            throws OwsExceptionReport;

    protected abstract void _visit(SweBoolean component)
            throws OwsExceptionReport;

    protected abstract void _visit(SweCategory component)
            throws OwsExceptionReport;

    protected abstract void _visit(SweObservableProperty component)
            throws OwsExceptionReport;

    protected abstract void _visit(SweQuantity component)
            throws OwsExceptionReport;

    protected abstract void _visit(SweQuantityRange component)
            throws OwsExceptionReport;

    protected abstract void _visit(SweText component)
            throws OwsExceptionReport;

    protected abstract void _visit(SweTime component)
            throws OwsExceptionReport;

    protected abstract void _visit(SweTimeRange component)
            throws OwsExceptionReport;

    protected abstract void _visit(SweEnvelope component)
            throws OwsExceptionReport;

    protected abstract void _visit(SweVector component)
            throws OwsExceptionReport;

    protected abstract void _visit(SweSimpleDataRecord component)
            throws OwsExceptionReport;

    protected abstract void _visit(SmlPosition component)
            throws OwsExceptionReport;

}
