package com.damdamdeo.formula.domain.usecase;

import com.damdamdeo.formula.domain.Formula;
import com.damdamdeo.formula.domain.SyntaxError;
import com.damdamdeo.formula.domain.ValidationException;
import com.damdamdeo.formula.domain.spi.Validator;
import com.damdamdeo.formula.domain.usecase.resolver.ValidatorParameterResolver;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.helpers.test.UniAssertSubscriber;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
@ExtendWith(ValidateUseCaseTest.DomainValidatorParameterResolver.class)
class ValidateUseCaseTest {

    @Mock
    private Validator<DomainSyntaxError> validator;

    private ValidateUseCase<DomainSyntaxError> validateUseCase;

    @BeforeEach
    void setup() {
        this.validateUseCase = new ValidateUseCase<>(validator);
    }

    @ValidatorParameterResolver.ValidTest
    void shouldReturnEmptyWhenFormulaIsValid(final ValidatorParameterResolver.GivenHappyPath<DomainSyntaxError> givenHappyPath) {
        // Given
        final Formula givenFormula = givenHappyPath.formula();
        doReturn(givenHappyPath.toUni()).when(validator).validate(givenFormula);

        // When
        final Uni<Optional<DomainSyntaxError>> executed = validateUseCase.execute(new ValidateCommand(givenFormula));

        // Then
        final UniAssertSubscriber<Optional<DomainSyntaxError>> subscriber = executed
                .subscribe().withSubscriber(UniAssertSubscriber.create());

        subscriber.assertCompleted().assertItem(Optional.empty());
    }

    @ValidatorParameterResolver.InvalidTest
    void shouldReturnDomainSyntaxErrorWhenFormulaIsInvalid(final ValidatorParameterResolver.GivenHappyPath<DomainSyntaxError> givenHappyPath) {
        // Given
        final Formula givenFormula = givenHappyPath.formula();
        doReturn(givenHappyPath.toUni()).when(validator).validate(givenFormula);

        // When
        final Uni<Optional<DomainSyntaxError>> executed = validateUseCase.execute(new ValidateCommand(givenFormula));

        // Then
        final UniAssertSubscriber<Optional<DomainSyntaxError>> subscriber = executed
                .subscribe().withSubscriber(UniAssertSubscriber.create());

        subscriber.assertCompleted().assertItem(Optional.of(new DomainSyntaxError("invalid char at position")));
    }

    @ValidatorParameterResolver.ValidationExceptionTest
    @Tag("IllegalStateException")
    void shouldFailWhenAnExceptionIsThrown(final ValidatorParameterResolver.GivenFailing givenFailing) {
        // Given
        final Formula givenFormula = givenFailing.formula();
        doReturn(givenFailing.toUni()).when(validator).validate(givenFormula);

        // When
        final Uni<Optional<DomainSyntaxError>> executed = validateUseCase.execute(new ValidateCommand(givenFormula));

        // Then
        final UniAssertSubscriber<Optional<DomainSyntaxError>> subscriber = executed
                .subscribe().withSubscriber(UniAssertSubscriber.create());

        assertAll(
                subscriber::assertFailed,
                () -> assertThat(subscriber.getFailure()).isInstanceOf(ValidationException.class)
                        .hasRootCauseInstanceOf(IllegalStateException.class)
                        .hasRootCauseMessage("something wrong happened in infra")
        );
    }

    static class DomainValidatorParameterResolver extends ValidatorParameterResolver<DomainSyntaxError> {
        @Override
        protected DomainSyntaxError givenInvalid() {
            return new DomainSyntaxError("invalid char at position");
        }

        @Override
        protected List<GivenFailing> givenFailings() {
            return List.of(
                    new GivenFailing(
                            new Formula("true"),
                            new IllegalStateException("something wrong happened in infra")
                    )
            );
        }

    }

    record DomainSyntaxError(String msg) implements SyntaxError {
        public DomainSyntaxError {
            Objects.requireNonNull(msg);
        }
    }
}