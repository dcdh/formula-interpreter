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

operations: left=structured_reference operation=type_operation right=structured_reference #structuredReferenceOperationStructuredReference
          | left=structured_reference operation=type_operation right=value #structuredReferenceOperationValue
          | left=value operation=type_operation right=structured_reference #valueOperationStructuredReference
          | left=value operation=type_operation right=value #valueOperationValue
          ;

type_operation: ADD
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
