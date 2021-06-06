/*
 * Copyright 2015-2021 52°North Initiative for Geospatial Open Source
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
// Generated from /home/specki/git/arctic-sea/svalbard/odata/src/main/antlr4/org/n52/svalbard/odata/grammar/STAQueryOptionsGrammar.g4 by ANTLR 4.8
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link STAQueryOptionsGrammar}.
 */
public interface STAQueryOptionsGrammarListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link STAQueryOptionsGrammar#queryOptions}.
	 * @param ctx the parse tree
	 */
	void enterQueryOptions(STAQueryOptionsGrammar.QueryOptionsContext ctx);
	/**
	 * Exit a parse tree produced by {@link STAQueryOptionsGrammar#queryOptions}.
	 * @param ctx the parse tree
	 */
	void exitQueryOptions(STAQueryOptionsGrammar.QueryOptionsContext ctx);
	/**
	 * Enter a parse tree produced by {@link STAQueryOptionsGrammar#systemQueryOption}.
	 * @param ctx the parse tree
	 */
	void enterSystemQueryOption(STAQueryOptionsGrammar.SystemQueryOptionContext ctx);
	/**
	 * Exit a parse tree produced by {@link STAQueryOptionsGrammar#systemQueryOption}.
	 * @param ctx the parse tree
	 */
	void exitSystemQueryOption(STAQueryOptionsGrammar.SystemQueryOptionContext ctx);
	/**
	 * Enter a parse tree produced by {@link STAQueryOptionsGrammar#count}.
	 * @param ctx the parse tree
	 */
	void enterCount(STAQueryOptionsGrammar.CountContext ctx);
	/**
	 * Exit a parse tree produced by {@link STAQueryOptionsGrammar#count}.
	 * @param ctx the parse tree
	 */
	void exitCount(STAQueryOptionsGrammar.CountContext ctx);
	/**
	 * Enter a parse tree produced by {@link STAQueryOptionsGrammar#expand}.
	 * @param ctx the parse tree
	 */
	void enterExpand(STAQueryOptionsGrammar.ExpandContext ctx);
	/**
	 * Exit a parse tree produced by {@link STAQueryOptionsGrammar#expand}.
	 * @param ctx the parse tree
	 */
	void exitExpand(STAQueryOptionsGrammar.ExpandContext ctx);
	/**
	 * Enter a parse tree produced by {@link STAQueryOptionsGrammar#expandItem}.
	 * @param ctx the parse tree
	 */
	void enterExpandItem(STAQueryOptionsGrammar.ExpandItemContext ctx);
	/**
	 * Exit a parse tree produced by {@link STAQueryOptionsGrammar#expandItem}.
	 * @param ctx the parse tree
	 */
	void exitExpandItem(STAQueryOptionsGrammar.ExpandItemContext ctx);
	/**
	 * Enter a parse tree produced by {@link STAQueryOptionsGrammar#filter}.
	 * @param ctx the parse tree
	 */
	void enterFilter(STAQueryOptionsGrammar.FilterContext ctx);
	/**
	 * Exit a parse tree produced by {@link STAQueryOptionsGrammar#filter}.
	 * @param ctx the parse tree
	 */
	void exitFilter(STAQueryOptionsGrammar.FilterContext ctx);
	/**
	 * Enter a parse tree produced by {@link STAQueryOptionsGrammar#orderby}.
	 * @param ctx the parse tree
	 */
	void enterOrderby(STAQueryOptionsGrammar.OrderbyContext ctx);
	/**
	 * Exit a parse tree produced by {@link STAQueryOptionsGrammar#orderby}.
	 * @param ctx the parse tree
	 */
	void exitOrderby(STAQueryOptionsGrammar.OrderbyContext ctx);
	/**
	 * Enter a parse tree produced by {@link STAQueryOptionsGrammar#orderbyItem}.
	 * @param ctx the parse tree
	 */
	void enterOrderbyItem(STAQueryOptionsGrammar.OrderbyItemContext ctx);
	/**
	 * Exit a parse tree produced by {@link STAQueryOptionsGrammar#orderbyItem}.
	 * @param ctx the parse tree
	 */
	void exitOrderbyItem(STAQueryOptionsGrammar.OrderbyItemContext ctx);
	/**
	 * Enter a parse tree produced by {@link STAQueryOptionsGrammar#skip}.
	 * @param ctx the parse tree
	 */
	void enterSkip(STAQueryOptionsGrammar.SkipContext ctx);
	/**
	 * Exit a parse tree produced by {@link STAQueryOptionsGrammar#skip}.
	 * @param ctx the parse tree
	 */
	void exitSkip(STAQueryOptionsGrammar.SkipContext ctx);
	/**
	 * Enter a parse tree produced by {@link STAQueryOptionsGrammar#top}.
	 * @param ctx the parse tree
	 */
	void enterTop(STAQueryOptionsGrammar.TopContext ctx);
	/**
	 * Exit a parse tree produced by {@link STAQueryOptionsGrammar#top}.
	 * @param ctx the parse tree
	 */
	void exitTop(STAQueryOptionsGrammar.TopContext ctx);
	/**
	 * Enter a parse tree produced by {@link STAQueryOptionsGrammar#select}.
	 * @param ctx the parse tree
	 */
	void enterSelect(STAQueryOptionsGrammar.SelectContext ctx);
	/**
	 * Exit a parse tree produced by {@link STAQueryOptionsGrammar#select}.
	 * @param ctx the parse tree
	 */
	void exitSelect(STAQueryOptionsGrammar.SelectContext ctx);
	/**
	 * Enter a parse tree produced by {@link STAQueryOptionsGrammar#selectItem}.
	 * @param ctx the parse tree
	 */
	void enterSelectItem(STAQueryOptionsGrammar.SelectItemContext ctx);
	/**
	 * Exit a parse tree produced by {@link STAQueryOptionsGrammar#selectItem}.
	 * @param ctx the parse tree
	 */
	void exitSelectItem(STAQueryOptionsGrammar.SelectItemContext ctx);
	/**
	 * Enter a parse tree produced by {@link STAQueryOptionsGrammar#boolExpr}.
	 * @param ctx the parse tree
	 */
	void enterBoolExpr(STAQueryOptionsGrammar.BoolExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link STAQueryOptionsGrammar#boolExpr}.
	 * @param ctx the parse tree
	 */
	void exitBoolExpr(STAQueryOptionsGrammar.BoolExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link STAQueryOptionsGrammar#boolParenExpr}.
	 * @param ctx the parse tree
	 */
	void enterBoolParenExpr(STAQueryOptionsGrammar.BoolParenExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link STAQueryOptionsGrammar#boolParenExpr}.
	 * @param ctx the parse tree
	 */
	void exitBoolParenExpr(STAQueryOptionsGrammar.BoolParenExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link STAQueryOptionsGrammar#anyExpr}.
	 * @param ctx the parse tree
	 */
	void enterAnyExpr(STAQueryOptionsGrammar.AnyExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link STAQueryOptionsGrammar#anyExpr}.
	 * @param ctx the parse tree
	 */
	void exitAnyExpr(STAQueryOptionsGrammar.AnyExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link STAQueryOptionsGrammar#parenExpr}.
	 * @param ctx the parse tree
	 */
	void enterParenExpr(STAQueryOptionsGrammar.ParenExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link STAQueryOptionsGrammar#parenExpr}.
	 * @param ctx the parse tree
	 */
	void exitParenExpr(STAQueryOptionsGrammar.ParenExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link STAQueryOptionsGrammar#arithmeticExpr}.
	 * @param ctx the parse tree
	 */
	void enterArithmeticExpr(STAQueryOptionsGrammar.ArithmeticExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link STAQueryOptionsGrammar#arithmeticExpr}.
	 * @param ctx the parse tree
	 */
	void exitArithmeticExpr(STAQueryOptionsGrammar.ArithmeticExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link STAQueryOptionsGrammar#timeExpr}.
	 * @param ctx the parse tree
	 */
	void enterTimeExpr(STAQueryOptionsGrammar.TimeExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link STAQueryOptionsGrammar#timeExpr}.
	 * @param ctx the parse tree
	 */
	void exitTimeExpr(STAQueryOptionsGrammar.TimeExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link STAQueryOptionsGrammar#textExpr}.
	 * @param ctx the parse tree
	 */
	void enterTextExpr(STAQueryOptionsGrammar.TextExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link STAQueryOptionsGrammar#textExpr}.
	 * @param ctx the parse tree
	 */
	void exitTextExpr(STAQueryOptionsGrammar.TextExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link STAQueryOptionsGrammar#geoExpr}.
	 * @param ctx the parse tree
	 */
	void enterGeoExpr(STAQueryOptionsGrammar.GeoExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link STAQueryOptionsGrammar#geoExpr}.
	 * @param ctx the parse tree
	 */
	void exitGeoExpr(STAQueryOptionsGrammar.GeoExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link STAQueryOptionsGrammar#memberExpr}.
	 * @param ctx the parse tree
	 */
	void enterMemberExpr(STAQueryOptionsGrammar.MemberExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link STAQueryOptionsGrammar#memberExpr}.
	 * @param ctx the parse tree
	 */
	void exitMemberExpr(STAQueryOptionsGrammar.MemberExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link STAQueryOptionsGrammar#textMethodCallExpr}.
	 * @param ctx the parse tree
	 */
	void enterTextMethodCallExpr(STAQueryOptionsGrammar.TextMethodCallExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link STAQueryOptionsGrammar#textMethodCallExpr}.
	 * @param ctx the parse tree
	 */
	void exitTextMethodCallExpr(STAQueryOptionsGrammar.TextMethodCallExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link STAQueryOptionsGrammar#arithmeticMethodCallExpr}.
	 * @param ctx the parse tree
	 */
	void enterArithmeticMethodCallExpr(STAQueryOptionsGrammar.ArithmeticMethodCallExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link STAQueryOptionsGrammar#arithmeticMethodCallExpr}.
	 * @param ctx the parse tree
	 */
	void exitArithmeticMethodCallExpr(STAQueryOptionsGrammar.ArithmeticMethodCallExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link STAQueryOptionsGrammar#temporalMethodCallExpr}.
	 * @param ctx the parse tree
	 */
	void enterTemporalMethodCallExpr(STAQueryOptionsGrammar.TemporalMethodCallExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link STAQueryOptionsGrammar#temporalMethodCallExpr}.
	 * @param ctx the parse tree
	 */
	void exitTemporalMethodCallExpr(STAQueryOptionsGrammar.TemporalMethodCallExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link STAQueryOptionsGrammar#boolMethodCallExpr}.
	 * @param ctx the parse tree
	 */
	void enterBoolMethodCallExpr(STAQueryOptionsGrammar.BoolMethodCallExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link STAQueryOptionsGrammar#boolMethodCallExpr}.
	 * @param ctx the parse tree
	 */
	void exitBoolMethodCallExpr(STAQueryOptionsGrammar.BoolMethodCallExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link STAQueryOptionsGrammar#textOrMember}.
	 * @param ctx the parse tree
	 */
	void enterTextOrMember(STAQueryOptionsGrammar.TextOrMemberContext ctx);
	/**
	 * Exit a parse tree produced by {@link STAQueryOptionsGrammar#textOrMember}.
	 * @param ctx the parse tree
	 */
	void exitTextOrMember(STAQueryOptionsGrammar.TextOrMemberContext ctx);
	/**
	 * Enter a parse tree produced by {@link STAQueryOptionsGrammar#temporalOrMemberOrISO8601Timestamp}.
	 * @param ctx the parse tree
	 */
	void enterTemporalOrMemberOrISO8601Timestamp(STAQueryOptionsGrammar.TemporalOrMemberOrISO8601TimestampContext ctx);
	/**
	 * Exit a parse tree produced by {@link STAQueryOptionsGrammar#temporalOrMemberOrISO8601Timestamp}.
	 * @param ctx the parse tree
	 */
	void exitTemporalOrMemberOrISO8601Timestamp(STAQueryOptionsGrammar.TemporalOrMemberOrISO8601TimestampContext ctx);
	/**
	 * Enter a parse tree produced by {@link STAQueryOptionsGrammar#geoOrMember}.
	 * @param ctx the parse tree
	 */
	void enterGeoOrMember(STAQueryOptionsGrammar.GeoOrMemberContext ctx);
	/**
	 * Exit a parse tree produced by {@link STAQueryOptionsGrammar#geoOrMember}.
	 * @param ctx the parse tree
	 */
	void exitGeoOrMember(STAQueryOptionsGrammar.GeoOrMemberContext ctx);
	/**
	 * Enter a parse tree produced by {@link STAQueryOptionsGrammar#iso8601Timestamp}.
	 * @param ctx the parse tree
	 */
	void enterIso8601Timestamp(STAQueryOptionsGrammar.Iso8601TimestampContext ctx);
	/**
	 * Exit a parse tree produced by {@link STAQueryOptionsGrammar#iso8601Timestamp}.
	 * @param ctx the parse tree
	 */
	void exitIso8601Timestamp(STAQueryOptionsGrammar.Iso8601TimestampContext ctx);
	/**
	 * Enter a parse tree produced by {@link STAQueryOptionsGrammar#iso8601Timezone}.
	 * @param ctx the parse tree
	 */
	void enterIso8601Timezone(STAQueryOptionsGrammar.Iso8601TimezoneContext ctx);
	/**
	 * Exit a parse tree produced by {@link STAQueryOptionsGrammar#iso8601Timezone}.
	 * @param ctx the parse tree
	 */
	void exitIso8601Timezone(STAQueryOptionsGrammar.Iso8601TimezoneContext ctx);
	/**
	 * Enter a parse tree produced by {@link STAQueryOptionsGrammar#substringMethodCallExpr}.
	 * @param ctx the parse tree
	 */
	void enterSubstringMethodCallExpr(STAQueryOptionsGrammar.SubstringMethodCallExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link STAQueryOptionsGrammar#substringMethodCallExpr}.
	 * @param ctx the parse tree
	 */
	void exitSubstringMethodCallExpr(STAQueryOptionsGrammar.SubstringMethodCallExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link STAQueryOptionsGrammar#toLowerMethodCallExpr}.
	 * @param ctx the parse tree
	 */
	void enterToLowerMethodCallExpr(STAQueryOptionsGrammar.ToLowerMethodCallExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link STAQueryOptionsGrammar#toLowerMethodCallExpr}.
	 * @param ctx the parse tree
	 */
	void exitToLowerMethodCallExpr(STAQueryOptionsGrammar.ToLowerMethodCallExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link STAQueryOptionsGrammar#toUpperMethodCallExpr}.
	 * @param ctx the parse tree
	 */
	void enterToUpperMethodCallExpr(STAQueryOptionsGrammar.ToUpperMethodCallExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link STAQueryOptionsGrammar#toUpperMethodCallExpr}.
	 * @param ctx the parse tree
	 */
	void exitToUpperMethodCallExpr(STAQueryOptionsGrammar.ToUpperMethodCallExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link STAQueryOptionsGrammar#trimMethodCallExpr}.
	 * @param ctx the parse tree
	 */
	void enterTrimMethodCallExpr(STAQueryOptionsGrammar.TrimMethodCallExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link STAQueryOptionsGrammar#trimMethodCallExpr}.
	 * @param ctx the parse tree
	 */
	void exitTrimMethodCallExpr(STAQueryOptionsGrammar.TrimMethodCallExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link STAQueryOptionsGrammar#concatMethodCallExpr}.
	 * @param ctx the parse tree
	 */
	void enterConcatMethodCallExpr(STAQueryOptionsGrammar.ConcatMethodCallExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link STAQueryOptionsGrammar#concatMethodCallExpr}.
	 * @param ctx the parse tree
	 */
	void exitConcatMethodCallExpr(STAQueryOptionsGrammar.ConcatMethodCallExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link STAQueryOptionsGrammar#substringOfMethodCallExpr}.
	 * @param ctx the parse tree
	 */
	void enterSubstringOfMethodCallExpr(STAQueryOptionsGrammar.SubstringOfMethodCallExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link STAQueryOptionsGrammar#substringOfMethodCallExpr}.
	 * @param ctx the parse tree
	 */
	void exitSubstringOfMethodCallExpr(STAQueryOptionsGrammar.SubstringOfMethodCallExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link STAQueryOptionsGrammar#startsWithMethodCallExpr}.
	 * @param ctx the parse tree
	 */
	void enterStartsWithMethodCallExpr(STAQueryOptionsGrammar.StartsWithMethodCallExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link STAQueryOptionsGrammar#startsWithMethodCallExpr}.
	 * @param ctx the parse tree
	 */
	void exitStartsWithMethodCallExpr(STAQueryOptionsGrammar.StartsWithMethodCallExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link STAQueryOptionsGrammar#endsWithMethodCallExpr}.
	 * @param ctx the parse tree
	 */
	void enterEndsWithMethodCallExpr(STAQueryOptionsGrammar.EndsWithMethodCallExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link STAQueryOptionsGrammar#endsWithMethodCallExpr}.
	 * @param ctx the parse tree
	 */
	void exitEndsWithMethodCallExpr(STAQueryOptionsGrammar.EndsWithMethodCallExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link STAQueryOptionsGrammar#containsMethodCallExpr}.
	 * @param ctx the parse tree
	 */
	void enterContainsMethodCallExpr(STAQueryOptionsGrammar.ContainsMethodCallExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link STAQueryOptionsGrammar#containsMethodCallExpr}.
	 * @param ctx the parse tree
	 */
	void exitContainsMethodCallExpr(STAQueryOptionsGrammar.ContainsMethodCallExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link STAQueryOptionsGrammar#intersectsMethodCallExpr}.
	 * @param ctx the parse tree
	 */
	void enterIntersectsMethodCallExpr(STAQueryOptionsGrammar.IntersectsMethodCallExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link STAQueryOptionsGrammar#intersectsMethodCallExpr}.
	 * @param ctx the parse tree
	 */
	void exitIntersectsMethodCallExpr(STAQueryOptionsGrammar.IntersectsMethodCallExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link STAQueryOptionsGrammar#st_commonMethodCallExpr}.
	 * @param ctx the parse tree
	 */
	void enterSt_commonMethodCallExpr(STAQueryOptionsGrammar.St_commonMethodCallExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link STAQueryOptionsGrammar#st_commonMethodCallExpr}.
	 * @param ctx the parse tree
	 */
	void exitSt_commonMethodCallExpr(STAQueryOptionsGrammar.St_commonMethodCallExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link STAQueryOptionsGrammar#st_equalsMethodCallExpr}.
	 * @param ctx the parse tree
	 */
	void enterSt_equalsMethodCallExpr(STAQueryOptionsGrammar.St_equalsMethodCallExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link STAQueryOptionsGrammar#st_equalsMethodCallExpr}.
	 * @param ctx the parse tree
	 */
	void exitSt_equalsMethodCallExpr(STAQueryOptionsGrammar.St_equalsMethodCallExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link STAQueryOptionsGrammar#st_disjointMethodCallExpr}.
	 * @param ctx the parse tree
	 */
	void enterSt_disjointMethodCallExpr(STAQueryOptionsGrammar.St_disjointMethodCallExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link STAQueryOptionsGrammar#st_disjointMethodCallExpr}.
	 * @param ctx the parse tree
	 */
	void exitSt_disjointMethodCallExpr(STAQueryOptionsGrammar.St_disjointMethodCallExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link STAQueryOptionsGrammar#st_touchesMethodCallExpr}.
	 * @param ctx the parse tree
	 */
	void enterSt_touchesMethodCallExpr(STAQueryOptionsGrammar.St_touchesMethodCallExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link STAQueryOptionsGrammar#st_touchesMethodCallExpr}.
	 * @param ctx the parse tree
	 */
	void exitSt_touchesMethodCallExpr(STAQueryOptionsGrammar.St_touchesMethodCallExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link STAQueryOptionsGrammar#st_withinMethodCallExpr}.
	 * @param ctx the parse tree
	 */
	void enterSt_withinMethodCallExpr(STAQueryOptionsGrammar.St_withinMethodCallExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link STAQueryOptionsGrammar#st_withinMethodCallExpr}.
	 * @param ctx the parse tree
	 */
	void exitSt_withinMethodCallExpr(STAQueryOptionsGrammar.St_withinMethodCallExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link STAQueryOptionsGrammar#st_overlapsMethodCallExpr}.
	 * @param ctx the parse tree
	 */
	void enterSt_overlapsMethodCallExpr(STAQueryOptionsGrammar.St_overlapsMethodCallExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link STAQueryOptionsGrammar#st_overlapsMethodCallExpr}.
	 * @param ctx the parse tree
	 */
	void exitSt_overlapsMethodCallExpr(STAQueryOptionsGrammar.St_overlapsMethodCallExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link STAQueryOptionsGrammar#st_crossesMethodCallExpr}.
	 * @param ctx the parse tree
	 */
	void enterSt_crossesMethodCallExpr(STAQueryOptionsGrammar.St_crossesMethodCallExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link STAQueryOptionsGrammar#st_crossesMethodCallExpr}.
	 * @param ctx the parse tree
	 */
	void exitSt_crossesMethodCallExpr(STAQueryOptionsGrammar.St_crossesMethodCallExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link STAQueryOptionsGrammar#st_intersectsMethodCallExpr}.
	 * @param ctx the parse tree
	 */
	void enterSt_intersectsMethodCallExpr(STAQueryOptionsGrammar.St_intersectsMethodCallExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link STAQueryOptionsGrammar#st_intersectsMethodCallExpr}.
	 * @param ctx the parse tree
	 */
	void exitSt_intersectsMethodCallExpr(STAQueryOptionsGrammar.St_intersectsMethodCallExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link STAQueryOptionsGrammar#st_containsMethodCallExpr}.
	 * @param ctx the parse tree
	 */
	void enterSt_containsMethodCallExpr(STAQueryOptionsGrammar.St_containsMethodCallExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link STAQueryOptionsGrammar#st_containsMethodCallExpr}.
	 * @param ctx the parse tree
	 */
	void exitSt_containsMethodCallExpr(STAQueryOptionsGrammar.St_containsMethodCallExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link STAQueryOptionsGrammar#st_relateMethodCallExpr}.
	 * @param ctx the parse tree
	 */
	void enterSt_relateMethodCallExpr(STAQueryOptionsGrammar.St_relateMethodCallExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link STAQueryOptionsGrammar#st_relateMethodCallExpr}.
	 * @param ctx the parse tree
	 */
	void exitSt_relateMethodCallExpr(STAQueryOptionsGrammar.St_relateMethodCallExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link STAQueryOptionsGrammar#lengthMethodCallExpr}.
	 * @param ctx the parse tree
	 */
	void enterLengthMethodCallExpr(STAQueryOptionsGrammar.LengthMethodCallExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link STAQueryOptionsGrammar#lengthMethodCallExpr}.
	 * @param ctx the parse tree
	 */
	void exitLengthMethodCallExpr(STAQueryOptionsGrammar.LengthMethodCallExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link STAQueryOptionsGrammar#indexOfMethodCallExpr}.
	 * @param ctx the parse tree
	 */
	void enterIndexOfMethodCallExpr(STAQueryOptionsGrammar.IndexOfMethodCallExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link STAQueryOptionsGrammar#indexOfMethodCallExpr}.
	 * @param ctx the parse tree
	 */
	void exitIndexOfMethodCallExpr(STAQueryOptionsGrammar.IndexOfMethodCallExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link STAQueryOptionsGrammar#yearMethodCallExpr}.
	 * @param ctx the parse tree
	 */
	void enterYearMethodCallExpr(STAQueryOptionsGrammar.YearMethodCallExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link STAQueryOptionsGrammar#yearMethodCallExpr}.
	 * @param ctx the parse tree
	 */
	void exitYearMethodCallExpr(STAQueryOptionsGrammar.YearMethodCallExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link STAQueryOptionsGrammar#monthMethodCallExpr}.
	 * @param ctx the parse tree
	 */
	void enterMonthMethodCallExpr(STAQueryOptionsGrammar.MonthMethodCallExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link STAQueryOptionsGrammar#monthMethodCallExpr}.
	 * @param ctx the parse tree
	 */
	void exitMonthMethodCallExpr(STAQueryOptionsGrammar.MonthMethodCallExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link STAQueryOptionsGrammar#dayMethodCallExpr}.
	 * @param ctx the parse tree
	 */
	void enterDayMethodCallExpr(STAQueryOptionsGrammar.DayMethodCallExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link STAQueryOptionsGrammar#dayMethodCallExpr}.
	 * @param ctx the parse tree
	 */
	void exitDayMethodCallExpr(STAQueryOptionsGrammar.DayMethodCallExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link STAQueryOptionsGrammar#daysMethodCallExpr}.
	 * @param ctx the parse tree
	 */
	void enterDaysMethodCallExpr(STAQueryOptionsGrammar.DaysMethodCallExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link STAQueryOptionsGrammar#daysMethodCallExpr}.
	 * @param ctx the parse tree
	 */
	void exitDaysMethodCallExpr(STAQueryOptionsGrammar.DaysMethodCallExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link STAQueryOptionsGrammar#hourMethodCallExpr}.
	 * @param ctx the parse tree
	 */
	void enterHourMethodCallExpr(STAQueryOptionsGrammar.HourMethodCallExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link STAQueryOptionsGrammar#hourMethodCallExpr}.
	 * @param ctx the parse tree
	 */
	void exitHourMethodCallExpr(STAQueryOptionsGrammar.HourMethodCallExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link STAQueryOptionsGrammar#minuteMethodCallExpr}.
	 * @param ctx the parse tree
	 */
	void enterMinuteMethodCallExpr(STAQueryOptionsGrammar.MinuteMethodCallExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link STAQueryOptionsGrammar#minuteMethodCallExpr}.
	 * @param ctx the parse tree
	 */
	void exitMinuteMethodCallExpr(STAQueryOptionsGrammar.MinuteMethodCallExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link STAQueryOptionsGrammar#secondMethodCallExpr}.
	 * @param ctx the parse tree
	 */
	void enterSecondMethodCallExpr(STAQueryOptionsGrammar.SecondMethodCallExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link STAQueryOptionsGrammar#secondMethodCallExpr}.
	 * @param ctx the parse tree
	 */
	void exitSecondMethodCallExpr(STAQueryOptionsGrammar.SecondMethodCallExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link STAQueryOptionsGrammar#timeMethodCallExpr}.
	 * @param ctx the parse tree
	 */
	void enterTimeMethodCallExpr(STAQueryOptionsGrammar.TimeMethodCallExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link STAQueryOptionsGrammar#timeMethodCallExpr}.
	 * @param ctx the parse tree
	 */
	void exitTimeMethodCallExpr(STAQueryOptionsGrammar.TimeMethodCallExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link STAQueryOptionsGrammar#dateMethodCallExpr}.
	 * @param ctx the parse tree
	 */
	void enterDateMethodCallExpr(STAQueryOptionsGrammar.DateMethodCallExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link STAQueryOptionsGrammar#dateMethodCallExpr}.
	 * @param ctx the parse tree
	 */
	void exitDateMethodCallExpr(STAQueryOptionsGrammar.DateMethodCallExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link STAQueryOptionsGrammar#roundMethodCallExpr}.
	 * @param ctx the parse tree
	 */
	void enterRoundMethodCallExpr(STAQueryOptionsGrammar.RoundMethodCallExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link STAQueryOptionsGrammar#roundMethodCallExpr}.
	 * @param ctx the parse tree
	 */
	void exitRoundMethodCallExpr(STAQueryOptionsGrammar.RoundMethodCallExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link STAQueryOptionsGrammar#floorMethodCallExpr}.
	 * @param ctx the parse tree
	 */
	void enterFloorMethodCallExpr(STAQueryOptionsGrammar.FloorMethodCallExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link STAQueryOptionsGrammar#floorMethodCallExpr}.
	 * @param ctx the parse tree
	 */
	void exitFloorMethodCallExpr(STAQueryOptionsGrammar.FloorMethodCallExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link STAQueryOptionsGrammar#ceilingMethodCallExpr}.
	 * @param ctx the parse tree
	 */
	void enterCeilingMethodCallExpr(STAQueryOptionsGrammar.CeilingMethodCallExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link STAQueryOptionsGrammar#ceilingMethodCallExpr}.
	 * @param ctx the parse tree
	 */
	void exitCeilingMethodCallExpr(STAQueryOptionsGrammar.CeilingMethodCallExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link STAQueryOptionsGrammar#totalOffsetMinutesExpr}.
	 * @param ctx the parse tree
	 */
	void enterTotalOffsetMinutesExpr(STAQueryOptionsGrammar.TotalOffsetMinutesExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link STAQueryOptionsGrammar#totalOffsetMinutesExpr}.
	 * @param ctx the parse tree
	 */
	void exitTotalOffsetMinutesExpr(STAQueryOptionsGrammar.TotalOffsetMinutesExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link STAQueryOptionsGrammar#distanceMethodCallExpr}.
	 * @param ctx the parse tree
	 */
	void enterDistanceMethodCallExpr(STAQueryOptionsGrammar.DistanceMethodCallExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link STAQueryOptionsGrammar#distanceMethodCallExpr}.
	 * @param ctx the parse tree
	 */
	void exitDistanceMethodCallExpr(STAQueryOptionsGrammar.DistanceMethodCallExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link STAQueryOptionsGrammar#geoLengthMethodCallExpr}.
	 * @param ctx the parse tree
	 */
	void enterGeoLengthMethodCallExpr(STAQueryOptionsGrammar.GeoLengthMethodCallExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link STAQueryOptionsGrammar#geoLengthMethodCallExpr}.
	 * @param ctx the parse tree
	 */
	void exitGeoLengthMethodCallExpr(STAQueryOptionsGrammar.GeoLengthMethodCallExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link STAQueryOptionsGrammar#minDate}.
	 * @param ctx the parse tree
	 */
	void enterMinDate(STAQueryOptionsGrammar.MinDateContext ctx);
	/**
	 * Exit a parse tree produced by {@link STAQueryOptionsGrammar#minDate}.
	 * @param ctx the parse tree
	 */
	void exitMinDate(STAQueryOptionsGrammar.MinDateContext ctx);
	/**
	 * Enter a parse tree produced by {@link STAQueryOptionsGrammar#maxDate}.
	 * @param ctx the parse tree
	 */
	void enterMaxDate(STAQueryOptionsGrammar.MaxDateContext ctx);
	/**
	 * Exit a parse tree produced by {@link STAQueryOptionsGrammar#maxDate}.
	 * @param ctx the parse tree
	 */
	void exitMaxDate(STAQueryOptionsGrammar.MaxDateContext ctx);
	/**
	 * Enter a parse tree produced by {@link STAQueryOptionsGrammar#nowDate}.
	 * @param ctx the parse tree
	 */
	void enterNowDate(STAQueryOptionsGrammar.NowDateContext ctx);
	/**
	 * Exit a parse tree produced by {@link STAQueryOptionsGrammar#nowDate}.
	 * @param ctx the parse tree
	 */
	void exitNowDate(STAQueryOptionsGrammar.NowDateContext ctx);
	/**
	 * Enter a parse tree produced by {@link STAQueryOptionsGrammar#andExpr}.
	 * @param ctx the parse tree
	 */
	void enterAndExpr(STAQueryOptionsGrammar.AndExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link STAQueryOptionsGrammar#andExpr}.
	 * @param ctx the parse tree
	 */
	void exitAndExpr(STAQueryOptionsGrammar.AndExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link STAQueryOptionsGrammar#orExpr}.
	 * @param ctx the parse tree
	 */
	void enterOrExpr(STAQueryOptionsGrammar.OrExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link STAQueryOptionsGrammar#orExpr}.
	 * @param ctx the parse tree
	 */
	void exitOrExpr(STAQueryOptionsGrammar.OrExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link STAQueryOptionsGrammar#notExpr}.
	 * @param ctx the parse tree
	 */
	void enterNotExpr(STAQueryOptionsGrammar.NotExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link STAQueryOptionsGrammar#notExpr}.
	 * @param ctx the parse tree
	 */
	void exitNotExpr(STAQueryOptionsGrammar.NotExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link STAQueryOptionsGrammar#eqExpr}.
	 * @param ctx the parse tree
	 */
	void enterEqExpr(STAQueryOptionsGrammar.EqExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link STAQueryOptionsGrammar#eqExpr}.
	 * @param ctx the parse tree
	 */
	void exitEqExpr(STAQueryOptionsGrammar.EqExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link STAQueryOptionsGrammar#neExpr}.
	 * @param ctx the parse tree
	 */
	void enterNeExpr(STAQueryOptionsGrammar.NeExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link STAQueryOptionsGrammar#neExpr}.
	 * @param ctx the parse tree
	 */
	void exitNeExpr(STAQueryOptionsGrammar.NeExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link STAQueryOptionsGrammar#ltExpr}.
	 * @param ctx the parse tree
	 */
	void enterLtExpr(STAQueryOptionsGrammar.LtExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link STAQueryOptionsGrammar#ltExpr}.
	 * @param ctx the parse tree
	 */
	void exitLtExpr(STAQueryOptionsGrammar.LtExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link STAQueryOptionsGrammar#leExpr}.
	 * @param ctx the parse tree
	 */
	void enterLeExpr(STAQueryOptionsGrammar.LeExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link STAQueryOptionsGrammar#leExpr}.
	 * @param ctx the parse tree
	 */
	void exitLeExpr(STAQueryOptionsGrammar.LeExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link STAQueryOptionsGrammar#gtExpr}.
	 * @param ctx the parse tree
	 */
	void enterGtExpr(STAQueryOptionsGrammar.GtExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link STAQueryOptionsGrammar#gtExpr}.
	 * @param ctx the parse tree
	 */
	void exitGtExpr(STAQueryOptionsGrammar.GtExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link STAQueryOptionsGrammar#geExpr}.
	 * @param ctx the parse tree
	 */
	void enterGeExpr(STAQueryOptionsGrammar.GeExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link STAQueryOptionsGrammar#geExpr}.
	 * @param ctx the parse tree
	 */
	void exitGeExpr(STAQueryOptionsGrammar.GeExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link STAQueryOptionsGrammar#addExpr}.
	 * @param ctx the parse tree
	 */
	void enterAddExpr(STAQueryOptionsGrammar.AddExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link STAQueryOptionsGrammar#addExpr}.
	 * @param ctx the parse tree
	 */
	void exitAddExpr(STAQueryOptionsGrammar.AddExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link STAQueryOptionsGrammar#subExpr}.
	 * @param ctx the parse tree
	 */
	void enterSubExpr(STAQueryOptionsGrammar.SubExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link STAQueryOptionsGrammar#subExpr}.
	 * @param ctx the parse tree
	 */
	void exitSubExpr(STAQueryOptionsGrammar.SubExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link STAQueryOptionsGrammar#mulExpr}.
	 * @param ctx the parse tree
	 */
	void enterMulExpr(STAQueryOptionsGrammar.MulExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link STAQueryOptionsGrammar#mulExpr}.
	 * @param ctx the parse tree
	 */
	void exitMulExpr(STAQueryOptionsGrammar.MulExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link STAQueryOptionsGrammar#divExpr}.
	 * @param ctx the parse tree
	 */
	void enterDivExpr(STAQueryOptionsGrammar.DivExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link STAQueryOptionsGrammar#divExpr}.
	 * @param ctx the parse tree
	 */
	void exitDivExpr(STAQueryOptionsGrammar.DivExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link STAQueryOptionsGrammar#modExpr}.
	 * @param ctx the parse tree
	 */
	void enterModExpr(STAQueryOptionsGrammar.ModExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link STAQueryOptionsGrammar#modExpr}.
	 * @param ctx the parse tree
	 */
	void exitModExpr(STAQueryOptionsGrammar.ModExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link STAQueryOptionsGrammar#negateExpr}.
	 * @param ctx the parse tree
	 */
	void enterNegateExpr(STAQueryOptionsGrammar.NegateExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link STAQueryOptionsGrammar#negateExpr}.
	 * @param ctx the parse tree
	 */
	void exitNegateExpr(STAQueryOptionsGrammar.NegateExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link STAQueryOptionsGrammar#numericLiteral}.
	 * @param ctx the parse tree
	 */
	void enterNumericLiteral(STAQueryOptionsGrammar.NumericLiteralContext ctx);
	/**
	 * Exit a parse tree produced by {@link STAQueryOptionsGrammar#numericLiteral}.
	 * @param ctx the parse tree
	 */
	void exitNumericLiteral(STAQueryOptionsGrammar.NumericLiteralContext ctx);
	/**
	 * Enter a parse tree produced by {@link STAQueryOptionsGrammar#decimalLiteral}.
	 * @param ctx the parse tree
	 */
	void enterDecimalLiteral(STAQueryOptionsGrammar.DecimalLiteralContext ctx);
	/**
	 * Exit a parse tree produced by {@link STAQueryOptionsGrammar#decimalLiteral}.
	 * @param ctx the parse tree
	 */
	void exitDecimalLiteral(STAQueryOptionsGrammar.DecimalLiteralContext ctx);
	/**
	 * Enter a parse tree produced by {@link STAQueryOptionsGrammar#escapedString}.
	 * @param ctx the parse tree
	 */
	void enterEscapedString(STAQueryOptionsGrammar.EscapedStringContext ctx);
	/**
	 * Exit a parse tree produced by {@link STAQueryOptionsGrammar#escapedString}.
	 * @param ctx the parse tree
	 */
	void exitEscapedString(STAQueryOptionsGrammar.EscapedStringContext ctx);
	/**
	 * Enter a parse tree produced by {@link STAQueryOptionsGrammar#geographyCollection}.
	 * @param ctx the parse tree
	 */
	void enterGeographyCollection(STAQueryOptionsGrammar.GeographyCollectionContext ctx);
	/**
	 * Exit a parse tree produced by {@link STAQueryOptionsGrammar#geographyCollection}.
	 * @param ctx the parse tree
	 */
	void exitGeographyCollection(STAQueryOptionsGrammar.GeographyCollectionContext ctx);
	/**
	 * Enter a parse tree produced by {@link STAQueryOptionsGrammar#fullCollectionLiteral}.
	 * @param ctx the parse tree
	 */
	void enterFullCollectionLiteral(STAQueryOptionsGrammar.FullCollectionLiteralContext ctx);
	/**
	 * Exit a parse tree produced by {@link STAQueryOptionsGrammar#fullCollectionLiteral}.
	 * @param ctx the parse tree
	 */
	void exitFullCollectionLiteral(STAQueryOptionsGrammar.FullCollectionLiteralContext ctx);
	/**
	 * Enter a parse tree produced by {@link STAQueryOptionsGrammar#collectionLiteral}.
	 * @param ctx the parse tree
	 */
	void enterCollectionLiteral(STAQueryOptionsGrammar.CollectionLiteralContext ctx);
	/**
	 * Exit a parse tree produced by {@link STAQueryOptionsGrammar#collectionLiteral}.
	 * @param ctx the parse tree
	 */
	void exitCollectionLiteral(STAQueryOptionsGrammar.CollectionLiteralContext ctx);
	/**
	 * Enter a parse tree produced by {@link STAQueryOptionsGrammar#geoLiteral}.
	 * @param ctx the parse tree
	 */
	void enterGeoLiteral(STAQueryOptionsGrammar.GeoLiteralContext ctx);
	/**
	 * Exit a parse tree produced by {@link STAQueryOptionsGrammar#geoLiteral}.
	 * @param ctx the parse tree
	 */
	void exitGeoLiteral(STAQueryOptionsGrammar.GeoLiteralContext ctx);
	/**
	 * Enter a parse tree produced by {@link STAQueryOptionsGrammar#geographyLineString}.
	 * @param ctx the parse tree
	 */
	void enterGeographyLineString(STAQueryOptionsGrammar.GeographyLineStringContext ctx);
	/**
	 * Exit a parse tree produced by {@link STAQueryOptionsGrammar#geographyLineString}.
	 * @param ctx the parse tree
	 */
	void exitGeographyLineString(STAQueryOptionsGrammar.GeographyLineStringContext ctx);
	/**
	 * Enter a parse tree produced by {@link STAQueryOptionsGrammar#fullLineStringLiteral}.
	 * @param ctx the parse tree
	 */
	void enterFullLineStringLiteral(STAQueryOptionsGrammar.FullLineStringLiteralContext ctx);
	/**
	 * Exit a parse tree produced by {@link STAQueryOptionsGrammar#fullLineStringLiteral}.
	 * @param ctx the parse tree
	 */
	void exitFullLineStringLiteral(STAQueryOptionsGrammar.FullLineStringLiteralContext ctx);
	/**
	 * Enter a parse tree produced by {@link STAQueryOptionsGrammar#lineStringLiteral}.
	 * @param ctx the parse tree
	 */
	void enterLineStringLiteral(STAQueryOptionsGrammar.LineStringLiteralContext ctx);
	/**
	 * Exit a parse tree produced by {@link STAQueryOptionsGrammar#lineStringLiteral}.
	 * @param ctx the parse tree
	 */
	void exitLineStringLiteral(STAQueryOptionsGrammar.LineStringLiteralContext ctx);
	/**
	 * Enter a parse tree produced by {@link STAQueryOptionsGrammar#lineStringData}.
	 * @param ctx the parse tree
	 */
	void enterLineStringData(STAQueryOptionsGrammar.LineStringDataContext ctx);
	/**
	 * Exit a parse tree produced by {@link STAQueryOptionsGrammar#lineStringData}.
	 * @param ctx the parse tree
	 */
	void exitLineStringData(STAQueryOptionsGrammar.LineStringDataContext ctx);
	/**
	 * Enter a parse tree produced by {@link STAQueryOptionsGrammar#geographyMultiLineString}.
	 * @param ctx the parse tree
	 */
	void enterGeographyMultiLineString(STAQueryOptionsGrammar.GeographyMultiLineStringContext ctx);
	/**
	 * Exit a parse tree produced by {@link STAQueryOptionsGrammar#geographyMultiLineString}.
	 * @param ctx the parse tree
	 */
	void exitGeographyMultiLineString(STAQueryOptionsGrammar.GeographyMultiLineStringContext ctx);
	/**
	 * Enter a parse tree produced by {@link STAQueryOptionsGrammar#fullMultiLineStringLiteral}.
	 * @param ctx the parse tree
	 */
	void enterFullMultiLineStringLiteral(STAQueryOptionsGrammar.FullMultiLineStringLiteralContext ctx);
	/**
	 * Exit a parse tree produced by {@link STAQueryOptionsGrammar#fullMultiLineStringLiteral}.
	 * @param ctx the parse tree
	 */
	void exitFullMultiLineStringLiteral(STAQueryOptionsGrammar.FullMultiLineStringLiteralContext ctx);
	/**
	 * Enter a parse tree produced by {@link STAQueryOptionsGrammar#multiLineStringLiteral}.
	 * @param ctx the parse tree
	 */
	void enterMultiLineStringLiteral(STAQueryOptionsGrammar.MultiLineStringLiteralContext ctx);
	/**
	 * Exit a parse tree produced by {@link STAQueryOptionsGrammar#multiLineStringLiteral}.
	 * @param ctx the parse tree
	 */
	void exitMultiLineStringLiteral(STAQueryOptionsGrammar.MultiLineStringLiteralContext ctx);
	/**
	 * Enter a parse tree produced by {@link STAQueryOptionsGrammar#geographyMultiPoint}.
	 * @param ctx the parse tree
	 */
	void enterGeographyMultiPoint(STAQueryOptionsGrammar.GeographyMultiPointContext ctx);
	/**
	 * Exit a parse tree produced by {@link STAQueryOptionsGrammar#geographyMultiPoint}.
	 * @param ctx the parse tree
	 */
	void exitGeographyMultiPoint(STAQueryOptionsGrammar.GeographyMultiPointContext ctx);
	/**
	 * Enter a parse tree produced by {@link STAQueryOptionsGrammar#fullMultiPointLiteral}.
	 * @param ctx the parse tree
	 */
	void enterFullMultiPointLiteral(STAQueryOptionsGrammar.FullMultiPointLiteralContext ctx);
	/**
	 * Exit a parse tree produced by {@link STAQueryOptionsGrammar#fullMultiPointLiteral}.
	 * @param ctx the parse tree
	 */
	void exitFullMultiPointLiteral(STAQueryOptionsGrammar.FullMultiPointLiteralContext ctx);
	/**
	 * Enter a parse tree produced by {@link STAQueryOptionsGrammar#multiPointLiteral}.
	 * @param ctx the parse tree
	 */
	void enterMultiPointLiteral(STAQueryOptionsGrammar.MultiPointLiteralContext ctx);
	/**
	 * Exit a parse tree produced by {@link STAQueryOptionsGrammar#multiPointLiteral}.
	 * @param ctx the parse tree
	 */
	void exitMultiPointLiteral(STAQueryOptionsGrammar.MultiPointLiteralContext ctx);
	/**
	 * Enter a parse tree produced by {@link STAQueryOptionsGrammar#geographyMultiPolygon}.
	 * @param ctx the parse tree
	 */
	void enterGeographyMultiPolygon(STAQueryOptionsGrammar.GeographyMultiPolygonContext ctx);
	/**
	 * Exit a parse tree produced by {@link STAQueryOptionsGrammar#geographyMultiPolygon}.
	 * @param ctx the parse tree
	 */
	void exitGeographyMultiPolygon(STAQueryOptionsGrammar.GeographyMultiPolygonContext ctx);
	/**
	 * Enter a parse tree produced by {@link STAQueryOptionsGrammar#fullMultiPolygonLiteral}.
	 * @param ctx the parse tree
	 */
	void enterFullMultiPolygonLiteral(STAQueryOptionsGrammar.FullMultiPolygonLiteralContext ctx);
	/**
	 * Exit a parse tree produced by {@link STAQueryOptionsGrammar#fullMultiPolygonLiteral}.
	 * @param ctx the parse tree
	 */
	void exitFullMultiPolygonLiteral(STAQueryOptionsGrammar.FullMultiPolygonLiteralContext ctx);
	/**
	 * Enter a parse tree produced by {@link STAQueryOptionsGrammar#multiPolygonLiteral}.
	 * @param ctx the parse tree
	 */
	void enterMultiPolygonLiteral(STAQueryOptionsGrammar.MultiPolygonLiteralContext ctx);
	/**
	 * Exit a parse tree produced by {@link STAQueryOptionsGrammar#multiPolygonLiteral}.
	 * @param ctx the parse tree
	 */
	void exitMultiPolygonLiteral(STAQueryOptionsGrammar.MultiPolygonLiteralContext ctx);
	/**
	 * Enter a parse tree produced by {@link STAQueryOptionsGrammar#geographyPoint}.
	 * @param ctx the parse tree
	 */
	void enterGeographyPoint(STAQueryOptionsGrammar.GeographyPointContext ctx);
	/**
	 * Exit a parse tree produced by {@link STAQueryOptionsGrammar#geographyPoint}.
	 * @param ctx the parse tree
	 */
	void exitGeographyPoint(STAQueryOptionsGrammar.GeographyPointContext ctx);
	/**
	 * Enter a parse tree produced by {@link STAQueryOptionsGrammar#fullPointLiteral}.
	 * @param ctx the parse tree
	 */
	void enterFullPointLiteral(STAQueryOptionsGrammar.FullPointLiteralContext ctx);
	/**
	 * Exit a parse tree produced by {@link STAQueryOptionsGrammar#fullPointLiteral}.
	 * @param ctx the parse tree
	 */
	void exitFullPointLiteral(STAQueryOptionsGrammar.FullPointLiteralContext ctx);
	/**
	 * Enter a parse tree produced by {@link STAQueryOptionsGrammar#sridLiteral}.
	 * @param ctx the parse tree
	 */
	void enterSridLiteral(STAQueryOptionsGrammar.SridLiteralContext ctx);
	/**
	 * Exit a parse tree produced by {@link STAQueryOptionsGrammar#sridLiteral}.
	 * @param ctx the parse tree
	 */
	void exitSridLiteral(STAQueryOptionsGrammar.SridLiteralContext ctx);
	/**
	 * Enter a parse tree produced by {@link STAQueryOptionsGrammar#pointLiteral}.
	 * @param ctx the parse tree
	 */
	void enterPointLiteral(STAQueryOptionsGrammar.PointLiteralContext ctx);
	/**
	 * Exit a parse tree produced by {@link STAQueryOptionsGrammar#pointLiteral}.
	 * @param ctx the parse tree
	 */
	void exitPointLiteral(STAQueryOptionsGrammar.PointLiteralContext ctx);
	/**
	 * Enter a parse tree produced by {@link STAQueryOptionsGrammar#pointData}.
	 * @param ctx the parse tree
	 */
	void enterPointData(STAQueryOptionsGrammar.PointDataContext ctx);
	/**
	 * Exit a parse tree produced by {@link STAQueryOptionsGrammar#pointData}.
	 * @param ctx the parse tree
	 */
	void exitPointData(STAQueryOptionsGrammar.PointDataContext ctx);
	/**
	 * Enter a parse tree produced by {@link STAQueryOptionsGrammar#positionLiteral}.
	 * @param ctx the parse tree
	 */
	void enterPositionLiteral(STAQueryOptionsGrammar.PositionLiteralContext ctx);
	/**
	 * Exit a parse tree produced by {@link STAQueryOptionsGrammar#positionLiteral}.
	 * @param ctx the parse tree
	 */
	void exitPositionLiteral(STAQueryOptionsGrammar.PositionLiteralContext ctx);
	/**
	 * Enter a parse tree produced by {@link STAQueryOptionsGrammar#coordinate}.
	 * @param ctx the parse tree
	 */
	void enterCoordinate(STAQueryOptionsGrammar.CoordinateContext ctx);
	/**
	 * Exit a parse tree produced by {@link STAQueryOptionsGrammar#coordinate}.
	 * @param ctx the parse tree
	 */
	void exitCoordinate(STAQueryOptionsGrammar.CoordinateContext ctx);
	/**
	 * Enter a parse tree produced by {@link STAQueryOptionsGrammar#geographyPolygon}.
	 * @param ctx the parse tree
	 */
	void enterGeographyPolygon(STAQueryOptionsGrammar.GeographyPolygonContext ctx);
	/**
	 * Exit a parse tree produced by {@link STAQueryOptionsGrammar#geographyPolygon}.
	 * @param ctx the parse tree
	 */
	void exitGeographyPolygon(STAQueryOptionsGrammar.GeographyPolygonContext ctx);
	/**
	 * Enter a parse tree produced by {@link STAQueryOptionsGrammar#fullPolygonLiteral}.
	 * @param ctx the parse tree
	 */
	void enterFullPolygonLiteral(STAQueryOptionsGrammar.FullPolygonLiteralContext ctx);
	/**
	 * Exit a parse tree produced by {@link STAQueryOptionsGrammar#fullPolygonLiteral}.
	 * @param ctx the parse tree
	 */
	void exitFullPolygonLiteral(STAQueryOptionsGrammar.FullPolygonLiteralContext ctx);
	/**
	 * Enter a parse tree produced by {@link STAQueryOptionsGrammar#polygonLiteral}.
	 * @param ctx the parse tree
	 */
	void enterPolygonLiteral(STAQueryOptionsGrammar.PolygonLiteralContext ctx);
	/**
	 * Exit a parse tree produced by {@link STAQueryOptionsGrammar#polygonLiteral}.
	 * @param ctx the parse tree
	 */
	void exitPolygonLiteral(STAQueryOptionsGrammar.PolygonLiteralContext ctx);
	/**
	 * Enter a parse tree produced by {@link STAQueryOptionsGrammar#polygonData}.
	 * @param ctx the parse tree
	 */
	void enterPolygonData(STAQueryOptionsGrammar.PolygonDataContext ctx);
	/**
	 * Exit a parse tree produced by {@link STAQueryOptionsGrammar#polygonData}.
	 * @param ctx the parse tree
	 */
	void exitPolygonData(STAQueryOptionsGrammar.PolygonDataContext ctx);
	/**
	 * Enter a parse tree produced by {@link STAQueryOptionsGrammar#ringLiteral}.
	 * @param ctx the parse tree
	 */
	void enterRingLiteral(STAQueryOptionsGrammar.RingLiteralContext ctx);
	/**
	 * Exit a parse tree produced by {@link STAQueryOptionsGrammar#ringLiteral}.
	 * @param ctx the parse tree
	 */
	void exitRingLiteral(STAQueryOptionsGrammar.RingLiteralContext ctx);
	/**
	 * Enter a parse tree produced by {@link STAQueryOptionsGrammar#geometryCollection}.
	 * @param ctx the parse tree
	 */
	void enterGeometryCollection(STAQueryOptionsGrammar.GeometryCollectionContext ctx);
	/**
	 * Exit a parse tree produced by {@link STAQueryOptionsGrammar#geometryCollection}.
	 * @param ctx the parse tree
	 */
	void exitGeometryCollection(STAQueryOptionsGrammar.GeometryCollectionContext ctx);
	/**
	 * Enter a parse tree produced by {@link STAQueryOptionsGrammar#geometryLineString}.
	 * @param ctx the parse tree
	 */
	void enterGeometryLineString(STAQueryOptionsGrammar.GeometryLineStringContext ctx);
	/**
	 * Exit a parse tree produced by {@link STAQueryOptionsGrammar#geometryLineString}.
	 * @param ctx the parse tree
	 */
	void exitGeometryLineString(STAQueryOptionsGrammar.GeometryLineStringContext ctx);
	/**
	 * Enter a parse tree produced by {@link STAQueryOptionsGrammar#geometryMultiLineString}.
	 * @param ctx the parse tree
	 */
	void enterGeometryMultiLineString(STAQueryOptionsGrammar.GeometryMultiLineStringContext ctx);
	/**
	 * Exit a parse tree produced by {@link STAQueryOptionsGrammar#geometryMultiLineString}.
	 * @param ctx the parse tree
	 */
	void exitGeometryMultiLineString(STAQueryOptionsGrammar.GeometryMultiLineStringContext ctx);
	/**
	 * Enter a parse tree produced by {@link STAQueryOptionsGrammar#geometryMultiPoint}.
	 * @param ctx the parse tree
	 */
	void enterGeometryMultiPoint(STAQueryOptionsGrammar.GeometryMultiPointContext ctx);
	/**
	 * Exit a parse tree produced by {@link STAQueryOptionsGrammar#geometryMultiPoint}.
	 * @param ctx the parse tree
	 */
	void exitGeometryMultiPoint(STAQueryOptionsGrammar.GeometryMultiPointContext ctx);
	/**
	 * Enter a parse tree produced by {@link STAQueryOptionsGrammar#geometryMultiPolygon}.
	 * @param ctx the parse tree
	 */
	void enterGeometryMultiPolygon(STAQueryOptionsGrammar.GeometryMultiPolygonContext ctx);
	/**
	 * Exit a parse tree produced by {@link STAQueryOptionsGrammar#geometryMultiPolygon}.
	 * @param ctx the parse tree
	 */
	void exitGeometryMultiPolygon(STAQueryOptionsGrammar.GeometryMultiPolygonContext ctx);
	/**
	 * Enter a parse tree produced by {@link STAQueryOptionsGrammar#geometryPoint}.
	 * @param ctx the parse tree
	 */
	void enterGeometryPoint(STAQueryOptionsGrammar.GeometryPointContext ctx);
	/**
	 * Exit a parse tree produced by {@link STAQueryOptionsGrammar#geometryPoint}.
	 * @param ctx the parse tree
	 */
	void exitGeometryPoint(STAQueryOptionsGrammar.GeometryPointContext ctx);
	/**
	 * Enter a parse tree produced by {@link STAQueryOptionsGrammar#geometryPolygon}.
	 * @param ctx the parse tree
	 */
	void enterGeometryPolygon(STAQueryOptionsGrammar.GeometryPolygonContext ctx);
	/**
	 * Exit a parse tree produced by {@link STAQueryOptionsGrammar#geometryPolygon}.
	 * @param ctx the parse tree
	 */
	void exitGeometryPolygon(STAQueryOptionsGrammar.GeometryPolygonContext ctx);
	/**
	 * Enter a parse tree produced by {@link STAQueryOptionsGrammar#geographyPrefix}.
	 * @param ctx the parse tree
	 */
	void enterGeographyPrefix(STAQueryOptionsGrammar.GeographyPrefixContext ctx);
	/**
	 * Exit a parse tree produced by {@link STAQueryOptionsGrammar#geographyPrefix}.
	 * @param ctx the parse tree
	 */
	void exitGeographyPrefix(STAQueryOptionsGrammar.GeographyPrefixContext ctx);
	/**
	 * Enter a parse tree produced by {@link STAQueryOptionsGrammar#geometryPrefix}.
	 * @param ctx the parse tree
	 */
	void enterGeometryPrefix(STAQueryOptionsGrammar.GeometryPrefixContext ctx);
	/**
	 * Exit a parse tree produced by {@link STAQueryOptionsGrammar#geometryPrefix}.
	 * @param ctx the parse tree
	 */
	void exitGeometryPrefix(STAQueryOptionsGrammar.GeometryPrefixContext ctx);
}