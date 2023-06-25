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

add_arithmetic_operations_expr: structured_reference_expr ADD structured_reference_expr #structuredReferenceAddStructuredReference
                              | structured_reference_expr ADD value_expr #structuredReferenceAddValue
                              | value_expr ADD structured_reference_expr #valueAddStructuredReference
                              | value_expr ADD value_expr #valueAddValue
                              ;

ADD: '+' ;
STRUCTURED_REFERENCE : '[@['[a-zA-Z0-9()€% ]+']]' ;
VALUE : [a-zA-Z0-9 ]+ ;
WS  : [ \t\r\n] -> skip ;
