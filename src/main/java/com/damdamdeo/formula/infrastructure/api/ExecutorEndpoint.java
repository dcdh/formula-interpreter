package com.damdamdeo.formula.infrastructure.api;

import com.damdamdeo.formula.domain.ExecutionException;
import com.damdamdeo.formula.domain.usecase.ExecuteUseCase;
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

@Path("/execute")
public final class ExecutorEndpoint {
    private final ExecuteUseCase executeUseCase;

    public ExecutorEndpoint(final ExecuteUseCase executeUseCase) {
        this.executeUseCase = Objects.requireNonNull(executeUseCase);
    }

    @POST
    @Consumes("application/vnd.formula-execute-v1+json")
    @Produces("application/vnd.formula-execution-v1+json")
    @RequestBody(
            required = true,
            content = @Content(
                    schema = @Schema(
                            implementation = ExecuteDTO.class
                    ),
                    examples = {
                            @ExampleObject(
                                    name = "Incomplete formula",
                                    description = "Should fail to execute",
                                    //language=JSON
                                    value = """
                                            {
                                                "formula": "IF",
                                                "structuredData": {
                                                    "Sales Amount": "260",
                                                    "% Commission": "10"
                                                },
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
                                                "structuredData": {
                                                    "Sales Amount": "260",
                                                    "% Commission": "10"
                                                },
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
                                                "structuredData": {
                                                    "Sales Amount": "260",
                                                    "% Commission": "10"
                                                },
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
                                                "structuredData": {
                                                    "Sales Person": "Joe",
                                                    "Sales Amount": "260",
                                                    "% Commission": "10"
                                                },
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
                                                "structuredData": {
                                                    "Sales Person": "Joe",
                                                    "Sales Amount": "260",
                                                    "% Commission": "10"
                                                },
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
                                                "structuredData": {
                                                    "Sales Amount": "260"
                                                },
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
                                                "structuredData": {
                                                    "Sales Amount": "260"
                                                },
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
                                    implementation = ExecutionResultDTO.class
                            ),
                            examples = @ExampleObject(
                                    name = "executed formula",
                                    description = "executed formula",
                                    //language=JSON
                                    value = """
                                            {
                                              "result": "26",
                                              "exactProcessedInNanos": 28231378,
                                              "parserExecutionProcessedIn": {
                                                "executedAtStart": "2024-05-05T01:09:39.695736969+02:00",
                                                "executedAtEnd": "2024-05-05T01:09:39.717878532+02:00",
                                                "processedInNanos": 22141563
                                              },
                                              "executionProcessedIn": {
                                                "executedAtStart": "2024-05-05T01:09:39.722660591+02:00",
                                                "executedAtEnd": "2024-05-05T01:09:39.728750406+02:00",
                                                "processedInNanos": 6089815
                                              },
                                              "elementExecutions": [
                                                {
                                                  "executedAtStart": "2024-05-05T01:09:39.724292501+02:00",
                                                  "executedAtEnd": "2024-05-05T01:09:39.728590951+02:00",
                                                  "processedInNanos": 4298450,
                                                  "range": {
                                                    "start": 0,
                                                    "end": 48
                                                  },
                                                  "inputs": [
                                                    {
                                                      "name": "left",
                                                      "value": "260",
                                                      "range": {
                                                        "start": 4,
                                                        "end": 20
                                                      }
                                                    },
                                                    {
                                                      "name": "right",
                                                      "value": "0.1",
                                                      "range": {
                                                        "start": 22,
                                                        "end": 47
                                                      }
                                                    }
                                                  ],
                                                  "result": "26"
                                                },
                                                {
                                                  "executedAtStart": "2024-05-05T01:09:39.724415205+02:00",
                                                  "executedAtEnd": "2024-05-05T01:09:39.726876407+02:00",
                                                  "processedInNanos": 2461202,
                                                  "range": {
                                                    "start": 4,
                                                    "end": 20
                                                  },
                                                  "inputs": [
                                                    {
                                                      "name": "structuredReference",
                                                      "value": "Sales Amount",
                                                      "range": {
                                                        "start": 7,
                                                        "end": 18
                                                      }
                                                    }
                                                  ],
                                                  "result": "260"
                                                },
                                                {
                                                  "executedAtStart": "2024-05-05T01:09:39.727326633+02:00",
                                                  "executedAtEnd": "2024-05-05T01:09:39.728449028+02:00",
                                                  "processedInNanos": 1122395,
                                                  "range": {
                                                    "start": 22,
                                                    "end": 47
                                                  },
                                                  "inputs": [
                                                    {
                                                      "name": "left",
                                                      "value": "10",
                                                      "range": {
                                                        "start": 26,
                                                        "end": 42
                                                      }
                                                    },
                                                    {
                                                      "name": "right",
                                                      "value": "100",
                                                      "range": {
                                                        "start": 44,
                                                        "end": 46
                                                      }
                                                    }
                                                  ],
                                                  "result": "0.1"
                                                },
                                                {
                                                  "executedAtStart": "2024-05-05T01:09:39.727336957+02:00",
                                                  "executedAtEnd": "2024-05-05T01:09:39.727368776+02:00",
                                                  "processedInNanos": 31819,
                                                  "range": {
                                                    "start": 26,
                                                    "end": 42
                                                  },
                                                  "inputs": [
                                                    {
                                                      "name": "structuredReference",
                                                      "value": "% Commission",
                                                      "range": {
                                                        "start": 29,
                                                        "end": 40
                                                      }
                                                    }
                                                  ],
                                                  "result": "10"
                                                },
                                                {
                                                  "executedAtStart": "2024-05-05T01:09:39.727476161+02:00",
                                                  "executedAtEnd": "2024-05-05T01:09:39.72750957+02:00",
                                                  "processedInNanos": 33409,
                                                  "range": {
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
    public Uni<ExecutionResultDTO> execute(final ExecuteDTO execute) throws ExecutionException {
        return executeUseCase.execute(execute.toExecuteCommand())
                .map(ExecutionResultDTO::new);
    }

}
