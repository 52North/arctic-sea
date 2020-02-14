package org.n52.svalbard.odata;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.n52.svalbard.odata.grammar.ODataQueryParserLexer;
import org.n52.svalbard.odata.grammar.ODataQueryParserParser;

/**
 * Test Harness with common functionality for all OData Query Option Tests
 *
 * @author <a href="mailto:j.speckamp@52north.org">Jan Speckamp</a>
 */
public abstract class QueryOptionTests {

    protected final String TOP_QO = "$top=";
    protected final String SKIP_QO = "$skip=";
    protected final String SELECT_QO = "$select=";
    protected final String EXPAND_QO = "$expand=";
    protected final String FILTER_QO = "$filter=";
    protected final String COUNT_QO = "$count=";
    protected final String ORDER_BY_QO = "$orderBy=";

    protected ODataQueryParserLexer lexer;
    protected ODataQueryParserParser parser;

    protected void init(String query) {
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
    }
}
