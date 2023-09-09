package com.damdamdeo.formula.infrastructure.api;

import com.damdamdeo.formula.domain.ExecutionException;
import com.damdamdeo.formula.domain.SuggestionException;
import com.damdamdeo.formula.domain.ValidationException;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.util.Objects;

@Schema(name = "ErrorMessage", required = true, requiredProperties = {"message"})
public record ErrorMessageDTO(@Schema(required = true) String message) {

    private final static String TEMPLATE =
            //language=JSON
            """
                    {
                        "message": "%s"
                    }
                    """;

    public ErrorMessageDTO(final ExecutionException executionException) {
        this(executionException.getMessage());
    }

    public ErrorMessageDTO(final ValidationException validationException) {
        this(validationException.getMessage());
    }

    public ErrorMessageDTO(final SuggestionException suggestionException) {
        this(suggestionException.getMessage());
    }

    public ErrorMessageDTO {
        Objects.requireNonNull(message);
    }

    public String toJson() {
        return TEMPLATE.formatted(message
                .replaceAll("\"", "\\\\\""));
    }
}
