package xyz.cofe.grammar.ll.m2;

import xyz.cofe.coll.im.Either;
import xyz.cofe.coll.im.ImList;
import xyz.cofe.coll.im.Tuple2;

import java.util.Optional;

/**
 * Пример описания грамматики
 */
public interface Math2Grammar {
    /**
     * Так описываем лексемы
     */
    sealed interface Lexem {
        /**
         * Пробельный символ - является частью лексемы, но самой не является, по скольку не реализует {@link Lexem}
         */
        record WhiteSpaceChar() {
            public static Optional<WhiteSpaceChar> parse(char c) {
                return Character.isWhitespace(c) ? Optional.of(new WhiteSpaceChar()) : Optional.empty();
            }
        }
        
        /**
         * Лексема (потому что {@link Lexem}) пробельных символов 
         * @param whiteSpace Список лексем или частей которые составляют лексему. <br/>
         *                   По скольку ImList&lt;..&gt; - это цикл, в данном случае кратность должна быть 1+, но тип ImList, подразумевает 0+
         */
        @Skip
        record WhiteSpace(ImList<WhiteSpaceChar> whiteSpace) implements Lexem {}

        /**
         * Цифра, часть лексемы числа, по скольку не реализует {@link Lexem}
         * @param value цифра
         */
        record Digit(int value) {
            public static int digitOf(char c) {
                return switch (c) {
                    case '0' -> 0;
                    case '1' -> 1;
                    case '2' -> 2;
                    case '3' -> 3;
                    case '4' -> 4;
                    case '5' -> 5;
                    case '6' -> 6;
                    case '7' -> 7;
                    case '8' -> 8;
                    case '9' -> 9;
                    default -> -1;
                };
            }

            public static Optional<Digit> match(char c) {
                return digitOf(c) >= 0 ? Optional.of(new Digit(digitOf(c))) : Optional.empty();
            }
        }

        /**
         * Некое число
         */
        sealed interface Num {}

        /**
         * Целое число, синтаксис: {@code head {tail}}
         * @param head Первая цифра
         * @param tail Остальные цифры
         */
        record IntNum(Digit head, ImList<Digit> tail) implements Lexem,
                                                                 Num {}
        /**
         * Часть лексемы
         */
        @Text(".")
        record Dot() {
        }

        /**
         * Лексема рационального числа, синтаксис {@code intPart dot floatPart}
         * @param intPart целая часть
         * @param dot точка
         * @param floatPart дробная часть
         */
        @Before(IntNum.class)
        record FloatNum(IntNum intPart, Dot dot, IntNum floatPart) implements Lexem,
                                                                              Num {}
        /**
         * Лексема открытия скобки
         */
        @Text("(")
        record OpenParentheses() implements Lexem {}

        /**
         * Лексема закрытия скобки
         */
        @Text(")")
        record CloseParentheses() implements Lexem {}

        /**
         * Лексема оператор
         */
        sealed interface SumOp extends Lexem {
            /**
             * Лексема оператор сложения
             */
            @Text("+")
            record PlusOperator() implements SumOp {}

            /**
             * Лексема оператор вычитания
             */
            @Text("-")
            record MinusOperator() implements SumOp {}
        }

        /**
         * Лексема оператор
         */
        sealed interface MulOp extends Lexem {
            /**
             * Лексема оператор умножения
             */
            @Text("*")
            record MultOperator() implements MulOp {}

            /**
             * Лексема оператор деления
             */
            @Text("/")
            record DivOperator() implements MulOp {}
        }
    }

    /**
     * Грамматика мат выражений
     */
    sealed interface Expr {
        /**
         * Сложение {@code Sum ::= head { Lexem.SumOp Mul } }
         * @param head
         * @param tail
         */
        @Start
        record Sum(Mul head, ImList<Tuple2<Lexem.SumOp, Mul>> tail) implements Expr {}

        /**
         * Умножение
         * <pre>
         *     Mul ::= ( Lexem.Num | Group ) { Lexem.MulOp Mul }
         * </pre>
         * @param head
         * @param tail
         */
        record Mul(Either<Lexem.Num, Group> head, ImList<Tuple2<Lexem.MulOp, Mul>> tail) implements Expr {}

        /**
         * Группы
         * <pre>
         * Group ::= '(' Exp ')'
         * </pre>
         * @param open
         * @param exp
         * @param close
         */
        record Group(Lexem.OpenParentheses open, Expr exp, Lexem.CloseParentheses close) implements Expr {}
    }
}
