package com.damdamdeo.formula.infrastructure.interfaces;

import com.damdamdeo.formula.infrastructure.antlr.autosuggest.AutoSuggestUnavailableException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;

@Provider
public final class AutoSuggestUnavailableExceptionMapper implements ExceptionMapper<AutoSuggestUnavailableException> {
    @Override
    putain si j'ai le meme response code, il y en a qu'un d'afficher !!! uiliser un autre code ?
    @APIResponse(responseCode = "500", description = "AutoSuggestion service execution unavailable while processing formula",
            content = @Content(
                    mediaType = "application/vnd.autosuggestion-unavailable-v1+text",
                    schema = @Schema(implementation = String.class)
            )
    )
    public Response toResponse(final AutoSuggestUnavailableException exception) {
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .type("application/vnd.autosuggestion-unavailable-v1+text")
                .entity(String.format("AutoSuggestion service execution unavailable while processing formula '%s' - msg '%s'",
                        exception.suggestedFormula().formula(),
                        exception.getCause().getMessage()))
                .build();
    }
}
