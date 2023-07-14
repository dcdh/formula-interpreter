package com.damdamdeo.formula.infrastructure.interfaces;

import com.damdamdeo.formula.infrastructure.antlr.autosuggest.AutoSuggestUnavailableException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public final class AutoSuggestUnavailableExceptionMapper implements ExceptionMapper<AutoSuggestUnavailableException> {
    @Override
    public Response toResponse(final AutoSuggestUnavailableException exception) {
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .type("application/vnd.autosuggestion-unavailable-v1+text")
                .entity(String.format("AutoSuggestion service execution unavailable while processing formula '%s' - msg '%s'",
                        exception.suggestedFormula().formula(),
                        exception.getCause().getMessage()))
                .build();
    }
}
