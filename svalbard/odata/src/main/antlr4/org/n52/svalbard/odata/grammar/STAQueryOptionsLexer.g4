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

/* ----------------------------------------------------------------------------
 * odata-v4.0-abnf subset for parsing Query Parameters expanded with
 * OGC SensorthingsAPI specific functionality. Based on examples given
 * in odata svn repository <a href="https://tools.oasis-open.org/version
 * -control/browse/wsvn/odata/trunk/spec/grammar/ANTLR/#_trunk_spec_grammar_ANTLR_"/a>
 * by Stefan Drees <stefan@drees.name>.
 * @author <a href="mailto:j.speckamp@52north.org">Jan Speckamp</a>
 * ----------------------------------------------------------------------------
 */
lexer grammar STAQueryOptionsLexer;

WS
   : ('\r' | '\t' | '\u000C' | '\n') -> skip
   ;

DOLLAR
   : '$' -> pushMode (Qo) , more
   ;

EQ
   : '='
   ;

COMMA
   : ','
   ;

SP
   : ' '
   ;

SEMI
   : ';'
   ;

MINUS
   : '-'
   ;

SQ
   : [']
   ;

AMPERSAND
   : [&]
   ;

OP
   : '('
   ;

CP
   : ')'
   ;

SIGN
   : (PLUS | MINUS)
   ;

SLASH
   : '/'
   ;

DOT
   : '.'
   ;

COLON
   : ':'
   ;

fragment ALPHASTAR
   : [a-zA-Z]*
   ;

ALPHAPLUS
   : [a-zA-Z]+
   ;

DIGIT
   : [0-9]
   ;

DIGITPLUS
   : DIGIT+
   ;

fragment PLUS
   : '+'
   ;

mode Qo;
// Query Options

QO_AMPERSAND
   : AMPERSAND -> type (AMPERSAND) , popMode
   ;

QO_COUNT
   : 'count' -> pushMode (Count)
   ;

QO_EXPAND
   : 'expand' -> pushMode (Expand)
   ;

QO_FILTER
   : 'filter' -> pushMode (Filter)
   ;

QO_ORDERBY
   : 'orderby' -> pushMode (OrderBy)
   ;

QO_SKIP
   : 'skip' -> pushMode (Skip)
   ;

QO_TOP
   : 'top' -> pushMode (Top)
   ;

QO_SELECT
   : 'select' -> pushMode (Select)
   ;

mode Count;
COUNT_EQ
   : EQ -> type (EQ)
   ;

True_LLC
   : 'true' -> popMode , popMode
   ;

False_LLC
   : 'false' -> popMode , popMode
   ;

mode Skip;
SKIP_EQ
   : EQ -> type (EQ)
   ;

SKIP_DIGITPLUS
   : DIGITPLUS -> type (DIGITPLUS) , popMode , popMode
   ;

mode Top;
TOP_EQ
   : EQ -> type (EQ)
   ;

TOP_DIGITPLUS
   : DIGITPLUS -> type (DIGITPLUS) , popMode , popMode
   ;

mode OrderBy;
ORDERBY_EQ
   : EQ -> type (EQ)
   ;

ORDERBY_SP
   : SP -> type (SP)
   ;

ORDERBY_SLASH
   : SLASH -> type (SLASH)
   ;

ORDERBY_COMMA
   : COMMA -> type (COMMA)
   ;

ASC_LLC
   : 'asc'
   ;

DESC_LLC
   : 'desc'
   ;

ORDERBY_ALPHAPLUS
   : ALPHAPLUS -> type (ALPHAPLUS)
   ;

ORDERBY_AMPERSAND
   : AMPERSAND -> popMode , popMode , type (AMPERSAND)
   ;

ORDERBY_SEMI
   : SEMI -> type (SEMI) , popMode , popMode
   ;

ORDERBY_CP
   : CP -> type (CP) , popMode , popMode
   ;

mode Select;
SELECT_EQ
   : EQ -> type (EQ)
   ;

SELECT_ALPHAPLUS
   : ALPHAPLUS -> type (ALPHAPLUS)
   ;

SELECT_SP
   : SP -> type (SP)
   ;

SELECT_COMMA
   : COMMA -> type (COMMA)
   ;

SELECT_AMPERSAND
   : AMPERSAND -> popMode , popMode , type (AMPERSAND)
   ;

SELECT_SEMI
   : SEMI -> type (SEMI) , popMode , popMode
   ;

SELECT_CP
   : CP -> type (CP) , popMode , popMode
   ;

mode Expand;
EXPAND_EQ
   : EQ -> type (EQ)
   ;

EXPAND_OP
   : OP -> pushMode (DEFAULT_MODE) , type (OP)
   ;

EXPAND_CP
   : CP -> popMode , popMode , popMode ,type (CP)
   ;

EXPAND_ALPHAPLUS
   : ALPHAPLUS -> type (ALPHAPLUS)
   ;

EXPAND_SP
   : SP -> type (SP)
   ;

EXPAND_SLASH
   : SLASH -> type (SLASH)
   ;

EXPAND_COMMA
   : COMMA -> type (COMMA)
   ;

EXPAND_SEMI
   : SEMI -> type (SEMI) , popMode , popMode
   ;

EXPAND_AMPERSAND
   : AMPERSAND -> popMode , popMode , type (AMPERSAND)
   ;

mode Filter;
FILTER_EQ
   : EQ -> type (EQ)
   ;

FILTER_AMPERSAND
   : AMPERSAND -> popMode , popMode , type (AMPERSAND)
   ;

FILTER_SP
   : SP -> type (SP)
   ;

FILTER_SEMI
   : SEMI -> type (SEMI) , popMode
   ;

FILTER_COMMA
   : COMMA -> type (COMMA)
   ;

FILTER_OP
   : OP -> type (OP) , pushMode (Filter), pushMode (Filter), pushMode (Filter)
   ;

FILTER_CP
   : CP -> type (CP), popMode, popMode, popMode
   ;

FILTER_SLASH
   : SLASH -> type (SLASH)
   ;

FILTER_SQ
   : ['] -> type (SQ)
   ;

LITERAL
   : SQ ~ [']* SQ
   ;

SubStringOf_LLC
   : 'substringof'
   ;

StartsWith_LLC
   : 'startswith'
   ;

EndsWith_LLC
   : 'endswith'
   ;

Length_LLC
   : 'length'
   ;

IndexOf_LLC
   : 'indexof'
   ;

Substring_LLC
   : 'substring'
   ;

ToLower_LLC
   : 'tolower'
   ;

ToUpper_LLC
   : 'toupper'
   ;

Trim_LLC
   : 'trim'
   ;

Concat_LLC
   : 'concat'
   ;

Contains_LLC
  : 'contains'
  ;
   // Date Functions
   
Year_LLC
   : 'year'
   ;

Month_LLC
   : 'month'
   ;

Day_LLC
   : 'day'
   ;

Days_LLC
   : 'days'
   ;

Hour_LLC
   : 'hour'
   ;

Minute_LLC
   : 'minute'
   ;

Second_LLC
   : 'second'
   ;

Date_LLC
   : 'date'
   ;

Time_LLC
   : 'time'
   ;

TotalOffsetMinutes_LLC
   : 'totaloffsetminutes'
   ;

MinDateTime_LLC
   : 'mindatetime'
   ;

MaxDateTime_LLC
   : 'maxdatetime'
   ;

Now_LLC
   : 'now'
   ;
   // Math Functions
   
Round_LLC
   : 'round'
   ;

Floor_LLC
   : 'floor'
   ;

Ceiling_LLC
   : 'ceiling'
   ;
   // Geospatial Functions
   
GeoDotDistance_LLC
   : 'geo.distance'
   ;

GeoLength_LLC
   : 'geo.length'
   ;

GeoDotIntersects_LLC
   : 'geo.intersects'
   ;
   // Spatial Relationship Functions
   
ST_equals_LLC
   : 'st_equals'
   ;

ST_disjoint_LLC
   : 'st_disjoint'
   ;

ST_touches_LLC
   : 'st_touches'
   ;

ST_within_LLC
   : 'st_within'
   ;

ST_overlaps_LLC
   : 'st_overlaps'
   ;

ST_crosses_LLC
   : 'st_crosses'
   ;

ST_intersects_LLC
   : 'st_intersects'
   ;

ST_contains_LLC
   : 'st_contains'
   ;

ST_relate_LLC
   : 'st_relate'
   ;
   // Logical Operators
   
And_LLC
   : 'and'
   ;

Or_LLC
   : 'or'
   ;

Not_LLC
   : 'not'
   ;
   // Comparison Operators
   
Eq_LLC
   : 'eq'
   ;

Ne_LLC
   : 'ne'
   ;

Lt_LLC
   : 'lt'
   ;

Le_LLC
   : 'le'
   ;

Gt_LLC
   : 'gt'
   ;

Ge_LLC
   : 'ge'
   ;
   // Arithmetic Operators
   
Add_LLC
   : 'add'
   ;

Sub_LLC
   : 'sub'
   ;

Mul_LLC
   : 'mul'
   ;

Div_LLC
   : 'div'
   ;

Mod_LLC
   : 'mod'
   ;

NotANumber_LXC
   : 'NaN'
   ;

Infinity_LUC
   : 'INF'
   ;

Null_LLC
   : 'null'
   ;

Geography_LLC
   : 'geography' SQ -> pushMode (GeoLiteral)
   ;

Geometry_LLC
   : 'geometry' SQ -> pushMode (GeoLiteral)
   ;

FILTER_ALPHAPLUS
   : ALPHAPLUS -> type (ALPHAPLUS)
   ;

FILTER_DIGIT
   : DIGIT -> type (DIGIT)
   ;

DIGIT4MINUS
   : DIGIT DIGIT DIGIT DIGIT MINUS -> pushMode (iso8601)
   ;

FILTER_DIGITPLUS
   : DIGITPLUS -> type (DIGITPLUS)
   ;

FILTER_FloatingPointLiteral
   : ('0' .. '9')+ '.' ('0' .. '9')* Exponent? FloatTypeSuffix?
   | '.' ('0' .. '9')+ Exponent? FloatTypeSuffix?
   | ('0' .. '9')+ Exponent FloatTypeSuffix?
   | ('0' .. '9')+ FloatTypeSuffix
   ;

fragment Exponent
   : ('e' | 'E') ('+' | '-')? ('0' .. '9')+
   ;

fragment FloatTypeSuffix
   : ('f' | 'F' | 'd' | 'D')
   ;

mode GeoLiteral;
GEOLITERAL_SQ
   : SQ -> type (SQ) , popMode
   ;

GEOLITERAL_OP
   : OP -> type (OP)
   ;

GEOLITERAL_DOT
   : DOT -> type (DOT)
   ;

GEOLITERAL_CP
   : CP -> type (CP)
   ;

GEOLITERAL_MINUS
   : MINUS -> type (MINUS)
   ;

GEOLITERAL_SP
   : SP -> type (SP)
   ;

GEOLITERAL_COMMA
   : COMMA -> type (COMMA)
   ;

GEOLITERAL_DIGITPLUS
   : DIGITPLUS -> type (DIGITPLUS)
   ;

MultiLineStringOP_LUC
   : Multi_LUC LineString_LUC OP
   ;

LineString_LUC
   : 'LINESTRING'
   ;

MultiPointOP_LUC
   : Multi_LUC Point_LUC OP
   ;

MultiPolygonOP_LUC
   : Multi_LUC Polygon_LUC OP
   ;

Point_LUC
   : 'POINT'
   ;

Polygon_LUC
   : 'POLYGON'
   ;

Multi_LUC
   : 'MULTI'
   ;

CollectionOP_LUC
   : 'COLLECTION' OP
   ;

SRID_LLC
   : 'srid'
   ;

DIGIT5
   : DIGIT DIGIT DIGIT DIGIT DIGIT
   ;

mode iso8601;
ISO8601_SP
   : SP -> type (SP), popMode
   ;

ISO8601_AMPERSAND
    : AMPERSAND -> type(AMPERSAND), popMode, popMode, popMode
    ;

ISO8601_CP
    : CP -> type (CP), popMode, popMode, popMode, popMode
    ;

ISO8601_MINUS
   : MINUS -> type (MINUS)
   ;

ISO8601_COLON
   : COLON -> type (COLON)
   ;

ISO8601_SIGN
   : SIGN -> type (SIGN)
   ;

T
   : 'T'
   ;

Z
   : 'Z'
   ;

DIGIT3
   : DIGIT DIGIT DIGIT
   ;

DIGIT2
   : DIGIT DIGIT
   ;

ISO8601_DIGIT
   : DIGIT -> type (DIGIT)
   ;

ISO8601_DOT
   : DOT -> type (DOT)
   ;

