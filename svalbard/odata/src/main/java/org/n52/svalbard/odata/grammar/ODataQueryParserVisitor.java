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
// Generated from org/n52/svalbard/odata/grammar/ODataQueryParser.g4 by ANTLR 4.8
package org.n52.svalbard.odata.grammar;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link ODataQueryParserParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface ODataQueryParserVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link ODataQueryParserParser#queryOptions}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitQueryOptions(ODataQueryParserParser.QueryOptionsContext ctx);
	/**
	 * Visit a parse tree produced by {@link ODataQueryParserParser#systemQueryOption}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSystemQueryOption(ODataQueryParserParser.SystemQueryOptionContext ctx);
	/**
	 * Visit a parse tree produced by {@link ODataQueryParserParser#count}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCount(ODataQueryParserParser.CountContext ctx);
	/**
	 * Visit a parse tree produced by {@link ODataQueryParserParser#expand}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpand(ODataQueryParserParser.ExpandContext ctx);
	/**
	 * Visit a parse tree produced by {@link ODataQueryParserParser#expandItem}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpandItem(ODataQueryParserParser.ExpandItemContext ctx);
	/**
	 * Visit a parse tree produced by {@link ODataQueryParserParser#filter}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFilter(ODataQueryParserParser.FilterContext ctx);
	/**
	 * Visit a parse tree produced by {@link ODataQueryParserParser#orderby}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOrderby(ODataQueryParserParser.OrderbyContext ctx);
	/**
	 * Visit a parse tree produced by {@link ODataQueryParserParser#orderbyItem}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOrderbyItem(ODataQueryParserParser.OrderbyItemContext ctx);
	/**
	 * Visit a parse tree produced by {@link ODataQueryParserParser#skip}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSkip(ODataQueryParserParser.SkipContext ctx);
	/**
	 * Visit a parse tree produced by {@link ODataQueryParserParser#top}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTop(ODataQueryParserParser.TopContext ctx);
	/**
	 * Visit a parse tree produced by {@link ODataQueryParserParser#select}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSelect(ODataQueryParserParser.SelectContext ctx);
	/**
	 * Visit a parse tree produced by {@link ODataQueryParserParser#selectItem}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSelectItem(ODataQueryParserParser.SelectItemContext ctx);
	/**
	 * Visit a parse tree produced by {@link ODataQueryParserParser#boolExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBoolExpr(ODataQueryParserParser.BoolExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link ODataQueryParserParser#boolParenExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBoolParenExpr(ODataQueryParserParser.BoolParenExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link ODataQueryParserParser#anyExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAnyExpr(ODataQueryParserParser.AnyExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link ODataQueryParserParser#parenExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParenExpr(ODataQueryParserParser.ParenExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link ODataQueryParserParser#arithmeticExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArithmeticExpr(ODataQueryParserParser.ArithmeticExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link ODataQueryParserParser#timeExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTimeExpr(ODataQueryParserParser.TimeExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link ODataQueryParserParser#textExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTextExpr(ODataQueryParserParser.TextExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link ODataQueryParserParser#geoExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGeoExpr(ODataQueryParserParser.GeoExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link ODataQueryParserParser#memberExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMemberExpr(ODataQueryParserParser.MemberExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link ODataQueryParserParser#textMethodCallExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTextMethodCallExpr(ODataQueryParserParser.TextMethodCallExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link ODataQueryParserParser#arithmeticMethodCallExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArithmeticMethodCallExpr(ODataQueryParserParser.ArithmeticMethodCallExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link ODataQueryParserParser#temporalMethodCallExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTemporalMethodCallExpr(ODataQueryParserParser.TemporalMethodCallExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link ODataQueryParserParser#boolMethodCallExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBoolMethodCallExpr(ODataQueryParserParser.BoolMethodCallExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link ODataQueryParserParser#textOrMember}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTextOrMember(ODataQueryParserParser.TextOrMemberContext ctx);
	/**
	 * Visit a parse tree produced by {@link ODataQueryParserParser#temporalOrMemberOrString}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTemporalOrMemberOrString(ODataQueryParserParser.TemporalOrMemberOrStringContext ctx);
	/**
	 * Visit a parse tree produced by {@link ODataQueryParserParser#geoOrMember}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGeoOrMember(ODataQueryParserParser.GeoOrMemberContext ctx);
	/**
	 * Visit a parse tree produced by {@link ODataQueryParserParser#substringMethodCallExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSubstringMethodCallExpr(ODataQueryParserParser.SubstringMethodCallExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link ODataQueryParserParser#toLowerMethodCallExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitToLowerMethodCallExpr(ODataQueryParserParser.ToLowerMethodCallExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link ODataQueryParserParser#toUpperMethodCallExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitToUpperMethodCallExpr(ODataQueryParserParser.ToUpperMethodCallExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link ODataQueryParserParser#trimMethodCallExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTrimMethodCallExpr(ODataQueryParserParser.TrimMethodCallExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link ODataQueryParserParser#concatMethodCallExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConcatMethodCallExpr(ODataQueryParserParser.ConcatMethodCallExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link ODataQueryParserParser#substringOfMethodCallExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSubstringOfMethodCallExpr(ODataQueryParserParser.SubstringOfMethodCallExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link ODataQueryParserParser#startsWithMethodCallExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStartsWithMethodCallExpr(ODataQueryParserParser.StartsWithMethodCallExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link ODataQueryParserParser#endsWithMethodCallExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEndsWithMethodCallExpr(ODataQueryParserParser.EndsWithMethodCallExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link ODataQueryParserParser#intersectsMethodCallExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIntersectsMethodCallExpr(ODataQueryParserParser.IntersectsMethodCallExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link ODataQueryParserParser#st_commonMethodCallExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSt_commonMethodCallExpr(ODataQueryParserParser.St_commonMethodCallExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link ODataQueryParserParser#st_equalsMethodCallExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSt_equalsMethodCallExpr(ODataQueryParserParser.St_equalsMethodCallExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link ODataQueryParserParser#st_disjointMethodCallExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSt_disjointMethodCallExpr(ODataQueryParserParser.St_disjointMethodCallExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link ODataQueryParserParser#st_touchesMethodCallExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSt_touchesMethodCallExpr(ODataQueryParserParser.St_touchesMethodCallExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link ODataQueryParserParser#st_withinMethodCallExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSt_withinMethodCallExpr(ODataQueryParserParser.St_withinMethodCallExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link ODataQueryParserParser#st_overlapsMethodCallExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSt_overlapsMethodCallExpr(ODataQueryParserParser.St_overlapsMethodCallExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link ODataQueryParserParser#st_crossesMethodCallExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSt_crossesMethodCallExpr(ODataQueryParserParser.St_crossesMethodCallExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link ODataQueryParserParser#st_intersectsMethodCallExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSt_intersectsMethodCallExpr(ODataQueryParserParser.St_intersectsMethodCallExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link ODataQueryParserParser#st_containsMethodCallExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSt_containsMethodCallExpr(ODataQueryParserParser.St_containsMethodCallExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link ODataQueryParserParser#st_relateMethodCallExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSt_relateMethodCallExpr(ODataQueryParserParser.St_relateMethodCallExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link ODataQueryParserParser#lengthMethodCallExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLengthMethodCallExpr(ODataQueryParserParser.LengthMethodCallExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link ODataQueryParserParser#indexOfMethodCallExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIndexOfMethodCallExpr(ODataQueryParserParser.IndexOfMethodCallExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link ODataQueryParserParser#yearMethodCallExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitYearMethodCallExpr(ODataQueryParserParser.YearMethodCallExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link ODataQueryParserParser#monthMethodCallExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMonthMethodCallExpr(ODataQueryParserParser.MonthMethodCallExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link ODataQueryParserParser#dayMethodCallExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDayMethodCallExpr(ODataQueryParserParser.DayMethodCallExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link ODataQueryParserParser#daysMethodCallExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDaysMethodCallExpr(ODataQueryParserParser.DaysMethodCallExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link ODataQueryParserParser#hourMethodCallExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitHourMethodCallExpr(ODataQueryParserParser.HourMethodCallExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link ODataQueryParserParser#minuteMethodCallExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMinuteMethodCallExpr(ODataQueryParserParser.MinuteMethodCallExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link ODataQueryParserParser#secondMethodCallExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSecondMethodCallExpr(ODataQueryParserParser.SecondMethodCallExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link ODataQueryParserParser#timeMethodCallExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTimeMethodCallExpr(ODataQueryParserParser.TimeMethodCallExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link ODataQueryParserParser#dateMethodCallExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDateMethodCallExpr(ODataQueryParserParser.DateMethodCallExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link ODataQueryParserParser#roundMethodCallExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRoundMethodCallExpr(ODataQueryParserParser.RoundMethodCallExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link ODataQueryParserParser#floorMethodCallExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFloorMethodCallExpr(ODataQueryParserParser.FloorMethodCallExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link ODataQueryParserParser#ceilingMethodCallExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCeilingMethodCallExpr(ODataQueryParserParser.CeilingMethodCallExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link ODataQueryParserParser#totalOffsetMinutesExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTotalOffsetMinutesExpr(ODataQueryParserParser.TotalOffsetMinutesExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link ODataQueryParserParser#distanceMethodCallExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDistanceMethodCallExpr(ODataQueryParserParser.DistanceMethodCallExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link ODataQueryParserParser#geoLengthMethodCallExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGeoLengthMethodCallExpr(ODataQueryParserParser.GeoLengthMethodCallExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link ODataQueryParserParser#minDate}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMinDate(ODataQueryParserParser.MinDateContext ctx);
	/**
	 * Visit a parse tree produced by {@link ODataQueryParserParser#maxDate}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMaxDate(ODataQueryParserParser.MaxDateContext ctx);
	/**
	 * Visit a parse tree produced by {@link ODataQueryParserParser#nowDate}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNowDate(ODataQueryParserParser.NowDateContext ctx);
	/**
	 * Visit a parse tree produced by {@link ODataQueryParserParser#andExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAndExpr(ODataQueryParserParser.AndExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link ODataQueryParserParser#orExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOrExpr(ODataQueryParserParser.OrExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link ODataQueryParserParser#notExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNotExpr(ODataQueryParserParser.NotExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link ODataQueryParserParser#eqExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEqExpr(ODataQueryParserParser.EqExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link ODataQueryParserParser#neExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNeExpr(ODataQueryParserParser.NeExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link ODataQueryParserParser#ltExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLtExpr(ODataQueryParserParser.LtExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link ODataQueryParserParser#leExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLeExpr(ODataQueryParserParser.LeExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link ODataQueryParserParser#gtExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGtExpr(ODataQueryParserParser.GtExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link ODataQueryParserParser#geExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGeExpr(ODataQueryParserParser.GeExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link ODataQueryParserParser#addExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAddExpr(ODataQueryParserParser.AddExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link ODataQueryParserParser#subExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSubExpr(ODataQueryParserParser.SubExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link ODataQueryParserParser#mulExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMulExpr(ODataQueryParserParser.MulExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link ODataQueryParserParser#divExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDivExpr(ODataQueryParserParser.DivExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link ODataQueryParserParser#modExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitModExpr(ODataQueryParserParser.ModExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link ODataQueryParserParser#negateExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNegateExpr(ODataQueryParserParser.NegateExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link ODataQueryParserParser#numericLiteral}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNumericLiteral(ODataQueryParserParser.NumericLiteralContext ctx);
	/**
	 * Visit a parse tree produced by {@link ODataQueryParserParser#sq_enclosed_string}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSq_enclosed_string(ODataQueryParserParser.Sq_enclosed_stringContext ctx);
	/**
	 * Visit a parse tree produced by {@link ODataQueryParserParser#geographyCollection}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGeographyCollection(ODataQueryParserParser.GeographyCollectionContext ctx);
	/**
	 * Visit a parse tree produced by {@link ODataQueryParserParser#fullCollectionLiteral}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFullCollectionLiteral(ODataQueryParserParser.FullCollectionLiteralContext ctx);
	/**
	 * Visit a parse tree produced by {@link ODataQueryParserParser#collectionLiteral}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCollectionLiteral(ODataQueryParserParser.CollectionLiteralContext ctx);
	/**
	 * Visit a parse tree produced by {@link ODataQueryParserParser#geoLiteral}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGeoLiteral(ODataQueryParserParser.GeoLiteralContext ctx);
	/**
	 * Visit a parse tree produced by {@link ODataQueryParserParser#geographyLineString}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGeographyLineString(ODataQueryParserParser.GeographyLineStringContext ctx);
	/**
	 * Visit a parse tree produced by {@link ODataQueryParserParser#fullLineStringLiteral}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFullLineStringLiteral(ODataQueryParserParser.FullLineStringLiteralContext ctx);
	/**
	 * Visit a parse tree produced by {@link ODataQueryParserParser#lineStringLiteral}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLineStringLiteral(ODataQueryParserParser.LineStringLiteralContext ctx);
	/**
	 * Visit a parse tree produced by {@link ODataQueryParserParser#lineStringData}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLineStringData(ODataQueryParserParser.LineStringDataContext ctx);
	/**
	 * Visit a parse tree produced by {@link ODataQueryParserParser#geographyMultiLineString}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGeographyMultiLineString(ODataQueryParserParser.GeographyMultiLineStringContext ctx);
	/**
	 * Visit a parse tree produced by {@link ODataQueryParserParser#fullMultiLineStringLiteral}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFullMultiLineStringLiteral(ODataQueryParserParser.FullMultiLineStringLiteralContext ctx);
	/**
	 * Visit a parse tree produced by {@link ODataQueryParserParser#multiLineStringLiteral}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMultiLineStringLiteral(ODataQueryParserParser.MultiLineStringLiteralContext ctx);
	/**
	 * Visit a parse tree produced by {@link ODataQueryParserParser#geographyMultiPoint}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGeographyMultiPoint(ODataQueryParserParser.GeographyMultiPointContext ctx);
	/**
	 * Visit a parse tree produced by {@link ODataQueryParserParser#fullMultiPointLiteral}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFullMultiPointLiteral(ODataQueryParserParser.FullMultiPointLiteralContext ctx);
	/**
	 * Visit a parse tree produced by {@link ODataQueryParserParser#multiPointLiteral}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMultiPointLiteral(ODataQueryParserParser.MultiPointLiteralContext ctx);
	/**
	 * Visit a parse tree produced by {@link ODataQueryParserParser#geographyMultiPolygon}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGeographyMultiPolygon(ODataQueryParserParser.GeographyMultiPolygonContext ctx);
	/**
	 * Visit a parse tree produced by {@link ODataQueryParserParser#fullMultiPolygonLiteral}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFullMultiPolygonLiteral(ODataQueryParserParser.FullMultiPolygonLiteralContext ctx);
	/**
	 * Visit a parse tree produced by {@link ODataQueryParserParser#multiPolygonLiteral}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMultiPolygonLiteral(ODataQueryParserParser.MultiPolygonLiteralContext ctx);
	/**
	 * Visit a parse tree produced by {@link ODataQueryParserParser#geographyPoint}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGeographyPoint(ODataQueryParserParser.GeographyPointContext ctx);
	/**
	 * Visit a parse tree produced by {@link ODataQueryParserParser#fullPointLiteral}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFullPointLiteral(ODataQueryParserParser.FullPointLiteralContext ctx);
	/**
	 * Visit a parse tree produced by {@link ODataQueryParserParser#sridLiteral}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSridLiteral(ODataQueryParserParser.SridLiteralContext ctx);
	/**
	 * Visit a parse tree produced by {@link ODataQueryParserParser#pointLiteral}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPointLiteral(ODataQueryParserParser.PointLiteralContext ctx);
	/**
	 * Visit a parse tree produced by {@link ODataQueryParserParser#pointData}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPointData(ODataQueryParserParser.PointDataContext ctx);
	/**
	 * Visit a parse tree produced by {@link ODataQueryParserParser#positionLiteral}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPositionLiteral(ODataQueryParserParser.PositionLiteralContext ctx);
	/**
	 * Visit a parse tree produced by {@link ODataQueryParserParser#coordinate}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCoordinate(ODataQueryParserParser.CoordinateContext ctx);
	/**
	 * Visit a parse tree produced by {@link ODataQueryParserParser#geographyPolygon}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGeographyPolygon(ODataQueryParserParser.GeographyPolygonContext ctx);
	/**
	 * Visit a parse tree produced by {@link ODataQueryParserParser#fullPolygonLiteral}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFullPolygonLiteral(ODataQueryParserParser.FullPolygonLiteralContext ctx);
	/**
	 * Visit a parse tree produced by {@link ODataQueryParserParser#polygonLiteral}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPolygonLiteral(ODataQueryParserParser.PolygonLiteralContext ctx);
	/**
	 * Visit a parse tree produced by {@link ODataQueryParserParser#polygonData}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPolygonData(ODataQueryParserParser.PolygonDataContext ctx);
	/**
	 * Visit a parse tree produced by {@link ODataQueryParserParser#ringLiteral}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRingLiteral(ODataQueryParserParser.RingLiteralContext ctx);
	/**
	 * Visit a parse tree produced by {@link ODataQueryParserParser#geometryCollection}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGeometryCollection(ODataQueryParserParser.GeometryCollectionContext ctx);
	/**
	 * Visit a parse tree produced by {@link ODataQueryParserParser#geometryLineString}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGeometryLineString(ODataQueryParserParser.GeometryLineStringContext ctx);
	/**
	 * Visit a parse tree produced by {@link ODataQueryParserParser#geometryMultiLineString}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGeometryMultiLineString(ODataQueryParserParser.GeometryMultiLineStringContext ctx);
	/**
	 * Visit a parse tree produced by {@link ODataQueryParserParser#geometryMultiPoint}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGeometryMultiPoint(ODataQueryParserParser.GeometryMultiPointContext ctx);
	/**
	 * Visit a parse tree produced by {@link ODataQueryParserParser#geometryMultiPolygon}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGeometryMultiPolygon(ODataQueryParserParser.GeometryMultiPolygonContext ctx);
	/**
	 * Visit a parse tree produced by {@link ODataQueryParserParser#geometryPoint}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGeometryPoint(ODataQueryParserParser.GeometryPointContext ctx);
	/**
	 * Visit a parse tree produced by {@link ODataQueryParserParser#geometryPolygon}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGeometryPolygon(ODataQueryParserParser.GeometryPolygonContext ctx);
	/**
	 * Visit a parse tree produced by {@link ODataQueryParserParser#geographyPrefix}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGeographyPrefix(ODataQueryParserParser.GeographyPrefixContext ctx);
	/**
	 * Visit a parse tree produced by {@link ODataQueryParserParser#geometryPrefix}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGeometryPrefix(ODataQueryParserParser.GeometryPrefixContext ctx);
}