package com.damdamdeo.formula.domain;

import java.util.Objects;

public record InputName(String name) {
    private static final InputName INPUT_NAME_LEFT = new InputName("left");
    private static final InputName INPUT_NAME_RIGHT = new InputName("right");
    private static final InputName INPUT_NAME_COMPARISON_VALUE = new InputName("comparisonValue");
    private static final InputName INPUT_NAME_VALUE = new InputName("value");
    private static final InputName INPUT_NAME_STRUCTURED_REFERENCE = new InputName("structuredReference");

    public static InputName ofLeft() {
        return INPUT_NAME_LEFT;
    }

    public static InputName ofRight() {
        return INPUT_NAME_RIGHT;
    }

    public static InputName ofComparisonValue() {
        return INPUT_NAME_COMPARISON_VALUE;
    }

    public static InputName ofValue() {
        return INPUT_NAME_VALUE;
    }

    public static InputName ofStructuredReference() {
        return INPUT_NAME_STRUCTURED_REFERENCE;
    }

    public InputName {
        Objects.requireNonNull(name);
    }
}
