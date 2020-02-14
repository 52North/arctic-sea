package org.n52.svalbard.odata.expr;

import java.util.Optional;

/**
 * @author <a href="mailto:j.speckamp@52north.org">Jan Speckamp</a>
 */
public interface TextExpr extends Expr {

    default boolean isTextValue() {
        return true;
    }

    default Optional<TextExpr> asTextValue() {
        return Optional.empty();
    }
}
