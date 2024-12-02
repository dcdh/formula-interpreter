grammar Formula;

program: expr EOF ;

expr: arithmetic_functions
    | comparison_functions
    | logical_boolean_functions
    | logical_comparison_functions
    | state_functions
    | argument
    ;

// TODO argument => value ...
// So delete Argument and use value instead ...
argument: STRUCTURED_REFERENCE  #argumentStructuredReference
        | TEXT #argumentText
        | NUMERIC #argumentNumeric
        | TRUE #argumentBooleanTrue
        | FALSE #argumentBooleanFalse
        ;

// custom functions defined to simplify grammar
arithmetic_functions: function=(ADD | SUB | DIV | MUL)'('left=operand','right=operand')'
                    ;

operand: argument
       | arithmetic_functions
       ;

comparison_functions: function=(EQ | NEQ | GT | GTE | LT | LTE)'('left=comparend','right=comparend')'
                    ;

comparend: argument
         | arithmetic_functions
         ;

// custom functions defined to simplify grammar
logical_boolean_functions: function=(AND | OR)'('left=boolean_operand','right=boolean_operand')'
                 ;

boolean_operand: argument
               | comparison_functions
               | logical_boolean_functions
               | logical_comparison_functions
               | state_functions
               ;

logical_comparison_functions: function=(IF | IFERROR | IFNA)'('comparison=logical_comparison','whenTrue=logical_when','whenFalse=logical_when')'
                 ;

logical_comparison: argument
                  | comparison_functions
                  | logical_boolean_functions
                  | logical_comparison_functions
                  | state_functions
                  ;

logical_when: arithmetic_functions
            | comparison_functions
            | logical_boolean_functions
            | logical_comparison_functions
            | state_functions
            | argument
            ;

state_functions: function=(ISNA | ISERROR | ISNUM | ISTEXT | ISBLANK | ISLOGICAL)'('value=STRUCTURED_REFERENCE')'
               ;

ADD: 'ADD' ;
SUB: 'SUB' ;
DIV: 'DIV' ;
MUL: 'MUL' ;
GT: 'GT' ;
GTE: 'GTE' ;
EQ: 'EQ' ;
NEQ: 'NEQ' ;
LT: 'LT' ;
LTE: 'LTE' ;
AND: 'AND' ;
OR: 'OR';
IF: 'IF' ;
IFERROR: 'IFERROR' ;
ISNUM: 'ISNUM' ;
ISLOGICAL: 'ISLOGICAL' ;
ISTEXT: 'ISTEXT' ;
ISBLANK: 'ISBLANK' ;
ISNA: 'ISNA' ;
ISERROR: 'ISERROR' ;
IFNA: 'IFNA' ;
TRUE: 'true'|'1';
FALSE: 'false'|'0';
STRUCTURED_REFERENCE : '[@['.*?']]' ;
TEXT : '"'.*?'"' ;
NUMERIC : '-'?[0-9]+'.'?[0-9]*('E'[0-9]+|'E+'[0-9]+|'E-'[0-9]+)? ;
WS  : [ \t\r\n] -> skip ;
