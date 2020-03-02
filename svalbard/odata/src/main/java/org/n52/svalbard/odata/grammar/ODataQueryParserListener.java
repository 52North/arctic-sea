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
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link ODataQueryParserParser}.
 */
public interface ODataQueryParserListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link ODataQueryParserParser#queryOptions}.
	 * @param ctx the parse tree
	 */
	void enterQueryOptions(ODataQueryParserParser.QueryOptionsContext ctx);
	/**
	 * Exit a parse tree produced by {@link ODataQueryParserParser#queryOptions}.
	 * @param ctx the parse tree
	 */
	void exitQueryOptions(ODataQueryParserParser.QueryOptionsContext ctx);
	/**
	 * Enter a parse tree produced by {@link ODataQueryParserParser#systemQueryOption}.
	 * @param ctx the parse tree
	 */
	void enterSystemQueryOption(ODataQueryParserParser.SystemQueryOptionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ODataQueryParserParser#systemQueryOption}.
	 * @param ctx the parse tree
	 */
	void exitSystemQueryOption(ODataQueryParserParser.SystemQueryOptionContext ctx);
	/**
	 * Enter a parse tree produced by {@link ODataQueryParserParser#count}.
	 * @param ctx the parse tree
	 */
	void enterCount(ODataQueryParserParser.CountContext ctx);
	/**
	 * Exit a parse tree produced by {@link ODataQueryParserParser#count}.
	 * @param ctx the parse tree
	 */
	void exitCount(ODataQueryParserParser.CountContext ctx);
	/**
	 * Enter a parse tree produced by {@link ODataQueryParserParser#expand}.
	 * @param ctx the parse tree
	 */
	void enterExpand(ODataQueryParserParser.ExpandContext ctx);
	/**
	 * Exit a parse tree produced by {@link ODataQueryParserParser#expand}.
	 * @param ctx the parse tree
	 */
	void exitExpand(ODataQueryParserParser.ExpandContext ctx);
	/**
	 * Enter a parse tree produced by {@link ODataQueryParserParser#expandItem}.
	 * @param ctx the parse tree
	 */
	void enterExpandItem(ODataQueryParserParser.ExpandItemContext ctx);
	/**
	 * Exit a parse tree produced by {@link ODataQueryParserParser#expandItem}.
	 * @param ctx the parse tree
	 */
	void exitExpandItem(ODataQueryParserParser.ExpandItemContext ctx);
	/**
	 * Enter a parse tree produced by {@link ODataQueryParserParser#filter}.
	 * @param ctx the parse tree
	 */
	void enterFilter(ODataQueryParserParser.FilterContext ctx);
	/**
	 * Exit a parse tree produced by {@link ODataQueryParserParser#filter}.
	 * @param ctx the parse tree
	 */
	void exitFilter(ODataQueryParserParser.FilterContext ctx);
	/**
	 * Enter a parse tree produced by {@link ODataQueryParserParser#orderby}.
	 * @param ctx the parse tree
	 */
	void enterOrderby(ODataQueryParserParser.OrderbyContext ctx);
	/**
	 * Exit a parse tree produced by {@link ODataQueryParserParser#orderby}.
	 * @param ctx the parse tree
	 */
	void exitOrderby(ODataQueryParserParser.OrderbyContext ctx);
	/**
	 * Enter a parse tree produced by {@link ODataQueryParserParser#orderbyItem}.
	 * @param ctx the parse tree
	 */
	void enterOrderbyItem(ODataQueryParserParser.OrderbyItemContext ctx);
	/**
	 * Exit a parse tree produced by {@link ODataQueryParserParser#orderbyItem}.
	 * @param ctx the parse tree
	 */
	void exitOrderbyItem(ODataQueryParserParser.OrderbyItemContext ctx);
	/**
	 * Enter a parse tree produced by {@link ODataQueryParserParser#skip}.
	 * @param ctx the parse tree
	 */
	void enterSkip(ODataQueryParserParser.SkipContext ctx);
	/**
	 * Exit a parse tree produced by {@link ODataQueryParserParser#skip}.
	 * @param ctx the parse tree
	 */
	void exitSkip(ODataQueryParserParser.SkipContext ctx);
	/**
	 * Enter a parse tree produced by {@link ODataQueryParserParser#top}.
	 * @param ctx the parse tree
	 */
	void enterTop(ODataQueryParserParser.TopContext ctx);
	/**
	 * Exit a parse tree produced by {@link ODataQueryParserParser#top}.
	 * @param ctx the parse tree
	 */
	void exitTop(ODataQueryParserParser.TopContext ctx);
	/**
	 * Enter a parse tree produced by {@link ODataQueryParserParser#select}.
	 * @param ctx the parse tree
	 */
	void enterSelect(ODataQueryParserParser.SelectContext ctx);
	/**
	 * Exit a parse tree produced by {@link ODataQueryParserParser#select}.
	 * @param ctx the parse tree
	 */
	void exitSelect(ODataQueryParserParser.SelectContext ctx);
	/**
	 * Enter a parse tree produced by {@link ODataQueryParserParser#selectItem}.
	 * @param ctx the parse tree
	 */
	void enterSelectItem(ODataQueryParserParser.SelectItemContext ctx);
	/**
	 * Exit a parse tree produced by {@link ODataQueryParserParser#selectItem}.
	 * @param ctx the parse tree
	 */
	void exitSelectItem(ODataQueryParserParser.SelectItemContext ctx);
	/**
	 * Enter a parse tree produced by {@link ODataQueryParserParser#boolExpr}.
	 * @param ctx the parse tree
	 */
	void enterBoolExpr(ODataQueryParserParser.BoolExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link ODataQueryParserParser#boolExpr}.
	 * @param ctx the parse tree
	 */
	void exitBoolExpr(ODataQueryParserParser.BoolExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link ODataQueryParserParser#boolParenExpr}.
	 * @param ctx the parse tree
	 */
	void enterBoolParenExpr(ODataQueryParserParser.BoolParenExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link ODataQueryParserParser#boolParenExpr}.
	 * @param ctx the parse tree
	 */
	void exitBoolParenExpr(ODataQueryParserParser.BoolParenExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link ODataQueryParserParser#anyExpr}.
	 * @param ctx the parse tree
	 */
	void enterAnyExpr(ODataQueryParserParser.AnyExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link ODataQueryParserParser#anyExpr}.
	 * @param ctx the parse tree
	 */
	void exitAnyExpr(ODataQueryParserParser.AnyExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link ODataQueryParserParser#parenExpr}.
	 * @param ctx the parse tree
	 */
	void enterParenExpr(ODataQueryParserParser.ParenExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link ODataQueryParserParser#parenExpr}.
	 * @param ctx the parse tree
	 */
	void exitParenExpr(ODataQueryParserParser.ParenExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link ODataQueryParserParser#arithmeticExpr}.
	 * @param ctx the parse tree
	 */
	void enterArithmeticExpr(ODataQueryParserParser.ArithmeticExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link ODataQueryParserParser#arithmeticExpr}.
	 * @param ctx the parse tree
	 */
	void exitArithmeticExpr(ODataQueryParserParser.ArithmeticExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link ODataQueryParserParser#timeExpr}.
	 * @param ctx the parse tree
	 */
	void enterTimeExpr(ODataQueryParserParser.TimeExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link ODataQueryParserParser#timeExpr}.
	 * @param ctx the parse tree
	 */
	void exitTimeExpr(ODataQueryParserParser.TimeExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link ODataQueryParserParser#textExpr}.
	 * @param ctx the parse tree
	 */
	void enterTextExpr(ODataQueryParserParser.TextExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link ODataQueryParserParser#textExpr}.
	 * @param ctx the parse tree
	 */
	void exitTextExpr(ODataQueryParserParser.TextExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link ODataQueryParserParser#geoExpr}.
	 * @param ctx the parse tree
	 */
	void enterGeoExpr(ODataQueryParserParser.GeoExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link ODataQueryParserParser#geoExpr}.
	 * @param ctx the parse tree
	 */
	void exitGeoExpr(ODataQueryParserParser.GeoExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link ODataQueryParserParser#memberExpr}.
	 * @param ctx the parse tree
	 */
	void enterMemberExpr(ODataQueryParserParser.MemberExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link ODataQueryParserParser#memberExpr}.
	 * @param ctx the parse tree
	 */
	void exitMemberExpr(ODataQueryParserParser.MemberExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link ODataQueryParserParser#textMethodCallExpr}.
	 * @param ctx the parse tree
	 */
	void enterTextMethodCallExpr(ODataQueryParserParser.TextMethodCallExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link ODataQueryParserParser#textMethodCallExpr}.
	 * @param ctx the parse tree
	 */
	void exitTextMethodCallExpr(ODataQueryParserParser.TextMethodCallExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link ODataQueryParserParser#arithmeticMethodCallExpr}.
	 * @param ctx the parse tree
	 */
	void enterArithmeticMethodCallExpr(ODataQueryParserParser.ArithmeticMethodCallExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link ODataQueryParserParser#arithmeticMethodCallExpr}.
	 * @param ctx the parse tree
	 */
	void exitArithmeticMethodCallExpr(ODataQueryParserParser.ArithmeticMethodCallExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link ODataQueryParserParser#temporalMethodCallExpr}.
	 * @param ctx the parse tree
	 */
	void enterTemporalMethodCallExpr(ODataQueryParserParser.TemporalMethodCallExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link ODataQueryParserParser#temporalMethodCallExpr}.
	 * @param ctx the parse tree
	 */
	void exitTemporalMethodCallExpr(ODataQueryParserParser.TemporalMethodCallExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link ODataQueryParserParser#boolMethodCallExpr}.
	 * @param ctx the parse tree
	 */
	void enterBoolMethodCallExpr(ODataQueryParserParser.BoolMethodCallExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link ODataQueryParserParser#boolMethodCallExpr}.
	 * @param ctx the parse tree
	 */
	void exitBoolMethodCallExpr(ODataQueryParserParser.BoolMethodCallExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link ODataQueryParserParser#textOrMember}.
	 * @param ctx the parse tree
	 */
	void enterTextOrMember(ODataQueryParserParser.TextOrMemberContext ctx);
	/**
	 * Exit a parse tree produced by {@link ODataQueryParserParser#textOrMember}.
	 * @param ctx the parse tree
	 */
	void exitTextOrMember(ODataQueryParserParser.TextOrMemberContext ctx);
	/**
	 * Enter a parse tree produced by {@link ODataQueryParserParser#temporalOrMemberOrISO8601Timestamp}.
	 * @param ctx the parse tree
	 */
	void enterTemporalOrMemberOrISO8601Timestamp(ODataQueryParserParser.TemporalOrMemberOrISO8601TimestampContext ctx);
	/**
	 * Exit a parse tree produced by {@link ODataQueryParserParser#temporalOrMemberOrISO8601Timestamp}.
	 * @param ctx the parse tree
	 */
	void exitTemporalOrMemberOrISO8601Timestamp(ODataQueryParserParser.TemporalOrMemberOrISO8601TimestampContext ctx);
	/**
	 * Enter a parse tree produced by {@link ODataQueryParserParser#geoOrMember}.
	 * @param ctx the parse tree
	 */
	void enterGeoOrMember(ODataQueryParserParser.GeoOrMemberContext ctx);
	/**
	 * Exit a parse tree produced by {@link ODataQueryParserParser#geoOrMember}.
	 * @param ctx the parse tree
	 */
	void exitGeoOrMember(ODataQueryParserParser.GeoOrMemberContext ctx);
	/**
	 * Enter a parse tree produced by {@link ODataQueryParserParser#iso8601Timestamp}.
	 * @param ctx the parse tree
	 */
	void enterIso8601Timestamp(ODataQueryParserParser.Iso8601TimestampContext ctx);
	/**
	 * Exit a parse tree produced by {@link ODataQueryParserParser#iso8601Timestamp}.
	 * @param ctx the parse tree
	 */
	void exitIso8601Timestamp(ODataQueryParserParser.Iso8601TimestampContext ctx);
	/**
	 * Enter a parse tree produced by {@link ODataQueryParserParser#iso8601Timezone}.
	 * @param ctx the parse tree
	 */
	void enterIso8601Timezone(ODataQueryParserParser.Iso8601TimezoneContext ctx);
	/**
	 * Exit a parse tree produced by {@link ODataQueryParserParser#iso8601Timezone}.
	 * @param ctx the parse tree
	 */
	void exitIso8601Timezone(ODataQueryParserParser.Iso8601TimezoneContext ctx);
	/**
	 * Enter a parse tree produced by {@link ODataQueryParserParser#substringMethodCallExpr}.
	 * @param ctx the parse tree
	 */
	void enterSubstringMethodCallExpr(ODataQueryParserParser.SubstringMethodCallExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link ODataQueryParserParser#substringMethodCallExpr}.
	 * @param ctx the parse tree
	 */
	void exitSubstringMethodCallExpr(ODataQueryParserParser.SubstringMethodCallExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link ODataQueryParserParser#toLowerMethodCallExpr}.
	 * @param ctx the parse tree
	 */
	void enterToLowerMethodCallExpr(ODataQueryParserParser.ToLowerMethodCallExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link ODataQueryParserParser#toLowerMethodCallExpr}.
	 * @param ctx the parse tree
	 */
	void exitToLowerMethodCallExpr(ODataQueryParserParser.ToLowerMethodCallExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link ODataQueryParserParser#toUpperMethodCallExpr}.
	 * @param ctx the parse tree
	 */
	void enterToUpperMethodCallExpr(ODataQueryParserParser.ToUpperMethodCallExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link ODataQueryParserParser#toUpperMethodCallExpr}.
	 * @param ctx the parse tree
	 */
	void exitToUpperMethodCallExpr(ODataQueryParserParser.ToUpperMethodCallExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link ODataQueryParserParser#trimMethodCallExpr}.
	 * @param ctx the parse tree
	 */
	void enterTrimMethodCallExpr(ODataQueryParserParser.TrimMethodCallExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link ODataQueryParserParser#trimMethodCallExpr}.
	 * @param ctx the parse tree
	 */
	void exitTrimMethodCallExpr(ODataQueryParserParser.TrimMethodCallExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link ODataQueryParserParser#concatMethodCallExpr}.
	 * @param ctx the parse tree
	 */
	void enterConcatMethodCallExpr(ODataQueryParserParser.ConcatMethodCallExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link ODataQueryParserParser#concatMethodCallExpr}.
	 * @param ctx the parse tree
	 */
	void exitConcatMethodCallExpr(ODataQueryParserParser.ConcatMethodCallExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link ODataQueryParserParser#substringOfMethodCallExpr}.
	 * @param ctx the parse tree
	 */
	void enterSubstringOfMethodCallExpr(ODataQueryParserParser.SubstringOfMethodCallExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link ODataQueryParserParser#substringOfMethodCallExpr}.
	 * @param ctx the parse tree
	 */
	void exitSubstringOfMethodCallExpr(ODataQueryParserParser.SubstringOfMethodCallExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link ODataQueryParserParser#startsWithMethodCallExpr}.
	 * @param ctx the parse tree
	 */
	void enterStartsWithMethodCallExpr(ODataQueryParserParser.StartsWithMethodCallExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link ODataQueryParserParser#startsWithMethodCallExpr}.
	 * @param ctx the parse tree
	 */
	void exitStartsWithMethodCallExpr(ODataQueryParserParser.StartsWithMethodCallExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link ODataQueryParserParser#endsWithMethodCallExpr}.
	 * @param ctx the parse tree
	 */
	void enterEndsWithMethodCallExpr(ODataQueryParserParser.EndsWithMethodCallExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link ODataQueryParserParser#endsWithMethodCallExpr}.
	 * @param ctx the parse tree
	 */
	void exitEndsWithMethodCallExpr(ODataQueryParserParser.EndsWithMethodCallExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link ODataQueryParserParser#intersectsMethodCallExpr}.
	 * @param ctx the parse tree
	 */
	void enterIntersectsMethodCallExpr(ODataQueryParserParser.IntersectsMethodCallExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link ODataQueryParserParser#intersectsMethodCallExpr}.
	 * @param ctx the parse tree
	 */
	void exitIntersectsMethodCallExpr(ODataQueryParserParser.IntersectsMethodCallExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link ODataQueryParserParser#st_commonMethodCallExpr}.
	 * @param ctx the parse tree
	 */
	void enterSt_commonMethodCallExpr(ODataQueryParserParser.St_commonMethodCallExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link ODataQueryParserParser#st_commonMethodCallExpr}.
	 * @param ctx the parse tree
	 */
	void exitSt_commonMethodCallExpr(ODataQueryParserParser.St_commonMethodCallExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link ODataQueryParserParser#st_equalsMethodCallExpr}.
	 * @param ctx the parse tree
	 */
	void enterSt_equalsMethodCallExpr(ODataQueryParserParser.St_equalsMethodCallExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link ODataQueryParserParser#st_equalsMethodCallExpr}.
	 * @param ctx the parse tree
	 */
	void exitSt_equalsMethodCallExpr(ODataQueryParserParser.St_equalsMethodCallExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link ODataQueryParserParser#st_disjointMethodCallExpr}.
	 * @param ctx the parse tree
	 */
	void enterSt_disjointMethodCallExpr(ODataQueryParserParser.St_disjointMethodCallExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link ODataQueryParserParser#st_disjointMethodCallExpr}.
	 * @param ctx the parse tree
	 */
	void exitSt_disjointMethodCallExpr(ODataQueryParserParser.St_disjointMethodCallExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link ODataQueryParserParser#st_touchesMethodCallExpr}.
	 * @param ctx the parse tree
	 */
	void enterSt_touchesMethodCallExpr(ODataQueryParserParser.St_touchesMethodCallExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link ODataQueryParserParser#st_touchesMethodCallExpr}.
	 * @param ctx the parse tree
	 */
	void exitSt_touchesMethodCallExpr(ODataQueryParserParser.St_touchesMethodCallExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link ODataQueryParserParser#st_withinMethodCallExpr}.
	 * @param ctx the parse tree
	 */
	void enterSt_withinMethodCallExpr(ODataQueryParserParser.St_withinMethodCallExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link ODataQueryParserParser#st_withinMethodCallExpr}.
	 * @param ctx the parse tree
	 */
	void exitSt_withinMethodCallExpr(ODataQueryParserParser.St_withinMethodCallExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link ODataQueryParserParser#st_overlapsMethodCallExpr}.
	 * @param ctx the parse tree
	 */
	void enterSt_overlapsMethodCallExpr(ODataQueryParserParser.St_overlapsMethodCallExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link ODataQueryParserParser#st_overlapsMethodCallExpr}.
	 * @param ctx the parse tree
	 */
	void exitSt_overlapsMethodCallExpr(ODataQueryParserParser.St_overlapsMethodCallExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link ODataQueryParserParser#st_crossesMethodCallExpr}.
	 * @param ctx the parse tree
	 */
	void enterSt_crossesMethodCallExpr(ODataQueryParserParser.St_crossesMethodCallExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link ODataQueryParserParser#st_crossesMethodCallExpr}.
	 * @param ctx the parse tree
	 */
	void exitSt_crossesMethodCallExpr(ODataQueryParserParser.St_crossesMethodCallExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link ODataQueryParserParser#st_intersectsMethodCallExpr}.
	 * @param ctx the parse tree
	 */
	void enterSt_intersectsMethodCallExpr(ODataQueryParserParser.St_intersectsMethodCallExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link ODataQueryParserParser#st_intersectsMethodCallExpr}.
	 * @param ctx the parse tree
	 */
	void exitSt_intersectsMethodCallExpr(ODataQueryParserParser.St_intersectsMethodCallExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link ODataQueryParserParser#st_containsMethodCallExpr}.
	 * @param ctx the parse tree
	 */
	void enterSt_containsMethodCallExpr(ODataQueryParserParser.St_containsMethodCallExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link ODataQueryParserParser#st_containsMethodCallExpr}.
	 * @param ctx the parse tree
	 */
	void exitSt_containsMethodCallExpr(ODataQueryParserParser.St_containsMethodCallExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link ODataQueryParserParser#st_relateMethodCallExpr}.
	 * @param ctx the parse tree
	 */
	void enterSt_relateMethodCallExpr(ODataQueryParserParser.St_relateMethodCallExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link ODataQueryParserParser#st_relateMethodCallExpr}.
	 * @param ctx the parse tree
	 */
	void exitSt_relateMethodCallExpr(ODataQueryParserParser.St_relateMethodCallExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link ODataQueryParserParser#lengthMethodCallExpr}.
	 * @param ctx the parse tree
	 */
	void enterLengthMethodCallExpr(ODataQueryParserParser.LengthMethodCallExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link ODataQueryParserParser#lengthMethodCallExpr}.
	 * @param ctx the parse tree
	 */
	void exitLengthMethodCallExpr(ODataQueryParserParser.LengthMethodCallExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link ODataQueryParserParser#indexOfMethodCallExpr}.
	 * @param ctx the parse tree
	 */
	void enterIndexOfMethodCallExpr(ODataQueryParserParser.IndexOfMethodCallExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link ODataQueryParserParser#indexOfMethodCallExpr}.
	 * @param ctx the parse tree
	 */
	void exitIndexOfMethodCallExpr(ODataQueryParserParser.IndexOfMethodCallExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link ODataQueryParserParser#yearMethodCallExpr}.
	 * @param ctx the parse tree
	 */
	void enterYearMethodCallExpr(ODataQueryParserParser.YearMethodCallExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link ODataQueryParserParser#yearMethodCallExpr}.
	 * @param ctx the parse tree
	 */
	void exitYearMethodCallExpr(ODataQueryParserParser.YearMethodCallExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link ODataQueryParserParser#monthMethodCallExpr}.
	 * @param ctx the parse tree
	 */
	void enterMonthMethodCallExpr(ODataQueryParserParser.MonthMethodCallExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link ODataQueryParserParser#monthMethodCallExpr}.
	 * @param ctx the parse tree
	 */
	void exitMonthMethodCallExpr(ODataQueryParserParser.MonthMethodCallExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link ODataQueryParserParser#dayMethodCallExpr}.
	 * @param ctx the parse tree
	 */
	void enterDayMethodCallExpr(ODataQueryParserParser.DayMethodCallExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link ODataQueryParserParser#dayMethodCallExpr}.
	 * @param ctx the parse tree
	 */
	void exitDayMethodCallExpr(ODataQueryParserParser.DayMethodCallExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link ODataQueryParserParser#daysMethodCallExpr}.
	 * @param ctx the parse tree
	 */
	void enterDaysMethodCallExpr(ODataQueryParserParser.DaysMethodCallExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link ODataQueryParserParser#daysMethodCallExpr}.
	 * @param ctx the parse tree
	 */
	void exitDaysMethodCallExpr(ODataQueryParserParser.DaysMethodCallExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link ODataQueryParserParser#hourMethodCallExpr}.
	 * @param ctx the parse tree
	 */
	void enterHourMethodCallExpr(ODataQueryParserParser.HourMethodCallExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link ODataQueryParserParser#hourMethodCallExpr}.
	 * @param ctx the parse tree
	 */
	void exitHourMethodCallExpr(ODataQueryParserParser.HourMethodCallExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link ODataQueryParserParser#minuteMethodCallExpr}.
	 * @param ctx the parse tree
	 */
	void enterMinuteMethodCallExpr(ODataQueryParserParser.MinuteMethodCallExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link ODataQueryParserParser#minuteMethodCallExpr}.
	 * @param ctx the parse tree
	 */
	void exitMinuteMethodCallExpr(ODataQueryParserParser.MinuteMethodCallExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link ODataQueryParserParser#secondMethodCallExpr}.
	 * @param ctx the parse tree
	 */
	void enterSecondMethodCallExpr(ODataQueryParserParser.SecondMethodCallExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link ODataQueryParserParser#secondMethodCallExpr}.
	 * @param ctx the parse tree
	 */
	void exitSecondMethodCallExpr(ODataQueryParserParser.SecondMethodCallExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link ODataQueryParserParser#timeMethodCallExpr}.
	 * @param ctx the parse tree
	 */
	void enterTimeMethodCallExpr(ODataQueryParserParser.TimeMethodCallExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link ODataQueryParserParser#timeMethodCallExpr}.
	 * @param ctx the parse tree
	 */
	void exitTimeMethodCallExpr(ODataQueryParserParser.TimeMethodCallExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link ODataQueryParserParser#dateMethodCallExpr}.
	 * @param ctx the parse tree
	 */
	void enterDateMethodCallExpr(ODataQueryParserParser.DateMethodCallExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link ODataQueryParserParser#dateMethodCallExpr}.
	 * @param ctx the parse tree
	 */
	void exitDateMethodCallExpr(ODataQueryParserParser.DateMethodCallExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link ODataQueryParserParser#roundMethodCallExpr}.
	 * @param ctx the parse tree
	 */
	void enterRoundMethodCallExpr(ODataQueryParserParser.RoundMethodCallExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link ODataQueryParserParser#roundMethodCallExpr}.
	 * @param ctx the parse tree
	 */
	void exitRoundMethodCallExpr(ODataQueryParserParser.RoundMethodCallExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link ODataQueryParserParser#floorMethodCallExpr}.
	 * @param ctx the parse tree
	 */
	void enterFloorMethodCallExpr(ODataQueryParserParser.FloorMethodCallExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link ODataQueryParserParser#floorMethodCallExpr}.
	 * @param ctx the parse tree
	 */
	void exitFloorMethodCallExpr(ODataQueryParserParser.FloorMethodCallExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link ODataQueryParserParser#ceilingMethodCallExpr}.
	 * @param ctx the parse tree
	 */
	void enterCeilingMethodCallExpr(ODataQueryParserParser.CeilingMethodCallExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link ODataQueryParserParser#ceilingMethodCallExpr}.
	 * @param ctx the parse tree
	 */
	void exitCeilingMethodCallExpr(ODataQueryParserParser.CeilingMethodCallExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link ODataQueryParserParser#totalOffsetMinutesExpr}.
	 * @param ctx the parse tree
	 */
	void enterTotalOffsetMinutesExpr(ODataQueryParserParser.TotalOffsetMinutesExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link ODataQueryParserParser#totalOffsetMinutesExpr}.
	 * @param ctx the parse tree
	 */
	void exitTotalOffsetMinutesExpr(ODataQueryParserParser.TotalOffsetMinutesExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link ODataQueryParserParser#distanceMethodCallExpr}.
	 * @param ctx the parse tree
	 */
	void enterDistanceMethodCallExpr(ODataQueryParserParser.DistanceMethodCallExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link ODataQueryParserParser#distanceMethodCallExpr}.
	 * @param ctx the parse tree
	 */
	void exitDistanceMethodCallExpr(ODataQueryParserParser.DistanceMethodCallExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link ODataQueryParserParser#geoLengthMethodCallExpr}.
	 * @param ctx the parse tree
	 */
	void enterGeoLengthMethodCallExpr(ODataQueryParserParser.GeoLengthMethodCallExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link ODataQueryParserParser#geoLengthMethodCallExpr}.
	 * @param ctx the parse tree
	 */
	void exitGeoLengthMethodCallExpr(ODataQueryParserParser.GeoLengthMethodCallExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link ODataQueryParserParser#minDate}.
	 * @param ctx the parse tree
	 */
	void enterMinDate(ODataQueryParserParser.MinDateContext ctx);
	/**
	 * Exit a parse tree produced by {@link ODataQueryParserParser#minDate}.
	 * @param ctx the parse tree
	 */
	void exitMinDate(ODataQueryParserParser.MinDateContext ctx);
	/**
	 * Enter a parse tree produced by {@link ODataQueryParserParser#maxDate}.
	 * @param ctx the parse tree
	 */
	void enterMaxDate(ODataQueryParserParser.MaxDateContext ctx);
	/**
	 * Exit a parse tree produced by {@link ODataQueryParserParser#maxDate}.
	 * @param ctx the parse tree
	 */
	void exitMaxDate(ODataQueryParserParser.MaxDateContext ctx);
	/**
	 * Enter a parse tree produced by {@link ODataQueryParserParser#nowDate}.
	 * @param ctx the parse tree
	 */
	void enterNowDate(ODataQueryParserParser.NowDateContext ctx);
	/**
	 * Exit a parse tree produced by {@link ODataQueryParserParser#nowDate}.
	 * @param ctx the parse tree
	 */
	void exitNowDate(ODataQueryParserParser.NowDateContext ctx);
	/**
	 * Enter a parse tree produced by {@link ODataQueryParserParser#andExpr}.
	 * @param ctx the parse tree
	 */
	void enterAndExpr(ODataQueryParserParser.AndExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link ODataQueryParserParser#andExpr}.
	 * @param ctx the parse tree
	 */
	void exitAndExpr(ODataQueryParserParser.AndExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link ODataQueryParserParser#orExpr}.
	 * @param ctx the parse tree
	 */
	void enterOrExpr(ODataQueryParserParser.OrExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link ODataQueryParserParser#orExpr}.
	 * @param ctx the parse tree
	 */
	void exitOrExpr(ODataQueryParserParser.OrExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link ODataQueryParserParser#notExpr}.
	 * @param ctx the parse tree
	 */
	void enterNotExpr(ODataQueryParserParser.NotExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link ODataQueryParserParser#notExpr}.
	 * @param ctx the parse tree
	 */
	void exitNotExpr(ODataQueryParserParser.NotExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link ODataQueryParserParser#eqExpr}.
	 * @param ctx the parse tree
	 */
	void enterEqExpr(ODataQueryParserParser.EqExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link ODataQueryParserParser#eqExpr}.
	 * @param ctx the parse tree
	 */
	void exitEqExpr(ODataQueryParserParser.EqExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link ODataQueryParserParser#neExpr}.
	 * @param ctx the parse tree
	 */
	void enterNeExpr(ODataQueryParserParser.NeExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link ODataQueryParserParser#neExpr}.
	 * @param ctx the parse tree
	 */
	void exitNeExpr(ODataQueryParserParser.NeExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link ODataQueryParserParser#ltExpr}.
	 * @param ctx the parse tree
	 */
	void enterLtExpr(ODataQueryParserParser.LtExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link ODataQueryParserParser#ltExpr}.
	 * @param ctx the parse tree
	 */
	void exitLtExpr(ODataQueryParserParser.LtExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link ODataQueryParserParser#leExpr}.
	 * @param ctx the parse tree
	 */
	void enterLeExpr(ODataQueryParserParser.LeExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link ODataQueryParserParser#leExpr}.
	 * @param ctx the parse tree
	 */
	void exitLeExpr(ODataQueryParserParser.LeExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link ODataQueryParserParser#gtExpr}.
	 * @param ctx the parse tree
	 */
	void enterGtExpr(ODataQueryParserParser.GtExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link ODataQueryParserParser#gtExpr}.
	 * @param ctx the parse tree
	 */
	void exitGtExpr(ODataQueryParserParser.GtExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link ODataQueryParserParser#geExpr}.
	 * @param ctx the parse tree
	 */
	void enterGeExpr(ODataQueryParserParser.GeExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link ODataQueryParserParser#geExpr}.
	 * @param ctx the parse tree
	 */
	void exitGeExpr(ODataQueryParserParser.GeExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link ODataQueryParserParser#addExpr}.
	 * @param ctx the parse tree
	 */
	void enterAddExpr(ODataQueryParserParser.AddExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link ODataQueryParserParser#addExpr}.
	 * @param ctx the parse tree
	 */
	void exitAddExpr(ODataQueryParserParser.AddExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link ODataQueryParserParser#subExpr}.
	 * @param ctx the parse tree
	 */
	void enterSubExpr(ODataQueryParserParser.SubExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link ODataQueryParserParser#subExpr}.
	 * @param ctx the parse tree
	 */
	void exitSubExpr(ODataQueryParserParser.SubExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link ODataQueryParserParser#mulExpr}.
	 * @param ctx the parse tree
	 */
	void enterMulExpr(ODataQueryParserParser.MulExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link ODataQueryParserParser#mulExpr}.
	 * @param ctx the parse tree
	 */
	void exitMulExpr(ODataQueryParserParser.MulExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link ODataQueryParserParser#divExpr}.
	 * @param ctx the parse tree
	 */
	void enterDivExpr(ODataQueryParserParser.DivExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link ODataQueryParserParser#divExpr}.
	 * @param ctx the parse tree
	 */
	void exitDivExpr(ODataQueryParserParser.DivExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link ODataQueryParserParser#modExpr}.
	 * @param ctx the parse tree
	 */
	void enterModExpr(ODataQueryParserParser.ModExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link ODataQueryParserParser#modExpr}.
	 * @param ctx the parse tree
	 */
	void exitModExpr(ODataQueryParserParser.ModExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link ODataQueryParserParser#negateExpr}.
	 * @param ctx the parse tree
	 */
	void enterNegateExpr(ODataQueryParserParser.NegateExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link ODataQueryParserParser#negateExpr}.
	 * @param ctx the parse tree
	 */
	void exitNegateExpr(ODataQueryParserParser.NegateExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link ODataQueryParserParser#numericLiteral}.
	 * @param ctx the parse tree
	 */
	void enterNumericLiteral(ODataQueryParserParser.NumericLiteralContext ctx);
	/**
	 * Exit a parse tree produced by {@link ODataQueryParserParser#numericLiteral}.
	 * @param ctx the parse tree
	 */
	void exitNumericLiteral(ODataQueryParserParser.NumericLiteralContext ctx);
	/**
	 * Enter a parse tree produced by {@link ODataQueryParserParser#decimalLiteral}.
	 * @param ctx the parse tree
	 */
	void enterDecimalLiteral(ODataQueryParserParser.DecimalLiteralContext ctx);
	/**
	 * Exit a parse tree produced by {@link ODataQueryParserParser#decimalLiteral}.
	 * @param ctx the parse tree
	 */
	void exitDecimalLiteral(ODataQueryParserParser.DecimalLiteralContext ctx);
	/**
	 * Enter a parse tree produced by {@link ODataQueryParserParser#escapedString}.
	 * @param ctx the parse tree
	 */
	void enterEscapedString(ODataQueryParserParser.EscapedStringContext ctx);
	/**
	 * Exit a parse tree produced by {@link ODataQueryParserParser#escapedString}.
	 * @param ctx the parse tree
	 */
	void exitEscapedString(ODataQueryParserParser.EscapedStringContext ctx);
	/**
	 * Enter a parse tree produced by {@link ODataQueryParserParser#escapedStringLiteral}.
	 * @param ctx the parse tree
	 */
	void enterEscapedStringLiteral(ODataQueryParserParser.EscapedStringLiteralContext ctx);
	/**
	 * Exit a parse tree produced by {@link ODataQueryParserParser#escapedStringLiteral}.
	 * @param ctx the parse tree
	 */
	void exitEscapedStringLiteral(ODataQueryParserParser.EscapedStringLiteralContext ctx);
	/**
	 * Enter a parse tree produced by {@link ODataQueryParserParser#geographyCollection}.
	 * @param ctx the parse tree
	 */
	void enterGeographyCollection(ODataQueryParserParser.GeographyCollectionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ODataQueryParserParser#geographyCollection}.
	 * @param ctx the parse tree
	 */
	void exitGeographyCollection(ODataQueryParserParser.GeographyCollectionContext ctx);
	/**
	 * Enter a parse tree produced by {@link ODataQueryParserParser#fullCollectionLiteral}.
	 * @param ctx the parse tree
	 */
	void enterFullCollectionLiteral(ODataQueryParserParser.FullCollectionLiteralContext ctx);
	/**
	 * Exit a parse tree produced by {@link ODataQueryParserParser#fullCollectionLiteral}.
	 * @param ctx the parse tree
	 */
	void exitFullCollectionLiteral(ODataQueryParserParser.FullCollectionLiteralContext ctx);
	/**
	 * Enter a parse tree produced by {@link ODataQueryParserParser#collectionLiteral}.
	 * @param ctx the parse tree
	 */
	void enterCollectionLiteral(ODataQueryParserParser.CollectionLiteralContext ctx);
	/**
	 * Exit a parse tree produced by {@link ODataQueryParserParser#collectionLiteral}.
	 * @param ctx the parse tree
	 */
	void exitCollectionLiteral(ODataQueryParserParser.CollectionLiteralContext ctx);
	/**
	 * Enter a parse tree produced by {@link ODataQueryParserParser#geoLiteral}.
	 * @param ctx the parse tree
	 */
	void enterGeoLiteral(ODataQueryParserParser.GeoLiteralContext ctx);
	/**
	 * Exit a parse tree produced by {@link ODataQueryParserParser#geoLiteral}.
	 * @param ctx the parse tree
	 */
	void exitGeoLiteral(ODataQueryParserParser.GeoLiteralContext ctx);
	/**
	 * Enter a parse tree produced by {@link ODataQueryParserParser#geographyLineString}.
	 * @param ctx the parse tree
	 */
	void enterGeographyLineString(ODataQueryParserParser.GeographyLineStringContext ctx);
	/**
	 * Exit a parse tree produced by {@link ODataQueryParserParser#geographyLineString}.
	 * @param ctx the parse tree
	 */
	void exitGeographyLineString(ODataQueryParserParser.GeographyLineStringContext ctx);
	/**
	 * Enter a parse tree produced by {@link ODataQueryParserParser#fullLineStringLiteral}.
	 * @param ctx the parse tree
	 */
	void enterFullLineStringLiteral(ODataQueryParserParser.FullLineStringLiteralContext ctx);
	/**
	 * Exit a parse tree produced by {@link ODataQueryParserParser#fullLineStringLiteral}.
	 * @param ctx the parse tree
	 */
	void exitFullLineStringLiteral(ODataQueryParserParser.FullLineStringLiteralContext ctx);
	/**
	 * Enter a parse tree produced by {@link ODataQueryParserParser#lineStringLiteral}.
	 * @param ctx the parse tree
	 */
	void enterLineStringLiteral(ODataQueryParserParser.LineStringLiteralContext ctx);
	/**
	 * Exit a parse tree produced by {@link ODataQueryParserParser#lineStringLiteral}.
	 * @param ctx the parse tree
	 */
	void exitLineStringLiteral(ODataQueryParserParser.LineStringLiteralContext ctx);
	/**
	 * Enter a parse tree produced by {@link ODataQueryParserParser#lineStringData}.
	 * @param ctx the parse tree
	 */
	void enterLineStringData(ODataQueryParserParser.LineStringDataContext ctx);
	/**
	 * Exit a parse tree produced by {@link ODataQueryParserParser#lineStringData}.
	 * @param ctx the parse tree
	 */
	void exitLineStringData(ODataQueryParserParser.LineStringDataContext ctx);
	/**
	 * Enter a parse tree produced by {@link ODataQueryParserParser#geographyMultiLineString}.
	 * @param ctx the parse tree
	 */
	void enterGeographyMultiLineString(ODataQueryParserParser.GeographyMultiLineStringContext ctx);
	/**
	 * Exit a parse tree produced by {@link ODataQueryParserParser#geographyMultiLineString}.
	 * @param ctx the parse tree
	 */
	void exitGeographyMultiLineString(ODataQueryParserParser.GeographyMultiLineStringContext ctx);
	/**
	 * Enter a parse tree produced by {@link ODataQueryParserParser#fullMultiLineStringLiteral}.
	 * @param ctx the parse tree
	 */
	void enterFullMultiLineStringLiteral(ODataQueryParserParser.FullMultiLineStringLiteralContext ctx);
	/**
	 * Exit a parse tree produced by {@link ODataQueryParserParser#fullMultiLineStringLiteral}.
	 * @param ctx the parse tree
	 */
	void exitFullMultiLineStringLiteral(ODataQueryParserParser.FullMultiLineStringLiteralContext ctx);
	/**
	 * Enter a parse tree produced by {@link ODataQueryParserParser#multiLineStringLiteral}.
	 * @param ctx the parse tree
	 */
	void enterMultiLineStringLiteral(ODataQueryParserParser.MultiLineStringLiteralContext ctx);
	/**
	 * Exit a parse tree produced by {@link ODataQueryParserParser#multiLineStringLiteral}.
	 * @param ctx the parse tree
	 */
	void exitMultiLineStringLiteral(ODataQueryParserParser.MultiLineStringLiteralContext ctx);
	/**
	 * Enter a parse tree produced by {@link ODataQueryParserParser#geographyMultiPoint}.
	 * @param ctx the parse tree
	 */
	void enterGeographyMultiPoint(ODataQueryParserParser.GeographyMultiPointContext ctx);
	/**
	 * Exit a parse tree produced by {@link ODataQueryParserParser#geographyMultiPoint}.
	 * @param ctx the parse tree
	 */
	void exitGeographyMultiPoint(ODataQueryParserParser.GeographyMultiPointContext ctx);
	/**
	 * Enter a parse tree produced by {@link ODataQueryParserParser#fullMultiPointLiteral}.
	 * @param ctx the parse tree
	 */
	void enterFullMultiPointLiteral(ODataQueryParserParser.FullMultiPointLiteralContext ctx);
	/**
	 * Exit a parse tree produced by {@link ODataQueryParserParser#fullMultiPointLiteral}.
	 * @param ctx the parse tree
	 */
	void exitFullMultiPointLiteral(ODataQueryParserParser.FullMultiPointLiteralContext ctx);
	/**
	 * Enter a parse tree produced by {@link ODataQueryParserParser#multiPointLiteral}.
	 * @param ctx the parse tree
	 */
	void enterMultiPointLiteral(ODataQueryParserParser.MultiPointLiteralContext ctx);
	/**
	 * Exit a parse tree produced by {@link ODataQueryParserParser#multiPointLiteral}.
	 * @param ctx the parse tree
	 */
	void exitMultiPointLiteral(ODataQueryParserParser.MultiPointLiteralContext ctx);
	/**
	 * Enter a parse tree produced by {@link ODataQueryParserParser#geographyMultiPolygon}.
	 * @param ctx the parse tree
	 */
	void enterGeographyMultiPolygon(ODataQueryParserParser.GeographyMultiPolygonContext ctx);
	/**
	 * Exit a parse tree produced by {@link ODataQueryParserParser#geographyMultiPolygon}.
	 * @param ctx the parse tree
	 */
	void exitGeographyMultiPolygon(ODataQueryParserParser.GeographyMultiPolygonContext ctx);
	/**
	 * Enter a parse tree produced by {@link ODataQueryParserParser#fullMultiPolygonLiteral}.
	 * @param ctx the parse tree
	 */
	void enterFullMultiPolygonLiteral(ODataQueryParserParser.FullMultiPolygonLiteralContext ctx);
	/**
	 * Exit a parse tree produced by {@link ODataQueryParserParser#fullMultiPolygonLiteral}.
	 * @param ctx the parse tree
	 */
	void exitFullMultiPolygonLiteral(ODataQueryParserParser.FullMultiPolygonLiteralContext ctx);
	/**
	 * Enter a parse tree produced by {@link ODataQueryParserParser#multiPolygonLiteral}.
	 * @param ctx the parse tree
	 */
	void enterMultiPolygonLiteral(ODataQueryParserParser.MultiPolygonLiteralContext ctx);
	/**
	 * Exit a parse tree produced by {@link ODataQueryParserParser#multiPolygonLiteral}.
	 * @param ctx the parse tree
	 */
	void exitMultiPolygonLiteral(ODataQueryParserParser.MultiPolygonLiteralContext ctx);
	/**
	 * Enter a parse tree produced by {@link ODataQueryParserParser#geographyPoint}.
	 * @param ctx the parse tree
	 */
	void enterGeographyPoint(ODataQueryParserParser.GeographyPointContext ctx);
	/**
	 * Exit a parse tree produced by {@link ODataQueryParserParser#geographyPoint}.
	 * @param ctx the parse tree
	 */
	void exitGeographyPoint(ODataQueryParserParser.GeographyPointContext ctx);
	/**
	 * Enter a parse tree produced by {@link ODataQueryParserParser#fullPointLiteral}.
	 * @param ctx the parse tree
	 */
	void enterFullPointLiteral(ODataQueryParserParser.FullPointLiteralContext ctx);
	/**
	 * Exit a parse tree produced by {@link ODataQueryParserParser#fullPointLiteral}.
	 * @param ctx the parse tree
	 */
	void exitFullPointLiteral(ODataQueryParserParser.FullPointLiteralContext ctx);
	/**
	 * Enter a parse tree produced by {@link ODataQueryParserParser#sridLiteral}.
	 * @param ctx the parse tree
	 */
	void enterSridLiteral(ODataQueryParserParser.SridLiteralContext ctx);
	/**
	 * Exit a parse tree produced by {@link ODataQueryParserParser#sridLiteral}.
	 * @param ctx the parse tree
	 */
	void exitSridLiteral(ODataQueryParserParser.SridLiteralContext ctx);
	/**
	 * Enter a parse tree produced by {@link ODataQueryParserParser#pointLiteral}.
	 * @param ctx the parse tree
	 */
	void enterPointLiteral(ODataQueryParserParser.PointLiteralContext ctx);
	/**
	 * Exit a parse tree produced by {@link ODataQueryParserParser#pointLiteral}.
	 * @param ctx the parse tree
	 */
	void exitPointLiteral(ODataQueryParserParser.PointLiteralContext ctx);
	/**
	 * Enter a parse tree produced by {@link ODataQueryParserParser#pointData}.
	 * @param ctx the parse tree
	 */
	void enterPointData(ODataQueryParserParser.PointDataContext ctx);
	/**
	 * Exit a parse tree produced by {@link ODataQueryParserParser#pointData}.
	 * @param ctx the parse tree
	 */
	void exitPointData(ODataQueryParserParser.PointDataContext ctx);
	/**
	 * Enter a parse tree produced by {@link ODataQueryParserParser#positionLiteral}.
	 * @param ctx the parse tree
	 */
	void enterPositionLiteral(ODataQueryParserParser.PositionLiteralContext ctx);
	/**
	 * Exit a parse tree produced by {@link ODataQueryParserParser#positionLiteral}.
	 * @param ctx the parse tree
	 */
	void exitPositionLiteral(ODataQueryParserParser.PositionLiteralContext ctx);
	/**
	 * Enter a parse tree produced by {@link ODataQueryParserParser#coordinate}.
	 * @param ctx the parse tree
	 */
	void enterCoordinate(ODataQueryParserParser.CoordinateContext ctx);
	/**
	 * Exit a parse tree produced by {@link ODataQueryParserParser#coordinate}.
	 * @param ctx the parse tree
	 */
	void exitCoordinate(ODataQueryParserParser.CoordinateContext ctx);
	/**
	 * Enter a parse tree produced by {@link ODataQueryParserParser#geographyPolygon}.
	 * @param ctx the parse tree
	 */
	void enterGeographyPolygon(ODataQueryParserParser.GeographyPolygonContext ctx);
	/**
	 * Exit a parse tree produced by {@link ODataQueryParserParser#geographyPolygon}.
	 * @param ctx the parse tree
	 */
	void exitGeographyPolygon(ODataQueryParserParser.GeographyPolygonContext ctx);
	/**
	 * Enter a parse tree produced by {@link ODataQueryParserParser#fullPolygonLiteral}.
	 * @param ctx the parse tree
	 */
	void enterFullPolygonLiteral(ODataQueryParserParser.FullPolygonLiteralContext ctx);
	/**
	 * Exit a parse tree produced by {@link ODataQueryParserParser#fullPolygonLiteral}.
	 * @param ctx the parse tree
	 */
	void exitFullPolygonLiteral(ODataQueryParserParser.FullPolygonLiteralContext ctx);
	/**
	 * Enter a parse tree produced by {@link ODataQueryParserParser#polygonLiteral}.
	 * @param ctx the parse tree
	 */
	void enterPolygonLiteral(ODataQueryParserParser.PolygonLiteralContext ctx);
	/**
	 * Exit a parse tree produced by {@link ODataQueryParserParser#polygonLiteral}.
	 * @param ctx the parse tree
	 */
	void exitPolygonLiteral(ODataQueryParserParser.PolygonLiteralContext ctx);
	/**
	 * Enter a parse tree produced by {@link ODataQueryParserParser#polygonData}.
	 * @param ctx the parse tree
	 */
	void enterPolygonData(ODataQueryParserParser.PolygonDataContext ctx);
	/**
	 * Exit a parse tree produced by {@link ODataQueryParserParser#polygonData}.
	 * @param ctx the parse tree
	 */
	void exitPolygonData(ODataQueryParserParser.PolygonDataContext ctx);
	/**
	 * Enter a parse tree produced by {@link ODataQueryParserParser#ringLiteral}.
	 * @param ctx the parse tree
	 */
	void enterRingLiteral(ODataQueryParserParser.RingLiteralContext ctx);
	/**
	 * Exit a parse tree produced by {@link ODataQueryParserParser#ringLiteral}.
	 * @param ctx the parse tree
	 */
	void exitRingLiteral(ODataQueryParserParser.RingLiteralContext ctx);
	/**
	 * Enter a parse tree produced by {@link ODataQueryParserParser#geometryCollection}.
	 * @param ctx the parse tree
	 */
	void enterGeometryCollection(ODataQueryParserParser.GeometryCollectionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ODataQueryParserParser#geometryCollection}.
	 * @param ctx the parse tree
	 */
	void exitGeometryCollection(ODataQueryParserParser.GeometryCollectionContext ctx);
	/**
	 * Enter a parse tree produced by {@link ODataQueryParserParser#geometryLineString}.
	 * @param ctx the parse tree
	 */
	void enterGeometryLineString(ODataQueryParserParser.GeometryLineStringContext ctx);
	/**
	 * Exit a parse tree produced by {@link ODataQueryParserParser#geometryLineString}.
	 * @param ctx the parse tree
	 */
	void exitGeometryLineString(ODataQueryParserParser.GeometryLineStringContext ctx);
	/**
	 * Enter a parse tree produced by {@link ODataQueryParserParser#geometryMultiLineString}.
	 * @param ctx the parse tree
	 */
	void enterGeometryMultiLineString(ODataQueryParserParser.GeometryMultiLineStringContext ctx);
	/**
	 * Exit a parse tree produced by {@link ODataQueryParserParser#geometryMultiLineString}.
	 * @param ctx the parse tree
	 */
	void exitGeometryMultiLineString(ODataQueryParserParser.GeometryMultiLineStringContext ctx);
	/**
	 * Enter a parse tree produced by {@link ODataQueryParserParser#geometryMultiPoint}.
	 * @param ctx the parse tree
	 */
	void enterGeometryMultiPoint(ODataQueryParserParser.GeometryMultiPointContext ctx);
	/**
	 * Exit a parse tree produced by {@link ODataQueryParserParser#geometryMultiPoint}.
	 * @param ctx the parse tree
	 */
	void exitGeometryMultiPoint(ODataQueryParserParser.GeometryMultiPointContext ctx);
	/**
	 * Enter a parse tree produced by {@link ODataQueryParserParser#geometryMultiPolygon}.
	 * @param ctx the parse tree
	 */
	void enterGeometryMultiPolygon(ODataQueryParserParser.GeometryMultiPolygonContext ctx);
	/**
	 * Exit a parse tree produced by {@link ODataQueryParserParser#geometryMultiPolygon}.
	 * @param ctx the parse tree
	 */
	void exitGeometryMultiPolygon(ODataQueryParserParser.GeometryMultiPolygonContext ctx);
	/**
	 * Enter a parse tree produced by {@link ODataQueryParserParser#geometryPoint}.
	 * @param ctx the parse tree
	 */
	void enterGeometryPoint(ODataQueryParserParser.GeometryPointContext ctx);
	/**
	 * Exit a parse tree produced by {@link ODataQueryParserParser#geometryPoint}.
	 * @param ctx the parse tree
	 */
	void exitGeometryPoint(ODataQueryParserParser.GeometryPointContext ctx);
	/**
	 * Enter a parse tree produced by {@link ODataQueryParserParser#geometryPolygon}.
	 * @param ctx the parse tree
	 */
	void enterGeometryPolygon(ODataQueryParserParser.GeometryPolygonContext ctx);
	/**
	 * Exit a parse tree produced by {@link ODataQueryParserParser#geometryPolygon}.
	 * @param ctx the parse tree
	 */
	void exitGeometryPolygon(ODataQueryParserParser.GeometryPolygonContext ctx);
	/**
	 * Enter a parse tree produced by {@link ODataQueryParserParser#geographyPrefix}.
	 * @param ctx the parse tree
	 */
	void enterGeographyPrefix(ODataQueryParserParser.GeographyPrefixContext ctx);
	/**
	 * Exit a parse tree produced by {@link ODataQueryParserParser#geographyPrefix}.
	 * @param ctx the parse tree
	 */
	void exitGeographyPrefix(ODataQueryParserParser.GeographyPrefixContext ctx);
	/**
	 * Enter a parse tree produced by {@link ODataQueryParserParser#geometryPrefix}.
	 * @param ctx the parse tree
	 */
	void enterGeometryPrefix(ODataQueryParserParser.GeometryPrefixContext ctx);
	/**
	 * Exit a parse tree produced by {@link ODataQueryParserParser#geometryPrefix}.
	 * @param ctx the parse tree
	 */
	void exitGeometryPrefix(ODataQueryParserParser.GeometryPrefixContext ctx);
}