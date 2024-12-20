package com.damdamdeo.formula.infrastructure.web.api;

import com.damdamdeo.formula.domain.Formula;
import com.damdamdeo.formula.domain.ValidationException;
import com.damdamdeo.formula.domain.usecase.ValidateCommand;
import com.damdamdeo.formula.domain.usecase.ValidateUseCase;
import com.damdamdeo.formula.infrastructure.parser.antlr.AntlrSyntaxError;
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

@Path("/validate")
public final class ValidatorEndpoint {
    private final ValidateUseCase<AntlrSyntaxError> validateUseCase;

    public ValidatorEndpoint(final ValidateUseCase<AntlrSyntaxError> validateUseCase) {
        this.validateUseCase = Objects.requireNonNull(validateUseCase);
    }

    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces("application/vnd.formula-validator-v1+json")
    @RequestBody(
            required = true,
            content = @Content(
                    mediaType = MediaType.MULTIPART_FORM_DATA,
                    schema = @Schema(
                            type = SchemaType.OBJECT,
                            required = true,
                            requiredProperties = {"formula"},
                            properties = {
                                    @SchemaProperty(
                                            name = "formula",
                                            type = SchemaType.STRING,
                                            description = "Formula to validate",
                                            example = "MUL([@[Sales Amount]],DIV([@[% Commission]],100))"
                                    )
                            }
                    )
            )
    )
    @APIResponses(
            value = {
                    @APIResponse(
                            name = "success",
                            responseCode = "200",
                            content = @Content(
                                    schema = @Schema(
                                            type = SchemaType.OBJECT,
                                            implementation = ValidationDTO.class
                                    ),
                                    examples = {
                                            @ExampleObject(
                                                    name = "Valid formula validated",
                                                    description = "MUL([@[Sales Amount]],DIV([@[% Commission]],100))",
                                                    //language=JSON
                                                    value = """
                                                            {
                                                              "valid": true,
                                                              "line": null,
                                                              "charPositionInLine": null,
                                                              "msg": null
                                                            }
                                                            """
                                            ),
                                            @ExampleObject(
                                                    name = "Invalid formula validated",
                                                    description = "MUL([@[Sales Amount]],DIV([@[% Commission]],100)",
                                                    //language=JSON
                                                    value = """
                                                            {
                                                              "valid": false,
                                                              "line": 1,
                                                              "charPositionInLine": 48,
                                                              "msg": "missing ')' at '<EOF>'"
                                                            }
                                                            """
                                            )
                                    }
                            )
                    )
            }
    )
    public Uni<ValidationDTO> validate(@FormParam("formula") final String formula) throws ValidationException {
        return validateUseCase.execute(new ValidateCommand(new Formula(formula)))
                .map(antlrSyntaxError -> antlrSyntaxError.map(ValidationDTO::new)
                        .orElseGet(ValidationDTO::new));
    }

}