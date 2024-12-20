package com.damdamdeo.formula.infrastructure.web.api;

import com.damdamdeo.formula.domain.SuggestionException;
import com.damdamdeo.formula.infrastructure.parser.antlr.AntlrAutoSuggestUnavailableException;
import com.damdamdeo.formula.infrastructure.parser.antlr.AntlrAutoSuggestionExecutionException;
import com.damdamdeo.formula.infrastructure.parser.antlr.AntlrAutoSuggestionExecutionTimedOutException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.ExampleObject;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;

@Provider
public final class SuggestionExceptionMapper implements ExceptionMapper<SuggestionException> {
    @Override
    @APIResponses(
            value = {
                    @APIResponse(responseCode = "500", description = "AutoSuggestion service execution exception while processing formula",
                            content = {
                                    @Content(
                                            mediaType = "application/vnd.autosuggestion-unavailable-v1+json",
                                            schema = @Schema(
                                                    implementation = ErrorMessageDTO.class),
                                            examples = {
                                                    @ExampleObject(
                                                            name = "Syntax in error",
                                                            description = "Syntax in error",
                                                            //language=JSON
                                                            value = """
                                                                    {
                                                                        "message": "AutoSuggestion service execution unavailable while processing formula 'IF(EQ([@[Sales Person]],\\"Joe\\"),MUL(MUL([@[Sales Amount]],DIV([@[% Commission]],100)),2),MUL([@[Sales Amount]],DIV([@[% Commission]],100)' - msg 'error'"
                                                                    }
                                                                    """)
                                            }
                                    ),
                                    @Content(
                                            mediaType = "application/vnd.autosuggestion-execution-exception-v1+json",
                                            schema = @Schema(
                                                    implementation = ErrorMessageDTO.class),
                                            examples = {
                                                    @ExampleObject(
                                                            name = "Syntax in error",
                                                            description = "Syntax in error",
                                                            //language=JSON
                                                            value = """
                                                                    {
                                                                        "message": "AutoSuggestion service execution exception while processing formula 'IF(EQ([@[Sales Person]],\\"Joe\\"),MUL(MUL([@[Sales Amount]],DIV([@[% Commission]],100)),2),MUL([@[Sales Amount]],DIV([@[% Commission]],100)' - msg 'error'"
                                                                    }
                                                                    """)
                                            }
                                    ),
                                    @Content(
                                            mediaType = "application/vnd.autosuggestion-unexpected-exception-v1+json",
                                            schema = @Schema(
                                                    implementation = ErrorMessageDTO.class),
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
                            }
                    ),
                    @APIResponse(responseCode = "503", description = "AutoSuggestion service has timed out while executing formula",
                            content = @Content(
                                    mediaType = "application/vnd.autosuggestion-execution-timed-out-v1+json",
                                    schema = @Schema(
                                            implementation = ErrorMessageDTO.class),
                                    examples = {
                                            @ExampleObject(
                                                    name = "Syntax in error",
                                                    description = "Syntax in error",
                                                    //language=JSON
                                                    value = """
                                            {
                                                "message": "AutoSuggestion service has timed out while executing formula 'IF(EQ([@[Sales Person]],\\"Joe\\"),MUL(MUL([@[Sales Amount]],DIV([@[% Commission]],100)),2),MUL([@[Sales Amount]],DIV([@[% Commission]],100)' - Infinite loop in Grammar - msg 'error'"
                                            }
                                            """)
                                    }
                            )
                    )
            }
    )
    public Response toResponse(final SuggestionException exception) {
        final String body = new ErrorMessageDTO(exception).toJson();
        if (exception.getCause() instanceof AntlrAutoSuggestUnavailableException) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .type("application/vnd.autosuggestion-unavailable-v1+json")
                    .entity(body)
                    .build();
        } else if (exception.getCause() instanceof AntlrAutoSuggestionExecutionException) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .type("application/vnd.autosuggestion-execution-exception-v1+json")
                    .entity(body)
                    .build();
        } else if (exception.getCause() instanceof AntlrAutoSuggestionExecutionTimedOutException) {
            return Response.status(Response.Status.SERVICE_UNAVAILABLE)
                    .type("application/vnd.autosuggestion-execution-timed-out-v1+json")
                    .entity(body)
                    .build();
        } else {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .type("application/vnd.autosuggestion-unexpected-exception-v1+json")
                    .entity(body)
                    .build();
        }
    }
}
