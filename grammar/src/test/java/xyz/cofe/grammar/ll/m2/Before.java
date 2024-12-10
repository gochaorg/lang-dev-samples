package xyz.cofe.grammar.ll.m2;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Определяет приоритет лексемы
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Before {
    Class<?>[] value();
}
