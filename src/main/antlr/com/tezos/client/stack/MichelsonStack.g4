// Define a grammar called Hello
grammar MichelsonStack;
all: '(' types errors ')' EOF ;
types: '(' 'types' '.' '(' stackTransformation* ')' ')' ;
errors: '(' 'errors' '.' '(' error* ')' ')' ;

stackTransformation: '(' instructionStart=OFFSET instructionEnd=OFFSET stackBefore=stack stackAfter=stack ')';
stack: '(' stackFrame* ')';
stackFrame: '(' type ')';

type: simpleType | nestedType ;
simpleType: typename=TYPE ANNOTATION* ;
nestedType:   '(' typename=TYPE ANNOTATION* type* ANNOTATION* ')'
            | ANNOTATION+ type ;

error: '(' startOffset=OFFSET endOffset=OFFSET message=STRING ')';

OFFSET: [0-9]+ ;
ANNOTATION: [%@:]([@%] |'%%' | [_a-zA-Z][_0-9a-zA-Z.]*)? ;
TYPE: [a-z_]+ ;
LEFT_PAR: '(';
RIGHT_PAR: ')';
STRING : '"' ( '\\"' | . )*? '"' ;
WS : [ \t\r\n]+ -> skip ; // skip spaces, tabs, newlines
