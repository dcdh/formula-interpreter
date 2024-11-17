package com.damdamdeo.formula.infrastructure.web.api;

import com.damdamdeo.formula.domain.SuggestedFormula;
import com.damdamdeo.formula.domain.SuggestionException;
import com.damdamdeo.formula.domain.SuggestionsCompletion;
import com.damdamdeo.formula.domain.usecase.SuggestCommand;
import com.damdamdeo.formula.domain.usecase.SuggestUseCase;
import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.ExampleObject;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.media.SchemaProperty;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;

import java.util.Objects;

@Path("/suggestCompletion")
public final class SuggestCompletionEndpoint {
    private final SuggestUseCase suggestUseCase;

    public SuggestCompletionEndpoint(final SuggestUseCase suggestUseCase) {
        this.suggestUseCase = Objects.requireNonNull(suggestUseCase);
    }

    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces("application/vnd.suggest-completion-v1+json")
    @RequestBody(
            required = true,
            content = @Content(
                    mediaType = MediaType.MULTIPART_FORM_DATA,
                    schema = @Schema(
                            type = SchemaType.OBJECT,
                            required = true,
                            requiredProperties = {"suggestedFormula"},
                            properties = {
                                    @SchemaProperty(
                                            name = "suggestedFormula",
                                            type = SchemaType.STRING,
                                            description = "Suggest tokens",
                                            example = "IF"
                                    )
                            }
                    )
            )
    )
    @APIResponses(
            value = @APIResponse(
                    name = "success",
                    responseCode = "200",
                    content = @Content(
                            schema = @Schema(
                                    type = SchemaType.ARRAY,
                                    implementation = String.class,
                                    required = true
                            ),
                            examples = @ExampleObject(
                                    name = "List of suggested tokens",
                                    description = "List of suggested tokens",
                                    //language=JSON
                                    value = """
                                            {
                                              "tokens":["("]
                                            }
                                            """
                            )
                    )
            )
    )
    public Uni<SuggestionsDTO> suggestCompletion(@FormParam("suggestedFormula") final String suggestedFormula) throws SuggestionException {
        return suggestUseCase.execute(new SuggestCommand(new SuggestedFormula(suggestedFormula)))
                .map(SuggestionsCompletion::tokens)
                .map(SuggestionsDTO::new);
    }
}
