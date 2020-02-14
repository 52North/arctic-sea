package org.n52.svalbard.odata;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.n52.shetland.filter.SkipTopFilter;
import org.n52.shetland.ogc.filter.FilterConstants;

/**
 * Basic Test cases for testing $top Query Option.
 * Note: Parser must start from root queryOptions() to match EOF
 *
 * @author <a href="mailto:j.speckamp@52north.org">Jan Speckamp</a>
 */
public class TopQueryOptionTest extends QueryOptionTests {

    @Test
    public void testInvalidTopOption() {
        init(TOP_QO + "sdf");
        Assertions.assertThrows(
                IllegalStateException.class,
                () -> parser.queryOptions().systemQueryOption(0).top().accept(new ODataQueryVisitor())
        );

        init(TOP_QO + "1.1");
        Assertions.assertThrows(
                IllegalStateException.class,
                () -> parser.queryOptions().systemQueryOption(0).top().accept(new ODataQueryVisitor())
        );

        init(TOP_QO + "");
        Assertions.assertThrows(
                NullPointerException.class,
                () -> parser.queryOptions().systemQueryOption(0).top().accept(new ODataQueryVisitor())
        );

        init(TOP_QO + "123" + Long.MAX_VALUE);
        Assertions.assertThrows(
                NumberFormatException.class,
                () -> parser.queryOptions().systemQueryOption(0).top().accept(new ODataQueryVisitor())
        );
    }

    @Test
    public void testValidTopOption() {
        int val = 1;
        SkipTopFilter filter;
        init(TOP_QO + val);
        filter = (SkipTopFilter) parser.queryOptions().systemQueryOption(0).top().accept(new ODataQueryVisitor());
        Assertions.assertEquals(filter.getOperator(), FilterConstants.SkipTopOperator.Top);
        Assertions.assertEquals(filter.getValue(), val);

        val = 0;
        init(TOP_QO + val);
        filter = (SkipTopFilter) parser.queryOptions().systemQueryOption(0).top().accept(new ODataQueryVisitor());
        Assertions.assertEquals(filter.getOperator(), FilterConstants.SkipTopOperator.Top);
        Assertions.assertEquals(filter.getValue(), val);

        val = 100000;
        init(TOP_QO + val);
        filter = (SkipTopFilter) parser.queryOptions().systemQueryOption(0).top().accept(new ODataQueryVisitor());
        Assertions.assertEquals(filter.getOperator(), FilterConstants.SkipTopOperator.Top);
        Assertions.assertEquals(filter.getValue(), val);

        long longval = Long.MAX_VALUE;
        init(TOP_QO + longval);
        filter = (SkipTopFilter) parser.queryOptions().systemQueryOption(0).top().accept(new ODataQueryVisitor());
        Assertions.assertEquals(filter.getOperator(), FilterConstants.SkipTopOperator.Top);
        Assertions.assertEquals(filter.getValue(), longval);
    }
}
