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
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link STAQueryOptionsGrammar}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface STAQueryOptionsGrammarVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link STAQueryOptionsGrammar#queryOptions}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitQueryOptions(STAQueryOptionsGrammar.QueryOptionsContext ctx);
	/**
	 * Visit a parse tree produced by {@link STAQueryOptionsGrammar#systemQueryOption}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSystemQueryOption(STAQueryOptionsGrammar.SystemQueryOptionContext ctx);
	/**
	 * Visit a parse tree produced by {@link STAQueryOptionsGrammar#count}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCount(STAQueryOptionsGrammar.CountContext ctx);
	/**
	 * Visit a parse tree produced by {@link STAQueryOptionsGrammar#expand}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpand(STAQueryOptionsGrammar.ExpandContext ctx);
	/**
	 * Visit a parse tree produced by {@link STAQueryOptionsGrammar#expandItem}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpandItem(STAQueryOptionsGrammar.ExpandItemContext ctx);
	/**
	 * Visit a parse tree produced by {@link STAQueryOptionsGrammar#filter}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFilter(STAQueryOptionsGrammar.FilterContext ctx);
	/**
	 * Visit a parse tree produced by {@link STAQueryOptionsGrammar#orderby}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOrderby(STAQueryOptionsGrammar.OrderbyContext ctx);
	/**
	 * Visit a parse tree produced by {@link STAQueryOptionsGrammar#orderbyItem}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOrderbyItem(STAQueryOptionsGrammar.OrderbyItemContext ctx);
	/**
	 * Visit a parse tree produced by {@link STAQueryOptionsGrammar#skip}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSkip(STAQueryOptionsGrammar.SkipContext ctx);
	/**
	 * Visit a parse tree produced by {@link STAQueryOptionsGrammar#top}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTop(STAQueryOptionsGrammar.TopContext ctx);
	/**
	 * Visit a parse tree produced by {@link STAQueryOptionsGrammar#select}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSelect(STAQueryOptionsGrammar.SelectContext ctx);
	/**
	 * Visit a parse tree produced by {@link STAQueryOptionsGrammar#selectItem}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSelectItem(STAQueryOptionsGrammar.SelectItemContext ctx);
	/**
	 * Visit a parse tree produced by {@link STAQueryOptionsGrammar#boolExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBoolExpr(STAQueryOptionsGrammar.BoolExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link STAQueryOptionsGrammar#boolParenExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBoolParenExpr(STAQueryOptionsGrammar.BoolParenExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link STAQueryOptionsGrammar#anyExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAnyExpr(STAQueryOptionsGrammar.AnyExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link STAQueryOptionsGrammar#parenExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParenExpr(STAQueryOptionsGrammar.ParenExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link STAQueryOptionsGrammar#arithmeticExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArithmeticExpr(STAQueryOptionsGrammar.ArithmeticExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link STAQueryOptionsGrammar#timeExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTimeExpr(STAQueryOptionsGrammar.TimeExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link STAQueryOptionsGrammar#textExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTextExpr(STAQueryOptionsGrammar.TextExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link STAQueryOptionsGrammar#geoExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGeoExpr(STAQueryOptionsGrammar.GeoExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link STAQueryOptionsGrammar#memberExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMemberExpr(STAQueryOptionsGrammar.MemberExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link STAQueryOptionsGrammar#textMethodCallExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTextMethodCallExpr(STAQueryOptionsGrammar.TextMethodCallExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link STAQueryOptionsGrammar#arithmeticMethodCallExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArithmeticMethodCallExpr(STAQueryOptionsGrammar.ArithmeticMethodCallExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link STAQueryOptionsGrammar#temporalMethodCallExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTemporalMethodCallExpr(STAQueryOptionsGrammar.TemporalMethodCallExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link STAQueryOptionsGrammar#boolMethodCallExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBoolMethodCallExpr(STAQueryOptionsGrammar.BoolMethodCallExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link STAQueryOptionsGrammar#textOrMember}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTextOrMember(STAQueryOptionsGrammar.TextOrMemberContext ctx);
	/**
	 * Visit a parse tree produced by {@link STAQueryOptionsGrammar#temporalOrMemberOrISO8601Timestamp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTemporalOrMemberOrISO8601Timestamp(STAQueryOptionsGrammar.TemporalOrMemberOrISO8601TimestampContext ctx);
	/**
	 * Visit a parse tree produced by {@link STAQueryOptionsGrammar#geoOrMember}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGeoOrMember(STAQueryOptionsGrammar.GeoOrMemberContext ctx);
	/**
	 * Visit a parse tree produced by {@link STAQueryOptionsGrammar#iso8601Timestamp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIso8601Timestamp(STAQueryOptionsGrammar.Iso8601TimestampContext ctx);
	/**
	 * Visit a parse tree produced by {@link STAQueryOptionsGrammar#iso8601Timezone}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIso8601Timezone(STAQueryOptionsGrammar.Iso8601TimezoneContext ctx);
	/**
	 * Visit a parse tree produced by {@link STAQueryOptionsGrammar#substringMethodCallExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSubstringMethodCallExpr(STAQueryOptionsGrammar.SubstringMethodCallExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link STAQueryOptionsGrammar#toLowerMethodCallExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitToLowerMethodCallExpr(STAQueryOptionsGrammar.ToLowerMethodCallExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link STAQueryOptionsGrammar#toUpperMethodCallExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitToUpperMethodCallExpr(STAQueryOptionsGrammar.ToUpperMethodCallExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link STAQueryOptionsGrammar#trimMethodCallExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTrimMethodCallExpr(STAQueryOptionsGrammar.TrimMethodCallExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link STAQueryOptionsGrammar#concatMethodCallExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConcatMethodCallExpr(STAQueryOptionsGrammar.ConcatMethodCallExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link STAQueryOptionsGrammar#substringOfMethodCallExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSubstringOfMethodCallExpr(STAQueryOptionsGrammar.SubstringOfMethodCallExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link STAQueryOptionsGrammar#startsWithMethodCallExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStartsWithMethodCallExpr(STAQueryOptionsGrammar.StartsWithMethodCallExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link STAQueryOptionsGrammar#endsWithMethodCallExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEndsWithMethodCallExpr(STAQueryOptionsGrammar.EndsWithMethodCallExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link STAQueryOptionsGrammar#intersectsMethodCallExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIntersectsMethodCallExpr(STAQueryOptionsGrammar.IntersectsMethodCallExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link STAQueryOptionsGrammar#st_commonMethodCallExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSt_commonMethodCallExpr(STAQueryOptionsGrammar.St_commonMethodCallExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link STAQueryOptionsGrammar#st_equalsMethodCallExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSt_equalsMethodCallExpr(STAQueryOptionsGrammar.St_equalsMethodCallExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link STAQueryOptionsGrammar#st_disjointMethodCallExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSt_disjointMethodCallExpr(STAQueryOptionsGrammar.St_disjointMethodCallExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link STAQueryOptionsGrammar#st_touchesMethodCallExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSt_touchesMethodCallExpr(STAQueryOptionsGrammar.St_touchesMethodCallExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link STAQueryOptionsGrammar#st_withinMethodCallExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSt_withinMethodCallExpr(STAQueryOptionsGrammar.St_withinMethodCallExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link STAQueryOptionsGrammar#st_overlapsMethodCallExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSt_overlapsMethodCallExpr(STAQueryOptionsGrammar.St_overlapsMethodCallExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link STAQueryOptionsGrammar#st_crossesMethodCallExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSt_crossesMethodCallExpr(STAQueryOptionsGrammar.St_crossesMethodCallExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link STAQueryOptionsGrammar#st_intersectsMethodCallExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSt_intersectsMethodCallExpr(STAQueryOptionsGrammar.St_intersectsMethodCallExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link STAQueryOptionsGrammar#st_containsMethodCallExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSt_containsMethodCallExpr(STAQueryOptionsGrammar.St_containsMethodCallExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link STAQueryOptionsGrammar#st_relateMethodCallExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSt_relateMethodCallExpr(STAQueryOptionsGrammar.St_relateMethodCallExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link STAQueryOptionsGrammar#lengthMethodCallExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLengthMethodCallExpr(STAQueryOptionsGrammar.LengthMethodCallExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link STAQueryOptionsGrammar#indexOfMethodCallExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIndexOfMethodCallExpr(STAQueryOptionsGrammar.IndexOfMethodCallExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link STAQueryOptionsGrammar#yearMethodCallExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitYearMethodCallExpr(STAQueryOptionsGrammar.YearMethodCallExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link STAQueryOptionsGrammar#monthMethodCallExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMonthMethodCallExpr(STAQueryOptionsGrammar.MonthMethodCallExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link STAQueryOptionsGrammar#dayMethodCallExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDayMethodCallExpr(STAQueryOptionsGrammar.DayMethodCallExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link STAQueryOptionsGrammar#daysMethodCallExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDaysMethodCallExpr(STAQueryOptionsGrammar.DaysMethodCallExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link STAQueryOptionsGrammar#hourMethodCallExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitHourMethodCallExpr(STAQueryOptionsGrammar.HourMethodCallExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link STAQueryOptionsGrammar#minuteMethodCallExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMinuteMethodCallExpr(STAQueryOptionsGrammar.MinuteMethodCallExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link STAQueryOptionsGrammar#secondMethodCallExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSecondMethodCallExpr(STAQueryOptionsGrammar.SecondMethodCallExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link STAQueryOptionsGrammar#timeMethodCallExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTimeMethodCallExpr(STAQueryOptionsGrammar.TimeMethodCallExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link STAQueryOptionsGrammar#dateMethodCallExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDateMethodCallExpr(STAQueryOptionsGrammar.DateMethodCallExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link STAQueryOptionsGrammar#roundMethodCallExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRoundMethodCallExpr(STAQueryOptionsGrammar.RoundMethodCallExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link STAQueryOptionsGrammar#floorMethodCallExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFloorMethodCallExpr(STAQueryOptionsGrammar.FloorMethodCallExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link STAQueryOptionsGrammar#ceilingMethodCallExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCeilingMethodCallExpr(STAQueryOptionsGrammar.CeilingMethodCallExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link STAQueryOptionsGrammar#totalOffsetMinutesExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTotalOffsetMinutesExpr(STAQueryOptionsGrammar.TotalOffsetMinutesExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link STAQueryOptionsGrammar#distanceMethodCallExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDistanceMethodCallExpr(STAQueryOptionsGrammar.DistanceMethodCallExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link STAQueryOptionsGrammar#geoLengthMethodCallExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGeoLengthMethodCallExpr(STAQueryOptionsGrammar.GeoLengthMethodCallExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link STAQueryOptionsGrammar#minDate}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMinDate(STAQueryOptionsGrammar.MinDateContext ctx);
	/**
	 * Visit a parse tree produced by {@link STAQueryOptionsGrammar#maxDate}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMaxDate(STAQueryOptionsGrammar.MaxDateContext ctx);
	/**
	 * Visit a parse tree produced by {@link STAQueryOptionsGrammar#nowDate}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNowDate(STAQueryOptionsGrammar.NowDateContext ctx);
	/**
	 * Visit a parse tree produced by {@link STAQueryOptionsGrammar#andExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAndExpr(STAQueryOptionsGrammar.AndExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link STAQueryOptionsGrammar#orExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOrExpr(STAQueryOptionsGrammar.OrExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link STAQueryOptionsGrammar#notExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNotExpr(STAQueryOptionsGrammar.NotExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link STAQueryOptionsGrammar#eqExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEqExpr(STAQueryOptionsGrammar.EqExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link STAQueryOptionsGrammar#neExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNeExpr(STAQueryOptionsGrammar.NeExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link STAQueryOptionsGrammar#ltExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLtExpr(STAQueryOptionsGrammar.LtExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link STAQueryOptionsGrammar#leExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLeExpr(STAQueryOptionsGrammar.LeExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link STAQueryOptionsGrammar#gtExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGtExpr(STAQueryOptionsGrammar.GtExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link STAQueryOptionsGrammar#geExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGeExpr(STAQueryOptionsGrammar.GeExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link STAQueryOptionsGrammar#addExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAddExpr(STAQueryOptionsGrammar.AddExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link STAQueryOptionsGrammar#subExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSubExpr(STAQueryOptionsGrammar.SubExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link STAQueryOptionsGrammar#mulExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMulExpr(STAQueryOptionsGrammar.MulExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link STAQueryOptionsGrammar#divExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDivExpr(STAQueryOptionsGrammar.DivExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link STAQueryOptionsGrammar#modExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitModExpr(STAQueryOptionsGrammar.ModExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link STAQueryOptionsGrammar#negateExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNegateExpr(STAQueryOptionsGrammar.NegateExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link STAQueryOptionsGrammar#numericLiteral}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNumericLiteral(STAQueryOptionsGrammar.NumericLiteralContext ctx);
	/**
	 * Visit a parse tree produced by {@link STAQueryOptionsGrammar#decimalLiteral}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDecimalLiteral(STAQueryOptionsGrammar.DecimalLiteralContext ctx);
	/**
	 * Visit a parse tree produced by {@link STAQueryOptionsGrammar#escapedString}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEscapedString(STAQueryOptionsGrammar.EscapedStringContext ctx);
	/**
	 * Visit a parse tree produced by {@link STAQueryOptionsGrammar#geographyCollection}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGeographyCollection(STAQueryOptionsGrammar.GeographyCollectionContext ctx);
	/**
	 * Visit a parse tree produced by {@link STAQueryOptionsGrammar#fullCollectionLiteral}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFullCollectionLiteral(STAQueryOptionsGrammar.FullCollectionLiteralContext ctx);
	/**
	 * Visit a parse tree produced by {@link STAQueryOptionsGrammar#collectionLiteral}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCollectionLiteral(STAQueryOptionsGrammar.CollectionLiteralContext ctx);
	/**
	 * Visit a parse tree produced by {@link STAQueryOptionsGrammar#geoLiteral}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGeoLiteral(STAQueryOptionsGrammar.GeoLiteralContext ctx);
	/**
	 * Visit a parse tree produced by {@link STAQueryOptionsGrammar#geographyLineString}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGeographyLineString(STAQueryOptionsGrammar.GeographyLineStringContext ctx);
	/**
	 * Visit a parse tree produced by {@link STAQueryOptionsGrammar#fullLineStringLiteral}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFullLineStringLiteral(STAQueryOptionsGrammar.FullLineStringLiteralContext ctx);
	/**
	 * Visit a parse tree produced by {@link STAQueryOptionsGrammar#lineStringLiteral}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLineStringLiteral(STAQueryOptionsGrammar.LineStringLiteralContext ctx);
	/**
	 * Visit a parse tree produced by {@link STAQueryOptionsGrammar#lineStringData}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLineStringData(STAQueryOptionsGrammar.LineStringDataContext ctx);
	/**
	 * Visit a parse tree produced by {@link STAQueryOptionsGrammar#geographyMultiLineString}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGeographyMultiLineString(STAQueryOptionsGrammar.GeographyMultiLineStringContext ctx);
	/**
	 * Visit a parse tree produced by {@link STAQueryOptionsGrammar#fullMultiLineStringLiteral}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFullMultiLineStringLiteral(STAQueryOptionsGrammar.FullMultiLineStringLiteralContext ctx);
	/**
	 * Visit a parse tree produced by {@link STAQueryOptionsGrammar#multiLineStringLiteral}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMultiLineStringLiteral(STAQueryOptionsGrammar.MultiLineStringLiteralContext ctx);
	/**
	 * Visit a parse tree produced by {@link STAQueryOptionsGrammar#geographyMultiPoint}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGeographyMultiPoint(STAQueryOptionsGrammar.GeographyMultiPointContext ctx);
	/**
	 * Visit a parse tree produced by {@link STAQueryOptionsGrammar#fullMultiPointLiteral}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFullMultiPointLiteral(STAQueryOptionsGrammar.FullMultiPointLiteralContext ctx);
	/**
	 * Visit a parse tree produced by {@link STAQueryOptionsGrammar#multiPointLiteral}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMultiPointLiteral(STAQueryOptionsGrammar.MultiPointLiteralContext ctx);
	/**
	 * Visit a parse tree produced by {@link STAQueryOptionsGrammar#geographyMultiPolygon}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGeographyMultiPolygon(STAQueryOptionsGrammar.GeographyMultiPolygonContext ctx);
	/**
	 * Visit a parse tree produced by {@link STAQueryOptionsGrammar#fullMultiPolygonLiteral}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFullMultiPolygonLiteral(STAQueryOptionsGrammar.FullMultiPolygonLiteralContext ctx);
	/**
	 * Visit a parse tree produced by {@link STAQueryOptionsGrammar#multiPolygonLiteral}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMultiPolygonLiteral(STAQueryOptionsGrammar.MultiPolygonLiteralContext ctx);
	/**
	 * Visit a parse tree produced by {@link STAQueryOptionsGrammar#geographyPoint}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGeographyPoint(STAQueryOptionsGrammar.GeographyPointContext ctx);
	/**
	 * Visit a parse tree produced by {@link STAQueryOptionsGrammar#fullPointLiteral}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFullPointLiteral(STAQueryOptionsGrammar.FullPointLiteralContext ctx);
	/**
	 * Visit a parse tree produced by {@link STAQueryOptionsGrammar#sridLiteral}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSridLiteral(STAQueryOptionsGrammar.SridLiteralContext ctx);
	/**
	 * Visit a parse tree produced by {@link STAQueryOptionsGrammar#pointLiteral}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPointLiteral(STAQueryOptionsGrammar.PointLiteralContext ctx);
	/**
	 * Visit a parse tree produced by {@link STAQueryOptionsGrammar#pointData}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPointData(STAQueryOptionsGrammar.PointDataContext ctx);
	/**
	 * Visit a parse tree produced by {@link STAQueryOptionsGrammar#positionLiteral}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPositionLiteral(STAQueryOptionsGrammar.PositionLiteralContext ctx);
	/**
	 * Visit a parse tree produced by {@link STAQueryOptionsGrammar#coordinate}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCoordinate(STAQueryOptionsGrammar.CoordinateContext ctx);
	/**
	 * Visit a parse tree produced by {@link STAQueryOptionsGrammar#geographyPolygon}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGeographyPolygon(STAQueryOptionsGrammar.GeographyPolygonContext ctx);
	/**
	 * Visit a parse tree produced by {@link STAQueryOptionsGrammar#fullPolygonLiteral}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFullPolygonLiteral(STAQueryOptionsGrammar.FullPolygonLiteralContext ctx);
	/**
	 * Visit a parse tree produced by {@link STAQueryOptionsGrammar#polygonLiteral}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPolygonLiteral(STAQueryOptionsGrammar.PolygonLiteralContext ctx);
	/**
	 * Visit a parse tree produced by {@link STAQueryOptionsGrammar#polygonData}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPolygonData(STAQueryOptionsGrammar.PolygonDataContext ctx);
	/**
	 * Visit a parse tree produced by {@link STAQueryOptionsGrammar#ringLiteral}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRingLiteral(STAQueryOptionsGrammar.RingLiteralContext ctx);
	/**
	 * Visit a parse tree produced by {@link STAQueryOptionsGrammar#geometryCollection}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGeometryCollection(STAQueryOptionsGrammar.GeometryCollectionContext ctx);
	/**
	 * Visit a parse tree produced by {@link STAQueryOptionsGrammar#geometryLineString}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGeometryLineString(STAQueryOptionsGrammar.GeometryLineStringContext ctx);
	/**
	 * Visit a parse tree produced by {@link STAQueryOptionsGrammar#geometryMultiLineString}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGeometryMultiLineString(STAQueryOptionsGrammar.GeometryMultiLineStringContext ctx);
	/**
	 * Visit a parse tree produced by {@link STAQueryOptionsGrammar#geometryMultiPoint}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGeometryMultiPoint(STAQueryOptionsGrammar.GeometryMultiPointContext ctx);
	/**
	 * Visit a parse tree produced by {@link STAQueryOptionsGrammar#geometryMultiPolygon}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGeometryMultiPolygon(STAQueryOptionsGrammar.GeometryMultiPolygonContext ctx);
	/**
	 * Visit a parse tree produced by {@link STAQueryOptionsGrammar#geometryPoint}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGeometryPoint(STAQueryOptionsGrammar.GeometryPointContext ctx);
	/**
	 * Visit a parse tree produced by {@link STAQueryOptionsGrammar#geometryPolygon}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGeometryPolygon(STAQueryOptionsGrammar.GeometryPolygonContext ctx);
	/**
	 * Visit a parse tree produced by {@link STAQueryOptionsGrammar#geographyPrefix}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGeographyPrefix(STAQueryOptionsGrammar.GeographyPrefixContext ctx);
	/**
	 * Visit a parse tree produced by {@link STAQueryOptionsGrammar#geometryPrefix}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGeometryPrefix(STAQueryOptionsGrammar.GeometryPrefixContext ctx);
}