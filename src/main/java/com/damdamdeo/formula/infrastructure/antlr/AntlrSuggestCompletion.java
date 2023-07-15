package com.damdamdeo.formula.infrastructure.antlr;

import com.damdamdeo.formula.FormulaLexer;
import com.damdamdeo.formula.FormulaParser;
import com.damdamdeo.formula.domain.SuggestCompletion;
import com.damdamdeo.formula.domain.SuggestedFormula;
import com.damdamdeo.formula.domain.SuggestionsCompletion;
import com.damdamdeo.formula.infrastructure.antlr.autosuggest.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

public class AntlrSuggestCompletion implements SuggestCompletion {
    private static final Logger LOGGER = LogManager.getLogger(AntlrSuggestCompletion.class);

    @Override
    public SuggestionsCompletion suggest(final SuggestedFormula suggestedFormula) {
        final ExecutorService executor = Executors.newSingleThreadExecutor();
        try {
            final Future<SuggestionsCompletion> future = executor.submit(() -> {
                final LexerAndParserFactory lexerAndParserFactory = new ReflectionLexerAndParserFactory(FormulaLexer.class, FormulaParser.class);
                final AutoSuggester suggester = new AutoSuggester(lexerAndParserFactory, suggestedFormula.formula());
                suggester.setCasePreference(CasePreference.BOTH);
                final List<String> suggestCompletions = suggester.suggestCompletions()
                        .stream()
                        .sorted(Comparator.reverseOrder())
                        .collect(Collectors.toList());
                return new SuggestionsCompletion(suggestCompletions);
            });
            try {
                return future.get(5, TimeUnit.SECONDS);
            } catch (final InterruptedException interruptedException) {
                LOGGER.error(interruptedException);
                throw new AutoSuggestUnavailableException(suggestedFormula, interruptedException);
            } catch (final ExecutionException executionException) {
                LOGGER.error(executionException);
                throw new AutoSuggestionExecutionException(suggestedFormula, executionException.getCause());
            } catch (final TimeoutException timeoutException) {
                LOGGER.error(timeoutException);
                future.cancel(true);
                throw new AutoSuggestionExecutionTimedOutException(suggestedFormula, timeoutException);
            } finally {
                executor.shutdown();
            }
        } catch (final RejectedExecutionException rejectedExecutionException) {
            throw new AutoSuggestUnavailableException(suggestedFormula, rejectedExecutionException);
        } finally {
            executor.shutdown();
        }
    }

}
