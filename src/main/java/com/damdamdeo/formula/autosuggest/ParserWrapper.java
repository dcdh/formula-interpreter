package com.damdamdeo.formula.autosuggest;

import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.Vocabulary;
import org.antlr.v4.runtime.atn.ATN;
import org.antlr.v4.runtime.atn.ATNState;
import org.antlr.v4.runtime.atn.AtomTransition;
import org.antlr.v4.runtime.atn.Transition;

class ParserWrapper {
    private final Vocabulary lexerVocabulary;

    private final ATN parserAtn;
    private final String[] parserRuleNames;

    public ParserWrapper(ParserFactory parserFactory, Vocabulary lexerVocabulary) {
        this.lexerVocabulary = lexerVocabulary;

        Parser parserForAtnOnly = parserFactory.createParser(null);
        this.parserAtn = parserForAtnOnly.getATN();
        this.parserRuleNames = parserForAtnOnly.getRuleNames();
    }

    public String toString(ATNState parserState) {
        String ruleName = this.parserRuleNames[parserState.ruleIndex];
        return "*" + ruleName + "* " + parserState.getClass().getSimpleName() + " " + parserState;
    }

    public String toString(Transition t) {
        String nameOrLabel = t.getClass().getSimpleName();
        if (t instanceof AtomTransition) {
            nameOrLabel += ' ' + this.lexerVocabulary.getDisplayName(((AtomTransition) t).label);
        }
        return nameOrLabel + " -> " + toString(t.target);
    }

    public ATNState getAtnState(int stateNumber) {
        return parserAtn.states.get(stateNumber);
    }
}
