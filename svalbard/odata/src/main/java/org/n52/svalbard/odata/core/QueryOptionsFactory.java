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
package org.n52.svalbard.odata.core;

import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.Vocabulary;
import org.n52.shetland.oasis.odata.query.option.QueryOptions;
import org.n52.shetland.ogc.filter.FilterClause;
import org.n52.svalbard.odata.grammar.STAQueryOptionsGrammar;
import org.n52.svalbard.odata.grammar.STAQueryOptionsLexer;

import java.util.Set;

/**
 * @author <a href="mailto:j.speckamp@52north.org">Jan Speckamp</a>
 */
@SuppressWarnings("unchecked")
public class QueryOptionsFactory {
    public STAQueryOptionsLexer createLexer(String query) {
        return new STAQueryOptionsLexer(CharStreams.fromString(query.trim()));
    }

    public STAQueryOptionsGrammar createGrammar(String query) {
        return createGrammar(createLexer(query));
    }

    private STAQueryOptionsGrammar createGrammar(STAQueryOptionsLexer lexer) {
        STAQueryOptionsGrammar parser = new STAQueryOptionsGrammar(new CommonTokenStream(lexer));
        parser.addErrorListener(new CustomErrorListener(lexer.getVocabulary()));
        return parser;
    }

    //TODO: make nicer
    public QueryOptions createQueryOptions(String query) {
        return createGrammar(query).queryOptions().<QueryOptions>accept(new STAQueryOptionVisitor());
    }

    public QueryOptions createQueryOptions(Set<FilterClause> filters) {
        return new QueryOptions("", filters);
    }

    public QueryOptions createDummy() {
        return new QueryOptions("", null);
    }

    private static final class CustomErrorListener extends BaseErrorListener {
        private final Vocabulary vocabulary;

        private CustomErrorListener(Vocabulary vocabulary) {
            this.vocabulary = vocabulary;
        }

        @Override
        public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine,
                                String msg, RecognitionException e) {

            String message = String.format("failed to parse due to %s with offending token: %s", msg,
                                           vocabulary.getDisplayName(e.getOffendingToken().getType()));
            throw new IllegalStateException(message, e);
        }
    }
}
