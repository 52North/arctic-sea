/*
 * Copyright 2015-2020 52°North Initiative for Geospatial Open Source
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

import com.google.common.escape.Escaper;
import com.google.common.net.PercentEscaper;
import org.apache.olingo.commons.api.edm.Edm;
import org.apache.olingo.commons.api.edm.EdmEnumType;
import org.apache.olingo.commons.api.edm.EdmType;
import org.apache.olingo.commons.api.ex.ODataException;
import org.apache.olingo.commons.core.edm.EdmProviderImpl;
import org.apache.olingo.server.api.ODataApplicationException;
import org.apache.olingo.server.api.uri.UriInfo;
import org.apache.olingo.server.api.uri.UriInfoResource;
import org.apache.olingo.server.api.uri.UriResource;
import org.apache.olingo.server.api.uri.queryoption.expression.BinaryOperatorKind;
import org.apache.olingo.server.api.uri.queryoption.expression.Expression;
import org.apache.olingo.server.api.uri.queryoption.expression.ExpressionVisitException;
import org.apache.olingo.server.api.uri.queryoption.expression.ExpressionVisitor;
import org.apache.olingo.server.api.uri.queryoption.expression.Literal;
import org.apache.olingo.server.api.uri.queryoption.expression.Member;
import org.apache.olingo.server.api.uri.queryoption.expression.MethodKind;
import org.apache.olingo.server.api.uri.queryoption.expression.UnaryOperatorKind;
import org.apache.olingo.server.core.ODataImpl;
import org.apache.olingo.server.core.uri.parser.Parser;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.PrecisionModel;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;
import org.n52.shetland.ogc.filter.BinaryLogicFilter;
import org.n52.shetland.ogc.filter.ComparisonFilter;
import org.n52.shetland.ogc.filter.Filter;
import org.n52.shetland.ogc.filter.FilterConstants.BinaryLogicOperator;
import org.n52.shetland.ogc.filter.FilterConstants.ComparisonOperator;
import org.n52.shetland.ogc.filter.FilterConstants.SpatialOperator;
import org.n52.shetland.ogc.filter.FilterConstants.UnaryLogicOperator;
import org.n52.shetland.ogc.filter.SpatialFilter;
import org.n52.shetland.ogc.filter.UnaryLogicFilter;
import org.n52.svalbard.decode.Decoder;
import org.n52.svalbard.decode.DecoderKey;
import org.n52.svalbard.decode.exception.DecodingException;
import org.n52.svalbard.odata.core.expr.Expr;
import org.n52.svalbard.odata.core.expr.ExprVisitor;
import org.n52.svalbard.odata.core.expr.GeoValueExpr;
import org.n52.svalbard.odata.core.expr.MemberExpr;
import org.n52.svalbard.odata.core.expr.MethodCallExpr;
import org.n52.svalbard.odata.core.expr.StringValueExpr;
import org.n52.svalbard.odata.core.expr.TextExpr;
import org.n52.svalbard.odata.core.expr.UnaryExpr;
import org.n52.svalbard.odata.core.expr.arithmetic.NumericValueExpr;
import org.n52.svalbard.odata.core.expr.arithmetic.SimpleArithmeticExpr;
import org.n52.svalbard.odata.core.expr.BinaryExpr;
import org.n52.svalbard.odata.core.expr.bool.BooleanBinaryExpr;
import org.n52.svalbard.odata.core.expr.bool.BooleanExpr;
import org.n52.svalbard.odata.core.expr.bool.BooleanUnaryExpr;
import org.n52.svalbard.odata.core.expr.bool.ComparisonExpr;
import org.n52.svalbard.odata.core.expr.temporal.TimeValueExpr;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.CheckReturnValue;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * Class to parse OData-based {@code $filter} expression into FES filters. See
 * {@link ObservationCsdlEdmProvider} for the available properties, their types
 * and the resulting value references.
 *
 * @author Christian Autermann
 * @see ObservationCsdlEdmProvider
 */
public class ODataFesParser
        implements
        Decoder<Filter<?>, String> {
    private static final String METHOD_CONTAINS = "contains";
    private static final String METHOD_STARTS_WITH = "startswith";
    private static final String METHOD_ENDS_WITH = "endswith";
    private static final String METHOD_GEO_INTERSECTS = "geo.intersects";
    private static final Logger LOG = LoggerFactory.getLogger(ODataFesParser.class);
    private static final String PATH = "/ObservationCollection";
    private static final String FRAGMENT = "";
    private static final String BASE_URI = "/";
    private static final String GEOGRAPHY_TYPE = "geography";
    private static final String SRID_PREFIX = "SRID=";
    private static final String GEOMETRY_TYPE = "geometry";
    private static final String FEATURE_EQUALS = "featureOfInterest eq '";
    private final Escaper urlEscaper;
    private final Edm edm;
    private final Parser parser;
    private final ObservationCsdlEdmProvider csdlProvider;
    private final ODataImpl odata;

    /**
     * Creates a new {@code ODataFesParser}.
     */
    public ODataFesParser() {
        this.urlEscaper = new PercentEscaper("-_.*", false);
        this.odata = new ODataImpl();
        this.csdlProvider = new ObservationCsdlEdmProvider();
        this.edm = new EdmProviderImpl(this.csdlProvider);
        // >=4.2.0
        this.parser = new Parser(this.edm, this.odata);
        // >=4.0.0 <4.2.0
        // this.parser = new Parser();

    }

    @Override
    public Filter<?> decode(String objectToDecode)
            throws DecodingException {
        LOG.debug("Parsing filter: {}", objectToDecode);
        if (objectToDecode == null || objectToDecode.isEmpty()) {
            return null;
        }
        try {
            String encode = urlEscaper.escape(checkForGeoFitler(objectToDecode));
            // >=4.4.0
            UriInfo parseUri = parser.parseUri(PATH, "$filter=" + encode, FRAGMENT, BASE_URI);
            // >=4.2.0 <4.4.0
            // UriInfo parseUri = parser.parseUri(PATH, "$filter=" + encode,
            // FRAGMENT);
            // >=4.0.0 <4.2.0
            // UriInfo parseUri = parser.parseUri(PATH, "$filter=" + encode,
            // FRAGMENT, this.edm);
            return parseUri.getFilterOption().getExpression().accept(new ExpressionGenerator())
                           .accept(new RenamingVisitor(csdlProvider::mapProperty)).accept(new FilterGenerator());
        } catch (ODataException ex) {
            throw new DecodingException(ex);
        }
    }

    @Override
    public Set<DecoderKey> getKeys() {
        // TODO implement ODataFesParser.getKeys()
        return Collections.emptySet();
    }

    private String checkForGeoFitler(String objectToDecode) {
        String modified = objectToDecode;
        if (objectToDecode.contains("geo.")) {
            modified = objectToDecode.replace(",'SRID", ",geometry'SRID").replace("(featureOfInterest,",
                                                                                  "(featureOfInterest/shape,");
        } else if (objectToDecode.contains(FEATURE_EQUALS)) {
            modified = modified.replace(FEATURE_EQUALS, "featureOfInterest/id eq '");
        }
        return modified;
    }

    /**
     * Parse the value expression as an {@code Geometry} in WKT or EWKT format.
     * Geographies are handled as if they would be geometries.
     *
     * @param val the geometry value
     * @return the geometry
     * @throws DecodingException if the geometry is invalid
     */
    private static Geometry parseGeometry(StringValueExpr val)
            throws DecodingException {
        String value = val.getValue();
        if (value.startsWith(GEOGRAPHY_TYPE)) {
            value = value.substring(GEOGRAPHY_TYPE.length());
        }
        if (value.startsWith(GEOMETRY_TYPE)) {
            value = value.substring(GEOMETRY_TYPE.length());
        }
        value = stripQuotes(value).toUpperCase();
        int srid = 4326;
        if (value.startsWith(SRID_PREFIX)) {
            int sep = value.indexOf(';');
            if (sep > SRID_PREFIX.length() && value.length() > sep) {
                try {
                    srid = Integer.parseInt(value.substring(SRID_PREFIX.length(), sep));
                } catch (NumberFormatException ex) {
                    throw invalidGeometry(val, ex);
                }
                value = value.substring(sep + 1);
            } else {
                throw invalidGeometry(val);
            }
        }
        PrecisionModel precisionModel = new PrecisionModel(PrecisionModel.FLOATING);
        GeometryFactory geometryFactory = new GeometryFactory(precisionModel, srid);
        WKTReader wktReader = new WKTReader(geometryFactory);
        try {

            return wktReader.read(value);
        } catch (ParseException ex) {
            throw invalidGeometry(val, ex);
        }
    }

    /**
     * Get the the pair of value and member expression from the to expressions
     * or {@code Optional.empty()} if the expression do not match the types.
     *
     * @param first  the first expression
     * @param second the second expression
     * @return the member-value-pair
     */
    private static Optional<MemberValueExprPair> getMemberValuePair(Expr first, Expr second) {
        if (first.asMember().isPresent()) {
            MemberExpr member = first.asMember().get();
            Optional<TextExpr> valueOpt = second.asTextValue();
            return valueOpt.map(textExpr -> new MemberValueExprPair(member, textExpr));
        } else {
            MemberExpr member = second.asMember().get();
            Optional<TextExpr> valueOpt = first.asTextValue();
            return valueOpt.map(textExpr -> new MemberValueExprPair(member, textExpr));
        }
    }

    /**
     * Get the the pair of value and member expression from the to expressions
     * or {@code Optional.empty()} if the expression do not match the types.
     *
     * @param expr the binary expression
     * @return the member-value-pair
     */
    private static Optional<MemberValueExprPair> getMemberValuePair(BinaryExpr<?> expr) {
        return getMemberValuePair(expr.getLeft(), expr.getRight());
    }

    /**
     * Get the the pair of value and member expression from the to expressions
     * or {@code Optional.empty()} if the expression do not match the types.
     *
     * @param expr the binary expression
     * @return the member-value-pair
     */
    private static Optional<MemberValueExprPair> getMemberValuePair(List<Expr> expr) {
        if (expr.size() != 2) {
            return Optional.empty();
        }
        Iterator<Expr> iter = expr.iterator();
        return getMemberValuePair(iter.next(), iter.next());
    }

    /**
     * Strip any enclosing single quotes from the string.
     *
     * @param value the string value
     * @return the string value without quotes
     */
    @CheckReturnValue
    private static String stripQuotes(String value) {
        return value != null && value.length() >= 2 && value.startsWith("'") && value.endsWith("'")
                ? value.substring(1, value.length() - 1)
                : value;
    }

    /**
     * Get the {@code ComparisonOperator} matching the supplied
     * {@code BinaryOperatorKind}.
     *
     * @param op the operator
     * @return the {@code ComparisonOperator} or {@code Optional.empty()} if
     * none matches
     */
    private static Optional<ComparisonOperator> getComparisonOperator(BinaryOperatorKind op) {
        switch (op) {
        case EQ:
            return Optional.of(ComparisonOperator.PropertyIsEqualTo);
        case GE:
            return Optional.of(ComparisonOperator.PropertyIsGreaterThanOrEqualTo);
        case LE:
            return Optional.of(ComparisonOperator.PropertyIsLessThanOrEqualTo);
        case GT:
            return Optional.of(ComparisonOperator.PropertyIsGreaterThan);
        case LT:
            return Optional.of(ComparisonOperator.PropertyIsLessThan);
        case NE:
            return Optional.of(ComparisonOperator.PropertyIsNotEqualTo);
        default:
            return Optional.empty();
        }
    }

    /**
     * Get the {@code BinaryLogicOperator} matching the supplied
     * {@code BinaryOperatorKind}.
     *
     * @param op the operator
     * @return the {@code BinaryLogicOperator} or {@code Optional.empty()} if
     * none matches
     */
    private static Optional<BinaryLogicOperator> getLogicOperator(BinaryOperatorKind op) {
        switch (op) {
        case AND:
            return Optional.of(BinaryLogicOperator.And);
        case OR:
            return Optional.of(BinaryLogicOperator.Or);
        default:
            return Optional.empty();
        }
    }

    /**
     * Createa new {@code DecodingException} indicating that the geometry in
     * {@code val} is invalid.
     *
     * @param val the value containing the invalid geometry
     * @return the exception
     */
    private static DecodingException invalidGeometry(StringValueExpr val) {
        return invalidGeometry(val, null);
    }

    /**
     * Createa new {@code DecodingException} indicating that the geometry in
     * {@code val} is invalid.
     *
     * @param val   the value containing the invalid geometry
     * @param cause the exception describing the invalidity
     * @return the exception
     */
    private static DecodingException invalidGeometry(StringValueExpr val, Throwable cause) {
        return new DecodingException(cause, "invalid geometry: %s", val.getValue());
    }

    /**
     * Class to hold a pair of member and value expressions.
     */
    private static final class MemberValueExprPair {
        private final MemberExpr member;
        private final StringValueExpr value;

        /**
         * Create a new {@code MemberValueExprPair}.
         *
         * @param member the member
         * @param value  the value
         */
        MemberValueExprPair(MemberExpr member, TextExpr value) {
            this.member = Objects.requireNonNull(member);
            this.value = Objects.requireNonNull((StringValueExpr) value);
        }

        /**
         * Get the member expression.
         *
         * @return the expression
         */
        MemberExpr getMember() {
            return member;
        }

        /**
         * Get the value expression.
         *
         * @return the expression
         */
        StringValueExpr getValue() {
            return value;
        }
    }


    /**
     * Adapter for {@link ExpressionVisitor} to compensate for olingo's terrible version incompatibilities.
     *
     * @param <T> The return type
     */
    private interface ExpressionVisitorAdapter<T> extends ExpressionVisitor<T> {
        // >=4.7.0
        @Override
        default T visitBinaryOperator(BinaryOperatorKind operator, T left, List<T> right)
                throws ExpressionVisitException, ODataApplicationException {
            T result = left;
            for (T expr : right) {
                result = visitBinaryOperator(operator, result, expr);
            }
            return result;
        }

        // >=4.2.0
        @Override
        default T visitMember(Member member)
                throws ExpressionVisitException, ODataApplicationException {
            return visitMember(member.getResourcePath());
        }

        // >=4.0.0<=4.2.0
        // @Override
        T visitMember(UriInfoResource member)
                throws ExpressionVisitException, ODataApplicationException;
    }


    /**
     * Class to generate a {@code Expr} from the Olingo structures.
     */
    private static final class ExpressionGenerator implements ExpressionVisitorAdapter<Expr> {

        @Override
        public Expr visitBinaryOperator(BinaryOperatorKind op, Expr left, Expr right)
                throws ExpressionVisitException {
            Supplier<ExpressionVisitException> exceptionSupplier = () -> new ExpressionVisitException(
                    String.format("Operator %s is not supported: %s %s %s", op, left, op, right));
            switch (op) {
            case AND:
            case OR: {
                BinaryLogicOperator operator = getLogicOperator(op).orElseThrow(exceptionSupplier);
                BooleanExpr leftOperand = left.asBoolean().orElseThrow(exceptionSupplier);
                BooleanExpr rightOperand = right.asBoolean().orElseThrow(exceptionSupplier);
                return new BooleanBinaryExpr(operator, leftOperand, rightOperand);
            }
            case EQ:
            case NE:
            case GT:
            case GE:
            case LT:
            case LE: {
                MemberValueExprPair mv = getMemberValuePair(left, right).orElseThrow(exceptionSupplier);
                ComparisonOperator operator = getComparisonOperator(op).orElseThrow(exceptionSupplier);
                return new ComparisonExpr(operator, mv.getMember(), mv.getValue());
            }
            default:
                throw exceptionSupplier.get();
            }

        }

        @Override
        public StringValueExpr visitLiteral(Literal literal) {
            return new StringValueExpr(stripQuotes(literal.getText()));
        }

        @Override
        public MethodCallExpr visitMethodCall(MethodKind methodCall, List<Expr> parameters) {
            return new MethodCallExpr(methodCall.toString(), parameters);
        }

        @Override
        public UnaryExpr<?> visitUnaryOperator(UnaryOperatorKind op, Expr operand)
                throws ExpressionVisitException {
            Supplier<ExpressionVisitException> exceptionSupplier =
                    () -> new ExpressionVisitException(String.format("Operator is not supported: %s %s", op,
                                                                     operand));
            switch (op) {
            case NOT:
                return new BooleanUnaryExpr(UnaryLogicOperator.Not,
                                            operand.asBoolean().orElseThrow(exceptionSupplier));
            case MINUS:
            default:
                throw exceptionSupplier.get();

            }
        }

        @Override
        public Expr visitLambdaExpression(String fun, String var, Expression expr)
                throws ExpressionVisitException {
            throw new ExpressionVisitException("Lambda expressions are not supported");
        }

        @Override
        public Expr visitMember(UriInfoResource member)
                throws ExpressionVisitException {
            return new MemberExpr(member.getUriResourceParts().stream().map(UriResource::getSegmentValue)
                                        .collect(Collectors.joining("/")));
        }

        @Override
        public Expr visitAlias(String aliasName)
                throws ExpressionVisitException {
            throw new ExpressionVisitException("aliases are not supported");
        }

        @Override
        public Expr visitTypeLiteral(EdmType type)
                throws ExpressionVisitException {
            throw new ExpressionVisitException("type literals are not supported");
        }

        @Override
        public Expr visitLambdaReference(String variableName)
                throws ExpressionVisitException {
            throw new ExpressionVisitException("Lambda references are not supported");
        }

        @Override
        public Expr visitEnum(EdmEnumType type, List<String> enumValues)
                throws ExpressionVisitException {
            throw new ExpressionVisitException("enums are not supported");
        }
    }


    /**
     * Class to create a {@code Filter} from an {@code Expr}.
     */
    private static final class FilterGenerator implements ExprVisitor<Filter<?>, DecodingException> {
        private static final String WILDCARD = "%";

        @Override
        public Filter<?> visitBooleanBinary(BooleanBinaryExpr expr) throws DecodingException {
            return new BinaryLogicFilter(expr.getOperator(),
                                         expr.getLeft().accept(this),
                                         expr.getRight().accept(this));
        }

        @Override
        public Filter<?> visitBooleanUnary(BooleanUnaryExpr expr) throws DecodingException {
            return new UnaryLogicFilter(expr.getOperand().accept(this));
        }

        @Override
        public Filter<?> visitComparison(ComparisonExpr expr) throws DecodingException {
            MemberValueExprPair memberValuePair = getMemberValuePair(expr).orElseThrow(this::unsupported);
            return new ComparisonFilter(expr.getOperator(),
                                        memberValuePair.getMember().getValue(),
                                        memberValuePair.getValue().getValue());
        }

        @Override
        public Filter<?> visitMethodCall(MethodCallExpr expr) throws DecodingException {
            switch (expr.getName()) {
            case METHOD_CONTAINS: {
                MemberValueExprPair mv = getMemberValuePair(expr.getParameters()).orElseThrow(this::unsupported);
                String referenceValue = mv.getMember().getValue();
                String value = WILDCARD + mv.getValue().getValue() + WILDCARD;
                return new ComparisonFilter(ComparisonOperator.PropertyIsLike, referenceValue, value);
            }
            case METHOD_STARTS_WITH: {
                MemberValueExprPair mv = getMemberValuePair(expr.getParameters()).orElseThrow(this::unsupported);
                String referenceValue = mv.getMember().getValue();
                String value = mv.getValue().getValue() + WILDCARD;
                return new ComparisonFilter(ComparisonOperator.PropertyIsLike, referenceValue, value);
            }
            case METHOD_ENDS_WITH: {
                MemberValueExprPair mv = getMemberValuePair(expr.getParameters()).orElseThrow(this::unsupported);
                String referenceValue = mv.getMember().getValue();
                String value = WILDCARD + mv.getValue().getValue();
                return new ComparisonFilter(ComparisonOperator.PropertyIsLike, referenceValue, value);

            }
            case METHOD_GEO_INTERSECTS: {
                MemberValueExprPair mv = getMemberValuePair(expr.getParameters()).orElseThrow(this::unsupported);
                String referenceValue = mv.getMember().getValue();
                if (referenceValue.equals("om:featureOfInterest")) {
                    referenceValue += "/*/sams:shape";
                }
                Geometry geometry = parseGeometry(mv.getValue());
                return new SpatialFilter(SpatialOperator.Intersects, geometry, referenceValue);
            }
            default:
                throw new DecodingException("unsupported method '%s'", expr.getName());
            }
        }

        @Override
        public Filter<?> visitMember(MemberExpr expr)
                throws DecodingException {
            throw new DecodingException("unexpected member expression '%s'", expr.getValue());
        }

        /**
         * Visit a value expression.
         *
         * @param expr the expression
         * @return the result of the visit
         * @throws DecodingException if the visit fails
         */
        @Override public Filter<?> visitString(StringValueExpr expr) throws DecodingException {
            return null;
        }

        /**
         * Visit a arithmetic expression.
         *
         * @param expr the expression
         * @return the result of the visit
         * @throws DecodingException if the visit fails
         */
        @Override public Filter<?> visitSimpleArithmetic(SimpleArithmeticExpr expr) throws DecodingException {
            return null;
        }

        /**
         * Visit a time expression.
         *
         * @param expr the expression
         * @return the result of the visit
         * @throws DecodingException if the visit fails
         */
        @Override public Filter<?> visitTime(TimeValueExpr expr) throws DecodingException {
            return null;
        }

        /**
         * Visit a geometry expression.
         *
         * @param expr the expression
         * @return the result of the visit
         * @throws DecodingException if the visit fails
         */
        @Override public Filter<?> visitGeometry(GeoValueExpr expr) throws DecodingException {
            return null;
        }

        /**
         * Visit a number expression.
         *
         * @param expr the expression
         * @return the result of the visit
         * @throws DecodingException if the visit fails
         */
        @Override public Filter<?> visitNumeric(NumericValueExpr expr) throws DecodingException {
            return null;
        }

        public Filter<?> visitValue(StringValueExpr expr)
                throws DecodingException {
            throw new DecodingException("unexpected value expression '%s'", expr.getValue());
        }

        /**
         * Creates an {@code DecodingException} indicating that the supplied
         * expression is not supported.
         *
         * @return the exception
         */
        private DecodingException unsupported() {
            return new DecodingException("unsupported expression");
        }
    }


    /**
     * Abstract transforming visitor that is able to modify expression.
     *
     * @param <T> The exception type
     */
    private static class AbstractExprTransformer<T extends Throwable>
            implements
            ExprVisitor<Expr, T> {

        @Override
        public Expr visitBooleanBinary(BooleanBinaryExpr expr)
                throws T {
            BinaryLogicOperator op = expr.getOperator();
            BooleanExpr left = expr.getLeft().accept(this).asBoolean().orElseThrow(Error::new);
            BooleanExpr right = expr.getRight().accept(this).asBoolean().orElseThrow(Error::new);
            return new BooleanBinaryExpr(op, left, right);
        }

        @Override
        public Expr visitBooleanUnary(BooleanUnaryExpr expr)
                throws T {
            UnaryLogicOperator op = expr.getOperator();
            BooleanExpr operand = expr.getOperand().accept(this).asBoolean().orElseThrow(Error::new);
            return new BooleanUnaryExpr(op, operand);
        }

        @Override
        public Expr visitComparison(ComparisonExpr expr)
                throws T {
            ComparisonOperator op = expr.getOperator();
            Expr left = expr.getLeft().accept(this);
            Expr right = expr.getRight().accept(this);
            return new ComparisonExpr(op, left, right);
        }

        @Override
        public Expr visitMethodCall(MethodCallExpr expr)
                throws T {
            String name = expr.getName();
            List<Expr> list = new ArrayList<>(expr.getParameters().size());
            for (Expr e : expr.getParameters()) {
                list.add(e.accept(this));
            }
            return new MethodCallExpr(name, list);
        }

        @Override
        public Expr visitMember(MemberExpr expr) {
            String value = expr.getValue();
            return new MemberExpr(value);
        }

        /**
         * Visit a value expression.
         *
         * @param expr the expression
         * @return the result of the visit
         * @throws T if the visit fails
         */
        @Override public Expr visitString(StringValueExpr expr) throws T {
            String value = expr.getValue();
            return new StringValueExpr(value);
        }

        /**
         * Visit a arithmetic expression.
         *
         * @param expr the expression
         * @return the result of the visit
         * @throws T if the visit fails
         */
        @Override public Expr visitSimpleArithmetic(SimpleArithmeticExpr expr) throws T {
            return null;
        }

        /**
         * Visit a time expression.
         *
         * @param expr the expression
         * @return the result of the visit
         * @throws T if the visit fails
         */
        @Override public Expr visitTime(TimeValueExpr expr) throws T {
            return null;
        }

        /**
         * Visit a geometry expression.
         *
         * @param expr the expression
         * @return the result of the visit
         * @throws T if the visit fails
         */
        @Override public Expr visitGeometry(GeoValueExpr expr) throws T {
            return null;
        }

        /**
         * Visit a number expression.
         *
         * @param expr the expression
         * @return the result of the visit
         * @throws T if the visit fails
         */
        @Override public Expr visitNumeric(NumericValueExpr expr) throws T {
            return null;
        }
    }


    /**
     * Transformer for expression that modifies the member referneces.
     */
    private static class RenamingVisitor
            extends
            AbstractExprTransformer<Error> {

        private final Function<String, String> mapper;

        /**
         * Create a new {@code RenamingVisitor}.
         *
         * @param mapper the mapper used to modifiy the member references
         */
        RenamingVisitor(Function<String, String> mapper) {
            this.mapper = mapper;
        }

        @Override
        public Expr visitMember(MemberExpr expr) {
            return new MemberExpr(mapper.apply(expr.getValue()));
        }
    }

}
