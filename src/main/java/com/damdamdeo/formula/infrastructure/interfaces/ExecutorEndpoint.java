package com.damdamdeo.formula.infrastructure.interfaces;

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
                            implementation = ExecuteDTO.class,
                            required = true
                    ),
                    examples = {
                            // rajouter IF pour tester que cela pete
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
                                    implementation = ExecutionResultDTO.class,
                                    required = true
                            ),
                            examples = @ExampleObject(
                                    name = "executed formula",
                                    description = "executed formula",
                                    //language=JSON
                                    value = """
                                            {
                                                "executedAtStart": "2023-08-28T23:54:43.955041713+02:00",
                                                "executedAtEnd": "2023-08-28T23:54:43.965929129+02:00",
                                                "processedInNanos": 10887416,
                                                "result": "26",
                                                "elementExecutions": [{
                                                    "executedAtStart": "2023-08-28T23:54:43.962445291+02:00",
                                                    "executedAtEnd": "2023-08-28T23:54:43.965758021+02:00",
                                                    "processedInNanos": 3312730,
                                                    "position": {
                                                        "start": 0,
                                                        "end": 48
                                                    },
                                                    "inputs": {
                                                        "left": "260",
                                                        "right": "0.1"
                                                    },
                                                    "result": "26"
                                                },
                                                {
                                                    "executedAtStart": "2023-08-28T23:54:43.962602627+02:00",
                                                    "executedAtEnd": "2023-08-28T23:54:43.963690954+02:00",
                                                    "processedInNanos": 1088327,
                                                    "position": {
                                                        "start": 4,
                                                        "end": 20
                                                    },
                                                    "inputs": {
                                                        "structuredReference": "Sales Amount"
                                                    },
                                                    "result": "260"
                                                },
                                                {
                                                    "executedAtStart": "2023-08-28T23:54:43.964600978+02:00",
                                                    "executedAtEnd": "2023-08-28T23:54:43.965641273+02:00",
                                                    "processedInNanos": 1040295,
                                                    "position": {
                                                        "start": 22,
                                                        "end": 47
                                                    },
                                                    "inputs": {
                                                        "left": "10",
                                                        "right": "100"
                                                    },
                                                    "result": "0.1"
                                                },
                                                {
                                                    "executedAtStart": "2023-08-28T23:54:43.964623803+02:00",
                                                    "executedAtEnd": "2023-08-28T23:54:43.964672892+02:00",
                                                    "processedInNanos": 49089,
                                                    "position": {
                                                        "start": 26,
                                                        "end": 42
                                                    },
                                                    "inputs": {
                                                        "structuredReference": "% Commission"
                                                    },
                                                    "result": "10"
                                                },
                                                {
                                                    "executedAtStart": "2023-08-28T23:54:43.964804017+02:00",
                                                    "executedAtEnd": "2023-08-28T23:54:43.964843986+02:00",
                                                    "processedInNanos": 39969,
                                                    "position": {
                                                        "start": 44,
                                                        "end": 46
                                                    },
                                                    "inputs": {},
                                                    "result": "100"
                                                }]
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
