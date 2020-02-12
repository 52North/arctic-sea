///*
// * Copyright 2015-2020 52°North Initiative for Geospatial Open Source
// * Software GmbH
// *
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// *    http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//
//package org.n52.svalbard.odata;
//
//import org.antlr.v4.runtime.tree.TerminalNode;
//import org.n52.shetland.filter.CountFilter;
//import org.n52.shetland.filter.ExpandFilter;
//import org.n52.shetland.filter.OrderByFilter;
//import org.n52.shetland.filter.OrderProperty;
//import org.n52.shetland.filter.PathFilterItem;
//import org.n52.shetland.filter.SelectFilter;
//import org.n52.shetland.filter.SkipTopFilter;
//import org.n52.shetland.oasis.odata.query.option.QueryOptions;
//import org.n52.shetland.ogc.filter.AbstractSelectionClause;
//import org.n52.shetland.ogc.filter.FilterClause;
//import org.n52.shetland.ogc.filter.FilterConstants;
//import org.n52.svalbard.odata.expr.BooleanBinaryExpr;
//import org.n52.svalbard.odata.expr.BooleanExpr;
//import org.n52.svalbard.odata.expr.BooleanUnaryExpr;
//import org.n52.svalbard.odata.expr.ComparisonExpr;
//import org.n52.svalbard.odata.expr.Expr;
//import org.n52.svalbard.odata.expr.NumericValueExpr;
//import org.n52.svalbard.odata.expr.SimpleArithmeticExpr;
//import org.n52.svalbard.odata.grammar.ODataQueryParserBaseVisitor;
//import org.n52.svalbard.odata.grammar.ODataQueryParserParser;
//
//import java.util.Collections;
//import java.util.HashSet;
//import java.util.Set;
//
///**
// * @author <a href="mailto:j.speckamp@52north.org">Jan Speckamp</a>
// */
//public class ODataQueryVisitor extends ODataQueryParserBaseVisitor {
//
//    @Override public QueryOptions visitQueryOptions(ODataQueryParserParser.QueryOptionsContext ctx) {
//        Set<FilterClause> qops = new HashSet<>();
//        for (ODataQueryParserParser.SystemQueryOptionContext systemQueryOptionContext : ctx.systemQueryOption()) {
//            qops.add(this.visitSystemQueryOption(systemQueryOptionContext));
//        }
//        return new QueryOptions("", qops);
//    }
//
//    @Override public FilterClause visitSystemQueryOption(ODataQueryParserParser.SystemQueryOptionContext ctx) {
//        if (ctx.count() != null) {
//            return this.visitCount(ctx.count());
//        }
//        if (ctx.expand() != null) {
//            return this.visitExpand(ctx.expand());
//        }
//        if (ctx.filter() != null) {
//            return this.visitFilter(ctx.filter());
//        }
//        if (ctx.orderby() != null) {
//            return this.visitOrderby(ctx.orderby());
//        }
//        if (ctx.select() != null) {
//            return this.visitSelect(ctx.select());
//        }
//        if (ctx.skip() != null) {
//            return this.visitSkip(ctx.skip());
//        }
//        if (ctx.top() != null) {
//            return this.visitTop(ctx.top());
//        }
//        return null;
//    }
//
//    @Override public SkipTopFilter visitSkip(ODataQueryParserParser.SkipContext ctx) {
//        return new SkipTopFilter(FilterConstants.SkipTopOperator.Skip, Long.parseLong(ctx.DecimalLiteral().getText()));
//    }
//
//    @Override public SkipTopFilter visitTop(ODataQueryParserParser.TopContext ctx) {
//        return new SkipTopFilter(FilterConstants.SkipTopOperator.Top, Long.parseLong(ctx.DecimalLiteral().getText()));
//    }
//
//    //TODO: check if we would like to also allow $count=false in the url
//    @Override public CountFilter visitCount(ODataQueryParserParser.CountContext ctx) {
//        return new CountFilter(true);
//    }
//
//    @Override public SelectFilter visitSelect(ODataQueryParserParser.SelectContext ctx) {
//        SelectFilter selectFilter = new SelectFilter();
//        for (ODataQueryParserParser.SelectItemContext selectItemContext : ctx.selectItem()) {
//            selectFilter.addItem(this.visitSelectItem(selectItemContext));
//        }
//        return selectFilter;
//    }
//
//    // TODO: expand grammar to allow for nested expressions on selectItem
//    // e.g.Customers?$select=Addresses($filter=startswith(City,'H');$top=5;)&$expand=Addresses/Country
//    @Override public PathFilterItem visitSelectItem(ODataQueryParserParser.SelectItemContext ctx) {
//        return new PathFilterItem(ctx.AlphaPlus().getText(), Collections.emptySet());
//    }
//
//    @Override public OrderByFilter visitOrderby(ODataQueryParserParser.OrderbyContext ctx) {
//        OrderByFilter orderByFilter = new OrderByFilter();
//        for (ODataQueryParserParser.OrderbyItemContext orderbyItemContext : ctx.orderbyItem()) {
//            orderByFilter.addSortProperty(this.visitOrderbyItem(orderbyItemContext));
//        }
//        return orderByFilter;
//    }
//
//    @Override public OrderProperty visitOrderbyItem(ODataQueryParserParser.OrderbyItemContext ctx) {
//        if (ctx.Asc_LLC() != null) {
//            return new OrderProperty(ctx.commonExpr().getText(), FilterConstants.SortOrder.ASC);
//        } else if (ctx.Desc_LLC() != null) {
//            return new OrderProperty(ctx.commonExpr().getText(), FilterConstants.SortOrder.DESC);
//        } else {
//            return new OrderProperty(ctx.commonExpr().getText());
//        }
//    }
//
//    @Override public ExpandFilter visitExpand(ODataQueryParserParser.ExpandContext ctx) {
//        ExpandFilter expandFilter = new ExpandFilter();
//        for (ODataQueryParserParser.ExpandItemContext expandItemContext : ctx.expandItem()) {
//            expandFilter.addItem(this.visitExpandItem(expandItemContext));
//        }
//        return expandFilter;
//    }
//
//    @Override public PathFilterItem visitExpandItem(ODataQueryParserParser.ExpandItemContext ctx) {
//        StringBuilder path = new StringBuilder();
//        for (TerminalNode navigation : ctx.AlphaPlus()) {
//            path.append(navigation);
//            path.append("/");
//        }
//        path.deleteCharAt(path.length() - 1);
//
//        if (ctx.OP() != null) {
//            Set<FilterClause> options = new HashSet<>();
//            for (ODataQueryParserParser.SystemQueryOptionContext expandQueryOptions : ctx.systemQueryOption()) {
//                options.add(this.visitSystemQueryOption(expandQueryOptions));
//            }
//            return new PathFilterItem(path.toString(), options);
//        } else {
//            return new PathFilterItem(path.toString(), Collections.emptySet());
//        }
//    }
//
//    @Override public BooleanExpr visitFilter(ODataQueryParserParser.FilterContext ctx) {
//        return this.visitBoolCommonExpr(ctx.boolCommonExpr());
//    }
//
//    // boolCommonExpr
//    //  : (boolMethodCallExpr | notExpr | commonExpr (eqExpr | neExpr | ltExpr | leExpr | gtExpr | geExpr) |
//    //  boolParenExpr) (andExpr | orExpr)?
//    //  ;
//    @Override public BooleanExpr visitBoolCommonExpr(ODataQueryParserParser.BoolCommonExprContext ctx) {
//        BooleanExpr left;
//
//        if (ctx.boolMethodCallExpr() != null) {
//            left = this.visitBoolMethodCallExpr(ctx.boolMethodCallExpr());
//        } else if (ctx.boolParenExpr() != null) {
//            left = this.visitBoolParenExpr(ctx.boolParenExpr());
//        } else if (ctx.notExpr() != null) {
//            left = new BooleanUnaryExpr(FilterConstants.UnaryLogicOperator.Not,
//                                        this.visitNotExpr(ctx.notExpr()));
//        } else if (ctx.commonExpr() != null) {
//            Expr right;
//            Expr common = this.visitCommonExpr(ctx.commonExpr());
//
//            if (ctx.eqExpr() != null) {
//                right = this.visitEqExpr(ctx.eqExpr());
//                left = new ComparisonExpr(FilterConstants.ComparisonOperator.PropertyIsEqualTo,
//                                          common,
//                                          right);
//            } else if (ctx.neExpr() != null) {
//                right = this.visitNeExpr(ctx.neExpr());
//                left = new ComparisonExpr(FilterConstants.ComparisonOperator.PropertyIsNotEqualTo,
//                                          common,
//
//                                          right);
//            } else if (ctx.ltExpr() != null) {
//                right = this.visitLtExpr(ctx.ltExpr());
//                left = new ComparisonExpr(FilterConstants.ComparisonOperator.PropertyIsLessThan,
//                                          common,
//                                          right);
//            } else if (ctx.leExpr() != null) {
//                right = this.visitLeExpr(ctx.leExpr());
//                left = new ComparisonExpr(FilterConstants.ComparisonOperator.PropertyIsLessThanOrEqualTo,
//                                          common,
//                                          right);
//            } else if (ctx.gtExpr() != null) {
//                right = this.visitGtExpr(ctx.gtExpr());
//                left = new ComparisonExpr(FilterConstants.ComparisonOperator.PropertyIsGreaterThan,
//                                          common,
//                                          right);
//            } else if (ctx.geExpr() != null) {
//                right = this.visitGeExpr(ctx.geExpr());
//                left = new ComparisonExpr(FilterConstants.ComparisonOperator.PropertyIsGreaterThanOrEqualTo,
//                                          common,
//                                          right);
//
//            }
//        }
//
//        // Handle appended AND or OR
//        if (ctx.andExpr() != null) {
//            BooleanExpr right = visitAndExpr(ctx.andExpr());
//            return new BooleanBinaryExpr(FilterConstants.BinaryLogicOperator.And,
//                                         left,
//                                         right);
//        } else if (ctx.orExpr() != null) {
//            BooleanExpr right = visitOrExpr(ctx.orExpr());
//            return new BooleanBinaryExpr(FilterConstants.BinaryLogicOperator.Or,
//                                         left,
//                                         right);
//        }
//        // return null;
//    }
//
//    @Override public Expr visitCommonExpr(ODataQueryParserParser.CommonExprContext ctx) {
//        //TODO!!!
//        // commonExpr
//        //   : (primitiveLiteral | memberExpr | negateExpr | methodCallExpr | parenExpr) (addExpr | subExpr | mulExpr
//        //   | divExpr | modExpr)?
//        //   ;
//        Expr left;
//        if (ctx.primitiveLiteral() != null) {
//            left = this.visitPrimitiveLiteral(ctx.primitiveLiteral());
//        } else if (ctx.memberExpr() != null) {
//            left = this.visitMemberExpr(ctx.memberExpr());
//        } else if (ctx.negateExpr() != null) {
//            left = this.visitNegateExpr(ctx.negateExpr());
//        } else if (ctx.methodCallExpr() != null) {
//            left = this.visitMethodCallExpr(ctx.methodCallExpr());
//        } else if (ctx.parenExpr() != null) {
//            left = this.visitParenExpr(ctx.parenExpr());
//        }
//
//        Expr right;
//        if (ctx.addExpr() != null) {
//            right = visitAddExpr(ctx.addExpr());
//            return new SimpleArithmeticExpr(FilterConstants.SimpleArithmeticOperator.Add, left, right);
//        } else if (ctx.subExpr() != null) {
//            right = visitSubExpr(ctx.subExpr());
//            return new SimpleArithmeticExpr(FilterConstants.SimpleArithmeticOperator.Sub, left, right);
//        } else if (ctx.mulExpr() != null) {
//            right = visitMulExpr(ctx.mulExpr());
//            return new SimpleArithmeticExpr(FilterConstants.SimpleArithmeticOperator.Mul, left, right);
//        } else if (ctx.divExpr() != null) {
//            right = visitDivExpr(ctx.divExpr());
//            return new SimpleArithmeticExpr(FilterConstants.SimpleArithmeticOperator.Div, left, right);
//        } else if (ctx.modExpr() != null) {
//            right = visitModExpr(ctx.modExpr());
//            return new SimpleArithmeticExpr(FilterConstants.SimpleArithmeticOperator.Mod, left, right);
//        } else {
//            // There is no right Expr. return left only
//            return left;
//        }
//    }
//
//    @Override public BooleanExpr visitNotExpr(ODataQueryParserParser.NotExprContext ctx) {
//        return this.visitBoolCommonExpr(ctx.boolCommonExpr());
//    }
//
//    @Override public Object visitPrimitiveLiteral(ODataQueryParserParser.PrimitiveLiteralContext ctx) {
//        if (ctx.DecimalLiteral() != null) {
//            return new NumericValueExpr ctx.DecimalLiteral().getText();
//        }
//    }
//
//    //TODO: check if we need to do something here to preserve precedence or if we can just leave out parentheses and
//    // precedence is given due to context
//    @Override public BooleanExpr visitBoolParenExpr(ODataQueryParserParser.BoolParenExprContext ctx) {
//        return this.visitBoolCommonExpr(ctx.boolCommonExpr());
//    }
//
//    @Override public BooleanExpr visitAndExpr(ODataQueryParserParser.AndExprContext ctx) {
//        return this.visitBoolCommonExpr(ctx.boolCommonExpr());
//    }
//
//    @Override public BooleanExpr visitOrExpr(ODataQueryParserParser.OrExprContext ctx) {
//        return this.visitBoolCommonExpr(ctx.boolCommonExpr());
//    }
//
//    @Override public Expr visitEqExpr(ODataQueryParserParser.EqExprContext ctx) {
//        return this.visitCommonExpr(ctx.commonExpr());
//    }
//
//    @Override public Expr visitNeExpr(ODataQueryParserParser.NeExprContext ctx) {
//        return this.visitCommonExpr(ctx.commonExpr());
//    }
//
//    @Override public Expr visitLtExpr(ODataQueryParserParser.LtExprContext ctx) {
//        return this.visitCommonExpr(ctx.commonExpr());
//    }
//
//    @Override public Expr visitLeExpr(ODataQueryParserParser.LeExprContext ctx) {
//        return this.visitCommonExpr(ctx.commonExpr());
//    }
//
//    @Override public Expr visitGtExpr(ODataQueryParserParser.GtExprContext ctx) {
//        return this.visitCommonExpr(ctx.commonExpr());
//    }
//
//    @Override public Expr visitGeExpr(ODataQueryParserParser.GeExprContext ctx) {
//        return this.visitCommonExpr(ctx.commonExpr());
//    }
//
//    @Override public Expr visitAddExpr(ODataQueryParserParser.AddExprContext ctx) {
//        return this.visitCommonExpr(ctx.commonExpr());
//    }
//
//    @Override public Expr visitSubExpr(ODataQueryParserParser.SubExprContext ctx) {
//        return this.visitCommonExpr(ctx.commonExpr());
//    }
//
//    @Override public Expr visitMulExpr(ODataQueryParserParser.MulExprContext ctx) {
//        return this.visitCommonExpr(ctx.commonExpr());
//    }
//
//    @Override public Expr visitDivExpr(ODataQueryParserParser.DivExprContext ctx) {
//        return this.visitCommonExpr(ctx.commonExpr());
//    }
//
//    @Override public Expr visitModExpr(ODataQueryParserParser.ModExprContext ctx) {
//        return this.visitCommonExpr(ctx.commonExpr());
//    }
//}
