package com.damdamdeo.formula.infrastructure.api.suggest;

import com.damdamdeo.formula.domain.SuggestedFormula;
import com.damdamdeo.formula.domain.SuggestionException;
import com.damdamdeo.formula.domain.spi.SuggestCompletion;
import com.damdamdeo.formula.domain.usecase.provider.SuggestCompletionTestResolver;
import com.damdamdeo.formula.infrastructure.antlr.AntlrAutoSuggestionExecutionException;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.ws.rs.core.MediaType;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.extension.ExtendWith;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.doReturn;

@ExtendWith(SuggestCompletionEndpointTest.AntlrSuggestCompletionTestResolver.class)
@QuarkusTest
class SuggestCompletionEndpointTest {

    @InjectMock
    SuggestCompletion suggestCompletion;

    @SuggestCompletionTestResolver.ValidTest
    void shouldSuggest(final SuggestedFormula suggestedFormula,
                       final SuggestCompletionTestResolver.GivenResponse givenResponse) {
        // Given
        doReturn(givenResponse.response()).when(suggestCompletion).suggest(suggestedFormula);

        // When && Then
        given()
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

    @SuggestCompletionTestResolver.DomainValidationExceptionTest
    void shouldHandleAutoSuggestionExecutionException(final SuggestedFormula suggestedFormula,
                                                      final SuggestCompletionTestResolver.GivenResponse givenResponse) {
        // Given
        doReturn(givenResponse.response()).when(suggestCompletion).suggest(suggestedFormula);

        // When && Then
        given()
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .accept("application/vnd.suggest-completion-v1+json")
                .multiPart("suggestedFormula", "IF(EQ([@[Sales Person]],\"Joe\"),MUL(MUL([@[Sales Amount]],DIV([@[% Commission]],100)),2),MUL([@[Sales Amount]],DIV([@[% Commission]],100)")
                .when()
                .post("/suggestCompletion")
                .then()
                .log().all()
                .statusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR)
                .contentType("application/vnd.autosuggestion-execution-exception-v1+json")
                .body("message", is("AutoSuggestion service execution exception while processing formula 'IF(EQ([@[Sales Person]],\"Joe\"),MUL(MUL([@[Sales Amount]],DIV([@[% Commission]],100)),2),MUL([@[Sales Amount]],DIV([@[% Commission]],100)' - msg 'error'"));
    }

    // TODO FCK déplacer reuse côté Antlr !!! GOLDEN RULE !!
    static class AntlrSuggestCompletionTestResolver extends SuggestCompletionTestResolver {
        private static final SuggestedFormula suggestedFormulaInError =
                new SuggestedFormula("IF(EQ([@[Sales Person]],\"Joe\"),MUL(MUL([@[Sales Amount]],DIV([@[% Commission]],100)),2),MUL([@[Sales Amount]],DIV([@[% Commission]],100)");

        @Override
        protected SuggestedFormula suggestedFormulaInError() {
            return suggestedFormulaInError;
        }

        @Override
        protected SuggestionException suggestionException() {
            return new SuggestionException(new AntlrAutoSuggestionExecutionException(suggestedFormulaInError, new RuntimeException("error")));
        }
    }

}
