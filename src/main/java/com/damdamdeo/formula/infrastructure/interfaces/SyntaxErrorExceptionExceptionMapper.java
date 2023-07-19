package com.damdamdeo.formula.infrastructure.interfaces;

import com.damdamdeo.formula.infrastructure.antlr.SyntaxErrorException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;

@Provider
public final class SyntaxErrorExceptionExceptionMapper implements ExceptionMapper<SyntaxErrorException> {
    @Override
    @APIResponse(responseCode = "400", description = "Syntax Error",
            content = @Content(
                    mediaType = "application/vnd.formula-syntax-error-v1+text",
                    schema = @Schema(implementation = String.class)
            )
    )
    public Response toResponse(final SyntaxErrorException exception) {
        return Response.status(Response.Status.BAD_REQUEST)
                .type("application/vnd.formula-syntax-error-v1+text")
                .entity(String.format("Syntax error at line '%d' at position '%d' with message '%s'",
                        exception.syntaxError().line(),
                        exception.syntaxError().charPositionInLine(),
                        exception.syntaxError().msg()))
                .build();
    }
}
