grammar Formula;

prog: expr EOF ;

expr: operations
    | comparators
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

comparators: left=structured_reference comparator=type_comparator right=structured_reference #structuredReferenceAddStructuredReference
           | left=structured_reference comparator=type_comparator right=value #structuredReferenceAddValue
           | left=value comparator=type_comparator right=structured_reference #valueAddStructuredReference
           | left=value comparator=type_comparator right=value #valueAddValue
           ;

type_comparator: GT
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
