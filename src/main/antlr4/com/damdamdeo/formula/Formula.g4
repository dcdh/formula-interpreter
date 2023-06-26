grammar Formula;

prog: expr EOF ;

expr: arithmetic_operations_expr
    | structured_reference_expr
    | value_expr
    ;


structured_reference_expr: STRUCTURED_REFERENCE  #structuredReferenceExpr
                         ;

value_expr: VALUE #valueExpr
          ;

arithmetic_operations_expr: add_arithmetic_operations_expr #addArithmeticOperationsExpr
                          ;

add_arithmetic_operations_expr: left=structured_reference_expr operation=operation_expr right=structured_reference_expr #structuredReferenceAddStructuredReference
                              | left=structured_reference_expr operation=operation_expr right=value_expr #structuredReferenceAddValue
                              | left=value_expr operation=operation_expr right=structured_reference_expr #valueAddStructuredReference
                              | left=value_expr operation=operation_expr right=value_expr #valueAddValue
                              ;

operation_expr: ADD ;

ADD: '+' ;
STRUCTURED_REFERENCE : '[@['[a-zA-Z0-9()€% ]+']]' ;
VALUE : [a-zA-Z0-9 ]+ ;
WS  : [ \t\r\n] -> skip ;
