/*
 * Copyright 2015 52°North Initiative for Geospatial Open Source
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
package org.n52.iceland.util;

import java.util.Comparator;

import javax.xml.namespace.QName;

/**
 * Comparator for {@link QName}s.
 *
 * @author <a href="mailto:c.autermann@52north.org">Christian Autermann</a>
 * @since 1.0.0
 *
 */
public class QNameComparator implements Comparator<QName> {
    public static final Comparator<QName> INSTANCE =
            Comparator.nullsLast(Comparator.comparing(QName::getPrefix, Comparator.nullsLast(String::compareTo))
                  .thenComparing(Comparator.comparing(QName::getLocalPart, Comparator.nullsLast(String::compareTo))));

    @Override
    public int compare(QName o1, QName o2) {
        return INSTANCE.compare(o1, o2);
    }

}
