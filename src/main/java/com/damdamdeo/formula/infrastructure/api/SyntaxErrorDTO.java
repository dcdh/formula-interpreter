package com.damdamdeo.formula.infrastructure.api;

import com.damdamdeo.formula.infrastructure.antlr.AntlrSyntaxError;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(name = "SyntaxError", required = true, requiredProperties = {"line", "charPositionInLine", "msg"})
public record SyntaxErrorDTO(@Schema(required = true) Integer line,
                             @Schema(required = true) Integer charPositionInLine,
                             @Schema(required = true) String msg) {
    public SyntaxErrorDTO(final AntlrSyntaxError antlrSyntaxError) {
        this(antlrSyntaxError.line(),
                antlrSyntaxError.charPositionInLine(),
                antlrSyntaxError.msg());
    }
}
