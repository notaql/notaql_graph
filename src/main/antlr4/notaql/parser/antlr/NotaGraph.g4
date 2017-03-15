

grammar NotaGraph;
/**
First of all, ANTLR's lexer will tokenize the input from top to bottom. So tokens defined first have a higher precedence than the ones below it. And in case rule have overlapping tokens, the rule that matches the most characters will take precedence (greedy match).
*/

Whitespace : [ \t\r\n]+ -> skip ;
IN: 'IN.';
OUT: 'OUT.';

ARROW: '<-';

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

TRUE: 'true';
FALSE: 'false';

Int: [0-9]+;
Float: [0-9]+ '.' [0-9]+;

/**
~ => not operator ==> alles drin, mit ' umrandet
*/
String: '\'' ~('\r' | '\n' | '\'')* '\'';

AVG: 'AVG';
COUNT: 'COUNT';
MAX: 'MAX';
MIN: 'MIN';
SUM: 'SUM';

AVGE: 'avg';
COUNTE: 'count';
MAXE: 'max';
MINE: 'min';
SUME: 'sum';
LISTE: 'list';
PATHLENGTHE: 'pathlength';



NODEID: '_id';
OUTGOING: '_outgoing';
INCOMING: '_incoming';

EDGE: '_e';
EDGETONODE: '_e_.';
LABEL: '_l';

Name: [a-zA-Z0-9_]+;

OPEN: '(';
CLOSE: ')';

PREDICATESYMBOL: '?';

DOT: '.';
COLON: ':';



/**
############################################ NotaQLGraph.Parser Rules
The complete notaql script consists of multiple transformations
*/
notaql: (transformation  ';')* transformation ';'? EOF;

/**
A transformation consists of input and output engine,
optional input and output filters, and some attribute specifications
*/
transformation: inEngine ','
                outEngine ','
                (repeat ',')?
                (inPredicate ',')?
              (  attributeSpecification (',' attributeSpecification)*)?;

/**
Input and output engines are composed by the key word IN-ENGINE/OUT-ENGINE followed by a keyword attribute list
*/
inEngine: 'IN-ENGINE:' engine;
outEngine: 'OUT-ENGINE:' engine;

engine: engineName=Name '(' (Name '<-' atom (',' Name '<-' atom)*)? ')';

repeat
    :'REPEAT:'wiederholung=Int
    |unendlich='REPEAT:-1'
    |'REPEAT:'Name '('wert=Float'%)'
    |'REPEAT:'Name '('wert=Int'%)'
    ;

inPredicate
    : 'IN-FILTER' ':' pre=predicate
    ;

/**
Predicates let the user define simple boolean expressions depending on arbitrarily complex vData. */
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
    | vDataPredicate op=(MULT|DIV) vDataPredicate                   #operationVDataP
    | vDataPredicate op=(PLUS|MINUS) vDataPredicate                 #operation2VDataP
    | inputDataPredicate                                            #InputDataVDataP
    | IN inputDataPredicate                                         #InputDataVDataPEdgeDestination
    ;

inputDataPredicate
         : Name                                                                         #nameIDP
         | NODEID                                                                       #nodeIDIDP
         | function=(SUME | AVGE | MINE | MAXE | COUNTE ) '(' edgevData ')'             #aggregatedEdgeIDP
         | edgevData                                                                    #edgeIDP
         ;


relativeInputPathExistence
    : Name                                                      #rIPNAME
    | edgevData                              #rIPEDGE
    ;

edgePredicate
    : '(' edgePredicate ')'                                                                      #bracedEPredicate
    | '!(' edgePredicate')'                                                                       #negatedEPredicate
    | left=edgePredicate AND right=edgePredicate                                                      #andEPredicate
    | left=edgePredicate OR right=edgePredicate                                                       #orEPredicate
    | left=edgevDataPredicate op=(LT | LTEQ | GT | GTEQ | EQ | NEQ) right=edgevDataPredicate          #relationalEPredicate
    | edgeInputPathExistence                                                                        #relativePathExistenceEPredicate
    ;


edgevDataPredicate
    : atom                                                       #atomVDataEP
    | '(' vDataPredicate ')'                                     #bracedVDataEP
    | edgevDataPredicate op=(MULT|DIV) edgevDataPredicate     #operationVDataEP
    | edgevDataPredicate op=(PLUS|MINUS) edgevDataPredicate     #operation2VDataEP
    | edgeInputData                                          #InputDataVDataEP
    ;

edgeInputData
    : LABEL                                                 #eidLabel
    | NODEID                                                #eidEdgeID
    | Name                                                  #eidName
    ;

edgeInputPathExistence
	: direction             #eipeDirection
	 | Name                  #eipeName
	 | String				#eipeLabel

	;



/**
    Represents an atomic value (Int, Float, String, Boolean) all wrapped in apostrophes */
atom
    : (Int | Float)     #numberAtom
    | String            #stringAtom
    | TRUE              #trueAtom
    | FALSE             #falseAtom
    ;



/**
An attribute specification consistes of an output path and an arbitrary vData expression
*/
attributeSpecification
    : OUT '$(IN._k?' pre=dollarPredicate? ')' ARROW data=vData          #DollarSpecification
    | 'OUT.$(IN._k)' ARROW data=vData          #DollarSpecificationN
    | OUT outpath ARROW vData                                           #NodeSpecification
    |'OUT._e_?('predicate ')' ARROW edgeConstructor                     #EdgeSpecification
    ;

outpath
    : NODEID                    #NodeIdOutPath
    | Name                      #NameOutPath
    ;


/**
vData makes complex expressions possible. They allow an arbitrary nesting level. */
vData
    : atom                          #atomVData
    | '(' vData ')'                 #bracedVData
    | vData op=(MULT|DIV) vData     #multiplicatedVData
    | vData op=(PLUS|MINUS) vData   #addedVData
    | IN inputData              #relativeInputPathVData
    | function=(SUM | AVG | MIN | MAX | COUNT ) '('vData?')'         #aggregateVData
    | function=(SUME | AVGE | MINE | MAXE | COUNTE | LISTE | PATHLENGTHE) '(' IN edgevData ')'         #aggregateEdgeVData
    | IN edgevData                                    #aggregateEdgeVDataNoFunction
    ;

inputData
         : '_v'                                                                   #valueID
         | NODEID                                                                  #nodeIDID
         | Name                                                                   #nameID
         ;

edgevData
 :'_e?('edgePredicate').' edgeInputData         #edgeIDeP
 |'_e.'edgeInputData                            #edgeID
 |'_e_.' inputData                              #nextNodeFinish
 |'_e?('edgePredicate')_.' inputData            #nextNodeFinisheP
 |'_e_?('predicate').' inputData                #nextNodeFinishnP
 |'_e?('edgePredicate')_?('predicate').' inputData   #nextNodeFinishePnP
 |EDGETONODE edgevData                              #nextNode
 |'_e?('edgePredicate')_.' edgevData            #nextNodeeP
 |'_e_?('predicate').' edgevData                #nextNodenP
 |'_e?('edgePredicate')_?('predicate').' edgevData   #nextNodeePnP
 |'_e_[' range '].' inputData                   #nextNodeLoop
 |'_e?('edgePredicate')_[' range '].' inputData            #nextNodeLoopeP
 |'_e_?('predicate')[' range '].' inputData                #nextNodeLoopnP
 |'_e?('edgePredicate')_?('predicate')[' range '].' inputData   #nextNodeLoopePnP
 ;

range
    :Int                #rangeExact
    |Int ',' Int        #rangeLoop
    |Int ', *'          #rangeInfinity
    ;

direction
    :OUTGOING           #directionOutgoing
    |INCOMING           #directionIncoming
    ;




/**
Dollarpredicates */

dollarPredicate
    : '(' dollarPredicate ')'                                                                      #bracedDPredicate
    | '!(' dollarPredicate')'                                                                       #negatedDPredicate
    | left=dollarPredicate AND right=dollarPredicate                                                      #andDPredicate
    | left=dollarPredicate OR right=dollarPredicate                                                       #orDPredicate
    | left=vDataDollarPredicate op=(LT | LTEQ | GT | GTEQ | EQ | NEQ) right=vDataDollarPredicate          #relationalDPredicate
    | existenceDollarPredicate                                                                   #existenceDPredicate
    ;

vDataDollarPredicate
    : atom                                                                   #atomVDataDP
    | '(' vDataDollarPredicate ')'                                           #bracedVDataDP
    | vDataDollarPredicate op=(MULT|DIV) vDataDollarPredicate                 #operationVDataDP
    | vDataDollarPredicate op=(PLUS|MINUS) vDataDollarPredicate                #operation2VDataDP
    | '@'                                                                      #aTVDataDP
    | inputDataPredicate                                                       #InputDataVDataDP
    ;



existenceDollarPredicate
    : Name                          #eDPName
    ;



/**
Create Edge */

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
    | '(' vData ')'                 #eCbracedVData
    | edgeConstructorvData op=(MULT|DIV) edgeConstructorvData     #eCmultiplicatedVData
    | edgeConstructorvData op=(PLUS|MINUS) edgeConstructorvData   #eCaddedVData
    | IN inputData              #eCrelativeInputPathVData
    | function=(SUME | AVGE | MINE | MAXE | COUNTE ) '(' IN edgevData ')'         #eCaggregateEdgeVData
    | IN edgevData                                    #eCaggregateEdgeVDataNoFunction
    | IN '_e[@].' edgeInputData                       #eCATEdgeInputvData
    ;

