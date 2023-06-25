package com.damdamdeo.formula.result;

import java.util.List;

public record ExecutionResult(Result result, List<MatchedToken> matchedTokens) {
}
