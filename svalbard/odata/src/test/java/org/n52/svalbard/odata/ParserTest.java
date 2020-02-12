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
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.atn.PredictionMode;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.Trees;
import org.junit.jupiter.api.Test;
import org.n52.shetland.oasis.odata.query.option.QueryOptions;
import org.n52.svalbard.odata.grammar.ODataQueryParserLexer;
import org.n52.svalbard.odata.grammar.ODataQueryParserParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

public class ParserTest implements TestConstants {

    private static final Logger LOGGER = LoggerFactory.getLogger(ParserTest.class);

    @Test
    public void testExampleQueries() throws Exception {
        for (String exampleQuery : EXAMPLE_QUERIES) {
            System.out.println(exampleQuery);
            ODataQueryParserLexer l =
                    new ODataQueryParserLexer(new ANTLRInputStream(exampleQuery));
            ODataQueryParserParser p = new ODataQueryParserParser(new CommonTokenStream(l));
            //TODO: change this mode to LL for production use!
            p.getInterpreter().setPredictionMode(PredictionMode.LL_EXACT_AMBIG_DETECTION);
            p.setProfile(true);
            p.addErrorListener(new BaseErrorListener() {
                @Override
                public void syntaxError(Recognizer<?, ?> recognizer,
                                        Object offendingSymbol,
                                        int line,
                                        int charPositionInLine,
                                        String msg,
                                        RecognitionException e) {
                    throw new IllegalStateException("failed to parse at line " + line + " due to " + msg + " with " +
                                                            "offending token: " + e.getOffendingToken().toString(), e);
                }
            });

            //ODataQueryParserParser.QueryOptionsContext queryOptionsContext = p.queryOptions();
            // QueryOptions accept = (QueryOptions) queryOptionsContext.accept(new ODataQueryVisitor());
            // System.out.println(accept.toString());

            System.out.println(printSyntaxTree(p, p.queryOptions()));
        }
    }

    @Test
    public void testExampleFunctions() throws Exception {
        for (String exampleFunction : EXAMPLE_FUNCTIONS) {
            System.out.println(exampleFunction);
            ODataQueryParserLexer l =
                    new ODataQueryParserLexer(new ANTLRInputStream(exampleFunction));
            ODataQueryParserParser p = new ODataQueryParserParser(new CommonTokenStream(l));
            //TODO: change this mode to LL for production use!
            p.getInterpreter().setPredictionMode(PredictionMode.LL_EXACT_AMBIG_DETECTION);
            p.setProfile(true);
            p.addErrorListener(new BaseErrorListener() {
                @Override
                public void syntaxError(Recognizer<?, ?> recognizer,
                                        Object offendingSymbol,
                                        int line,
                                        int charPositionInLine,
                                        String msg,
                                        RecognitionException e) {
                    throw new IllegalStateException("failed to parse at line " + line + " due to " + msg + " with " +
                                                            "offending token: " + e.getOffendingToken().toString(), e);
                }
            });

            // ODataQueryParserParser.QueryOptionsContext queryOptionsContext = p.queryOptions();
            // queryOptionsContext.accept(new STAVisitor());
            //System.out.println(queryOptionsContext.toStringTree());

            System.out.println(printSyntaxTree(p, p.boolExpr()));
        }
    }

    // DEBUG ONLY! TAKEN FROM: https://stackoverflow
    // .com/questions/50064110/antlr4-java-pretty-print-parse-tree-to-stdout
    public static String printSyntaxTree(Parser parser, ParseTree root) {
        StringBuilder buf = new StringBuilder();
        recursive(root, buf, 0, Arrays.asList(parser.getRuleNames()));
        return buf.toString();
    }

    // DEBUG ONLY! TAKEN FROM: https://stackoverflow
    // .com/questions/50064110/antlr4-java-pretty-print-parse-tree-to-stdout
    private static void recursive(ParseTree aRoot, StringBuilder buf, int offset, List<String> ruleNames) {
        for (int i = 0; i < offset; i++) {
            buf.append("  ");
        }
        String text = Trees.getNodeText(aRoot, ruleNames);
        if (text.equals(" ")) {
            text = "SP";
        }
        ;
        buf.append(text + "\n");
        if (aRoot instanceof ParserRuleContext) {
            ParserRuleContext prc = (ParserRuleContext) aRoot;
            if (prc.children != null) {
                for (ParseTree child : prc.children) {
                    recursive(child, buf, offset + 1, ruleNames);
                }
            }
        }
    }
}
