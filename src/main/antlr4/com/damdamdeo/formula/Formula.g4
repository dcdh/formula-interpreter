grammar Formula;

program: expr EOF ;

expr: arithmetic_functions
    | comparison_functions
    | logical_functions
    | argument
    ;

argument: STRUCTURED_REFERENCE  #argumentStructuredReference
        | VALUE #argumentValue
        | NUMERIC #argumentNumeric
        | TRUE #argumentBooleanTrue
        | FALSE #argumentBooleanFalse
        ;

arithmetic_functions: operator=(ADD | SUB | DIV | MUL)'('left=operand','right=operand')' #arithmeticFunctionsOperatorLeftOpRight
                    ;

operand: argument
       | arithmetic_functions
       ;

comparison_functions: numericalComparator=(GT | GTE | LT | LTE)'('left=comparend','right=comparend')' #comparisonFunctionsNumerical
                    | equalityComparator=(EQ | NEQ)'('left=comparend','right=comparend')' #comparisonFunctionsEquality
                    ;

comparend: argument
         | arithmetic_functions
         ;

logical_functions: logicalOperator=(AND | OR)'('left=logical_operand','right=logical_operand')' #logicalOperatorFunction
                 | IF'('comparison=if_comparison','whenTrue=when_if','whenFalse=when_if')' #ifFunction
                 | IFERROR'('comparison=if_comparison','whenTrue=when_if','whenFalse=when_if')' #ifErrorFunction
                 | isOperator=(ISNUM | ISTEXT | ISBLANK | ISLOGICAL)'('value=argument')' #isFunction
                 | ISNA'('value=argument')' #isNaFunction
                 | ISERROR'('value=argument')' #isErrorFunction
                 ;

logical_operand: argument
               | comparison_functions
               ;

if_comparison: argument
             | comparison_functions
             ;

when_if: arithmetic_functions
       | comparison_functions
       | logical_functions
       | argument
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
TRUE: 'true'|'1';
FALSE: 'false'|'0';
STRUCTURED_REFERENCE : '[@['.*?']]' ;
VALUE : '"'.*?'"' ;
NUMERIC : '-'?[0-9]+'.'?[0-9]*('E'[0-9]+|'E+'[0-9]+|'E-'[0-9]+)? ;
WS  : [ \t\r\n] -> skip ;
