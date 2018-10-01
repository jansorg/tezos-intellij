# Version 0.5.0
- Code cmpletion for tag names

# Version 0.4.0
- Live template `contract` to insert a new, empty contract
- Indentation in empty blocks
- Parser: Handle map literals and list literals
- Parser: Handle list literals and empty lists / blocks
- Code completion for sections (basic and smart)
- Code completion for instructions
- Code completion for type names

# Version 0.3.0
- Formatter: new option "leading space in line comment". Adds a leading space if there's no space yet betwee # and the comment. Keeps existing spaces.
- Formatter: new option to align end-of-line comments inside of instruction blocks, disabled by default
- Formatter: new option to align annotations insode of complex types, disabled by default
- Brace matcher to highlight pairs of () and {}
- Structure view for Michelson files
- Folding builder for Michelson files

# Version 0.2.0
- Highlighting errors in macro names and macro arguments
  (DUP, DIP, PAIR, UNPAIR, CADR, SET_CADR, MAP_CADR and variants)
- Basic formatting of Michelson files

# Version 0.1.0
- Initial release
- Michelson parsing and error highlighting
- Michelson syntax highlighting
- New file template for Michelson
