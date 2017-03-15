

grammar Nota;

Whitespace : [ \t\r\n]+ -> skip ;
TRUE: 'true';
FALSE: 'false';
Int: [0-9]+;
Float: [0-9]+ '.' [0-9]+;
String: '\'' ~('\r' | '\n' | '\'')* '\'';
Name: [a-zA-Z0-9_]+;



notaql: (transformation  ';')* transformation ';'?  EOF;

transformation: inEngine ','
                 outEngine ','
                 any*
                 ;

any:
    '['
    |']'
    |'@'
    | '<-'
    | '&'
    | '|'
    | '?'
    | '='
    | '<'
    |'>'
    |'!'
    |'+'
    |'*'
    |'/'
    |':'
    |'%'
    |'-'
    |'('
    |')'
    |','
    |'.'
   | '\''
   | '$'
   | Name
   | String
   | Float
   | Int
   | FALSE
   | TRUE
;

inEngine: 'IN-ENGINE:' engine;
outEngine: 'OUT-ENGINE:' engine;

engine: engineName=Name '(' (Name '<-' atom (',' Name '<-' atom)*)? ')';

atom
  : (Int | Float)     #numberAtom
  | String            #stringAtom
  | TRUE              #trueAtom
  | FALSE             #falseAtom
  ;

