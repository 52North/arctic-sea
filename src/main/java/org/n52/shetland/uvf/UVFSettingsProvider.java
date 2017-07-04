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
package org.n52.shetland.uvf;

public interface UVFSettingsProvider {

   String DEFAULT_CRS_SETTING_KEY = "uvf.default.crs";
   String UVF_TIME_ZONE_SETTING_KEY = "uvf.timeZone";
   String UVF_LINE_ENDING_KEY = "uvf.lineEnding";

//   SettingDefinitionGroup GROUP =
//            new SettingDefinitionGroup()
//                            .setTitle("UVF Encoding")
//                        .setOrder(ORDER_10);
//
//    private static final StringSettingDefinition DEFAULT_CRS_SETTING_DEFINITION =
//            new StringSettingDefinition()
//            .setGroup(GROUP)
//            .setOrder(1)
//            .setKey(DEFAULT_CRS_SETTING_KEY)
//            .setDefaultValue("31466")
//            .setTitle("The default CRS EPSG code used in UVF response")
//            .setDescription(String.format("The default CRS EPSG code that is used if no swe extension is present in "
//                    + "the request that specifies one. Allowed values are: <tt>%s</tt>.", UVFConstants.ALLOWED_CRS));
//
//    private static final StringSettingDefinition UVF_TIME_ZONE__SETTING_DEFINITION =
//            new StringSettingDefinition()
//            .setGroup(GROUP)
//            .setOrder(2)
//            .setKey(UVF_TIME_ZONE_SETTING_KEY)
//            .setDefaultValue("CET")
//            .setTitle("Returned Time zone for the UVF encoding")
//            .setDescription("Define the time zone in which the time should be encoded in the UVF responce"
//                    + "Valid values are see <a href=\"http://docs.oracle.com/javase/8/docs/api/java/util/TimeZone.html\" target=\"_blank\">Java TimeZone</a>."
//                    + " Default is CET.");
//
//   ChoiceSettingDefinition UVF_LINE_ENDING_DEFINITION =
//            new ChoiceSettingDefinition()
//            .setGroup(GROUP)
//            .setOrder(3)
//            .setKey(UVF_LINE_ENDING_KEY)
//            .setDefaultValue(UVFConstants.LineEnding.Unix.name())
//            .setTitle("The line ending that should be used for UVF")
//            .addOption(UVFConstants.LineEnding.Windows.name(), UVFConstants.LineEnding.Windows.name())
//            .addOption(UVFConstants.LineEnding.Unix.name(), UVFConstants.LineEnding.Unix.name())
//            .addOption(UVFConstants.LineEnding.Mac.name(), UVFConstants.LineEnding.Mac.name());
//
//    @Override
//    public Set<SettingDefinition<?, ?>> getSettingDefinitions() {
//       return ImmutableSet.<SettingDefinition<?,?>>of(DEFAULT_CRS_SETTING_DEFINITION, UVF_TIME_ZONE__SETTING_DEFINITION, UVF_LINE_ENDING_DEFINITION);
//    }

}
