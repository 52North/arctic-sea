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

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.n52.shetland.oasis.odata.query.option.QueryOptions;
import org.n52.shetland.ogc.filter.FilterClause;
import org.n52.svalbard.odata.grammar.ODataQueryParserLexer;
import org.n52.svalbard.odata.grammar.ODataQueryParserParser;

import java.util.Set;

/**
 * @author <a href="mailto:j.speckamp@52north.org">Jan Speckamp</a>
 */
public class QueryOptionsFactory {

    private ODataQueryParserLexer lexer;
    private ODataQueryParserParser parser;
    private ODataQueryVisitor visitor = new ODataQueryVisitor();

    //TODO: make nicer
    public QueryOptions createQueryOptions(String query) {
        lexer = new ODataQueryParserLexer(new ANTLRInputStream(query));
        parser = new ODataQueryParserParser(new CommonTokenStream(lexer));
        parser.addErrorListener(new BaseErrorListener() {
            @Override
            public void syntaxError(Recognizer<?, ?> recognizer,
                                    Object offendingSymbol,
                                    int line,
                                    int charPositionInLine,
                                    String msg,
                                    RecognitionException e) {
                throw new IllegalStateException("failed to parse due to " + msg + " with " +
                                                        "offending token: " + lexer.getVocabulary()
                                                                                   .getDisplayName(e.getOffendingToken()
                                                                                                    .getType()), e);
            }
        });
        return parser.queryOptions().<QueryOptions>accept(visitor);
    }

    public QueryOptions createQueryOptions(Set<FilterClause> filters) {
        return new QueryOptions("", filters);
    }

    public QueryOptions createDummy() {
        return new QueryOptions("", null);
    }
}
