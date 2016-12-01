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
package org.n52.shetland.ogc.wps;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import com.google.common.base.Strings;

/**
 * This attribute type is used to specify process control options. The WPS
 * specification only defines "execute-sync" and "execute-async", each with an
 * associated execution protocol. Extensions may specify additional control
 * options, such as "dimiss" which is defined in the WPS dismiss extension.
 *
 * @author Christian Autermann
 */
public class JobControlOption implements Comparable<JobControlOption> {

    private static final JobControlOption SYNC_EXECUTE
            = new JobControlOption("sync-execute");
    private static final JobControlOption ASYNC_EXECUTE
            = new JobControlOption("async-execute");
    private static final JobControlOption DISMISS
            = new JobControlOption("dismiss");
    private static final Comparator<JobControlOption> COMPARATOR
            = Comparator.nullsLast(Comparator.comparing(JobControlOption::getValue));

    private final String value;

    public JobControlOption(String value) {
        this.value = Objects.requireNonNull(Strings.emptyToNull(value));
    }

    public String getValue() {
        return value;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.value);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (obj instanceof JobControlOption) {
            final JobControlOption other = (JobControlOption) obj;
            return Objects.equals(this.getValue(), other.getValue());
        }
        return false;
    }

    @Override
    public String toString() {
        return getValue();
    }

    @Override
    public int compareTo(JobControlOption o) {
        return COMPARATOR.compare(this, o);
    }

    public static JobControlOption sync() {
        return SYNC_EXECUTE;
    }

    public static JobControlOption async() {
        return ASYNC_EXECUTE;
    }

    public static JobControlOption dismiss() {
        return DISMISS;
    }

    public static Set<JobControlOption> defaultOptions() {
        return new HashSet<>(Arrays.asList(sync(), async(), dismiss()));
    }
}
