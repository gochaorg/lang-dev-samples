package xyz.cofe.grammar.ll.bind;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Список лексем
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Terms {
    Class<?>[] value();
}
