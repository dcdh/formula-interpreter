package com.damdamdeo.formula.infrastructure.interfaces;

import com.damdamdeo.formula.infrastructure.antlr.autosuggest.AutoSuggestionExecutionException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;

@Provider
public final class AutoSuggestionExecutionExceptionMapper implements ExceptionMapper<AutoSuggestionExecutionException> {
    @Override
    @APIResponse(responseCode = "500", description = "AutoSuggestion service execution exception while processing formula",
            content = @Content(
                    mediaType = "application/vnd.autosuggestion-execution-exception-v1+text",
                    schema = @Schema(implementation = String.class)
            )
    )
    public Response toResponse(final AutoSuggestionExecutionException exception) {
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .type("application/vnd.autosuggestion-execution-exception-v1+text")
                .entity(String.format("AutoSuggestion service execution exception while processing formula '%s' - msg '%s'",
                        exception.suggestedFormula().formula(),
                        exception.getCause().getMessage()))
                .build();
    }
}
