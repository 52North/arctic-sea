/*
 * Copyright 2015-2022 52°North Initiative for Geospatial Open Source
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
package org.n52.shetland.util;

import java.util.LinkedHashSet;
import java.util.Set;

import org.n52.faroe.annotation.Configurable;
import org.n52.faroe.annotation.Setting;

@Configurable
public class AqdHelper {

    private Set<Integer> validityFlags = new LinkedHashSet<>();
    private Set<Integer> verificationFlags = new LinkedHashSet<>();

    /**
     * @return the validityFlags
     */
    public Set<Integer> getValidityFlags() {
        return validityFlags;
    }

    /**
     * @param validityFlags the validityFlags to set
     */
    @Setting(value = EReportingSetting.EREPORTING_VALIDITY_FLAGS, required = false)
    public void setValidityFlags(String validityFlags) {
        this.validityFlags.clear();
        if (validityFlags != null && !validityFlags.isEmpty()) {
            this.validityFlags.addAll(JavaHelper.getIntegerSetFromString(validityFlags));
        }
    }

    public boolean isSetValidityFlags() {
        return CollectionHelper.isNotEmpty(getValidityFlags());
    }

    /**
     * @return the verificationFlags
     */
    public Set<Integer> getVerificationFlags() {
        return verificationFlags;
    }

    /**
     * @param verificationFlags the verificationFlags to set
     */
    @Setting(value = EReportingSetting.EREPORTING_VERIFICATION_FLAGS, required = false)
    public void setVerificationFlags(String verificationFlags) {
        this.verificationFlags.clear();
        if (verificationFlags != null && !verificationFlags.isEmpty()) {
            this.verificationFlags = JavaHelper.getIntegerSetFromString(verificationFlags);
        }
    }

    public boolean isSetVerificationFlags() {
        return CollectionHelper.isNotEmpty(getVerificationFlags());
    }

}