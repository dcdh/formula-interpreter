package com.damdamdeo.formula.infrastructure.interfaces;

import com.damdamdeo.formula.infrastructure.antlr.SyntaxErrorException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public final class SyntaxErrorExceptionExceptionMapper implements ExceptionMapper<SyntaxErrorException> {
    @Override
    public Response toResponse(final SyntaxErrorException exception) {
        return Response.status(Response.Status.BAD_REQUEST)
                .type("application/vnd.formula-syntax-error-v1+json")
                .entity(String.format("Syntax error at line '%d' at position '%d' with message '%s'",
                        exception.syntaxError().line(),
                        exception.syntaxError().charPositionInLine(),
                        exception.syntaxError().msg()))
                .build();
    }
}
