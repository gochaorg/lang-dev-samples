package xyz.cofe.grammar.ll.m1;

import xyz.cofe.grammar.ll.bind.TermBind;

public enum Parentheses {
    @TermBind("(")
    Open,

    @TermBind(")")
    Close;
}
