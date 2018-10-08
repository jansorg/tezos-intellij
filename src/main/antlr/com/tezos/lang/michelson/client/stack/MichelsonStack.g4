// Define a grammar called Hello
grammar MichelsonStack;
all: '(' types errors ')' EOF ;
types: '(' 'types' '.' '(' stackTransformation* ')' ')' ;
errors: '(' 'errors' '.' ')' ;

stackTransformation: '(' startOffset=OFFSET endOffset=OFFSET stackBefore=stack stackAfter=stack ')';
stack: '(' frames=stackFrame* ')';
stackFrame: '(' type ')';

type: simpleType | nestedType ;
simpleType: typename=TYPE annotations=ANNOTATION* ;
nestedType: '(' typename=TYPE annotations=ANNOTATION* arguments=type* annotations=ANNOTATION* ')';

OFFSET: [0-9]+ ;
ANNOTATION: [@:%][a-z]+ ;
TYPE: [a-z]+ ;
LEFT_PAR: '(';
RIGHT_PAR: ')';
WS : [ \t\r\n]+ -> skip ; // skip spaces, tabs, newlines
