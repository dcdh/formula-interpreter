package com.damdamdeo.formula.domain.provider;

import com.damdamdeo.formula.domain.Reference;
import com.damdamdeo.formula.domain.ReferenceNaming;
import com.damdamdeo.formula.domain.StructuredReference;
import com.damdamdeo.formula.domain.Value;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public final class StructuredReferenceProviders {

    public enum State {
        RESOLVED, UNKNOWN;

        public static boolean matchTag(final String tag) {
            for (final State state : State.values()) {
                if (state.name().equals(tag)) {
                    return true;
                }
            }
            return false;
        }
    }

    private final Map<State, List<GivenStructuredReference>> STRUCTURED_REFERENCE_BY_STATE = new HashMap<>();

    public static List<GivenStructuredReference> byState(final State state) {
        return new StructuredReferenceProviders().STRUCTURED_REFERENCE_BY_STATE.get(state);
    }

    public StructuredReferenceProviders() {
        final Reference reference = new Reference("[@[% Commission]]");
        STRUCTURED_REFERENCE_BY_STATE.put(
                State.RESOLVED,
                List.of(
                        new GivenStructuredReference(
                                reference,
                                List.of(
                                        new StructuredReference(
                                                new ReferenceNaming("% Commission"),
                                                Value.ofNumeric("0.10")))))
        );
        STRUCTURED_REFERENCE_BY_STATE.put(
                State.UNKNOWN,
                List.of(
                        new GivenStructuredReference(
                                reference,
                                List.of()))
        );
    }

    public record GivenStructuredReference(Reference reference, List<StructuredReference> structuredReferences) {
        public GivenStructuredReference {
            Objects.requireNonNull(reference);
            Objects.requireNonNull(structuredReferences);
        }
    }
}
