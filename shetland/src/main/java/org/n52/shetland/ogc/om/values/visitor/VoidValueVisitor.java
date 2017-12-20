/*
 * Copyright 2015-2017 52°North Initiative for Geospatial Open Source
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
package org.n52.shetland.ogc.om.values.visitor;

import org.n52.shetland.ogc.om.values.BooleanValue;
import org.n52.shetland.ogc.om.values.CategoryValue;
import org.n52.shetland.ogc.om.values.ComplexValue;
import org.n52.shetland.ogc.om.values.CountValue;
import org.n52.shetland.ogc.om.values.GeometryValue;
import org.n52.shetland.ogc.om.values.HrefAttributeValue;
import org.n52.shetland.ogc.om.values.NilTemplateValue;
import org.n52.shetland.ogc.om.values.QuantityValue;
import org.n52.shetland.ogc.om.values.ReferenceValue;
import org.n52.shetland.ogc.om.values.SweDataArrayValue;
import org.n52.shetland.ogc.om.values.TLVTValue;
import org.n52.shetland.ogc.om.values.TVPValue;
import org.n52.shetland.ogc.om.values.TextValue;
import org.n52.shetland.ogc.om.values.UnknownValue;
import org.n52.shetland.ogc.om.values.XmlValue;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public abstract class VoidValueVisitor<E extends Exception>
        implements ValueVisitor<Void, E> {

    @Override
    public Void visit(BooleanValue value)
            throws E {
        _visit(value);
        return null;
    }

    @Override
    public Void visit(CategoryValue value)
            throws E {
        _visit(value);
        return null;
    }

    @Override
    public Void visit(ComplexValue value)
            throws E {
        _visit(value);
        return null;
    }

    @Override
    public Void visit(CountValue value)
            throws E {
        _visit(value);
        return null;
    }

    @Override
    public Void visit(GeometryValue value)
            throws E {
        _visit(value);
        return null;
    }

    @Override
    public Void visit(HrefAttributeValue value)
            throws E {
        _visit(value);
        return null;
    }

    @Override
    public Void visit(NilTemplateValue value)
            throws E {
        _visit(value);
        return null;
    }

    @Override
    public Void visit(QuantityValue value)
            throws E {
        _visit(value);
        return null;
    }

    @Override
    public Void visit(ReferenceValue value)
            throws E {
        _visit(value);
        return null;
    }

    @Override
    public Void visit(SweDataArrayValue value)
            throws E {
        _visit(value);
        return null;
    }

    @Override
    public Void visit(TVPValue value)
            throws E {
        _visit(value);
        return null;
    }

    @Override
    public Void visit(TLVTValue value)
            throws E {
        _visit(value);
        return null;
    }

    @Override
    public Void visit(TextValue value)
            throws E {
        _visit(value);
        return null;
    }

    @Override
    public Void visit(UnknownValue value)
            throws E {
        _visit(value);
        return null;
    }

    @Override
    public Void visit(XmlValue<?> value)
            throws E {
        _visit(value);
        return null;
    }

    protected abstract void _visit(BooleanValue value)
            throws E;

    protected abstract void _visit(CategoryValue value)
            throws E;

    protected abstract void _visit(ComplexValue value)
            throws E;

    protected abstract void _visit(CountValue value)
            throws E;

    protected abstract void _visit(GeometryValue value)
            throws E;

    protected abstract void _visit(HrefAttributeValue value)
            throws E;

    protected abstract void _visit(NilTemplateValue value)
            throws E;

    protected abstract void _visit(QuantityValue value)
            throws E;

    protected abstract void _visit(ReferenceValue value)
            throws E;

    protected abstract void _visit(SweDataArrayValue value)
            throws E;

    protected abstract void _visit(TVPValue value)
            throws E;

    protected abstract void _visit(TLVTValue value)
            throws E;

    protected abstract void _visit(TextValue value)
            throws E;

    protected abstract void _visit(UnknownValue value)
            throws E;

    protected abstract void _visit(XmlValue<?> value)
            throws E;
}
