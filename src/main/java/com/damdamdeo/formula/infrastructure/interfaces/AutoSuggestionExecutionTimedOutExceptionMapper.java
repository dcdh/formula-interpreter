package com.damdamdeo.formula.infrastructure.interfaces;

import com.damdamdeo.formula.infrastructure.antlr.autosuggest.AutoSuggestionExecutionTimedOutException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;

@Provider
public final class AutoSuggestionExecutionTimedOutExceptionMapper implements ExceptionMapper<AutoSuggestionExecutionTimedOutException> {
    @Override
    @APIResponse(responseCode = "503", description = "AutoSuggestion service has timed out while executing formula",
            content = @Content(
                    mediaType = "application/vnd.autosuggestion-execution-timed-out-v1+text",
                    schema = @Schema(implementation = String.class)
            )
    )
    public Response toResponse(final AutoSuggestionExecutionTimedOutException exception) {
        return Response.status(Response.Status.SERVICE_UNAVAILABLE)
                .type("application/vnd.autosuggestion-execution-timed-out-v1+text")
                .entity(String.format("AutoSuggestion service has timed out while executing formula '%s' - Infinite loop in Grammar - msg '%s'",
                        exception.suggestedFormula().formula(),
                        exception.getCause().getMessage()))
                .build();
    }
}
