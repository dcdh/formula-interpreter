grammar Formula;

prog: expr EOF ;

expr: operations
    | comparisons
    | argument
    ;

argument : STRUCTURED_REFERENCE  #argumentStructuredReference
         | VALUE #argumentValue
         ;

operations: left=operand op=operator right=operand #operationsLeftOpRight
          ;

operand: argument
       ;

operator: ADD
        | SUB
        | DIV
        | MUL
        ;

comparisons: left=comparend co=comparator right=comparend #comparatorsLeftCoRight
           ;

comparend: argument
         ;

comparator: GT
          | GTE
          | EQ
          | LT
          | LTE
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
