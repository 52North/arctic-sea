/*
 * Copyright 2015-2022 52°North Spatial Information Research GmbH
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
package org.n52.svalbard.odata.core;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.n52.shetland.oasis.odata.query.option.QueryOptions;

/**
 * @author <a href="mailto:j.speckamp@52north.org">Jan Speckamp</a>
 */
public class QueryOptionToStringTest extends QueryOptionTests {

    private final String countFilter = "$count=true";
    private final String orderByFilter = "$orderby=id";
    private final String selectFilter = "$select=id";
    private final String skipFilter = "$skip=0";
    private final String topFilter = "$top=1";
    private final String expandFilter = "$expand=Datastreams";
    private final String ampersand = "&";

    private final String filterFilter_literal = "$filter=id eq '52N'";

    private final String filterFilter_spatial = "$filter=st_equals(location, geography'POINT (30 10)')";
    private final String filterFilter_temporal = "$filter=phenomenonTime le 2020-04-21T04:50:00.000Z";
    private final String filterFilter_temporal_tz = "$filter=phenomenonTime le 2020-04-21T04:50:00.000+02:00";

    private final String filterFilter_binary_combination = "$filter=(id eq '52N' and id ne '23')";
    private final String filterFilter_binary_combination_brackets = "$filter=((id eq '52N') and (id ne '23'))";
    private final String filterFilter_unary = "$filter=not (id eq '52N')";

    @Test
    public void testToStringLiteralFilter() {
        String base = countFilter + ampersand
                + selectFilter + ampersand
                + expandFilter + ampersand
                + skipFilter + ampersand
                + topFilter + ampersand
                + orderByFilter + ampersand;
        testToString(base + filterFilter_literal);
        testToString(base + filterFilter_spatial);
        testToString(base + filterFilter_temporal);
        testToString(base + filterFilter_temporal_tz);

        testToString(base + filterFilter_binary_combination);
        testToString(base + filterFilter_binary_combination_brackets);
        testToString(base + filterFilter_unary);

    }

    private void testToString(String reference) {
        init(reference);
        QueryOptions parsedRef = (QueryOptions) parser.queryOptions().accept(new STAQueryOptionVisitor());

        init(parsedRef.toString());
        QueryOptions parsedAct = (QueryOptions) parser.queryOptions().accept(new STAQueryOptionVisitor());

        Assertions.assertEquals(
                parsedRef,
                parsedAct,
                String.format("Parsed QO do not match original! Original: %s Parsed: %s", parsedRef, parsedAct));
    }
}
