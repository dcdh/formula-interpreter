grammar Formula;

prog: expr EOF ;

expr: operations_expr
    | comparators_expr
    | structured_reference_expr
    | value_expr
    ;


structured_reference_expr: STRUCTURED_REFERENCE  #structuredReferenceExpr
                         ;

value_expr: VALUE #valueExpr
          ;

operations_expr: left=structured_reference_expr operation=type_operation_expr right=structured_reference_expr #structuredReferenceOperationStructuredReference
               | left=structured_reference_expr operation=type_operation_expr right=value_expr #structuredReferenceOperationValue
               | left=value_expr operation=type_operation_expr right=structured_reference_expr #valueOperationStructuredReference
               | left=value_expr operation=type_operation_expr right=value_expr #valueOperationValue
               ;

type_operation_expr: ADD
                   | SUB
                   | DIV
                   | MUL
                   ;

comparators_expr: left=structured_reference_expr comparator=type_comparator_expr right=structured_reference_expr #structuredReferenceAddStructuredReference
                | left=structured_reference_expr comparator=type_comparator_expr right=value_expr #structuredReferenceAddValue
                | left=value_expr comparator=type_comparator_expr right=structured_reference_expr #valueAddStructuredReference
                | left=value_expr comparator=type_comparator_expr right=value_expr #valueAddValue
                ;

type_comparator_expr: GT
                    | GTE
                    ;

ADD: '+' ;
SUB: '-' ;
DIV: '/' ;
MUL: '*' ;
GT: '>' ;
GTE: '>=' ;
STRUCTURED_REFERENCE : '[@['[a-zA-Z0-9()€% ]+']]' ;
VALUE : [a-zA-Z0-9 ]+ ;
WS  : [ \t\r\n] -> skip ;
