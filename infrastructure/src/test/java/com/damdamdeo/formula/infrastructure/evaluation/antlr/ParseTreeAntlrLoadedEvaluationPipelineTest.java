package com.damdamdeo.formula.infrastructure.evaluation.antlr;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class ParseTreeAntlrLoadedEvaluationPipelineTest {
    @Inject
    ParseTreeAntlrLoadedEvaluationPipeline parseTreeAntlrLoadedEvaluationPipeline;

    @Test
    void shouldLoadAntlr() {

    }

// FCK    TODO ici je dois pouvoir TOUTES les evaluer :) en passant par Antlr ... putain c'est la que la magie opere ...
//    je dois avoir une liste de formule et de son equivalent evaluer ... et bien sure je dois pouvoir la reutiliser ...
//    je dois faire deux versions une avec le debug et une sans ... je pense ... mais je tiens le bon bout !
    @Test
    void shouldEvaluate() {

    }
//TODO recuperer l'evaluation depuis le domaine ^^
}