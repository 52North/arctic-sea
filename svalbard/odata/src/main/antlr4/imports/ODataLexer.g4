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
lexer grammar STALexer;

DecimalLiteral
   : ('0' | '1' .. '9' '0' .. '9'*)
   ;

FloatingPointLiteral
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

WS
   : ('\r' | '\t' | '\u000C' | '\n') -> skip
   ;

SQ
   : [']
   ;

DQ
   : '"'
   ;

SP
   : ' '
   ;

HTAB
   : '\u0009'
   ;

SEMI
   : ';'
   ;

COMMA
   : ','
   ;

EQ
   : '='
   ;

DOLLAR
   : '$'
   ;

PLUS
   : '+'
   ;

MINUS
   : '-'
   ;

SIGN
   : (PLUS | MINUS)
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

OB
   : '['
   ;

CB
   : ']'
   ;

OC
   : '{'
   ;

CC
   : '}'
   ;

TILDE
   : '~'
   ;

STAR
   : '*'
   ;

ESCAPE
   : [\u005C]
   ; // reverse solidus U+005C
   
SLASH
   : '/'
   ;

DOT
   : '.'
   ;

COLON
   : ':'
   ;

PERCENT
   : '%'
   ;

AT_SIGN
   : '@'
   ;

EXCLAMATION
   : '!'
   ;

QUESTION
   : '?'
   ;

UNDERSCORE
   : '_'
   ;

ZERO
   : '0'
   ;

ONE
   : '1'
   ;

TWO
   : '2'
   ;

THREE
   : '3'
   ;
   // Query Options
   
QO_COUNT
   : DOLLAR 'count'
   ;

QO_EXPAND
   : DOLLAR 'expand'
   ;

QO_FILTER
   : DOLLAR 'filter'
   ;

QO_ORDERBY
   : DOLLAR 'orderby'
   ;

QO_SKIP
   : DOLLAR 'skip'
   ;

QO_TOP
   : DOLLAR 'top'
   ;

QO_SELECT
   : DOLLAR 'select'
   ;
   // Options for orderBy
   
Asc_LLC
   : 'asc'
   ;

Desc_LLC
   : 'desc'
   ;
   // String Functions
   
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

D_LUC
   : 'D'
   ;

H_LUC
   : 'H'
   ;

M_LUC
   : 'M'
   ;

P_LUC
   : 'P'
   ;

S_LUC
   : 'S'
   ;

T_LUC
   : 'T'
   ;

X_LUC
   : 'X'
   ;

Z_LUC
   : 'Z'
   ;

B_LLC
   : 'b'
   ;

F_LLC
   : 'f'
   ;

N_LLC
   : 'n'
   ;

R_LLC
   : 'r'
   ;

T_LLC
   : 't'
   ;

V_LLC
   : 'v'
   ;

U_LLC
   : 'u'
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

True_LLC
   : 'true'
   ;

False_LLC
   : 'false'
   ;
   //DateTimeOffset_LAC: Date_LAC Time_LAC O F F S E T ;
   
   //Duration_LAC : D U R A T I O N ;
   
   //TimeOfDay_LAC : Time_LAC O F D A Y ;
   
   //Date_LAC : D A T E ;
   
   //Time_LAC : T I M E ;
   
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

Geography_LLC
   : 'geography'
   ;

Geometry_LLC
   : 'geometry'
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
   // Fallbacks if nothing else matches

Alpha
   : [a-zA-Z]
   ;

Digit
   : [0-9]
   ;

AlphaPlus
   : [a-zA-Z]+
   ;

Digit5
   : Digit Digit Digit Digit Digit
   ;

