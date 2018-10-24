# Version 0.7.0
- Add option to settings to autodetect the client on $PATH (tezos-client, mainnet.sh and alphanet.sh)
- Add button to copy the currently selected client configuration to the settings panel

# Version 0.6.1
- Include all changes scheduled for 0.6.0

# Version 0.6.0
- Improved parsing of nested tags
- Feature: stack visualization for Michelson files
- Feature: settings to configure the appearance of the stack visualization 
- Feature: settings to configure the available Tezos clients. It supports `tezos-client` and docker scripts (`alphanet.sh`, `mainnet.sh`, etc.)

# Version 0.5.0
- Improved error recovery and error handling in the parser
- Improved code cmpletion for instructions
- Improved code cmpletion for type names
- Improved code cmpletion for tag names

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
