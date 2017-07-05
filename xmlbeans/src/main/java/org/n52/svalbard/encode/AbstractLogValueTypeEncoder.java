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
package org.n52.svalbard.encode;

import org.n52.shetland.ogc.om.values.ProfileLevel;
import org.n52.svalbard.encode.exception.EncodingException;

import net.opengis.gwmlWell.x22.LogValueType;

public abstract class AbstractLogValueTypeEncoder<T> extends AbstractGroundWaterMLEncoder<T, ProfileLevel> {

    protected LogValueType encodeLogValue(ProfileLevel profileLevel) throws EncodingException {
        LogValueType lvt = LogValueType.Factory.newInstance();
        setFromDepth(lvt, profileLevel);
        setToDepth(lvt, profileLevel);
        setValue(lvt, profileLevel);
        return lvt;
    }

    private void setFromDepth(LogValueType lvt, ProfileLevel profileLevel) throws EncodingException {
        if (profileLevel.isSetLevelStart()) {
            lvt.addNewFromDepth().addNewQuantity().set(encodeSweCommon(profileLevel.getLevelStart()));
        }
    }

    private void setToDepth(LogValueType lvt, ProfileLevel profileLevel) throws EncodingException {
        if (profileLevel.isSetLevelEnd()) {
            lvt.addNewToDepth().addNewQuantity().set(encodeSweCommon(profileLevel.getLevelEnd()));
        }
    }

    private void setValue(LogValueType lvt, ProfileLevel profileLevel) throws EncodingException {
        if (profileLevel.isSetValue()) {
            lvt.addNewValue().addNewDataRecord().set(encodeSweCommon(profileLevel.valueAsDataRecord()));
        }
    }
}
