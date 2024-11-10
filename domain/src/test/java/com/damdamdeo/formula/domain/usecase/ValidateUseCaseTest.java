package com.damdamdeo.formula.domain.usecase;

import com.damdamdeo.formula.domain.Formula;
import com.damdamdeo.formula.domain.SyntaxError;
import com.damdamdeo.formula.domain.ValidationException;
import com.damdamdeo.formula.domain.spi.Validator;
import com.damdamdeo.formula.domain.usecase.provider.ValidatorTestResolver;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.helpers.test.UniAssertSubscriber;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Objects;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(ValidateUseCaseTest.DomainValidatorTestResolver.class)
class ValidateUseCaseTest {

    private Validator<DomainSyntaxError> validator;

    private ValidateUseCase<DomainSyntaxError> validateUseCase;

    @BeforeEach
    void setUp() {
        this.validator = mock(Validator.class);
        this.validateUseCase = new ValidateUseCase<>(validator);
    }

    @ValidatorTestResolver.ValidTest
    void shouldReturnEmptyWhenFormulaIsValid(final Formula givenFormula,
                                             final ValidatorTestResolver.GivenResponse<DomainSyntaxError> givenResponse) {
        // Given
        doReturn(givenResponse.response()).when(validator).validate(givenFormula);

        // When
        final Uni<Optional<DomainSyntaxError>> executed = validateUseCase.execute(new ValidateCommand(givenFormula));

        // Then
        final UniAssertSubscriber<Optional<DomainSyntaxError>> subscriber = executed
                .subscribe().withSubscriber(UniAssertSubscriber.create());

        assertAll(
                () -> subscriber.assertCompleted().assertItem(Optional.empty()),
                () -> verify(validator, times(1)).validate(any())
        );
    }

    @ValidatorTestResolver.InValidTest
    void shouldReturnDomainSyntaxErrorWhenFormulaIsInvalid(final Formula givenFormula,
                                                           final ValidatorTestResolver.GivenResponse<DomainSyntaxError> givenResponse) {
        // Given
        doReturn(givenResponse.response()).when(validator).validate(givenFormula);

        // When
        final Uni<Optional<DomainSyntaxError>> executed = validateUseCase.execute(new ValidateCommand(givenFormula));

        // Then
        final UniAssertSubscriber<Optional<DomainSyntaxError>> subscriber = executed
                .subscribe().withSubscriber(UniAssertSubscriber.create());

        assertAll(
                () -> subscriber.assertCompleted().assertItem(Optional.of(new DomainSyntaxError("invalid char at position"))),
                () -> verify(validator, times(1)).validate(any())
        );
    }

    @ValidatorTestResolver.DomainValidationExceptionTest
    void shouldFailWhenAnExceptionIsThrown(final Formula givenFormula,
                                           final ValidatorTestResolver.GivenResponse<DomainSyntaxError> givenResponse) {
        // Given
        doReturn(givenResponse.response()).when(validator).validate(givenFormula);

        // When
        final Uni<Optional<DomainSyntaxError>> executed = validateUseCase.execute(new ValidateCommand(givenFormula));

        // Then
        final UniAssertSubscriber<Optional<DomainSyntaxError>> subscriber = executed
                .subscribe().withSubscriber(UniAssertSubscriber.create());

        assertAll(
                subscriber::assertFailed,
                () -> assertThat(subscriber.getFailure()).isInstanceOf(ValidationException.class)
                        .hasRootCauseInstanceOf(IllegalStateException.class)
                        .hasRootCauseMessage("something wrong happened in infra"),
                () -> verify(validator, times(1)).validate(any())
        );
    }

    static class DomainValidatorTestResolver extends ValidatorTestResolver<DomainSyntaxError> {

        @Override
        protected DomainSyntaxError invalid() {
            return new DomainSyntaxError("invalid char at position");
        }

        @Override
        protected ValidationException validationException() {
            return new ValidationException(new IllegalStateException("something wrong happened in infra"));
        }
    }

    record DomainSyntaxError(String msg) implements SyntaxError {
        public DomainSyntaxError {
            Objects.requireNonNull(msg);
        }
    }

}
