package com.damdamdeo.formula.infrastructure.interfaces;

import com.damdamdeo.formula.infrastructure.antlr.AntlrAutoSuggestUnavailableException;
import com.damdamdeo.formula.infrastructure.antlr.AntlrAutoSuggestionExecutionException;
import com.damdamdeo.formula.infrastructure.antlr.AntlrAutoSuggestionExecutionTimedOutException;
import com.damdamdeo.formula.domain.SuggestionException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;

@Provider
public final class SuggestionExceptionMapper implements ExceptionMapper<SuggestionException> {
    @Override
    @APIResponse(responseCode = "500", description = "AutoSuggestion service execution exception while processing formula",
            content = {
                    @Content(
                            mediaType = "application/vnd.autosuggestion-unavailable-v1+text",
                            schema = @Schema(implementation = String.class)
                    ),
                    @Content(
                            mediaType = "application/vnd.autosuggestion-execution-exception-v1+text",
                            schema = @Schema(implementation = String.class)
                    ),
                    @Content(
                            mediaType = "application/vnd.autosuggestion-unexpected-exception-v1+text",
                            schema = @Schema(implementation = String.class)
                    )
            }
    )
    @APIResponse(responseCode = "503", description = "AutoSuggestion service has timed out while executing formula",
            content = @Content(
                    mediaType = "application/vnd.autosuggestion-execution-timed-out-v1+text",
                    schema = @Schema(implementation = String.class)
            )
    )
    public Response toResponse(final SuggestionException exception) {
        if (exception.getCause() instanceof AntlrAutoSuggestUnavailableException) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .type("application/vnd.autosuggestion-unavailable-v1+text")
                    .entity(exception.getMessage())
                    .build();
        } else if (exception.getCause() instanceof AntlrAutoSuggestionExecutionException) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .type("application/vnd.autosuggestion-execution-exception-v1+text")
                    .entity(exception.getMessage())
                    .build();
        } else if (exception.getCause() instanceof AntlrAutoSuggestionExecutionTimedOutException) {
            return Response.status(Response.Status.SERVICE_UNAVAILABLE)
                    .type("application/vnd.autosuggestion-execution-timed-out-v1+text")
                    .entity(exception.getMessage())
                    .build();
        } else {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .type("application/vnd.autosuggestion-unexpected-exception-v1+text")
                    .entity(exception.getMessage())
                    .build();
        }
    }
}
