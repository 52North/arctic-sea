/*
 * Copyright 2015 52°North Initiative for Geospatial Open Source Software GmbH.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.n52.iceland.ogc.ows;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import java.net.URI;
import org.n52.iceland.ogc.AbstractCodeType;

/**
 *
 * @author <a href="mailto:d.nuest@52north.org">Daniel Nüst</a>
 */
public class OwsCodeType extends AbstractCodeType {

    public OwsCodeType(String value) {
        super(value);
    }

    public OwsCodeType(String value, URI codespace) {
        super(value, codespace);
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
        if (obj instanceof OwsCodeType) {
            OwsCodeType that = (OwsCodeType) obj;
            return Objects.equal(getValue(), that.getValue())
                    && Objects.equal(getCodeSpace(), that.getCodeSpace());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getValue(), getCodeSpace());
    }

}
