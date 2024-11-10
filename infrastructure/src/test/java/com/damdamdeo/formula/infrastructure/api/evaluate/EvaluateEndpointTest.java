package com.damdamdeo.formula.infrastructure.api.evaluate;

import com.damdamdeo.formula.domain.*;
import com.damdamdeo.formula.domain.usecase.EvaluateCommand;
import com.damdamdeo.formula.domain.usecase.EvaluateUseCase;
import com.damdamdeo.formula.infrastructure.antlr.AntlrSyntaxError;
import com.damdamdeo.formula.infrastructure.antlr.AntlrSyntaxErrorException;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.smallrye.mutiny.Uni;
import org.apache.http.HttpStatus;
import org.json.JSONException;
import org.junit.jupiter.api.Test;

import java.time.ZonedDateTime;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.doReturn;

// TODO FCK !!!
@QuarkusTest
public class EvaluateEndpointTest {

    @InjectMock
    EvaluateUseCase evaluateUseCase;

    je dois mocker aussi le EvaluateAtProvider !!!

    @Test
    public void shouldProcess() throws JSONException {
        // Given
        doReturn(
                ok donc il me faut le test qui va bien qui me retourne ceci coté domain !
                Uni.createFrom().item(new EvaluationResult(
                        new Value("true"),
                        new ParserEvaluationProcessedIn(
                                new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:14:58+01:00[Europe/Paris]")),
                                new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:14:59+01:00[Europe/Paris]"))
                        ),
                        List.of(
                                new IntermediateResult(
                                        Value.of("10"),
                                        new PositionedAt(4, 5),
                                        List.of(
                                                new Input(new InputName("reference"), new Value("ref"), new PositionedAt(2, 3))),
                                        new EvaluationProcessedIn(
                                                new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:00+01:00[Europe/Paris]")),
                                                new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:01+01:00[Europe/Paris]"))))
                        ),
                        new EvaluationProcessedIn(
                                new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:00+01:00[Europe/Paris]")),
                                new EvaluatedAt(ZonedDateTime.parse("2023-12-25T10:15:02+01:00[Europe/Paris]")))))
        )
                .when(evaluateUseCase)
                .execute(new EvaluateCommand(new Formula("true"),
                        List.of(
                                new StructuredReference(new Reference("ref"), new Value("val"))),
                        DebugFeature.ACTIVE,
                        EvaluateOn.ANTLR));ce n'est pas la bonne formule ... fuck !!!
        //language=JSON
        final String request = """
                {
                    "formula":"true",
                    "structuredReferences": {
                        "ref": "val"
                    },
                    "evaluateOn": "ANTLR",
                    "debugFeature": "ACTIVE"
                }
                """;

        // When && Then
        given()
                .contentType("application/vnd.formula-evaluate-v1+json")
                .accept("application/vnd.formula-evaluated-v1+json")
                .body(request)
                .when()
                .post("/evaluate")
                .then()
                .log().all()
                .statusCode(HttpStatus.SC_OK)
                .contentType("application/vnd.formula-evaluated-v1+json")
                .body("result", equalTo("true"))
                .body("exactProcessedInNanos", equalTo(3000000000L))
                .body("parserExecutionProcessedIn.executedAtStart", equalTo("2023-12-25T10:14:58+01:00"))
                .body("parserExecutionProcessedIn.executedAtEnd", equalTo("2023-12-25T10:14:59+01:00"))
                .body("parserExecutionProcessedIn.processedInNanos", equalTo(1000000000))
                .body("executionProcessedIn.executedAtStart", equalTo("2023-12-25T10:15:00+01:00"))
                .body("executionProcessedIn.executedAtEnd", equalTo("2023-12-25T10:15:02+01:00"))
                .body("executionProcessedIn.processedInNanos", equalTo(2000000000))
                .body("elementExecutions.size()", equalTo(1))
                .body("elementExecutions[0].executedAtStart", equalTo("2023-12-25T10:15:00+01:00"))
                .body("elementExecutions[0].executedAtEnd", equalTo("2023-12-25T10:15:01+01:00"))
                .body("elementExecutions[0].processedInNanos", equalTo(1000000000))
                .body("elementExecutions[0].range.start", equalTo(4))
                .body("elementExecutions[0].range.end", equalTo(5))
                .body("elementExecutions[0].inputs.size()", equalTo(1))
                .body("elementExecutions[0].inputs[0].name", equalTo("reference"))
                .body("elementExecutions[0].inputs[0].value", equalTo("ref"))
                .body("elementExecutions[0].inputs[0].range.start", equalTo(2))
                .body("elementExecutions[0].inputs[0].range.end", equalTo(3))
                .body("elementExecutions[0].result", equalTo("10"));
    }

    @Test
    public void shouldHandleSyntaxErrorException() {
        // Given
        doReturn(
                Uni.createFrom().failure(
                        new EvaluationException(new AntlrSyntaxErrorException(new Formula("true"), new AntlrSyntaxError(0, 1, "custom \"msg\"")))
                )
        )
                .when(evaluateUseCase).execute(
                        new EvaluateCommand(
                                new Formula("true"),
                                List.of(
                                        new StructuredReference(new Reference("ref"), new Value("val"))),
                                DebugFeature.ACTIVE,
                                EvaluateOn.ANTLR));
        //language=JSON
        final String request = """
                {
                    "formula":"true",
                    "structuredReferences": {
                        "ref":"val"
                    },
                    "evaluateOn": "ANTLR",
                    "debugFeature": "ACTIVE"
                }
                """;

        // When && Then
        given()
                .contentType("application/vnd.formula-evaluate-v1+json")
                .accept("application/vnd.formula-evaluated-v1+json")
                .body(request)
                .when()
                .post("/evaluate")
                .then()
                .log().all()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .contentType("application/vnd.evaluation-syntax-error-v1+json")
                .body("message", is("Syntax error at line '0' at positionedAt '1' with message 'custom \"msg\"'"));
    }

    @Test
    public void shouldHandleException() {
        // Given
        doReturn(
                Uni.createFrom().failure(new EvaluationException(new Exception("unexpected \"exception\"")))
        )
                .when(evaluateUseCase).execute(new EvaluateCommand(
                        new Formula("true"),
                        List.of(
                                new StructuredReference(new Reference("ref"), new Value("val"))),
                        DebugFeature.ACTIVE,
                        EvaluateOn.ANTLR));
        //language=JSON
        final String request = """
                {
                    "formula":"true",
                    "structuredReferences": {
                        "ref":"val"
                    },
                    "evaluateOn": "ANTLR",
                    "debugFeature": "ACTIVE"
                }
                """;

        // When && Then
        given()
                .contentType("application/vnd.formula-evaluate-v1+json")
                .accept("application/vnd.formula-evaluated-v1+json")
                .body(request)
                .when()
                .post("/evaluate")
                .then()
                .log().all()
                .statusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR)
                .contentType("application/vnd.evaluation-unexpected-exception-v1+json")
                .body("message", is("unexpected \"exception\""));
    }
}
