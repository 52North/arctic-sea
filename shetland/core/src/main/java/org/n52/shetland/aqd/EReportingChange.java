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
package org.n52.shetland.aqd;

import java.util.Objects;

import javax.annotation.Nullable;

import com.google.common.base.MoreObjects;
import com.google.common.base.Optional;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

public class EReportingChange {
    private boolean changed;
    private Optional<String> description;

    public EReportingChange(@Nullable String description) {
        this(true, description);
    }

    public EReportingChange() {
        this(false, null);
    }

    public EReportingChange(boolean changed) {
        this(changed, null);
    }

    @SuppressFBWarnings(value = "NP_PARAMETER_MUST_BE_NONNULL_BUT_MARKED_AS_NULLABLE")
    public EReportingChange(boolean changed, @Nullable String description) {
        this.changed = changed;
        this.description = Optional.fromNullable(description);
    }

    public boolean isChange() {
        return this.changed;
    }

    public Optional<String> getDescription() {
        return description;
    }

    @SuppressFBWarnings(value = "NP_PARAMETER_MUST_BE_NONNULL_BUT_MARKED_AS_NULLABLE")
    public void setDescription(@Nullable String description) {
        this.description = Optional.fromNullable(description);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getDescription());
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof EReportingChange &&
               java.util.Objects.equals(getDescription(), ((EReportingChange) obj).getDescription());
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("change", isChange())
                .add("description", getDescription())
                .toString();
    }

}
