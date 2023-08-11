package com.damdamdeo.formula.infrastructure.antlr.autosuggest;

import org.antlr.v4.runtime.atn.ATNState;
import org.antlr.v4.runtime.atn.AtomTransition;
import org.antlr.v4.runtime.atn.SetTransition;
import org.antlr.v4.runtime.atn.Transition;

import java.util.*;

class TokenSuggester {
    private final LexerWrapper lexerWrapper;
    private final CasePreference casePreference;
    private final Set<String> suggestions;
    private final List<Integer> visitedLexerStates;
    private final String origPartialToken;

    public TokenSuggester(final String origPartialToken,
                          final LexerWrapper lexerWrapper,
                          final CasePreference casePreference) {
        this.origPartialToken = Objects.requireNonNull(origPartialToken);
        this.lexerWrapper = Objects.requireNonNull(lexerWrapper);
        this.casePreference = Objects.requireNonNull(casePreference);
        this.suggestions = new TreeSet<>();
        this.visitedLexerStates = new ArrayList<>();
    }

    public Collection<String> suggest(final Collection<Integer> nextParserTransitionLabels) {
        for (final int nextParserTransitionLabel : nextParserTransitionLabels) {
            final int nextTokenRuleNumber = nextParserTransitionLabel - 1; // Count from 0 not from 1
            final ATNState lexerState = this.lexerWrapper.findStateByRuleNumber(nextTokenRuleNumber);
            suggest("", lexerState, origPartialToken);
        }
        return suggestions;
    }

    private void suggest(final String tokenSoFar, final ATNState lexerState, final String remainingText) {
        if (visitedLexerStates.contains(lexerState.stateNumber)) {
            return; // avoid infinite loop and stack overflow
        }
        visitedLexerStates.add(lexerState.stateNumber);
        try {
            final Transition[] transitions = lexerState.getTransitions();
            final boolean tokenNotEmpty = tokenSoFar.length() > 0;
            final boolean noMoreCharactersInToken = (transitions.length == 0);
            if (tokenNotEmpty && noMoreCharactersInToken) {
                addSuggestedToken(tokenSoFar);
                return;
            }
            for (Transition trans : transitions) {
                suggestViaLexerTransition(tokenSoFar, remainingText, trans);
            }
        } finally {
            visitedLexerStates.remove(visitedLexerStates.size() - 1);
        }
    }

    private void suggestViaLexerTransition(final String tokenSoFar, final String remainingText, final Transition trans) {
        if (trans.isEpsilon()) {
            suggest(tokenSoFar, trans.target, remainingText);
        } else if (trans instanceof AtomTransition) {
            final String newTokenChar = getAddedTextFor((AtomTransition) trans);
            if (remainingText.isEmpty() || remainingText.startsWith(newTokenChar)) {
                suggestViaNonEpsilonLexerTransition(tokenSoFar, remainingText, newTokenChar, trans.target);
            }
        } else if (trans instanceof SetTransition) {
            final List<Integer> symbols = ((SetTransition) trans).label().toList();
            for (Integer symbol : symbols) {
                final char[] charArr = Character.toChars(symbol);
                final String charStr = new String(charArr);
                final boolean shouldIgnoreCase = shouldIgnoreThisCase(charArr[0], symbols); // TODO: check for non-BMP
                if (!shouldIgnoreCase && (remainingText.isEmpty() || remainingText.startsWith(charStr))) {
                    suggestViaNonEpsilonLexerTransition(tokenSoFar, remainingText, charStr, trans.target);
                }
            }
        }
    }

    private void suggestViaNonEpsilonLexerTransition(final String tokenSoFar,
                                                     final String remainingText,
                                                     final String newTokenChar,
                                                     final ATNState targetState) {
        final String newRemainingText = (remainingText.length() > 0) ? remainingText.substring(1) : remainingText;
        suggest(tokenSoFar + newTokenChar, targetState, newRemainingText);
    }

    private void addSuggestedToken(final String tokenToAdd) {
        final String justTheCompletionPart = chopOffCommonStart(tokenToAdd, this.origPartialToken);
        suggestions.add(justTheCompletionPart);
    }

    private String chopOffCommonStart(final String a, final String b) {
        final int charsToChopOff = Math.min(b.length(), a.length());
        return a.substring(charsToChopOff);
    }

    private String getAddedTextFor(final AtomTransition transition) {
        return new String(Character.toChars(transition.label));
    }

    private boolean shouldIgnoreThisCase(final char transChar, final List<Integer> allTransChars) {
        switch (this.casePreference) {
            case BOTH:
                return false;
            case LOWER:
                return Character.isUpperCase(transChar) && allTransChars.contains((int) Character.toLowerCase(transChar));
            case UPPER:
                return Character.isLowerCase(transChar) && allTransChars.contains((int) Character.toUpperCase(transChar));
            default:
                return false;
        }
    }

}
