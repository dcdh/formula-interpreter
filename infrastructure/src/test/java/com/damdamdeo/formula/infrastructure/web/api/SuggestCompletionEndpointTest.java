package com.damdamdeo.formula.infrastructure.web.api;

import com.damdamdeo.formula.domain.SuggestedFormula;
import com.damdamdeo.formula.domain.SuggestionException;
import com.damdamdeo.formula.domain.usecase.SuggestCommand;
import com.damdamdeo.formula.domain.usecase.SuggestUseCase;
import com.damdamdeo.formula.domain.usecase.resolver.SuggestCompletionParameterResolver;
import com.damdamdeo.formula.infrastructure.parser.antlr.AntlrAutoSuggestUnavailableException;
import com.damdamdeo.formula.infrastructure.parser.antlr.AntlrAutoSuggestionExecutionException;
import com.damdamdeo.formula.infrastructure.parser.antlr.AntlrAutoSuggestionExecutionTimedOutException;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.core.MediaType;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.doReturn;

@QuarkusTest
@ExtendWith(SuggestCompletionEndpointTest.AntlrSuggestCompletionParameterResolver.class)
public class SuggestCompletionEndpointTest {

    @InjectMock
    private SuggestUseCase suggestUseCase;

    @Test
    public void shouldSuggestCompletion(final SuggestCompletionParameterResolver.GivenHappyPath givenHappyPath) {
        // Given
        final SuggestedFormula givenSuggestedFormula = givenHappyPath.suggestedFormula();
        doReturn(givenHappyPath.toUni()).when(suggestUseCase).execute(new SuggestCommand(givenSuggestedFormula));

        // When && Then
        given()
                .log().all()
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .accept("application/vnd.suggest-completion-v1+json")
                .multiPart("suggestedFormula", "IF")
                .when()
                .post("/suggestCompletion")
                .then()
                .log().all()
                .statusCode(HttpStatus.SC_OK)
                .contentType("application/vnd.suggest-completion-v1+json;charset=UTF-8")
                .body("tokens", contains("("));
    }

    @Test
    @Tag("AntlrAutoSuggestionExecutionException")
    public void shouldHandleAntlrAutoSuggestionExecutionException(final SuggestCompletionParameterResolver.GivenFailing givenFailing) {
        // Given
        final SuggestedFormula givenSuggestedFormula = givenFailing.suggestedFormula();
        doReturn(Uni.createFrom().failure(new SuggestionException(givenFailing.cause())))
                .when(suggestUseCase).execute(new SuggestCommand(givenSuggestedFormula));

        // When && Then
        given()
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .accept("application/vnd.suggest-completion-v1+json")
                .multiPart("suggestedFormula", givenSuggestedFormula.formula())
                .when()
                .post("/suggestCompletion")
                .then()
                .log().all()
                .statusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR)
                .contentType("application/vnd.autosuggestion-execution-exception-v1+json")
                .body("message", is("AutoSuggestion service execution exception while processing formula 'IF(EQ([@[Sales Person]],\"Joe\"),MUL(MUL([@[Sales Amount]],DIV([@[% Commission]],100)),2),MUL([@[Sales Amount]],DIV([@[% Commission]],100)' - msg 'error'"));
    }

    @Test
    @Tag("AntlrAutoSuggestionExecutionTimedOutException")
    public void shouldHandleAntlrAutoSuggestionExecutionTimedOutException(final SuggestCompletionParameterResolver.GivenFailing givenFailing) {
        // Given
        final SuggestedFormula givenSuggestedFormula = givenFailing.suggestedFormula();
        doReturn(Uni.createFrom().failure(new SuggestionException(givenFailing.cause())))
                .when(suggestUseCase).execute(new SuggestCommand(givenSuggestedFormula));

        // When && Then
        given()
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .accept("application/vnd.suggest-completion-v1+json")
                .multiPart("suggestedFormula", givenSuggestedFormula.formula())
                .when()
                .post("/suggestCompletion")
                .then()
                .log().all()
                .statusCode(HttpStatus.SC_SERVICE_UNAVAILABLE)
                .contentType("application/vnd.autosuggestion-execution-timed-out-v1+json")
                .body("message", is("AutoSuggestion service has timed out while executing formula 'IF(EQ([@[Sales Person]],\"Joe\"),MUL(MUL([@[Sales Amount]],DIV([@[% Commission]],100)),2),MUL([@[Sales Amount]],DIV([@[% Commission]],100)' - Infinite loop in Grammar - msg 'error'"));
    }

    @Test
    @Tag("AntlrAutoSuggestUnavailableException")
    public void shouldHandleAntlrAutoSuggestUnavailableException(final SuggestCompletionParameterResolver.GivenFailing givenFailing) {
        // Given
        final SuggestedFormula givenSuggestedFormula = givenFailing.suggestedFormula();
        doReturn(Uni.createFrom().failure(new SuggestionException(givenFailing.cause())))
                .when(suggestUseCase).execute(new SuggestCommand(givenSuggestedFormula));

        // When && Then
        given()
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .accept("application/vnd.suggest-completion-v1+json")
                .multiPart("suggestedFormula", givenSuggestedFormula.formula())
                .when()
                .post("/suggestCompletion")
                .then()
                .log().all()
                .statusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR)
                .contentType("application/vnd.autosuggestion-unavailable-v1+json")
                .body("message", is("AutoSuggestion service execution unavailable while processing formula 'IF(EQ([@[Sales Person]],\"Joe\"),MUL(MUL([@[Sales Amount]],DIV([@[% Commission]],100)),2),MUL([@[Sales Amount]],DIV([@[% Commission]],100)' - msg 'error'"));
    }

    @Test
    @Tag("Exception")
    public void shouldHandleException(final SuggestCompletionParameterResolver.GivenFailing givenFailing) {
        // Given
        final SuggestedFormula givenSuggestedFormula = givenFailing.suggestedFormula();
        doReturn(Uni.createFrom().failure(new SuggestionException(givenFailing.cause())))
                .when(suggestUseCase).execute(new SuggestCommand(givenSuggestedFormula));

        // When && Then
        given()
                .log().all()
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .accept("application/vnd.suggest-completion-v1+json")
                .multiPart("suggestedFormula", givenSuggestedFormula.formula())
                .when()
                .post("/suggestCompletion")
                .then()
                .log().all()
                .statusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR)
                .contentType("application/vnd.autosuggestion-unexpected-exception-v1+json")
                .body("message", is("unexpected \"cause\""));
    }

    static class AntlrSuggestCompletionParameterResolver extends SuggestCompletionParameterResolver {
        @Override
        protected List<GivenFailing> givenFailings() {
            final SuggestedFormula givenSuggestedFormula = new SuggestedFormula("IF(EQ([@[Sales Person]],\"Joe\"),MUL(MUL([@[Sales Amount]],DIV([@[% Commission]],100)),2),MUL([@[Sales Amount]],DIV([@[% Commission]],100)");
            return List.of(
                    new GivenFailing(
                            givenSuggestedFormula,
                            new AntlrAutoSuggestionExecutionException(givenSuggestedFormula, new RuntimeException("error"))),
                    new GivenFailing(
                            givenSuggestedFormula,
                            new AntlrAutoSuggestionExecutionTimedOutException(givenSuggestedFormula, new RuntimeException("error"))),
                    new GivenFailing(
                            givenSuggestedFormula,
                            new AntlrAutoSuggestUnavailableException(givenSuggestedFormula, new RuntimeException("error"))),
                    new GivenFailing(
                            givenSuggestedFormula,
                            new Exception("unexpected \"cause\""))
            );
        }
    }

}
