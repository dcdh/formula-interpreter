package com.damdamdeo.formula.infrastructure.api;

import com.damdamdeo.formula.domain.ExecutionException;
import com.damdamdeo.formula.infrastructure.antlr.AntlrSyntaxErrorException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.ExampleObject;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;

@Provider
public final class ExecutionExceptionMapper implements ExceptionMapper<ExecutionException> {
    @Override
    @APIResponse(responseCode = "400", description = "Syntax exception while executing formula",
            content = @Content(
                    mediaType = "application/vnd.execution-syntax-error-v1+json",
                    schema = @Schema(
                            implementation = ErrorMessageDTO.class,
                            required = true,
                            requiredProperties = {"message"}),
                    examples = {
                            @ExampleObject(
                                    name = "Syntax in error",
                                    description = "Syntax in error",
                                    //language=JSON
                                    value = """
                                            {
                                                "message": "Syntax error at line '0' at position '1' with message 'custom \\"msg\\"'"
                                            }
                                            """)
                    }
            )
    )
    @APIResponse(responseCode = "500", description = "Unhandled exception while executing formula",
            content = @Content(
                    mediaType = "application/vnd.execution-unexpected-exception-v1+json",
                    schema = @Schema(
                            implementation = ErrorMessageDTO.class,
                            required = true,
                            requiredProperties = {"message"}),
                    examples = {
                            @ExampleObject(
                                    name = "Unexpected exception",
                                    description = "Unexpected exception",
                                    //language=JSON
                                    value = """
                                            {
                                                "message": "unexpected \\"exception\\""
                                            }
                                            """)
                    }
            )
    )
    public Response toResponse(final ExecutionException exception) {
        final String body = new ErrorMessageDTO(exception).toJson();
        if (exception.getCause() instanceof AntlrSyntaxErrorException) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .type("application/vnd.execution-syntax-error-v1+json")
                    .entity(body)
                    .build();
        } else {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .type("application/vnd.execution-unexpected-exception-v1+json")
                    .entity(body)
                    .build();
        }
    }
}
