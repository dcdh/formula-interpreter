package com.damdamdeo.formula.infrastructure.web.api;

import com.damdamdeo.formula.domain.EvaluationException;
import com.damdamdeo.formula.domain.usecase.EvaluateUseCase;
import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.ExampleObject;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;

import java.util.Objects;

@Path("/evaluate")
public final class EvaluateEndpoint {
    private final EvaluateUseCase evaluateUseCase;

    public EvaluateEndpoint(final EvaluateUseCase evaluateUseCase) {
        this.evaluateUseCase = Objects.requireNonNull(evaluateUseCase);
    }

    @POST
    @Consumes("application/vnd.formula-evaluate-v1+json")
    @Produces("application/vnd.formula-evaluated-v1+json")
    @RequestBody(
            required = true,
            content = @Content(
                    schema = @Schema(
                            implementation = EvaluateDTO.class
                    ),
                    examples = {
                            @ExampleObject(
                                    name = "Incomplete formula",
                                    description = "Should fail to evaluate",
                                    //language=JSON
                                    value = """
                                            {
                                                "formula": "IF",
                                                "structuredReferences": {
                                                    "Sales Amount": "260",
                                                    "% Commission": "10"
                                                },
                                                "evaluateOn": "ANTLR",
                                                "debugFeature": "ACTIVE"
                                            }
                                            """),
                            @ExampleObject(
                                    name = "Compute commission amount by multiplying Sales Amount by Percent Commission",
                                    description = "Compute commission amount by multiplying Sales Amount by Percent Commission",
                                    //language=JSON
                                    value = """
                                            {
                                                "formula": "MUL([@[Sales Amount]],DIV([@[% Commission]],100))",
                                                "structuredReferences": {
                                                    "Sales Amount": "260",
                                                    "% Commission": "10"
                                                },
                                                "evaluateOn": "ANTLR",
                                                "debugFeature": "ACTIVE"
                                            }
                                            """),
                            @ExampleObject(
                                    name = "Compute commission amount by multiplying Sales Amount by Percent Commission if it is Joe multiply by two",
                                    description = "Compute commission amount by multiplying Sales Amount by Percent Commission if it is Joe multiply by two",
                                    //language=JSON
                                    value = """
                                            {
                                                "formula": "IF(EQ([@[Sales Person]],\\"Joe\\"),MUL(MUL([@[Sales Amount]],DIV([@[% Commission]],100)),2),MUL([@[Sales Amount]],DIV([@[% Commission]],100)))",
                                                "structuredReferences": {
                                                    "Sales Amount": "260",
                                                    "% Commission": "10"
                                                },
                                                "evaluateOn": "ANTLR",
                                                "debugFeature": "ACTIVE"
                                            }
                                            """),
                            @ExampleObject(
                                    name = "Fail on unknown Structured Reference",
                                    description = "Should return #REF! on unknown Structured Reference",
                                    //language=JSON
                                    value = """
                                            {
                                                "formula": "IF(EQ([@[Sales Person]],\\"Joe\\"),MUL(MUL([@[Sales Amount BOOM]],DIV([@[% Commission]],100)),2),MUL([@[Sales Amount]],DIV([@[% Commission]],100)))",
                                                "structuredReferences": {
                                                    "Sales Person": "Joe",
                                                    "Sales Amount": "260",
                                                    "% Commission": "10"
                                                },
                                                "evaluateOn": "ANTLR",
                                                "debugFeature": "ACTIVE"
                                            }
                                            """),
                            @ExampleObject(
                                    name = "Fail when dividing by zero",
                                    description = "Should return #DIV/0! when dividing by zero",
                                    //language=JSON
                                    value = """
                                            {
                                                "formula": "IF(EQ([@[Sales Person]],\\"Joe\\"),MUL(MUL([@[Sales Amount]],DIV([@[% Commission]],0)),2),MUL([@[Sales Amount]],DIV([@[% Commission]],100)))",
                                                "structuredReferences": {
                                                    "Sales Person": "Joe",
                                                    "Sales Amount": "260",
                                                    "% Commission": "10"
                                                },
                                                "evaluateOn": "ANTLR",
                                                "debugFeature": "ACTIVE"
                                            }
                                            """),
                            @ExampleObject(
                                    name = "Fail when doing an operation on not a number",
                                    description = "Should return #NUM! when doing an operation on not a number",
                                    //language=JSON
                                    value = """
                                            {
                                                "formula": "MUL([@[Sales Amount]],\\"not a number\\")",
                                                "structuredReferences": {
                                                    "Sales Amount": "260"
                                                },
                                                "evaluateOn": "ANTLR",
                                                "debugFeature": "ACTIVE"
                                            }
                                            """),
                            @ExampleObject(
                                    name = "Check if it is a number",
                                    description = "Should return I am a number if the Structured Reference is a number",
                                    //language=JSON
                                    value = """
                                            {
                                                "formula": "IF(ISNUM([@[Sales Amount]]),\\"I am a number\\", \\"I am not a number\\")",
                                                "structuredReferences": {
                                                    "Sales Amount": "260"
                                                },
                                                "evaluateOn": "ANTLR",
                                                "debugFeature": "ACTIVE"
                                            }
                                            """)
                    }
            )
    )
    @APIResponses(
            value = @APIResponse(
                    name = "success",
                    responseCode = "200",
                    content = @Content(
                            schema = @Schema(
                                    implementation = EvaluationResultDTO.class
                            ),
                            examples = @ExampleObject(
                                    name = "evaluated formula",
                                    description = "evaluated formula",
                                    //language=JSON
                                    value = """
                                            {
                                              "result": "26",
                                              "exactProcessedInNanos": 28231378,
                                              "parserEvaluationProcessedIn": {
                                                "evaluatedAtStart": "2024-05-05T01:09:39.695736969+02:00",
                                                "evaluatedAtEnd": "2024-05-05T01:09:39.717878532+02:00",
                                                "processedInNanos": 22141563
                                              },
                                              "evaluationProcessedIn": {
                                                "evaluatedAtStart": "2024-05-05T01:09:39.722660591+02:00",
                                                "evaluatedAtEnd": "2024-05-05T01:09:39.728750406+02:00",
                                                "processedInNanos": 6089815
                                              },
                                              "intermediateResults": [
                                                {
                                                  "evaluatedAtStart": "2024-05-05T01:09:39.724292501+02:00",
                                                  "evaluatedAtEnd": "2024-05-05T01:09:39.728590951+02:00",
                                                  "processedInNanos": 4298450,
                                                  "positionedAt": {
                                                    "start": 0,
                                                    "end": 48
                                                  },
                                                  "inputs": [
                                                    {
                                                      "name": "left",
                                                      "value": "260",
                                                      "positionedAt": {
                                                        "start": 4,
                                                        "end": 20
                                                      }
                                                    },
                                                    {
                                                      "name": "right",
                                                      "value": "0.1",
                                                      "positionedAt": {
                                                        "start": 22,
                                                        "end": 47
                                                      }
                                                    }
                                                  ],
                                                  "result": "26"
                                                },
                                                {
                                                  "evaluatedAtStart": "2024-05-05T01:09:39.724415205+02:00",
                                                  "evaluatedAtEnd": "2024-05-05T01:09:39.726876407+02:00",
                                                  "processedInNanos": 2461202,
                                                  "positionedAt": {
                                                    "start": 4,
                                                    "end": 20
                                                  },
                                                  "inputs": [
                                                    {
                                                      "name": "structuredReference",
                                                      "value": "Sales Amount",
                                                      "positionedAt": {
                                                        "start": 7,
                                                        "end": 18
                                                      }
                                                    }
                                                  ],
                                                  "result": "260"
                                                },
                                                {
                                                  "evaluatedAtStart": "2024-05-05T01:09:39.727326633+02:00",
                                                  "evaluatedAtEnd": "2024-05-05T01:09:39.728449028+02:00",
                                                  "processedInNanos": 1122395,
                                                  "positionedAt": {
                                                    "start": 22,
                                                    "end": 47
                                                  },
                                                  "inputs": [
                                                    {
                                                      "name": "left",
                                                      "value": "10",
                                                      "positionedAt": {
                                                        "start": 26,
                                                        "end": 42
                                                      }
                                                    },
                                                    {
                                                      "name": "right",
                                                      "value": "100",
                                                      "positionedAt": {
                                                        "start": 44,
                                                        "end": 46
                                                      }
                                                    }
                                                  ],
                                                  "result": "0.1"
                                                },
                                                {
                                                  "evaluatedAtStart": "2024-05-05T01:09:39.727336957+02:00",
                                                  "evaluatedAtEnd": "2024-05-05T01:09:39.727368776+02:00",
                                                  "processedInNanos": 31819,
                                                  "positionedAt": {
                                                    "start": 26,
                                                    "end": 42
                                                  },
                                                  "inputs": [
                                                    {
                                                      "name": "structuredReference",
                                                      "value": "% Commission",
                                                      "positionedAt": {
                                                        "start": 29,
                                                        "end": 40
                                                      }
                                                    }
                                                  ],
                                                  "result": "10"
                                                },
                                                {
                                                  "evaluatedAtStart": "2024-05-05T01:09:39.727476161+02:00",
                                                  "evaluatedAtEnd": "2024-05-05T01:09:39.72750957+02:00",
                                                  "processedInNanos": 33409,
                                                  "positionedAt": {
                                                    "start": 44,
                                                    "end": 46
                                                  },
                                                  "inputs": [],
                                                  "result": "100"
                                                }
                                              ]
                                            }
                                            """
                            )
                    )
            )
    )
    public Uni<EvaluationResultDTO> evaluate(final EvaluateDTO evaluateDTO) throws EvaluationException {
        return evaluateUseCase.execute(evaluateDTO.toEvaluateCommand())
                .map(EvaluationResultDTO::new);
    }

}
