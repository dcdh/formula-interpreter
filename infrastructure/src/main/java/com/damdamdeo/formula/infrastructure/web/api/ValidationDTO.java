package com.damdamdeo.formula.infrastructure.web.api;

import com.damdamdeo.formula.infrastructure.parser.antlr.AntlrSyntaxError;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(name = "Validation", required = true, requiredProperties = {"valid"})
public record ValidationDTO(@Schema(required = true) Boolean valid,
                            Integer line,
                            Integer charPositionInLine,
                            String msg) {
    public ValidationDTO() {
        this(Boolean.TRUE, null, null, null);
    }

    public ValidationDTO(final AntlrSyntaxError antlrSyntaxError) {
        this(Boolean.FALSE,
                antlrSyntaxError.line(),
                antlrSyntaxError.charPositionInLine(),
                antlrSyntaxError.msg());
    }
}
