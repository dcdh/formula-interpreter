grammar Formula;

prog: expr EOF ;

expr: operations
    | comparisons
    | argument
    ;

argument : STRUCTURED_REFERENCE  #argumentStructuredReference
         | VALUE #argumentValue
         ;

operations: left=operand operator=(ADD | SUB | DIV | MUL) right=operand #operationsLeftOpRight
          ;

operand: argument
       ;

comparisons: left=comparend comparator=(GT | GTE | EQ | LT | LTE) right=comparend #comparatorsLeftCoRight
           ;

comparend: argument
         ;

ADD: '+' ;
SUB: '-' ;
DIV: '/' ;
MUL: '*' ;
GT: '>' ;
GTE: '>=' ;
EQ: '=' ;
LT: '<' ;
LTE: '<=' ;
STRUCTURED_REFERENCE : '[@['[a-zA-Z0-9()€% ]+']]' ;
VALUE : [a-zA-Z0-9 ]+ ;
WS  : [ \t\r\n] -> skip ;
