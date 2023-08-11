package com.damdamdeo.formula.infrastructure.antlr.autosuggest;

import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.atn.ATNState;
import org.antlr.v4.runtime.atn.AtomTransition;
import org.antlr.v4.runtime.atn.SetTransition;
import org.antlr.v4.runtime.atn.Transition;
import org.antlr.v4.runtime.misc.Interval;

import java.util.*;
import java.util.stream.Collectors;

public class AutoSuggester {
    private final ParserWrapper parserWrapper;
    private final LexerWrapper lexerWrapper;
    private final String input;
    private final CasePreference casePreference;
    private final Map<ATNState, Integer> parserStateToTokenListIndexWhereLastVisited = new HashMap<>();

    public AutoSuggester(final LexerAndParserFactory lexerAndParserFactory,
                         final String input,
                         final CasePreference casePreference) {
        this.lexerWrapper = new LexerWrapper(lexerAndParserFactory);
        this.parserWrapper = new ParserWrapper(lexerAndParserFactory, lexerWrapper.getVocabulary());
        this.input = input;
        this.casePreference = casePreference;
    }

    public Collection<String> suggestCompletions() {
        this.parserStateToTokenListIndexWhereLastVisited.clear();
        final TokenizationResult tokenizationResult = lexerWrapper.tokenizeNonDefaultChannel(this.input);
        final ATNState initialState = this.parserWrapper.getAtnState(0);
        return parseAndCollectTokenSuggestions(initialState, tokenizationResult, 0);
    }

    /**
     * Recursive through the parser ATN to process all tokens. When successful (out of tokens) - collect completion
     * suggestions.
     */
    private Set<String> parseAndCollectTokenSuggestions(final ATNState parserState,
                                                        final TokenizationResult tokenizationResult,
                                                        final int tokenListIndex) {
        final Set<String> collectedSuggestions = new HashSet<>();
        if (didVisitParserStateOnThisTokenIndex(parserState, tokenListIndex)) {
            return collectedSuggestions;
        }
        final Integer previousTokenListIndexForThisState = setParserStateLastVisitedOnThisTokenIndex(parserState, tokenListIndex);
        try {
            if (!haveMoreTokens(tokenizationResult, tokenListIndex)) { // stop condition for recursion
                final Set<String> suggestions = suggestNextTokensForParserState(parserState, tokenizationResult);
                collectedSuggestions.addAll(suggestions);
                return collectedSuggestions;
            }
            for (Transition trans : parserState.getTransitions()) {
                if (trans.isEpsilon()) {
                    final Set<String> suggestions = handleEpsilonTransition(trans, tokenizationResult, tokenListIndex);
                    collectedSuggestions.addAll(suggestions);
                } else if (trans instanceof AtomTransition) {
                    final Set<String> suggestions = handleAtomicTransition((AtomTransition) trans, tokenizationResult, tokenListIndex);
                    collectedSuggestions.addAll(suggestions);
                } else {
                    final Set<String> suggestions = handleSetTransition((SetTransition) trans, tokenizationResult, tokenListIndex);
                    collectedSuggestions.addAll(suggestions);
                }
            }
        } finally {
            setParserStateLastVisitedOnThisTokenIndex(parserState, previousTokenListIndexForThisState);
        }
        return collectedSuggestions;
    }

    private boolean didVisitParserStateOnThisTokenIndex(final ATNState parserState, final Integer currentTokenListIndex) {
        final Integer lastVisitedThisStateAtTokenListIndex = parserStateToTokenListIndexWhereLastVisited.get(parserState);
        return currentTokenListIndex.equals(lastVisitedThisStateAtTokenListIndex);
    }

    private Integer setParserStateLastVisitedOnThisTokenIndex(final ATNState parserState, final Integer tokenListIndex) {
        if (tokenListIndex == null) {
            return parserStateToTokenListIndexWhereLastVisited.remove(parserState);
        } else {
            return parserStateToTokenListIndexWhereLastVisited.put(parserState, tokenListIndex);
        }
    }

    private boolean haveMoreTokens(final TokenizationResult tokenizationResult, final int tokenListIndex) {
        return tokenListIndex < tokenizationResult.tokens().size();
    }

    private Set<String> handleEpsilonTransition(final Transition trans,
                                                final TokenizationResult tokenizationResult,
                                                final int tokenListIndex) {
        // Epsilon transitions don't consume a token, so don't move the index
        return parseAndCollectTokenSuggestions(trans.target, tokenizationResult, tokenListIndex);
    }

    private Set<String> handleAtomicTransition(final AtomTransition trans,
                                               final TokenizationResult tokenizationResult,
                                               final int tokenListIndex) {
        final int nextTokenType = tokenizationResult.tokens().get(tokenListIndex).getType();
        final boolean nextTokenMatchesTransition = (trans.label == nextTokenType);
        if (nextTokenMatchesTransition) {
            return parseAndCollectTokenSuggestions(trans.target, tokenizationResult, tokenListIndex + 1);
        }
        return Set.of();
    }

    private Set<String> handleSetTransition(final SetTransition trans,
                                            final TokenizationResult tokenizationResult,
                                            final int tokenListIndex) {
        final Token nextToken = tokenizationResult.tokens().get(tokenListIndex);
        final int nextTokenType = nextToken.getType();
        for (final int transitionTokenType : trans.label().toList()) {
            final boolean nextTokenMatchesTransition = (transitionTokenType == nextTokenType);
            if (nextTokenMatchesTransition) {
                return parseAndCollectTokenSuggestions(trans.target, tokenizationResult, tokenListIndex + 1);
            }
        }
        return Set.of();
    }

    private Set<String> suggestNextTokensForParserState(final ATNState parserState,
                                                        final TokenizationResult tokenizationResult) {
        final Set<Integer> transitionLabels = new HashSet<>();
        fillParserTransitionLabels(parserState, transitionLabels, new HashSet<>());
        final TokenSuggester tokenSuggester = new TokenSuggester(tokenizationResult.untokenizedText(), lexerWrapper, this.casePreference);
        final Collection<String> suggestions = tokenSuggester.suggest(transitionLabels);
        return parseSuggestionsAndAddValidOnes(parserState, tokenizationResult, suggestions);
    }

    private void fillParserTransitionLabels(final ATNState parserState,
                                            final Collection<Integer> result,
                                            final Set<TransitionWrapper> visitedTransitions) {
        for (final Transition trans : parserState.getTransitions()) {
            final TransitionWrapper transWrapper = new TransitionWrapper(parserState, trans);
            if (visitedTransitions.contains(transWrapper)) {
                continue;
            }
            if (trans.isEpsilon()) {
                try {
                    visitedTransitions.add(transWrapper);
                    fillParserTransitionLabels(trans.target, result, visitedTransitions);
                } finally {
                    visitedTransitions.remove(transWrapper);
                }
            } else if (trans instanceof AtomTransition) {
                int label = ((AtomTransition) trans).label;
                if (label >= 1) { // EOF would be -1
                    result.add(label);
                }
            } else if (trans instanceof SetTransition) {
                for (Interval interval : ((SetTransition) trans).label().getIntervals()) {
                    for (int i = interval.a; i <= interval.b; ++i) {
                        result.add(i);
                    }
                }
            }
        }
    }

    private Set<String> parseSuggestionsAndAddValidOnes(final ATNState parserState,
                                                        final TokenizationResult tokenizationResult,
                                                        final Collection<String> suggestions) {
        return suggestions.stream()
                .filter(suggestion ->
                        Optional.ofNullable(getAddedToken(tokenizationResult, suggestion))
                                .map(addedToken -> isParseableWithAddedToken(parserState, addedToken, new HashSet<>()))
                                .orElse(false)
                ).collect(Collectors.toSet());
    }

    private Token getAddedToken(final TokenizationResult tokenizationResult, final String suggestedCompletion) {
        final String completedText = this.input + suggestedCompletion;
        final List<? extends Token> completedTextTokens = this.lexerWrapper.tokenizeNonDefaultChannel(completedText).tokens();
        if (completedTextTokens.size() <= tokenizationResult.tokens().size()) {
            return null; // Completion didn't yield whole token, could be just a token fragment
        }
        final Token newToken = completedTextTokens.get(completedTextTokens.size() - 1);
        return newToken;
    }

    private boolean isParseableWithAddedToken(final ATNState parserState,
                                              final Token newToken,
                                              final Set<TransitionWrapper> visitedTransitions) {
        for (final Transition parserTransition : parserState.getTransitions()) {
            if (parserTransition.isEpsilon()) { // Recurse through any epsilon transitionsStr
                final TransitionWrapper transWrapper = new TransitionWrapper(parserState, parserTransition);
                if (visitedTransitions.contains(transWrapper)) {
                    continue;
                }
                visitedTransitions.add(transWrapper);
                try {
                    if (isParseableWithAddedToken(parserTransition.target, newToken, visitedTransitions)) {
                        return true;
                    }
                } finally {
                    visitedTransitions.remove(transWrapper);
                }
            } else if (parserTransition instanceof AtomTransition) {
                final AtomTransition parserAtomTransition = (AtomTransition) parserTransition;
                final int transitionTokenType = parserAtomTransition.label;
                if (transitionTokenType == newToken.getType()) {
                    return true;
                }
            } else if (parserTransition instanceof SetTransition) {
                final SetTransition parserSetTransition = (SetTransition) parserTransition;
                for (final int transitionTokenType : parserSetTransition.label().toList()) {
                    if (transitionTokenType == newToken.getType()) {
                        return true;
                    }
                }
            } else {
                throw new IllegalStateException("Unexpected: " + parserWrapper.toString(parserTransition));
            }
        }
        return false;
    }

}
