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
package org.n52.svalbard.odata;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.olingo.commons.api.edm.EdmPrimitiveTypeKind;
import org.apache.olingo.commons.api.edm.FullQualifiedName;
import org.apache.olingo.commons.api.edm.provider.CsdlAbstractEdmProvider;
import org.apache.olingo.commons.api.edm.provider.CsdlComplexType;
import org.apache.olingo.commons.api.edm.provider.CsdlEntityContainer;
import org.apache.olingo.commons.api.edm.provider.CsdlEntityContainerInfo;
import org.apache.olingo.commons.api.edm.provider.CsdlEntitySet;
import org.apache.olingo.commons.api.edm.provider.CsdlEntityType;
import org.apache.olingo.commons.api.edm.provider.CsdlProperty;
import org.apache.olingo.commons.api.edm.provider.CsdlPropertyRef;
import org.apache.olingo.commons.api.edm.provider.CsdlSchema;
import org.apache.olingo.commons.api.ex.ODataException;

import org.n52.shetland.ogc.gml.GmlConstants;
import org.n52.shetland.ogc.om.OmConstants;
import org.n52.shetland.ogc.sos.Sos2Constants;

/**
 * {@code CsdlEdmProvider} for O&amp;M Observations.
 *
 * This provider establishes the following properties:
 * <table>
 * <thead>
 * <tr><th>Name</th><th>Type</th><th>Value reference</th></tr>
 * </thead>
 * <tbody>
 * <tr><td>value</td><td>string</td><td>om:result</td></tr>
 * <tr><td>values</td><td>string</td><td>om:result</td></tr>
 * <tr><td>countValue</td><td>long</td><td>om:result</td></tr>
 * <tr><td>countValues</td><td>long</td><td>om:result</td></tr>
 * <tr><td>numericValue</td><td>double</td><td>om:result</td></tr>
 * <tr><td>numericValues</td><td>double</td><td>om:result</td></tr>
 * <tr><td>quantity</td><td>double</td><td>om:result</td></tr>
 * <tr><td>quantities</td><td>double</td><td>om:result</td></tr>
 * <tr><td>textValue</td><td>string</td><td>om:result</td></tr>
 * <tr><td>textValues</td><td>string</td><td>om:result</td></tr>
 * <tr><td>samplingGeometry</td><td>geometry</td>
 * <td>http://www.opengis.net/req/omxml/2.0/data/samplingGeometry</td></tr>
 * <tr><td>featureOfInterest</td><td>string</td><td>om:featureOfInterest</td></tr>
 * <tr><td>featureOfInterest/id</td><td>string</td><td>om:featureOfInterest</td></tr>
 * <tr><td>featureOfInterest/shape</td><td>geometry</td><td>om:featureOfInterest/&#x2a;/sams:shape</td></tr>
 * <tr><td>id</td><td>string</td><td>gml:identifier</td></tr>
 * <tr><td>identifier</td><td>string</td><td>gml:identifier</td></tr>
 * <tr><td>observedProperty</td><td>string</td><td>om:observedProperty</td></tr>
 * <tr><td>procedure</td><td>string</td><td>om:procedure</td></tr>
 * <tr><td>offering</td><td>string</td><td>sos:offering</td></tr>
 * <tr><td>phenomenonTime</td><td>gml:AbstractTimeObject</td><td>om:phenomenonTime</td></tr>
 * <tr><td>resultTime</td><td>gml:AbstractTimeObject</td><td>om:resultTime</td></tr>
 * <tr><td>validTime</td><td>gml:TimePeriod</td><td>om:validTime</td></tr>
 * </tbody>
 * </table>
 *
 *
 * @author Christian Autermann
 */
public class ObservationCsdlEdmProvider extends CsdlAbstractEdmProvider {

    private static final String CONTAINER_NAME = "Container";
    private static final String NS_OM = OmConstants.NS_OM_PREFIX;
    private static final String NS_GML = GmlConstants.NS_GML_PREFIX;

    @Override
    public CsdlEntityContainer getEntityContainer() throws ODataException {
        CsdlEntityContainer entityContainer = new CsdlEntityContainer();
        entityContainer.setName(CONTAINER_NAME);
        entityContainer.setEntitySets(Collections
                .singletonList(getEntitySet(FQN.CONTAINER, Prop.OBSERVATION_COLLECTION)));
        return entityContainer;
    }

    @Override
    public CsdlEntityContainerInfo getEntityContainerInfo(FullQualifiedName name) throws ODataException {
        if (name == null || name.equals(FQN.CONTAINER)) {
            return newEntityContainerInfo(FQN.CONTAINER);
        }
        return super.getEntityContainerInfo(name);
    }

    @Override
    public CsdlEntitySet getEntitySet(FullQualifiedName container, String name) throws ODataException {
        if (container.equals(FQN.CONTAINER)) {
            if (name.equals(Prop.OBSERVATION_COLLECTION)) {
                return newEntitySet(Prop.OBSERVATION_COLLECTION, FQN.OBSERVATION);
            }
        }
        return super.getEntitySet(container, name);
    }

    /**
     * Get the canonical value reference for the supplied property.
     *
     * @param property the property
     *
     * @return the value reference
     */
    public String mapProperty(String property) {
        if (property == null) {
            return null;
        }
        switch (property) {
            case Prop.VALUE:
            case Prop.VALUES:
            case Prop.COUNT_VALUE:
            case Prop.COUNT_VALUES:
            case Prop.NUMERIC_VALUE:
            case Prop.NUMERIC_VALUES:
            case Prop.TEXT_VALUE:
            case Prop.TEXT_VALUES:
            case Prop.QUANTITY:
            case Prop.QUANTITIES:
            case Prop.RESULT:
                return ValueReference.RESULT;

            case Prop.SAMPLING_GEOMETRY:
                return ValueReference.SAMPLING_GEOMETRY;

            case Prop.PHENOMENON_TIME:
                return ValueReference.PHENOMENON_TIME;

            case Prop.RESULT_TIME:
                return ValueReference.RESULT_TIME;

            case Prop.VALID_TIME:
                return ValueReference.VALID_TIME;

            case Prop.OBSERVED_PROPERTY:
                return ValueReference.OBSERVED_PROPERTY;

            case Prop.PROCEDURE:
                return ValueReference.PROCEDURE;

            case Prop.FEATURE_OF_INTEREST_ID:
            case Prop.FEATURE_OF_INTEREST:
                return ValueReference.FEATURE_OF_INTEREST;
            case Prop.FEATURE_OF_INTEREST_SHAPE:
                return ValueReference.FEATURE_OF_INTEREST_SHAPE;

            case Prop.OFFERING:
                return ValueReference.OFFERING;

            case Prop.ID:
            case Prop.IDENTIFIER:
                return ValueReference.IDENTIFIER;

            default:
                return property;
        }
    }

    @Override
    public CsdlEntityType getEntityType(FullQualifiedName name) throws ODataException {
        if (name.equals(FQN.OBSERVATION)) {
            return new CsdlEntityType()
                    .setName(FQN.OBSERVATION.getName())
                    .setKey(Collections.singletonList(newPropertyRef(Prop.IDENTIFIER)))
                    .setOpenType(true)
                    .setProperties(Arrays.asList(
                            newProperty(Prop.IDENTIFIER, FQN.STRING),
                            newProperty(Prop.ID, FQN.STRING),
                            newProperty(Prop.VALUE, FQN.STRING),
                            newProperty(Prop.VALUES, FQN.STRING),
                            newProperty(Prop.COUNT_VALUE, FQN.COUNT),
                            newProperty(Prop.COUNT_VALUES, FQN.COUNT),
                            newProperty(Prop.NUMERIC_VALUE, FQN.QUANTITY),
                            newProperty(Prop.NUMERIC_VALUES, FQN.QUANTITY),
                            newProperty(Prop.QUANTITY, FQN.QUANTITY),
                            newProperty(Prop.QUANTITIES, FQN.QUANTITY),
                            newProperty(Prop.TEXT_VALUE, FQN.STRING),
                            newProperty(Prop.TEXT_VALUES, FQN.STRING),
                            newProperty(Prop.RESULT, FQN.STRING),
                            newProperty(Prop.PHENOMENON_TIME, FQN.ABSTRACT_TIME_OBJECT),
                            newProperty(Prop.RESULT_TIME, FQN.ABSTRACT_TIME_OBJECT),
                            newProperty(Prop.VALID_TIME, FQN.TIME_PERIOD),
                            newProperty(Prop.OBSERVED_PROPERTY, FQN.STRING),
                            newProperty(Prop.PROCEDURE, FQN.STRING),
                            newProperty(Prop.FEATURE_OF_INTEREST, FQN.FEATURE_OF_INTEREST),
                            newProperty(Prop.SAMPLING_GEOMETRY, FQN.GEOMETRY_POINT),
                            newProperty(Prop.OFFERING, FQN.STRING)));
        }
        return super.getEntityType(name);
    }

    @Override
    public List<CsdlSchema> getSchemas() throws ODataException {
        return Collections.singletonList(new CsdlSchema()
                .setNamespace(NS_OM)
                .setComplexTypes(Arrays.asList(getComplexType(FQN.ABSTRACT_TIME_OBJECT),
                                               getComplexType(FQN.TIME_INSTANT),
                                               getComplexType(FQN.TIME_PERIOD),
                                               getComplexType(FQN.RESULT),
                                               getComplexType(FQN.FEATURE_OF_INTEREST)))
                .setEntityTypes(Arrays.asList(getEntityType(FQN.OBSERVATION)))
                .setEntityContainer(getEntityContainer()));
    }

    @Override
    public CsdlComplexType getComplexType(FullQualifiedName name) throws ODataException {
        if (name.equals(FQN.ABSTRACT_TIME_OBJECT)) {
            return newAbstractComplexType(FQN.ABSTRACT_TIME_OBJECT);
        } else if (name.equals(FQN.TIME_INSTANT)) {
            return newComplexType(FQN.TIME_INSTANT, FQN.ABSTRACT_TIME_OBJECT);
        } else if (name.equals(FQN.TIME_PERIOD)) {
            return newComplexType(FQN.TIME_PERIOD,
                                  FQN.ABSTRACT_TIME_OBJECT,
                                  newProperty(Prop.EN_BEGIN_POSITION, FQN.DATE_TIME),
                                  newProperty(Prop.EN_END_POSITION, FQN.DATE_TIME));
        } else if (name.equals(FQN.FEATURE_OF_INTEREST)) {
            return newComplexType(FQN.FEATURE_OF_INTEREST,
                                  newProperty(Prop.ID, FQN.STRING),
                                  newProperty(Prop.SHAPE, FQN.GEOMETRY_POINT));
        } else if (name.equals(FQN.RESULT)) {
            return newAbstractComplexType(FQN.RESULT);
        } else {
            return super.getComplexType(name);
        }
    }

    private static CsdlProperty newProperty(String name, FullQualifiedName type) {
        return new CsdlProperty().setName(name).setType(type);
    }

    private static CsdlEntitySet newEntitySet(String name, FullQualifiedName type) {
        return new CsdlEntitySet().setName(name).setType(type);
    }

    private static CsdlEntityContainerInfo newEntityContainerInfo(FullQualifiedName name) {
        return new CsdlEntityContainerInfo().setContainerName(name);
    }

    private static CsdlPropertyRef newPropertyRef(String name) {
        return new CsdlPropertyRef().setName(name);
    }

    private static CsdlComplexType newAbstractComplexType(FullQualifiedName name, CsdlProperty... properties) {
        return new CsdlComplexType().setOpenType(true).setAbstract(true).setName(name.getName()).setProperties(Arrays
                .asList(properties));
    }

    private static CsdlComplexType newComplexType(FullQualifiedName name, CsdlProperty... properties) {
        return new CsdlComplexType().setOpenType(true).setAbstract(false).setName(name.getName()).setProperties(Arrays
                .asList(properties));
    }

    private static CsdlComplexType newComplexType(FullQualifiedName name, FullQualifiedName base,
                                                  CsdlProperty... properties) {
        return newComplexType(name, properties).setBaseType(base);
    }

    /**
     * Fully qualified name constants.
     */
    private interface FQN {
        FullQualifiedName CONTAINER = new FullQualifiedName(NS_OM, CONTAINER_NAME);
        FullQualifiedName OBSERVATION = new FullQualifiedName(NS_OM, Prop.OBSERVATION);
        FullQualifiedName TIME_PERIOD = new FullQualifiedName(NS_OM, Prop.TIME_PERIOD);
        FullQualifiedName ABSTRACT_TIME_OBJECT = new FullQualifiedName(NS_GML, Prop.ABSTRACT_TIME_OBJECT);
        FullQualifiedName TIME_INSTANT = new FullQualifiedName(NS_OM, Prop.TIME_INSTANT);
        FullQualifiedName RESULT = new FullQualifiedName(NS_OM, Prop.RESULT);
        FullQualifiedName FEATURE_OF_INTEREST = new FullQualifiedName(NS_OM, Prop.FEATURE_OF_INTEREST);
        FullQualifiedName STRING = EdmPrimitiveTypeKind.String.getFullQualifiedName();
        FullQualifiedName DATE_TIME = EdmPrimitiveTypeKind.String.getFullQualifiedName();
        FullQualifiedName QUANTITY = EdmPrimitiveTypeKind.Decimal.getFullQualifiedName();
        FullQualifiedName COUNT = EdmPrimitiveTypeKind.Int64.getFullQualifiedName();
        FullQualifiedName GEOMETRY_POINT = EdmPrimitiveTypeKind.GeometryPoint.getFullQualifiedName();
    }

    /**
     * Property constants.
     */
    private interface Prop {
        String VALUE = "value";
        String VALUES = "values";
        String COUNT_VALUE = "countValue";
        String COUNT_VALUES = "countValues";
        String NUMERIC_VALUE = "numericValue";
        String NUMERIC_VALUES = "numericValues";
        String TEXT_VALUE = "textValue";
        String TEXT_VALUES = "textValues";
        String OFFERING = "offering";
        String SAMPLING_GEOMETRY = "samplingGeometry";
        String QUANTITIES = "quantities";
        String QUANTITY = "quantity";
        String ID = "id";
        String IDENTIFIER = GmlConstants.EN_IDENTIFIER;
        String RESULT = OmConstants.EN_RESULT;
        String PHENOMENON_TIME = OmConstants.EN_PHENOMENON_TIME;
        String RESULT_TIME = OmConstants.EN_RESULT_TIME;
        String VALID_TIME = OmConstants.EN_VALID_TIME;
        String PROCEDURE = OmConstants.EN_PROCEDURE;
        String FEATURE_OF_INTEREST = OmConstants.EN_FEATURE_OF_INTEREST;
        String OBSERVED_PROPERTY = OmConstants.EN_OBSERVED_PROPERTY;

        String OBSERVATION_COLLECTION = OmConstants.EN_OBSERVATION_COLLECTION;
        String OBSERVATION = OmConstants.EN_OBSERVATION;
        String ABSTRACT_TIME_OBJECT = GmlConstants.EN_ABSTRACT_TIME_OBJECT;
        String TIME_INSTANT = GmlConstants.EN_TIME_INSTANT;
        String TIME_PERIOD = GmlConstants.EN_TIME_PERIOD;
        String SHAPE = "shape";
        String EN_END_POSITION = GmlConstants.EN_END_POSITION;
        String EN_BEGIN_POSITION = GmlConstants.EN_BEGIN_POSITION;

        String FEATURE_OF_INTEREST_ID = "featureOfInterest/id";
        String FEATURE_OF_INTEREST_SHAPE = "featureOfInterest/shape";

    }

    /**
     * Value reference constants.
     */
    private interface ValueReference {
        String RESULT = "om:result";
        String PHENOMENON_TIME = "om:phenomenonTime";
        String RESULT_TIME = "om:resultTime";
        String VALID_TIME = "om:validTime";
        String OBSERVED_PROPERTY = "om:observedProperty";
        String PROCEDURE = "om:procedure";
        String FEATURE_OF_INTEREST = "om:featureOfInterest";
        String OFFERING = "sos:offering";
        String IDENTIFIER = "gml:identifier";
        String FEATURE_OF_INTEREST_SHAPE = "om:featureOfInterest/*/sams:shape";
        String SAMPLING_GEOMETRY = Sos2Constants.VALUE_REFERENCE_SPATIAL_FILTERING_PROFILE;
    }

}
