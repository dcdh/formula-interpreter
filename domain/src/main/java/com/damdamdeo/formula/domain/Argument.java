package com.damdamdeo.formula.domain;

import org.apache.commons.lang3.Validate;

import java.util.List;
import java.util.Objects;

// NO ! not an Argument but a Value !
// TODO merge into Value
public record Argument(Kind kind, Value input, Reference reference) {
    public enum Kind {
        TEXT, NUMERIC, BOOLEAN, STRUCTURED_REFERENCE
    }

    public Argument {
        Objects.requireNonNull(kind);
        Validate.validState(!Kind.STRUCTURED_REFERENCE.equals(kind) || input == null && reference != null);
        Validate.validState(Kind.STRUCTURED_REFERENCE.equals(kind) || input != null && reference == null);
    }

    public static Argument ofText(final Value input) {
        return new Argument(Kind.TEXT, input, null);
    }

    public static Argument ofNumeric(final Value input) {
        return new Argument(Kind.NUMERIC, input, null);
    }

    public static Argument ofBoolean(final Value input) {
        return new Argument(Kind.BOOLEAN, input, null);
    }

    public static Argument ofStructuredReference(final Reference reference) {
        return new Argument(Kind.STRUCTURED_REFERENCE, null, reference);
    }

    public Value resolveArgument(final List<StructuredReference> structuredReferences) {
        return switch (kind) {
            case TEXT -> {
                Validate.validState(input.isText());
                yield input;
            }
            case NUMERIC -> {
                Validate.validState(input.isNumeric());
                yield input;
            }
            case BOOLEAN -> {
                Validate.validState(input.isBoolean());
                yield input;
            }
            case STRUCTURED_REFERENCE -> {
                // TODO have a method inside Value called ofStructuredReference(Reference reference, List<StructuredReference> structuredReferences)
                final ReferenceNaming referenceNaming = reference.toReferenceNaming();
                yield structuredReferences.stream()
                        .filter(structuredReference -> structuredReference.referenceNaming().equals(referenceNaming))
                        .map(StructuredReference::value)
                        .findFirst()
                        .orElseGet(Value::ofUnknownRef);
            }
        };
    }
}
