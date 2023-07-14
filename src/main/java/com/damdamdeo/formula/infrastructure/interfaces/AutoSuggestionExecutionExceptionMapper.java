package com.damdamdeo.formula.infrastructure.interfaces;

import com.damdamdeo.formula.infrastructure.antlr.autosuggest.AutoSuggestionExecutionException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public final class AutoSuggestionExecutionExceptionMapper implements ExceptionMapper<AutoSuggestionExecutionException> {
    @Override
    public Response toResponse(final AutoSuggestionExecutionException exception) {
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .type("application/vnd.autosuggestion-execution-exception-v1+text")
                .entity(String.format("AutoSuggestion service execution exception while processing formula '%s' - msg '%s'",
                        exception.suggestedFormula().formula(),
                        exception.getCause().getMessage()))
                .build();
    }
}
