// Define a grammar called Hello
grammar MichelsonStack;
all: '(' types errors ')' EOF ;
types: '(' 'types' '.' '(' stackTransformation* ')' ')' ;
errors: '(' 'errors' '.' '(' error* ')' ')' ;

stackTransformation: '(' startOffset=OFFSET endOffset=OFFSET stackBefore=stack stackAfter=stack ')';
stack: '(' frames=stackFrame* ')';
stackFrame: '(' type ')';

type: simpleType | nestedType ;
simpleType: typename=TYPE annotations=ANNOTATION* ;
nestedType:   '(' typename=TYPE annotations=ANNOTATION* arguments=type* annotations=ANNOTATION* ')'
            | annotations=ANNOTATION+ arguments=type ;

error: '(' startOffset=OFFSET endOffset=OFFSET message=STRING ')';

OFFSET: [0-9]+ ;
ANNOTATION: [%@:]([@%] |'%%' | [_a-zA-Z][_0-9a-zA-Z.]*)? ;
TYPE: [a-z_]+ ;
LEFT_PAR: '(';
RIGHT_PAR: ')';
STRING : '"' ( '\\"' | . )*? '"' ;
WS : [ \t\r\n]+ -> skip ; // skip spaces, tabs, newlines
