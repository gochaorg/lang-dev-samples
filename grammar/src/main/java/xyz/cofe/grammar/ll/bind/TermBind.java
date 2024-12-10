package xyz.cofe.grammar.ll.bind;

import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Привязка класса-лексемы к конкретному значению
 *
 * <pre>
 * public enum Parentheses {
 *     // Указываем какой символ привязываем
 *     &#x40;TermBind("(")
 *     Open,
 *
 *     &#x40;TermBind(")")
 *     Close;
 * }
 *
 * public static Atom parse(
 *   // Указываем ссылку на этот символ
 *   &#x40;TermBind("(") Parentheses left,
 *   Expr expr,
 *   &#x40;TermBind(")") Parentheses right)
 * </pre>
 */
@Repeatable(TermBinds.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface TermBind {
    /**
     * Идентификатор по которому происходит привязка
     * @return Символ(ы) к которым привязываемся
     */
    String value();
    boolean skip() default false;
}
