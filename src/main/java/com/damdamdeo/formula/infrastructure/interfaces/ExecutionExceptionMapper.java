package com.damdamdeo.formula.infrastructure.interfaces;

import com.damdamdeo.formula.domain.ExecutionException;
import com.damdamdeo.formula.domain.SyntaxErrorException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;

@Provider
public final class ExecutionExceptionMapper implements ExceptionMapper<ExecutionException> {
    @Override
    @APIResponse(responseCode = "400", description = "Syntax exception while executing formula",
            content = @Content(
                    mediaType = "application/vnd.execution-syntax-error-v1+json",
                    schema = @Schema(implementation = String.class)
            )
    )
    @APIResponse(responseCode = "500", description = "Unhandled exception while executing formula",
            content = @Content(
                    mediaType = "application/vnd.execution-unexpected-exception-v1+text",
                    schema = @Schema(implementation = String.class)
            )
    )
    public Response toResponse(final ExecutionException exception) {
        if (exception.getCause() instanceof SyntaxErrorException) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .type("application/vnd.execution-syntax-error-v1+json")
                    .entity(exception.getMessage())
                    .build();
        } else {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .type("application/vnd.execution-unexpected-exception-v1+text")
                    .entity(exception.getMessage())
                    .build();
        }
    }
}
