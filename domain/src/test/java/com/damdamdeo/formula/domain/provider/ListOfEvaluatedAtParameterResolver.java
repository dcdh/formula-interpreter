package com.damdamdeo.formula.domain.provider;

import com.damdamdeo.formula.domain.ProcessedAt;
import org.apache.commons.lang3.Validate;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;

public final class ListOfEvaluatedAtParameterResolver implements ParameterResolver {
    public static final ListOfEvaluatedAt LIST_OF_EXECUTED_ATS = new ListOfEvaluatedAt(
            List.of(
                    new ProcessedAt(ZonedDateTime.parse("2023-12-25T10:15:00+01:00[Europe/Paris]")),
                    new ProcessedAt(ZonedDateTime.parse("2023-12-25T10:15:01+01:00[Europe/Paris]")),
                    new ProcessedAt(ZonedDateTime.parse("2023-12-25T10:15:02+01:00[Europe/Paris]")),
                    new ProcessedAt(ZonedDateTime.parse("2023-12-25T10:15:03+01:00[Europe/Paris]")),
                    new ProcessedAt(ZonedDateTime.parse("2023-12-25T10:15:04+01:00[Europe/Paris]")),
                    new ProcessedAt(ZonedDateTime.parse("2023-12-25T10:15:05+01:00[Europe/Paris]")),
                    new ProcessedAt(ZonedDateTime.parse("2023-12-25T10:15:06+01:00[Europe/Paris]")),
                    new ProcessedAt(ZonedDateTime.parse("2023-12-25T10:15:07+01:00[Europe/Paris]")),
                    new ProcessedAt(ZonedDateTime.parse("2023-12-25T10:15:08+01:00[Europe/Paris]")),
                    new ProcessedAt(ZonedDateTime.parse("2023-12-25T10:15:09+01:00[Europe/Paris]")),
                    new ProcessedAt(ZonedDateTime.parse("2023-12-25T10:15:10+01:00[Europe/Paris]")),
                    new ProcessedAt(ZonedDateTime.parse("2023-12-25T10:15:11+01:00[Europe/Paris]")),
                    new ProcessedAt(ZonedDateTime.parse("2023-12-25T10:15:12+01:00[Europe/Paris]")),
                    new ProcessedAt(ZonedDateTime.parse("2023-12-25T10:15:13+01:00[Europe/Paris]")),
                    new ProcessedAt(ZonedDateTime.parse("2023-12-25T10:15:14+01:00[Europe/Paris]")),
                    new ProcessedAt(ZonedDateTime.parse("2023-12-25T10:15:15+01:00[Europe/Paris]")),
                    new ProcessedAt(ZonedDateTime.parse("2023-12-25T10:15:16+01:00[Europe/Paris]")),
                    new ProcessedAt(ZonedDateTime.parse("2023-12-25T10:15:17+01:00[Europe/Paris]")),
                    new ProcessedAt(ZonedDateTime.parse("2023-12-25T10:15:18+01:00[Europe/Paris]")),
                    new ProcessedAt(ZonedDateTime.parse("2023-12-25T10:15:19+01:00[Europe/Paris]")),
                    new ProcessedAt(ZonedDateTime.parse("2023-12-25T10:15:20+01:00[Europe/Paris]")),
                    new ProcessedAt(ZonedDateTime.parse("2023-12-25T10:15:21+01:00[Europe/Paris]"))));

    @Override
    public boolean supportsParameter(final ParameterContext parameterContext,
                                     final ExtensionContext extensionContext) throws ParameterResolutionException {
        return ListOfEvaluatedAt.class.equals(parameterContext.getParameter().getType());
    }

    @Override
    public Object resolveParameter(final ParameterContext parameterContext,
                                   final ExtensionContext extensionContext) throws ParameterResolutionException {
        if (ListOfEvaluatedAt.class.equals(parameterContext.getParameter().getType())) {
            return LIST_OF_EXECUTED_ATS;
        } else {
            throw new IllegalStateException("Should not be here: unsupported parameter type");
        }
    }

    public record ListOfEvaluatedAt(List<ProcessedAt> listOf) {
        public ListOfEvaluatedAt {
            Objects.requireNonNull(listOf);
            Validate.isTrue(!listOf.isEmpty());
        }

        public ProcessedAt first() {
            return listOf.get(0);
        }

        public ProcessedAt[] next() {
            return listOf.subList(1, listOf.size()).toArray(new ProcessedAt[0]);
        }
    }

}
