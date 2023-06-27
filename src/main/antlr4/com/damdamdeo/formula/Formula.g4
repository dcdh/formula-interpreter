grammar Formula;

prog: expr EOF ;

expr: operations
    | comparisons
    | structured_reference
    | value
    ;


structured_reference: STRUCTURED_REFERENCE  #structuredReference
                    ;

value: VALUE #val
     ;

operations: left=operand op=operator right=operand #operationsLeftOpRight
          ;

operand: structured_reference
       | value
       ;

operator: ADD
        | SUB
        | DIV
        | MUL
        ;

comparisons: left=comparend co=comparator right=comparend #comparatorsLeftCoRight
           ;

comparend: structured_reference
          | value
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
