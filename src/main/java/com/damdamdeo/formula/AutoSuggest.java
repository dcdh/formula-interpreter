package com.damdamdeo.formula;

import com.damdamdeo.formula.autosuggest.*;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

public class AutoSuggest {


    public AutoSuggest() {
    }

    public List<String> execute(final Formula formula) {
        final ExecutorService executor  = Executors.newSingleThreadExecutor();
        final Future<List<String>> future = executor.submit(() -> {
            final LexerAndParserFactory lexerAndParserFactory = new ReflectionLexerAndParserFactory(FormulaLexer.class, FormulaParser.class);
            final AutoSuggester suggester = new AutoSuggester(lexerAndParserFactory, formula.formula());
            suggester.setCasePreference(CasePreference.BOTH);
            return suggester.suggestCompletions()
                    .stream()
                    .sorted(Comparator.reverseOrder())
                    .collect(Collectors.toList());
        });
        try {
            return future.get(5, TimeUnit.SECONDS);
        } catch (final InterruptedException e) {
            throw new AutoSuggestUnavailableException(e);
        } catch (final ExecutionException e) {
            throw new AutoSuggestionExecutionException(e.getCause());
        } catch (final TimeoutException e) {
            future.cancel(true);
            throw new AutoSuggestionProcessingTimedOutException(e);
        } finally {
            executor.shutdown();
        }
    }

}
