/*
 * Copyright 2016-2017 52°North Initiative for Geospatial Open Source
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
package org.n52.shetland.ogc.gml;

import java.net.URI;
import java.net.URISyntaxException;

import org.n52.shetland.ogc.AbstractCodeType;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

/**
 * Class represents a GML conform CodeType element
 *
 * @since 1.0.0
 *
 */
public class CodeType extends AbstractCodeType implements Cloneable {

    public CodeType(final String value) {
        super(value);
    }

    @Deprecated
    public CodeType(final String value, final String codespace) throws URISyntaxException {
        super(value, new URI(codespace));
    }

    public CodeType(final String value, final URI codespace) {
        super(value, codespace);
    }

    @Override
    public CodeType clone() throws CloneNotSupportedException {
        return new CodeType(getValue(), getCodeSpace());
    }


    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("value", getValue())
                .add("codeSpace", getCodeSpace())
                .toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof CodeType) {
            CodeType that = (CodeType) obj;
            return Objects.equal(getValue(), that.getValue()) &&
                   Objects.equal(getCodeSpace(), that.getCodeSpace());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getValue(), getCodeSpace());
    }

}
