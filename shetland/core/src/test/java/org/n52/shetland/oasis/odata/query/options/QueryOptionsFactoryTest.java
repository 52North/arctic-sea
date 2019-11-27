package org.n52.shetland.oasis.odata.query.options;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.n52.shetland.oasis.odata.ODataConstants;
import org.n52.shetland.oasis.odata.query.option.QueryOptionFactory;
import org.n52.shetland.oasis.odata.query.option.QueryOptions;

public class QueryOptionsFactoryTest {

    private static final String URL = "http://test.org/";
    private QueryOptionFactory factory = new QueryOptionFactory();

    @Test
    public void countOptionTrue() {
        QueryOptions options = factory.createQueryOptions(createMap(ODataConstants.QueryOptions.COUNT, "true"), URL);
        assertTrue(options.hasCountOption());
        assertTrue(options.getCountOption().getValue());
    }

    @Test
    public void countOptionFalse() {
        QueryOptions options = factory.createQueryOptions(createMap(ODataConstants.QueryOptions.COUNT, "false"), URL);
        assertTrue(options.hasCountOption());
        assertFalse(options.getCountOption().getValue());
    }

    @Test
    public void expandOption() {
        QueryOptions options = factory.createQueryOptions(createMap(ODataConstants.QueryOptions.EXPAND, "test"), URL);
        assertTrue(options.hasExpandOption());
    }

    @Test
    public void filterOption() {
        QueryOptions options = factory.createQueryOptions(createMap(ODataConstants.QueryOptions.FILTER, "test"), URL);
        assertTrue(options.hasFilterOption());
    }

    @Test
    public void orderByOption() {
        QueryOptions options = factory.createQueryOptions(createMap(ODataConstants.QueryOptions.ORDERBY, "test"), URL);
        assertTrue(options.hasOrderByOption());
    }

    @Test
    public void selectOption() {
        QueryOptions options = factory.createQueryOptions(createMap(ODataConstants.QueryOptions.SELECT, "test"), URL);
        assertTrue(options.hasSelectOption());
    }

    @Test
    public void skipOption() {
        QueryOptions options = factory.createQueryOptions(createMap(ODataConstants.QueryOptions.SKIP, "1"), URL);
        assertTrue(options.hasSkipOption());
        assertEquals(1, options.getSkipOption().getValue());
    }

    @Test
    public void topOption() {
        QueryOptions options = factory.createQueryOptions(createMap(ODataConstants.QueryOptions.TOP, "1"), URL);
        assertTrue(options.hasTopOption());
        assertEquals(1, options.getTopOption().getValue());
    }

    private Map<String, String> createMap(String key, String value) {
        Map<String, String> map = new LinkedHashMap<String, String>();
        map.put(key, value);
        return map;
    }

}
