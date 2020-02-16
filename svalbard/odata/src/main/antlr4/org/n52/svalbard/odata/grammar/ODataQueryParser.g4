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
grammar ODataQueryParser;

import ODataLexer;
queryOptions
   : systemQueryOption (AMPERSAND systemQueryOption)* EOF
   ;

systemQueryOption
   : expand
   | filter
   | orderby
   | count
   | skip
   | top
   | select
   ;

count
   : QO_COUNT EQ True_LLC
   ;

expand
   : QO_EXPAND EQ expandItem (COMMA (SP)* expandItem)*
   ;

expandItem
   : memberExpr (OP systemQueryOption (SEMI systemQueryOption)* CP)?
   ;

filter
   : QO_FILTER EQ boolExpr
   ;

orderby
   : QO_ORDERBY EQ orderbyItem (COMMA (SP)* orderbyItem)*
   ;

orderbyItem
   : memberExpr (SP (Asc_LLC | Desc_LLC))?
   ;

skip
   : QO_SKIP EQ DecimalLiteral
   ;

top
   : QO_TOP EQ DecimalLiteral
   ;

select
   : QO_SELECT EQ selectItem (COMMA (SP)* selectItem)*
   ;

selectItem
   : memberExpr (OP systemQueryOption (SEMI systemQueryOption)* CP)?
   ;
/* ----------------------------------------------------------------------------
 * 3. Expressions
 * ----------------------------------------------------------------------------
 */
   
   
boolExpr
   : (boolMethodCallExpr | notExpr | anyExpr (eqExpr | neExpr | ltExpr | leExpr | gtExpr | geExpr) | boolParenExpr) (andExpr | orExpr)?
   ;

boolParenExpr
   : OP (SP)* boolExpr (SP)* CP
   ;

anyExpr
   : memberExpr
   | arithmeticExpr
   | geoExpr
   | timeExpr
   | textExpr
   | parenExpr
   ;

parenExpr
   : OP (SP)* anyExpr (SP)* CP
   ;

arithmeticExpr
   : (OP (SP)*)? (numericLiteral | memberExpr | negateExpr | arithmeticMethodCallExpr) (addExpr | subExpr | mulExpr | divExpr | modExpr)? (OP (SP)*)?
   ;

timeExpr
   : temporalMethodCallExpr
   ;
   //TODO: expand this and do timestamp validation here
   
textExpr
   : sq_enclosed_string
   | textMethodCallExpr
   ;

geoExpr
   : geographyCollection
   | geographyLineString
   | geographyMultiLineString
   | geographyMultiPoint
   | geographyMultiPolygon
   | geographyPoint
   | geographyPolygon
   | geometryCollection
   | geometryLineString
   | geometryMultiLineString
   | geometryMultiPoint
   | geometryMultiPolygon
   | geometryPoint
   | geometryPolygon
   ;

memberExpr
   : AlphaPlus (SLASH AlphaPlus)*
   ;

textMethodCallExpr
   :
   | toLowerMethodCallExpr
   | toUpperMethodCallExpr
   | trimMethodCallExpr
   | substringMethodCallExpr
   | concatMethodCallExpr
   ;

arithmeticMethodCallExpr
   : lengthMethodCallExpr
   | indexOfMethodCallExpr
   | yearMethodCallExpr
   | monthMethodCallExpr
   | dayMethodCallExpr
   | daysMethodCallExpr
   | hourMethodCallExpr
   | minuteMethodCallExpr
   | secondMethodCallExpr
   | dateMethodCallExpr
   | roundMethodCallExpr
   | floorMethodCallExpr
   | ceilingMethodCallExpr
   | distanceMethodCallExpr
   | geoLengthMethodCallExpr
   | totalOffsetMinutesExpr
   ;

temporalMethodCallExpr
   : timeMethodCallExpr
   | nowDate
   | minDate
   | maxDate
   ;

boolMethodCallExpr
   : endsWithMethodCallExpr
   | startsWithMethodCallExpr
   | substringOfMethodCallExpr
   | intersectsMethodCallExpr
   | st_equalsMethodCallExpr
   | st_disjointMethodCallExpr
   | st_touchesMethodCallExpr
   | st_withinMethodCallExpr
   | st_overlapsMethodCallExpr
   | st_crossesMethodCallExpr
   | st_intersectsMethodCallExpr
   | st_containsMethodCallExpr
   | st_relateMethodCallExpr
   ;

textOrMember
   : (textExpr | memberExpr)
   ;

temporalOrMemberOrString
   : (temporalMethodCallExpr | memberExpr | sq_enclosed_string)
   ;

geoOrMember
   : (geoExpr | memberExpr)
   ;

substringMethodCallExpr
   : Substring_LLC OP (SP)* textOrMember (SP)* COMMA (SP)* arithmeticExpr CP
   ;

toLowerMethodCallExpr
   : ToLower_LLC OP (SP)* textOrMember (SP)* CP
   ;

toUpperMethodCallExpr
   : ToUpper_LLC OP (SP)* textOrMember (SP)* CP
   ;

trimMethodCallExpr
   : Trim_LLC OP (SP)* textOrMember (SP)* CP
   ;

concatMethodCallExpr
   : Concat_LLC OP (SP)* textOrMember (SP)* COMMA (SP)* textOrMember (SP)* CP
   ;

substringOfMethodCallExpr
   : SubStringOf_LLC OP (SP)* textOrMember (SP)* COMMA (SP)* textOrMember (SP)* CP
   ;

startsWithMethodCallExpr
   : StartsWith_LLC OP (SP)* textOrMember (SP)* COMMA (SP)* textOrMember (SP)* CP
   ;

endsWithMethodCallExpr
   : EndsWith_LLC OP (SP)* textOrMember (SP)* COMMA (SP)* textOrMember (SP)* CP
   ;

intersectsMethodCallExpr
   : GeoDotIntersects_LLC OP (SP)* geoOrMember (SP)* COMMA (SP)* geoOrMember (SP)* CP
   ;
   // Spatial Relationship Functions
   
st_commonMethodCallExpr
   : OP (SP)* geoOrMember (SP)* COMMA (SP)* geoOrMember (SP)* CP
   ;

st_equalsMethodCallExpr
   : ST_equals_LLC st_commonMethodCallExpr
   ;

st_disjointMethodCallExpr
   : ST_disjoint_LLC st_commonMethodCallExpr
   ;

st_touchesMethodCallExpr
   : ST_touches_LLC st_commonMethodCallExpr
   ;

st_withinMethodCallExpr
   : ST_within_LLC st_commonMethodCallExpr
   ;

st_overlapsMethodCallExpr
   : ST_overlaps_LLC st_commonMethodCallExpr
   ;

st_crossesMethodCallExpr
   : ST_crosses_LLC st_commonMethodCallExpr
   ;

st_intersectsMethodCallExpr
   : ST_intersects_LLC st_commonMethodCallExpr
   ;

st_containsMethodCallExpr
   : ST_contains_LLC st_commonMethodCallExpr
   ;

st_relateMethodCallExpr
   : ST_relate_LLC OP (SP)* geoOrMember (SP)* COMMA (SP)* geoOrMember (SP)* COMMA (SP)* sq_enclosed_string (SP)* CP
   ;

lengthMethodCallExpr
   : Length_LLC OP (SP)* textOrMember (SP)* CP
   ;

indexOfMethodCallExpr
   : IndexOf_LLC OP (SP)* textOrMember (SP)* COMMA (SP)* textOrMember (SP)* CP
   ;

yearMethodCallExpr
   : Year_LLC OP (SP)* temporalOrMemberOrString (SP)* CP
   ;

monthMethodCallExpr
   : Month_LLC OP (SP)* temporalOrMemberOrString (SP)* CP
   ;

dayMethodCallExpr
   : Day_LLC OP (SP)* temporalOrMemberOrString (SP)* CP
   ;

daysMethodCallExpr
   : Days_LLC OP (SP)* temporalOrMemberOrString (SP)* CP
   ;

hourMethodCallExpr
   : Hour_LLC OP (SP)* temporalOrMemberOrString (SP)* CP
   ;

minuteMethodCallExpr
   : Minute_LLC OP (SP)* temporalOrMemberOrString (SP)* CP
   ;

secondMethodCallExpr
   : Second_LLC OP (SP)* temporalOrMemberOrString (SP)* CP
   ;

timeMethodCallExpr
   : Time_LLC OP (SP)* temporalOrMemberOrString (SP)* CP
   ;

dateMethodCallExpr
   : Date_LLC OP (SP)* temporalOrMemberOrString (SP)* CP
   ;

roundMethodCallExpr
   : Round_LLC OP (SP)* arithmeticExpr (SP)* CP
   ;

floorMethodCallExpr
   : Floor_LLC OP (SP)* arithmeticExpr (SP)* CP
   ;

ceilingMethodCallExpr
   : Ceiling_LLC OP (SP)* arithmeticExpr (SP)* CP
   ;

totalOffsetMinutesExpr
   : TotalOffsetMinutes_LLC OP (SP)* temporalOrMemberOrString (SP)* CP
   ;

distanceMethodCallExpr
   : GeoDotDistance_LLC OP (SP)* geoOrMember (SP)* COMMA (SP)* geoOrMember (SP)* CP
   ;

geoLengthMethodCallExpr
   : GeoLength_LLC OP (SP)* geoOrMember (SP)* CP
   ;

minDate
   : MinDateTime_LLC OP (SP)* CP
   ;

maxDate
   : MaxDateTime_LLC OP (SP)* CP
   ;

nowDate
   : Now_LLC OP (SP)* CP
   ;

andExpr
   : SP And_LLC SP boolExpr
   ;

orExpr
   : SP Or_LLC SP boolExpr
   ;

notExpr
   : Not_LLC SP boolExpr
   ;

eqExpr
   : SP Eq_LLC SP anyExpr
   ;

neExpr
   : SP Ne_LLC SP anyExpr
   ;

ltExpr
   : SP Lt_LLC SP anyExpr
   ;

leExpr
   : SP Le_LLC SP anyExpr
   ;

gtExpr
   : SP Gt_LLC SP anyExpr
   ;

geExpr
   : SP Ge_LLC SP anyExpr
   ;

addExpr
   : SP Add_LLC SP arithmeticExpr
   ;

subExpr
   : SP Sub_LLC SP arithmeticExpr
   ;

mulExpr
   : SP Mul_LLC SP arithmeticExpr
   ;

divExpr
   : SP Div_LLC SP arithmeticExpr
   ;

modExpr
   : SP Mod_LLC SP arithmeticExpr
   ;

negateExpr
   : MINUS (SP)* arithmeticExpr
   ;
   //TODO(specki): What part of this do we want to do? Do we need "single" which was previously detected here or is "number" enough
   
numericLiteral
   : DecimalLiteral
   | FloatingPointLiteral
   ;

sq_enclosed_string
   : SQ (SP | Alpha | AlphaPlus | STAR | COMMA | SEMI | DecimalLiteral | FloatingPointLiteral)* SQ
   ;
   //TODO: possibly expand this to allow for more diverse characters (e.g. special characters).
   
geographyCollection
   : geographyPrefix fullCollectionLiteral SQ
   ;

fullCollectionLiteral
   : (sridLiteral)? collectionLiteral
   ;

collectionLiteral
   : CollectionOP_LUC (SP)* geoLiteral (COMMA geoLiteral)* (SP)* CP
   ;

geoLiteral
   : collectionLiteral
   | lineStringLiteral
   | multiPointLiteral
   | multiLineStringLiteral
   | multiPolygonLiteral
   | pointLiteral
   | polygonLiteral
   ;

geographyLineString
   : geographyPrefix fullLineStringLiteral SQ
   ;

fullLineStringLiteral
   : (sridLiteral)? lineStringLiteral
   ;

lineStringLiteral
   : LineString_LUC (SP)* lineStringData (SP)*
   ;

lineStringData
   : OP positionLiteral (COMMA (SP)? positionLiteral)+ CP
   ;

geographyMultiLineString
   : geographyPrefix fullMultiLineStringLiteral SQ
   ;

fullMultiLineStringLiteral
   : (sridLiteral)? multiLineStringLiteral
   ;

multiLineStringLiteral
   : MultiLineStringOP_LUC (SP)* (lineStringData (COMMA (SP)? lineStringData)*)? (SP)* CP
   ;

geographyMultiPoint
   : geographyPrefix fullMultiPointLiteral SQ
   ;

fullMultiPointLiteral
   : (sridLiteral)? multiPointLiteral
   ;

multiPointLiteral
   : MultiPointOP_LUC (SP)* (pointData* (COMMA (SP)? pointData))? (SP)* CP
   ;

geographyMultiPolygon
   : geographyPrefix fullMultiPolygonLiteral SQ
   ;

fullMultiPolygonLiteral
   : (sridLiteral)? multiPolygonLiteral
   ;

multiPolygonLiteral
   : MultiPolygonOP_LUC (SP)* (polygonData (COMMA (SP)? polygonData)*)? (SP)* CP
   ;

geographyPoint
   : geographyPrefix fullPointLiteral SQ
   ;

fullPointLiteral
   : (sridLiteral)? pointLiteral
   ;

sridLiteral
   : SRID_LLC EQ (Digit5)+ SEMI
   ;

pointLiteral
   : Point_LUC (SP)* pointData (SP)*
   ;

pointData
   : OP positionLiteral CP
   ;

positionLiteral
   : coordinate SP coordinate
   ;
   //TODO: add validation that coordinates are in possible range e.g. <=180
   
coordinate
   : (MINUS)? (FloatingPointLiteral | DecimalLiteral)
   ;

geographyPolygon
   : geographyPrefix fullPolygonLiteral SQ
   ;

fullPolygonLiteral
   : (sridLiteral)? polygonLiteral
   ;

polygonLiteral
   : Polygon_LUC (SP)* polygonData (SP)*
   ;

polygonData
   : OP ringLiteral (COMMA (SP)? ringLiteral)* CP
   ;

ringLiteral
   : OP positionLiteral (COMMA (SP)? positionLiteral)* CP
   ;

geometryCollection
   : geometryPrefix fullCollectionLiteral SQ
   ;

geometryLineString
   : geometryPrefix fullLineStringLiteral SQ
   ;

geometryMultiLineString
   : geometryPrefix fullMultiLineStringLiteral SQ
   ;

geometryMultiPoint
   : geometryPrefix fullMultiPointLiteral SQ
   ;

geometryMultiPolygon
   : geometryPrefix fullMultiPolygonLiteral SQ
   ;

geometryPoint
   : geometryPrefix fullPointLiteral SQ
   ;

geometryPolygon
   : geometryPrefix fullPolygonLiteral SQ
   ;

geographyPrefix
   : Geography_LLC SQ
   ;

geometryPrefix
   : Geometry_LLC SQ
   ;

