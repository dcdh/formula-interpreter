package com.damdamdeo.formula.infrastructure.api;

import com.damdamdeo.formula.domain.ValidationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.ExampleObject;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;

@Provider
public final class ValidationExceptionMapper implements ExceptionMapper<ValidationException> {
    @Override
    @APIResponses(
            value = {
                    @APIResponse(responseCode = "500", description = "Validation service execution exception while processing formula",
                            content = @Content(
                                    mediaType = "application/vnd.validation-unexpected-exception-v1+json",
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
                                                                "message": "unexpected \\"exception\\""
                                                            }
                                                            """)
                                    }
                            )
                    )
            }
    )
    public Response toResponse(final ValidationException exception) {
        final String body = new ErrorMessageDTO(exception).toJson();
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .type("application/vnd.validation-unexpected-exception-v1+json")
                .entity(body)
                .build();
    }
}
