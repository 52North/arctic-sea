package org.n52.iceland.util;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class CRSHelper {
    public static String asHttpPrefix(String prefix) {
        return (!prefix.endsWith("/") && !prefix.isEmpty() && prefix
                .startsWith("http")) ? prefix + "/" : prefix;
    }

    public static String asUrnPrefix(String prefix) {
        return (!prefix.endsWith(":") && !prefix.isEmpty() && prefix
                .startsWith("urn")) ? prefix + ":" : prefix;
    }
}
