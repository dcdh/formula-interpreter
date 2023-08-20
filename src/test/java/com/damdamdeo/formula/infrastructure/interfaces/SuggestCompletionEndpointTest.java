package com.damdamdeo.formula.infrastructure.interfaces;

import com.damdamdeo.formula.domain.*;
import com.damdamdeo.formula.domain.usecase.SuggestCommand;
import com.damdamdeo.formula.domain.usecase.SuggestUseCase;
import com.damdamdeo.formula.infrastructure.antlr.AntlrAutoSuggestUnavailableException;
import com.damdamdeo.formula.infrastructure.antlr.AntlrAutoSuggestionExecutionException;
import com.damdamdeo.formula.infrastructure.antlr.AntlrAutoSuggestionExecutionTimedOutException;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import org.apache.http.HttpStatus;
import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

@QuarkusTest
public class SuggestCompletionEndpointTest {

    @InjectMock
    private SuggestUseCase suggestUseCase;

    @Test
    public void shouldSuggestCompletion() throws JSONException {
        // Given
        doReturn(new SuggestionsCompletion(List.of("(")))
                .when(suggestUseCase).execute(new SuggestCommand(new SuggestedFormula("IF")));

        // When && Then
        //language=JSON
        final String expectedBody = """
                [
                    "("
                ]
                """;
        final String actualBody = given()
                .accept("application/vnd.suggest-completion-v1+json")
                .formParam("suggestedFormula", "IF")
                .when()
                .post("/suggestCompletion")
                .then()
                .log().all()
                .statusCode(HttpStatus.SC_OK)
                .contentType("application/vnd.suggest-completion-v1+json;charset=UTF-8")
                .extract().response().body().asString();
        JSONAssert.assertEquals(expectedBody, actualBody, JSONCompareMode.STRICT);
    }

    @Test
    public void shouldHandleAutoSuggestionExecutionException() {
        // Given
        final SuggestedFormula givenSuggestedFormula
                = new SuggestedFormula("IF(EQ([@[Sales Person]],\"Joe\"),MUL(MUL([@[Sales Amount]],DIV([@[% Commission]],100)),2),MUL([@[Sales Amount]],DIV([@[% Commission]],100)");
        doThrow(new SuggestionException(new AntlrAutoSuggestionExecutionException(givenSuggestedFormula, new RuntimeException("error"))))
                .when(suggestUseCase).execute(new SuggestCommand(givenSuggestedFormula));

        // When && Then
        given()
                .accept("application/vnd.suggest-completion-v1+json")
                .formParam("suggestedFormula", "IF(EQ([@[Sales Person]],\"Joe\"),MUL(MUL([@[Sales Amount]],DIV([@[% Commission]],100)),2),MUL([@[Sales Amount]],DIV([@[% Commission]],100)")
                .when()
                .post("/suggestCompletion")
                .then()
                .log().all()
                .statusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR)
                .contentType("application/vnd.autosuggestion-execution-exception-v1+json")
                .body("message", is("AutoSuggestion service execution exception while processing formula 'IF(EQ([@[Sales Person]],\"Joe\"),MUL(MUL([@[Sales Amount]],DIV([@[% Commission]],100)),2),MUL([@[Sales Amount]],DIV([@[% Commission]],100)' - msg 'error'"));
    }

    @Test
    public void shouldHandleAutoSuggestionExecutionTimedOutException() {
        // Given
        final SuggestedFormula givenSuggestedFormula
                = new SuggestedFormula("IF(EQ([@[Sales Person]],\"Joe\"),MUL(MUL([@[Sales Amount]],DIV([@[% Commission]],100)),2),MUL([@[Sales Amount]],DIV([@[% Commission]],100)");
        doThrow(new SuggestionException(new AntlrAutoSuggestionExecutionTimedOutException(givenSuggestedFormula, new RuntimeException("error"))))
                .when(suggestUseCase).execute(new SuggestCommand(givenSuggestedFormula));

        // When && Then
        given()
                .accept("application/vnd.suggest-completion-v1+json")
                .formParam("suggestedFormula", "IF(EQ([@[Sales Person]],\"Joe\"),MUL(MUL([@[Sales Amount]],DIV([@[% Commission]],100)),2),MUL([@[Sales Amount]],DIV([@[% Commission]],100)")
                .when()
                .post("/suggestCompletion")
                .then()
                .log().all()
                .statusCode(HttpStatus.SC_SERVICE_UNAVAILABLE)
                .contentType("application/vnd.autosuggestion-execution-timed-out-v1+json")
                .body("message", is("AutoSuggestion service has timed out while executing formula 'IF(EQ([@[Sales Person]],\"Joe\"),MUL(MUL([@[Sales Amount]],DIV([@[% Commission]],100)),2),MUL([@[Sales Amount]],DIV([@[% Commission]],100)' - Infinite loop in Grammar - msg 'error'"));
    }

    @Test
    public void shouldHandleAutoSuggestUnavailableException() {
        // Given
        final SuggestedFormula givenSuggestedFormula
                = new SuggestedFormula("IF(EQ([@[Sales Person]],\"Joe\"),MUL(MUL([@[Sales Amount]],DIV([@[% Commission]],100)),2),MUL([@[Sales Amount]],DIV([@[% Commission]],100)");
        doThrow(new SuggestionException(new AntlrAutoSuggestUnavailableException(givenSuggestedFormula, new RuntimeException("error"))))
                .when(suggestUseCase).execute(new SuggestCommand(givenSuggestedFormula));

        // When && Then
        given()
                .accept("application/vnd.suggest-completion-v1+json")
                .formParam("suggestedFormula", "IF(EQ([@[Sales Person]],\"Joe\"),MUL(MUL([@[Sales Amount]],DIV([@[% Commission]],100)),2),MUL([@[Sales Amount]],DIV([@[% Commission]],100)")
                .when()
                .post("/suggestCompletion")
                .then()
                .log().all()
                .statusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR)
                .contentType("application/vnd.autosuggestion-unavailable-v1+json")
                .body("message", is("AutoSuggestion service execution unavailable while processing formula 'IF(EQ([@[Sales Person]],\"Joe\"),MUL(MUL([@[Sales Amount]],DIV([@[% Commission]],100)),2),MUL([@[Sales Amount]],DIV([@[% Commission]],100)' - msg 'error'"));
    }

    @Test
    public void shouldHandleException() {
        // Given
        doThrow(new SuggestionException(new Exception("unexpected \"exception\"")))
                .when(suggestUseCase).execute(new SuggestCommand(new SuggestedFormula("IF")));

        // When && Then
        given()
                .accept("application/vnd.suggest-completion-v1+json")
                .formParam("suggestedFormula", "IF")
                .when()
                .post("/suggestCompletion")
                .then()
                .log().all()
                .statusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR)
                .contentType("application/vnd.autosuggestion-unexpected-exception-v1+json")
                .body("message", is("unexpected \"exception\""));
    }
}
