grammar Formula;

prog: expr EOF ;

expr: STRUCTURED_REFERENCE #structuredReferenceExpr
    | VALUE #valueExpr
    ;

STRUCTURED_REFERENCE : '[@['[a-zA-Z0-9()€% ]+']]' ;
VALUE : [a-zA-Z0-9 ]+ ;
WS  : [ \t\r\n] -> skip ;
