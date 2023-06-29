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
                 ;

logical_operand: argument
               | comparison_functions
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
STRUCTURED_REFERENCE : '[@['[a-zA-Z0-9()% ]+']]' ;
VALUE : '"'[a-zA-Z0-9 ()%\\+.-]+'"' ;
NUMERIC : '-'?[0-9]+'.'?[0-9]*('E'[0-9]+|'E+'[0-9]+|'E-'[0-9]+)? ;
WS  : [ \t\r\n] -> skip ;
