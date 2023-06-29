grammar Formula;

program: expr EOF ;

expr: arithmetic_functions
    | comparisons
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

comparisons: left=comparend comparator=(GT | GTE | EQ | LT | LTE) right=comparend #comparatorsLeftCoRight
           ;

comparend: argument
         ;

ADD: 'ADD' ;
SUB: 'SUB' ;
DIV: 'DIV' ;
MUL: 'MUL' ;
GT: '>' ;
GTE: '>=' ;
EQ: '=' ;
LT: '<' ;
LTE: '<=' ;
STRUCTURED_REFERENCE : '[@['[a-zA-Z0-9()% ]+']]' ;
VALUE : '"'[a-zA-Z0-9 ()%\\+.-]+'"' ;
NUMERIC : '-'?[0-9]+'.'?[0-9]*('E'[0-9]+|'E+'[0-9]+|'E-'[0-9]+)? ;
WS  : [ \t\r\n] -> skip ;
