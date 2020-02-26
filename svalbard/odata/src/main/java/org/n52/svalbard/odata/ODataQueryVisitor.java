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

import org.antlr.v4.runtime.tree.TerminalNode;
import org.n52.shetland.filter.CountFilter;
import org.n52.shetland.filter.ExpandFilter;
import org.n52.shetland.filter.ExpandItem;
import org.n52.shetland.filter.FilterFilter;
import org.n52.shetland.filter.OrderByFilter;
import org.n52.shetland.filter.OrderProperty;
import org.n52.shetland.filter.SelectFilter;
import org.n52.shetland.filter.SkipTopFilter;
import org.n52.shetland.oasis.odata.query.option.QueryOptions;
import org.n52.shetland.ogc.filter.FilterClause;
import org.n52.shetland.ogc.filter.FilterConstants;
import org.n52.svalbard.odata.expr.Expr;
import org.n52.svalbard.odata.expr.GeoValueExpr;
import org.n52.svalbard.odata.expr.MemberExpr;
import org.n52.svalbard.odata.expr.MethodCallExpr;
import org.n52.svalbard.odata.expr.StringValueExpr;
import org.n52.svalbard.odata.expr.TextExpr;
import org.n52.svalbard.odata.expr.arithmetic.ArithmeticExpr;
import org.n52.svalbard.odata.expr.arithmetic.NumericValueExpr;
import org.n52.svalbard.odata.expr.arithmetic.SimpleArithmeticExpr;
import org.n52.svalbard.odata.expr.binary.BooleanBinaryExpr;
import org.n52.svalbard.odata.expr.binary.BooleanExpr;
import org.n52.svalbard.odata.expr.binary.BooleanUnaryExpr;
import org.n52.svalbard.odata.expr.binary.ComparisonExpr;
import org.n52.svalbard.odata.expr.temporal.TemporalExpr;
import org.n52.svalbard.odata.expr.temporal.TimeValueExpr;
import org.n52.svalbard.odata.grammar.ODataQueryParserBaseVisitor;
import org.n52.svalbard.odata.grammar.ODataQueryParserParser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author <a href="mailto:j.speckamp@52north.org">Jan Speckamp</a>
 */
public class ODataQueryVisitor extends ODataQueryParserBaseVisitor {

    @Override public QueryOptions visitQueryOptions(ODataQueryParserParser.QueryOptionsContext ctx) {
        Set<FilterClause> qops = new HashSet<>();
        for (ODataQueryParserParser.SystemQueryOptionContext systemQueryOptionContext : ctx.systemQueryOption()) {
            qops.add(this.visitSystemQueryOption(systemQueryOptionContext));
        }
        return new QueryOptions("", qops);
    }

    @Override public FilterClause visitSystemQueryOption(ODataQueryParserParser.SystemQueryOptionContext ctx) {
        if (ctx.count() != null) {
            return this.visitCount(ctx.count());
        }
        if (ctx.expand() != null) {
            return this.visitExpand(ctx.expand());
        }
        if (ctx.filter() != null) {
            return this.visitFilter(ctx.filter());
        }
        if (ctx.orderby() != null) {
            return this.visitOrderby(ctx.orderby());
        }
        if (ctx.select() != null) {
            return this.visitSelect(ctx.select());
        }
        if (ctx.skip() != null) {
            return this.visitSkip(ctx.skip());
        }
        if (ctx.top() != null) {
            return this.visitTop(ctx.top());
        }
        return null;
    }

    @Override public SkipTopFilter visitSkip(ODataQueryParserParser.SkipContext ctx) {
        return new SkipTopFilter(FilterConstants.SkipTopOperator.Skip, Long.parseLong(ctx.DecimalLiteral().getText()));
    }

    @Override public SkipTopFilter visitTop(ODataQueryParserParser.TopContext ctx) {
        return new SkipTopFilter(FilterConstants.SkipTopOperator.Top, Long.parseLong(ctx.DecimalLiteral().getText()));
    }

    //TODO: check if we would like to also allow $count=false in the url
    @Override public CountFilter visitCount(ODataQueryParserParser.CountContext ctx) {
        return new CountFilter(true);
    }

    @Override public SelectFilter visitSelect(ODataQueryParserParser.SelectContext ctx) {
        Set<String> pathFilterItems = new HashSet<>();
        for (ODataQueryParserParser.SelectItemContext selectItemContext : ctx.selectItem()) {
            pathFilterItems.add(visitSelectItem(selectItemContext));
        }
        return new SelectFilter(pathFilterItems);
    }

    @Override public String visitSelectItem(ODataQueryParserParser.SelectItemContext ctx) {
        return ctx.getText();
    }

    @Override public OrderByFilter visitOrderby(ODataQueryParserParser.OrderbyContext ctx) {
        List<OrderProperty> orderProperties = new ArrayList<>();
        for (ODataQueryParserParser.OrderbyItemContext orderbyItemContext : ctx.orderbyItem()) {
            orderProperties.add(this.visitOrderbyItem(orderbyItemContext));
        }
        return new OrderByFilter(orderProperties);
    }

    @Override public OrderProperty visitOrderbyItem(ODataQueryParserParser.OrderbyItemContext ctx) {
        if (ctx.Asc_LLC() != null) {
            return new OrderProperty(ctx.memberExpr().getText(), FilterConstants.SortOrder.ASC);
        } else if (ctx.Desc_LLC() != null) {
            return new OrderProperty(ctx.memberExpr().getText(), FilterConstants.SortOrder.DESC);
        } else {
            return new OrderProperty(ctx.memberExpr().getText());
        }
    }

    @Override public ExpandFilter visitExpand(ODataQueryParserParser.ExpandContext ctx) {
        Set<ExpandItem> expandItems = new HashSet<>();
        for (ODataQueryParserParser.ExpandItemContext expandItemContext : ctx.expandItem()) {
            expandItems.add(this.visitExpandItem(expandItemContext));
        }
        return new ExpandFilter(expandItems);
    }

    @Override public ExpandItem visitExpandItem(ODataQueryParserParser.ExpandItemContext ctx) {
        if (!ctx.systemQueryOption().isEmpty()) {
            Set<FilterClause> options = new HashSet<>();
            for (ODataQueryParserParser.SystemQueryOptionContext expandQueryOptions : ctx.systemQueryOption()) {
                options.add(this.visitSystemQueryOption(expandQueryOptions));
            }
            return new ExpandItem(ctx.memberExpr().getText(), new QueryOptions("", options));
        } else {
            // Rewrite slash to normal expand systemQueryOption
            if (!ctx.memberExpr().SLASH().isEmpty()) {
                QueryOptions base = new QueryOptions("", null);
                for (int i = ctx.memberExpr().AlphaPlus().size() - 1; i >= 1; i--) {
                    ExpandItem expandItem = new ExpandItem(ctx.memberExpr().AlphaPlus(i).getText(), base);
                    ExpandFilter expandFilter = new ExpandFilter(expandItem);
                    base = new QueryOptions("", Collections.singleton(expandFilter));
                }
                return new ExpandItem(ctx.memberExpr().AlphaPlus(0).getText(), base);
            } else {
                return new ExpandItem(ctx.getText(), new QueryOptions("", null));
            }
        }
    }

    @Override public FilterFilter visitFilter(ODataQueryParserParser.FilterContext ctx) {
        return new FilterFilter(this.visitBoolExpr(ctx.boolExpr()));
    }

    @Override public BooleanExpr visitBoolExpr(ODataQueryParserParser.BoolExprContext ctx) {
        BooleanExpr left = null;

        if (ctx.boolMethodCallExpr() != null) {
            left = this.visitBoolMethodCallExpr(ctx.boolMethodCallExpr());
        } else if (ctx.boolParenExpr() != null) {
            left = this.visitBoolParenExpr(ctx.boolParenExpr());
        } else if (ctx.notExpr() != null) {
            left = new BooleanUnaryExpr(FilterConstants.UnaryLogicOperator.Not,
                                        this.visitNotExpr(ctx.notExpr()));
        } else if (ctx.anyExpr() != null) {
            Expr right;
            Expr common = this.visitAnyExpr(ctx.anyExpr());

            if (ctx.eqExpr() != null) {
                right = this.visitEqExpr(ctx.eqExpr());
                left = new ComparisonExpr(FilterConstants.ComparisonOperator.PropertyIsEqualTo,
                                          common,
                                          right);
            } else if (ctx.neExpr() != null) {
                right = this.visitNeExpr(ctx.neExpr());
                left = new ComparisonExpr(FilterConstants.ComparisonOperator.PropertyIsNotEqualTo,
                                          common,

                                          right);
            } else if (ctx.ltExpr() != null) {
                right = this.visitLtExpr(ctx.ltExpr());
                left = new ComparisonExpr(FilterConstants.ComparisonOperator.PropertyIsLessThan,
                                          common,
                                          right);
            } else if (ctx.leExpr() != null) {
                right = this.visitLeExpr(ctx.leExpr());
                left = new ComparisonExpr(FilterConstants.ComparisonOperator.PropertyIsLessThanOrEqualTo,
                                          common,
                                          right);
            } else if (ctx.gtExpr() != null) {
                right = this.visitGtExpr(ctx.gtExpr());
                left = new ComparisonExpr(FilterConstants.ComparisonOperator.PropertyIsGreaterThan,
                                          common,
                                          right);
            } else if (ctx.geExpr() != null) {
                right = this.visitGeExpr(ctx.geExpr());
                left = new ComparisonExpr(FilterConstants.ComparisonOperator.PropertyIsGreaterThanOrEqualTo,
                                          common,
                                          right);

            }
        }

        // Handle appended AND or OR
        if (ctx.andExpr() != null) {
            BooleanExpr right = visitAndExpr(ctx.andExpr());
            return new BooleanBinaryExpr(FilterConstants.BinaryLogicOperator.And,
                                         left,
                                         right);
        } else if (ctx.orExpr() != null) {
            BooleanExpr right = visitOrExpr(ctx.orExpr());
            return new BooleanBinaryExpr(FilterConstants.BinaryLogicOperator.Or,
                                         left,
                                         right);
        } else {
            return left;
        }
    }

    @Override public Expr visitAnyExpr(ODataQueryParserParser.AnyExprContext ctx) {
        //TODO!!!
        // anyExpr
        //   : memberExpr
        //   | arithmeticExpr
        //   | geoExpr
        //   | timeExpr
        //   | textExpr
        //   | parenExpr
        //   ;
        if (ctx.memberExpr() != null) {
            return this.visitMemberExpr(ctx.memberExpr());
        } else if (ctx.arithmeticExpr() != null) {
            return this.visitArithmeticExpr(ctx.arithmeticExpr());
        } else if (ctx.geoExpr() != null) {
            return this.visitGeoExpr(ctx.geoExpr());
        } else if (ctx.timeExpr() != null) {
            return this.visitTimeExpr(ctx.timeExpr());
        } else if (ctx.textExpr() != null) {
            return this.visitTextExpr(ctx.textExpr());
        } else if (ctx.parenExpr() != null) {
            return this.visitParenExpr(ctx.parenExpr());
        }

        // This will never happen
        return null;
    }

    @Override public ArithmeticExpr visitArithmeticExpr(ODataQueryParserParser.ArithmeticExprContext ctx) {
        // arithmeticExpr
        //   : (OP (SP)*)? (numericLiteral | memberExpr | negateExpr | arithmeticMethodCallExpr) (addExpr | subExpr |
        //   mulExpr | divExpr | modExpr)? (OP (SP)*)?
        //   ;
        ArithmeticExpr left;
        if (ctx.numericLiteral() != null) {
            left = new NumericValueExpr(ctx.numericLiteral().getText());
        } else if (ctx.memberExpr() != null) {
            left = new MemberExpr(ctx.memberExpr().getText());
        } else if (ctx.negateExpr() != null) {
            //TODO: check if this can be done nicer than subtracting from 0
            left = new SimpleArithmeticExpr(FilterConstants.SimpleArithmeticOperator.Sub,
                                            new NumericValueExpr(0),
                                            this.visitArithmeticExpr(ctx.negateExpr().arithmeticExpr()));
        } else {
            left = this.visitArithmeticMethodCallExpr(ctx.arithmeticMethodCallExpr());
        }

        ArithmeticExpr right;
        if (ctx.addExpr() != null) {
            right = visitAddExpr(ctx.addExpr());
            return new SimpleArithmeticExpr(FilterConstants.SimpleArithmeticOperator.Add, left, right);
        } else if (ctx.subExpr() != null) {
            right = visitSubExpr(ctx.subExpr());
            return new SimpleArithmeticExpr(FilterConstants.SimpleArithmeticOperator.Sub, left, right);
        } else if (ctx.mulExpr() != null) {
            right = visitMulExpr(ctx.mulExpr());
            return new SimpleArithmeticExpr(FilterConstants.SimpleArithmeticOperator.Mul, left, right);
        } else if (ctx.divExpr() != null) {
            right = visitDivExpr(ctx.divExpr());
            return new SimpleArithmeticExpr(FilterConstants.SimpleArithmeticOperator.Div, left, right);
        } else if (ctx.modExpr() != null) {
            right = visitModExpr(ctx.modExpr());
            return new SimpleArithmeticExpr(FilterConstants.SimpleArithmeticOperator.Mod, left, right);
        } else {
            // There is no right Expr. return left only
            return left;
        }
    }

    @Override
    public ArithmeticExpr visitArithmeticMethodCallExpr(ODataQueryParserParser.ArithmeticMethodCallExprContext ctx) {
        for (int i = 0; i < ctx.getChildCount(); i++) {
            if (ctx.getChild(i) != null) {
                return ctx.getChild(i).<ArithmeticExpr>accept(this);
            }
        }
        // This will never happen
        return null;
    }

    @Override public BooleanExpr visitNotExpr(ODataQueryParserParser.NotExprContext ctx) {
        return this.visitBoolExpr(ctx.boolExpr());
    }

    //TODO: check if we need to do something here to preserve precedence or if we can just leave out parentheses and
    // precedence is given due to nested context
    @Override public BooleanExpr visitBoolParenExpr(ODataQueryParserParser.BoolParenExprContext ctx) {
        return this.visitBoolExpr(ctx.boolExpr());
    }

    @Override public BooleanExpr visitAndExpr(ODataQueryParserParser.AndExprContext ctx) {
        return this.visitBoolExpr(ctx.boolExpr());
    }

    @Override public BooleanExpr visitOrExpr(ODataQueryParserParser.OrExprContext ctx) {
        return this.visitBoolExpr(ctx.boolExpr());
    }

    @Override public Expr visitEqExpr(ODataQueryParserParser.EqExprContext ctx) {
        return this.visitAnyExpr(ctx.anyExpr());
    }

    @Override public Expr visitNeExpr(ODataQueryParserParser.NeExprContext ctx) {
        return this.visitAnyExpr(ctx.anyExpr());
    }

    @Override public Expr visitLtExpr(ODataQueryParserParser.LtExprContext ctx) {
        return this.visitAnyExpr(ctx.anyExpr());
    }

    @Override public Expr visitLeExpr(ODataQueryParserParser.LeExprContext ctx) {
        return this.visitAnyExpr(ctx.anyExpr());
    }

    @Override public Expr visitGtExpr(ODataQueryParserParser.GtExprContext ctx) {
        return this.visitAnyExpr(ctx.anyExpr());
    }

    @Override public Expr visitGeExpr(ODataQueryParserParser.GeExprContext ctx) {
        return this.visitAnyExpr(ctx.anyExpr());
    }

    @Override public ArithmeticExpr visitAddExpr(ODataQueryParserParser.AddExprContext ctx) {
        return this.visitArithmeticExpr(ctx.arithmeticExpr());
    }

    @Override public ArithmeticExpr visitSubExpr(ODataQueryParserParser.SubExprContext ctx) {
        return this.visitArithmeticExpr(ctx.arithmeticExpr());
    }

    @Override public ArithmeticExpr visitMulExpr(ODataQueryParserParser.MulExprContext ctx) {
        return this.visitArithmeticExpr(ctx.arithmeticExpr());
    }

    @Override public ArithmeticExpr visitDivExpr(ODataQueryParserParser.DivExprContext ctx) {
        return this.visitArithmeticExpr(ctx.arithmeticExpr());
    }

    @Override public ArithmeticExpr visitModExpr(ODataQueryParserParser.ModExprContext ctx) {
        return this.visitArithmeticExpr(ctx.arithmeticExpr());
    }

    @Override public ArithmeticExpr visitLengthMethodCallExpr(ODataQueryParserParser.LengthMethodCallExprContext ctx) {
        return new MethodCallExpr(ctx.Length_LLC().getText(), this.visitTextOrMember(ctx.textOrMember()));
    }

    @Override
    public ArithmeticExpr visitIndexOfMethodCallExpr(ODataQueryParserParser.IndexOfMethodCallExprContext ctx) {
        return new MethodCallExpr(ctx.IndexOf_LLC().getText(),
                                  this.visitTextOrMember(ctx.textOrMember(0)),
                                  this.visitTextOrMember(ctx.textOrMember(1)));
    }

    @Override public ArithmeticExpr visitYearMethodCallExpr(ODataQueryParserParser.YearMethodCallExprContext ctx) {
        return new MethodCallExpr(ctx.Year_LLC().getText(),
                                  this.visitTemporalOrMemberOrString(ctx.temporalOrMemberOrString()));
    }

    @Override public ArithmeticExpr visitMonthMethodCallExpr(ODataQueryParserParser.MonthMethodCallExprContext ctx) {
        return new MethodCallExpr(ctx.Month_LLC().getText(),
                                  this.visitTemporalOrMemberOrString(ctx.temporalOrMemberOrString()));
    }

    @Override public ArithmeticExpr visitDayMethodCallExpr(ODataQueryParserParser.DayMethodCallExprContext ctx) {
        return new MethodCallExpr(ctx.Day_LLC().getText(),
                                  this.visitTemporalOrMemberOrString(ctx.temporalOrMemberOrString()));
    }

    @Override public ArithmeticExpr visitDaysMethodCallExpr(ODataQueryParserParser.DaysMethodCallExprContext ctx) {
        return new MethodCallExpr(ctx.Days_LLC().getText(),
                                  this.visitTemporalOrMemberOrString(ctx.temporalOrMemberOrString()));
    }

    @Override public ArithmeticExpr visitHourMethodCallExpr(ODataQueryParserParser.HourMethodCallExprContext ctx) {
        return new MethodCallExpr(ctx.Hour_LLC().getText(),
                                  this.visitTemporalOrMemberOrString(ctx.temporalOrMemberOrString()));
    }

    @Override public ArithmeticExpr visitMinuteMethodCallExpr(ODataQueryParserParser.MinuteMethodCallExprContext ctx) {
        return new MethodCallExpr(ctx.Minute_LLC().getText(),
                                  this.visitTemporalOrMemberOrString(ctx.temporalOrMemberOrString()));
    }

    @Override public ArithmeticExpr visitSecondMethodCallExpr(ODataQueryParserParser.SecondMethodCallExprContext ctx) {
        return new MethodCallExpr(ctx.Second_LLC().getText(),
                                  this.visitTemporalOrMemberOrString(ctx.temporalOrMemberOrString()));
    }

    @Override public ArithmeticExpr visitDateMethodCallExpr(ODataQueryParserParser.DateMethodCallExprContext ctx) {
        return new MethodCallExpr(ctx.Date_LLC().getText(),
                                  this.visitTemporalOrMemberOrString(ctx.temporalOrMemberOrString()));
    }

    @Override public ArithmeticExpr visitRoundMethodCallExpr(ODataQueryParserParser.RoundMethodCallExprContext ctx) {
        return new MethodCallExpr(ctx.Round_LLC().getText(), visitArithmeticExpr(ctx.arithmeticExpr()));
    }

    @Override public ArithmeticExpr visitFloorMethodCallExpr(ODataQueryParserParser.FloorMethodCallExprContext ctx) {
        return new MethodCallExpr(ctx.Floor_LLC().getText(), visitArithmeticExpr(ctx.arithmeticExpr()));
    }

    @Override
    public ArithmeticExpr visitCeilingMethodCallExpr(ODataQueryParserParser.CeilingMethodCallExprContext ctx) {
        return new MethodCallExpr(ctx.Ceiling_LLC().getText(), visitArithmeticExpr(ctx.arithmeticExpr()));
    }

    @Override
    public ArithmeticExpr visitDistanceMethodCallExpr(ODataQueryParserParser.DistanceMethodCallExprContext ctx) {
        return new MethodCallExpr(ctx.GeoDotDistance_LLC().getText(),
                                  this.visitGeoOrMember(ctx.geoOrMember(0)),
                                  this.visitGeoOrMember(ctx.geoOrMember(1)));
    }

    @Override
    public ArithmeticExpr visitGeoLengthMethodCallExpr(ODataQueryParserParser.GeoLengthMethodCallExprContext ctx) {
        return new MethodCallExpr(ctx.GeoLength_LLC().getText(), visitGeoOrMember(ctx.geoOrMember()));
    }

    @Override
    public ArithmeticExpr visitTotalOffsetMinutesExpr(ODataQueryParserParser.TotalOffsetMinutesExprContext ctx) {
        return new MethodCallExpr(ctx.TotalOffsetMinutes_LLC().getText(),
                                  visitTemporalOrMemberOrString(ctx.temporalOrMemberOrString()));
    }

    @Override public TextExpr visitTextOrMember(ODataQueryParserParser.TextOrMemberContext ctx) {
        if (ctx.textExpr() != null) {
            return this.visitTextExpr(ctx.textExpr());
        } else {
            return new MemberExpr(ctx.memberExpr().getText());
        }
    }

    @Override
    public TemporalExpr visitTemporalOrMemberOrString(ODataQueryParserParser.TemporalOrMemberOrStringContext ctx) {
        if (ctx.temporalMethodCallExpr() != null) {
            return visitTemporalMethodCallExpr(ctx.temporalMethodCallExpr());
        } else if (ctx.memberExpr() != null) {
            return new TimeValueExpr(ctx.memberExpr().getText());
        } else {
            return new TimeValueExpr(ctx.escapedString().escapedStringLiteral().getText());
        }
    }

    @Override public GeoValueExpr visitGeoOrMember(ODataQueryParserParser.GeoOrMemberContext ctx) {
        if (ctx.geoExpr() != null) {
            return new GeoValueExpr(ctx.geoExpr().getText());
        } else {
            return new GeoValueExpr(ctx.memberExpr().getText());
        }
    }

    @Override public TemporalExpr visitTimeExpr(ODataQueryParserParser.TimeExprContext ctx) {
        return visitTemporalMethodCallExpr(ctx.temporalMethodCallExpr());
    }

    @Override public GeoValueExpr visitGeoExpr(ODataQueryParserParser.GeoExprContext ctx) {
        return new GeoValueExpr(ctx.getText());
    }

    @Override public TextExpr visitTextExpr(ODataQueryParserParser.TextExprContext ctx) {
        if (ctx.escapedString() != null) {
            return new StringValueExpr(ctx.escapedString().escapedStringLiteral().getText());
        } else {
            return visitTextMethodCallExpr(ctx.textMethodCallExpr());
        }
    }

    @Override public BooleanExpr visitBoolMethodCallExpr(ODataQueryParserParser.BoolMethodCallExprContext ctx) {
        // boolMethodCallExpr
        //   : endsWithMethodCallExpr
        //   | startsWithMethodCallExpr
        //   | substringOfMethodCallExpr
        //   | intersectsMethodCallExpr
        //   | st_equalsMethodCallExpr
        //   | st_disjointMethodCallExpr
        //   | st_touchesMethodCallExpr
        //   | st_withinMethodCallExpr
        //   | st_overlapsMethodCallExpr
        //   | st_crossesMethodCallExpr
        //   | st_intersectsMethodCallExpr
        //   | st_containsMethodCallExpr
        //   | st_relateMethodCallExpr
        //   ;
        for (int i = 0; i < ctx.getChildCount(); i++) {
            if (ctx.getChild(i) != null) {
                return ctx.getChild(i).<BooleanExpr>accept(this);
            }
        }
        // This will never happen
        return null;
    }

    @Override public BooleanExpr visitEndsWithMethodCallExpr(ODataQueryParserParser.EndsWithMethodCallExprContext ctx) {
        return new MethodCallExpr(ctx.EndsWith_LLC().getText(),
                                  visitTextOrMember(ctx.textOrMember(0)),
                                  visitTextOrMember(ctx.textOrMember(1)));
    }

    @Override
    public BooleanExpr visitStartsWithMethodCallExpr(ODataQueryParserParser.StartsWithMethodCallExprContext ctx) {
        return new MethodCallExpr(ctx.StartsWith_LLC().getText(),
                                  visitTextOrMember(ctx.textOrMember(0)),
                                  visitTextOrMember(ctx.textOrMember(1)));
    }

    @Override
    public BooleanExpr visitSubstringOfMethodCallExpr(ODataQueryParserParser.SubstringOfMethodCallExprContext ctx) {
        return new MethodCallExpr(ctx.SubStringOf_LLC().getText(),
                                  visitTextOrMember(ctx.textOrMember(0)),
                                  visitTextOrMember(ctx.textOrMember(1)));
    }

    @Override
    public BooleanExpr visitIntersectsMethodCallExpr(ODataQueryParserParser.IntersectsMethodCallExprContext ctx) {
        return new MethodCallExpr(ctx.GeoDotIntersects_LLC().getText(),
                                  visitGeoOrMember(ctx.geoOrMember(0)),
                                  visitGeoOrMember(ctx.geoOrMember(1)));
    }

    @Override public Object visitSt_equalsMethodCallExpr(ODataQueryParserParser.St_equalsMethodCallExprContext ctx) {
        return new MethodCallExpr(ctx.ST_equals_LLC().getText(),
                                  visitGeoOrMember(ctx.st_commonMethodCallExpr().geoOrMember(0)),
                                  visitGeoOrMember(ctx.st_commonMethodCallExpr().geoOrMember(1)));
    }

    @Override
    public Object visitSt_disjointMethodCallExpr(ODataQueryParserParser.St_disjointMethodCallExprContext ctx) {
        return new MethodCallExpr(ctx.ST_disjoint_LLC().getText(),
                                  visitGeoOrMember(ctx.st_commonMethodCallExpr().geoOrMember(0)),
                                  visitGeoOrMember(ctx.st_commonMethodCallExpr().geoOrMember(1)));
    }

    @Override public Object visitSt_touchesMethodCallExpr(ODataQueryParserParser.St_touchesMethodCallExprContext ctx) {
        return new MethodCallExpr(ctx.ST_touches_LLC().getText(),
                                  visitGeoOrMember(ctx.st_commonMethodCallExpr().geoOrMember(0)),
                                  visitGeoOrMember(ctx.st_commonMethodCallExpr().geoOrMember(1)));
    }

    @Override public Object visitSt_withinMethodCallExpr(ODataQueryParserParser.St_withinMethodCallExprContext ctx) {
        return new MethodCallExpr(ctx.ST_within_LLC().getText(),
                                  visitGeoOrMember(ctx.st_commonMethodCallExpr().geoOrMember(0)),
                                  visitGeoOrMember(ctx.st_commonMethodCallExpr().geoOrMember(1)));
    }

    @Override
    public Object visitSt_overlapsMethodCallExpr(ODataQueryParserParser.St_overlapsMethodCallExprContext ctx) {
        return new MethodCallExpr(ctx.ST_overlaps_LLC().getText(),
                                  visitGeoOrMember(ctx.st_commonMethodCallExpr().geoOrMember(0)),
                                  visitGeoOrMember(ctx.st_commonMethodCallExpr().geoOrMember(1)));
    }

    @Override public Object visitSt_crossesMethodCallExpr(ODataQueryParserParser.St_crossesMethodCallExprContext ctx) {
        return new MethodCallExpr(ctx.ST_crosses_LLC().getText(),
                                  visitGeoOrMember(ctx.st_commonMethodCallExpr().geoOrMember(0)),
                                  visitGeoOrMember(ctx.st_commonMethodCallExpr().geoOrMember(1)));
    }

    @Override
    public Object visitSt_intersectsMethodCallExpr(ODataQueryParserParser.St_intersectsMethodCallExprContext ctx) {
        return new MethodCallExpr(ctx.ST_intersects_LLC().getText(),
                                  visitGeoOrMember(ctx.st_commonMethodCallExpr().geoOrMember(0)),
                                  visitGeoOrMember(ctx.st_commonMethodCallExpr().geoOrMember(1)));
    }

    @Override
    public Object visitSt_containsMethodCallExpr(ODataQueryParserParser.St_containsMethodCallExprContext ctx) {
        return new MethodCallExpr(ctx.ST_contains_LLC().getText(),
                                  visitGeoOrMember(ctx.st_commonMethodCallExpr().geoOrMember(0)),
                                  visitGeoOrMember(ctx.st_commonMethodCallExpr().geoOrMember(1)));
    }

    @Override public Object visitSt_relateMethodCallExpr(ODataQueryParserParser.St_relateMethodCallExprContext ctx) {
        return new MethodCallExpr(ctx.ST_relate_LLC().getText(),
                                  visitGeoOrMember(ctx.geoOrMember(0)),
                                  visitGeoOrMember(ctx.geoOrMember(1)),
                                  new StringValueExpr(ctx.escapedString().escapedStringLiteral().getText()));
    }

    @Override public TextExpr visitTextMethodCallExpr(ODataQueryParserParser.TextMethodCallExprContext ctx) {
        // textMethodCallExpr
        //   :
        //   | toLowerMethodCallExpr
        //   | toUpperMethodCallExpr
        //   | trimMethodCallExpr
        //   | substringMethodCallExpr
        //   | concatMethodCallExpr
        //   ;
        for (int i = 0; i < ctx.getChildCount(); i++) {
            if (ctx.getChild(i) != null) {
                return ctx.getChild(i).<TextExpr>accept(this);
            }
        }
        // This will never happen
        return null;
    }

    @Override
    public TemporalExpr visitTemporalMethodCallExpr(ODataQueryParserParser.TemporalMethodCallExprContext ctx) {
        //temporalMethodCallExpr
        //   : timeMethodCallExpr
        //   | nowDate
        //   | minDate
        //   | maxDate
        //   ;
        for (int i = 0; i < ctx.getChildCount(); i++) {
            if (ctx.getChild(i) != null) {
                return ctx.getChild(i).<TemporalExpr>accept(this);
            }
        }
        // This will never happen
        return null;
    }

    @Override public Object visitNowDate(ODataQueryParserParser.NowDateContext ctx) {
        return new MethodCallExpr(ctx.Now_LLC().getText());
    }

    @Override public Object visitMinDate(ODataQueryParserParser.MinDateContext ctx) {
        return new MethodCallExpr(ctx.MinDateTime_LLC().getText());
    }

    @Override public Object visitMaxDate(ODataQueryParserParser.MaxDateContext ctx) {
        return new MethodCallExpr(ctx.MaxDateTime_LLC().getText());
    }

    @Override public Object visitTimeMethodCallExpr(ODataQueryParserParser.TimeMethodCallExprContext ctx) {
        return new MethodCallExpr(ctx.Time_LLC().getText(),
                                  this.visitTemporalOrMemberOrString(ctx.temporalOrMemberOrString()));
    }

    @Override public MemberExpr visitMemberExpr(ODataQueryParserParser.MemberExprContext ctx) {
        return new MemberExpr(ctx.getText());
    }

    @Override
    public TextExpr visitToLowerMethodCallExpr(ODataQueryParserParser.ToLowerMethodCallExprContext ctx) {
        return new MethodCallExpr(ctx.ToLower_LLC().getText(), visitTextOrMember(ctx.textOrMember()));
    }

    @Override
    public TextExpr visitToUpperMethodCallExpr(ODataQueryParserParser.ToUpperMethodCallExprContext ctx) {
        return new MethodCallExpr(ctx.ToUpper_LLC().getText(), visitTextOrMember(ctx.textOrMember()));
    }

    @Override public TextExpr visitTrimMethodCallExpr(ODataQueryParserParser.TrimMethodCallExprContext ctx) {
        return new MethodCallExpr(ctx.Trim_LLC().getText(), visitTextOrMember(ctx.textOrMember()));
    }

    @Override
    public TextExpr visitSubstringMethodCallExpr(ODataQueryParserParser.SubstringMethodCallExprContext ctx) {
        return new MethodCallExpr(ctx.Substring_LLC().getText(),
                                  visitTextOrMember(ctx.textOrMember()),
                                  visitArithmeticExpr(ctx.arithmeticExpr()));
    }

    @Override public TextExpr visitConcatMethodCallExpr(ODataQueryParserParser.ConcatMethodCallExprContext ctx) {
        return new MethodCallExpr(ctx.Concat_LLC().getText(),
                                  visitTextOrMember(ctx.textOrMember(0)),
                                  visitTextOrMember(ctx.textOrMember(1)));
    }

    @Override public Expr visitParenExpr(ODataQueryParserParser.ParenExprContext ctx) {
        return visitAnyExpr(ctx.anyExpr());
    }
}
