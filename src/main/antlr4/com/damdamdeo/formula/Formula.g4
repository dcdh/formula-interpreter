grammar Formula;

prog: expr EOF ;

expr: VALUE #valueExpr
    ;

VALUE : [a-zA-Z0-9 ]+ ;
WS  : [ \t\r\n] -> skip ;
