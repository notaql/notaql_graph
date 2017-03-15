grammar Import;

Whitespace : [ \t\r\n]+ -> skip ;
OUT: 'OUT.';
IN: 'IN.';
EQ: '=';
NEQ: '!=';
LT: '<';
LTEQ: '<=';
GT: '>';
GTEQ: '>=';
AND: '&&';
OR: '||';
PLUS: '+';
MINUS: '-';
DIV: '/';
MULT: '*';
OUTGOING: '_outgoing';
INCOMING: '_incoming';
LABEL: '_l';
ARROW: '<-';
TRUE: 'true';
FALSE: 'false';
Int: [0-9]+;
Float: [0-9]+ '.' [0-9]+;

Name: [a-zA-Z0-9_]+;
String: '\'' ~('\r' | '\n' | '\'')* '\'';




transformation: inEngine ','
                outEngine ','
                  (any* ',')?
                                 (  attributeSpecificatione (',' attributeSpecificatione)*)?
                ;

attributeSpecificatione
    : OUT '$' any*                      #aSdollar
      |'OUT._e_?('predicate ')' ARROW edgeConstructor          #asEdge
      | OUT any*                #asNormal
    ;

edgeConstructor
    :'EDGE(' direction ',' edgeConstructorLabel (',' edgeConstructorAttribute)* ')'     #edgeConstructorFunction
    ;

edgeConstructorLabel
    : LABEL ARROW edgeConstructorvData              #edgeConstructorLabelLabel
    ;

edgeConstructorAttribute
    : Name ARROW edgeConstructorvData               #edgeConstructorAttributeLabel
    ;

edgeConstructorvData
    : atom                          #eCatomVData
    | '(' edgeConstructorvData ')'                 #eCbracedVData
    | edgeConstructorvData op=(MULT|DIV|PLUS|MINUS) edgeConstructorvData     #eCmultiplicatedVData
    | IN inputData*              #eCrelativeInputPathVData
;

predicate
    : '(' predicate ')'                                                                      #bracedPredicate
    | '!(' predicate')'                                                                       #negatedPredicate
    | left=predicate AND right=predicate                                                      #andPredicate
    | left=predicate OR right=predicate                                                       #orPredicate
    | left=vDataPredicate op=(LT | LTEQ | GT | GTEQ | EQ | NEQ) right=vDataPredicate          #relationalPredicate
    | relativeInputPathExistence                                                                        #relativePathExistencePredicate
    ;

vDataPredicate
    : atom                                                          #atomVDataP
    | '(' vDataPredicate ')'                                        #bracedVDataP
    | vDataPredicate op=(MULT|DIV|PLUS|MINUS) vDataPredicate                   #operationVDataP
    | IN inputData*                                          #InputDataVDataP
    | inputData*                                            #InputDataZielKnoten
;

direction
    :OUTGOING           #directionOutgoing
    |INCOMING           #directionIncoming
    ;

relativeInputPathExistence
    : inputData*
    ;


any:

IN
|EQ
|NEQ
|LT
|LTEQ
|GT
|GTEQ
|AND
|OR
|PLUS
|MINUS
|DIV
|MULT
|OUTGOING
|INCOMING
|LABEL
|ARROW

   | '['
    |']'
    |'@'
    | '?'
    |':'
    |'('
    |')'
    |'.'
    | '\''
    | '$'
   | Name
   | String
   | Float
   | Int
   | FALSE
   | TRUE
   | IN
   | '*'
;

inputData
   : Name
    |'['
    |']'
    |'@'
    |'.'
    |'('
    |')'
    |'*'
    |'?'
    |'<'
    |'>'
    |'='
    |'!'
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
