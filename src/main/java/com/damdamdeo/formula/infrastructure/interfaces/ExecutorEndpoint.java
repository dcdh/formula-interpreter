package com.damdamdeo.formula.infrastructure.interfaces;

import com.damdamdeo.formula.domain.ExecutionResult;
import com.damdamdeo.formula.domain.usecase.ExecuteUseCase;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;

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
    public ExecutionResultDTO execute(final ExecuteDTO execute) {
        final ExecutionResult executionResult = executeUseCase.execute(execute.toExecuteCommand());
        return new ExecutionResultDTO(executionResult);
    }

}
