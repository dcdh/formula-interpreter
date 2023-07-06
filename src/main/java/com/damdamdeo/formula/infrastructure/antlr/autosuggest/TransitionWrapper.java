package com.damdamdeo.formula.infrastructure.antlr.autosuggest;

import org.antlr.v4.runtime.atn.ATNState;
import org.antlr.v4.runtime.atn.Transition;

public record TransitionWrapper(ATNState source, Transition transition) {

    @Override
    public String toString() {
        return transition.getClass().getSimpleName() + " from " + source + " to " + transition.target;
    }

}
