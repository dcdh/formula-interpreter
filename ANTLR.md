# ANTLR

## Setup IntelliJ

Antlr is a code generator placing generated code into **target/generated-sources**. You must set up IntelliJ to recognize this folder as sources to use.

![define_antlr_target_generated_source_as_Generated_Source_Root](define_antlr_target_generated_source_as_Generated_Source_Root.png)

## Grammar

See `Formula.g4`

Sample : https://support.microsoft.com/en-gb/office/using-structured-references-with-excel-tables-f5ed2452-2337-4f71-bed3-c8ae6d2b276e

## Tips

### Tests

Each time the grammar is changed the antlr goal must be re-executed before running the tests !

### Token

**Token** = TOKEN_REF, RULE_REF, LEXER_CHAR_SET, STRING_LITERAL, BEGIN_ACTION, OPTIONS, LPAREN, RPAREN, OR, DOT, NOT
