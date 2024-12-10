package xyz.cofe.grammar.ll.m2;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Определяет текст лексемы
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Text {
    String value();
}
