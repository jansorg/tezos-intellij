# Version 1.0.0
- Feature: Smart completion of instructions. Only those instructions which are compatible with the current stack are suggested.
- Feature: Intentions to wrap a type in parentheses, e.g. "int" to "(int)"
- Feature: Intentions to unwrap a type, e.g. "(int)" to "int"
- Feature: Error highlighting of trailing annotations on instructions and types
- Feature: Intention to move trailing annotation to the right place
- Feature: Intention to remove a unsupported or unexpected annotation, e.g. to remove a field annotation which isn't supported by the current instruction
- Feature: Smart completion of types and tags where an argument of an instruction is expected. Only the values which are valid at the position of the argument are suggested.
- Feature: Warn about missing, superfluous, and unsupported arguments to a type, e.g. `pair int`, `pair int int int` or `map unit int`
- Don't suggest section names inside of sections or types
- Fix `contract` live template
- Treat `*.tez` and `*.tez` as Michelson files (same as in Emacs).

# Version 0.10.0
- Feature: Run configurations for Michelson files. You will be asked for the input values by default.
- Show an error instead of failing silently when no tezos client is configured
- Fix exception when "Find Action..." was invoked
- Update stack info when a file is opened
- Update stack info when a file's editor is selected
- Make colored stack output the default, remove toggle to simplify ui
- Show annotations in stack info by default

# Version 0.9.0
- Feature: Support parameter info `View > Parameter Info` for instructions, types and tags
- Fix wrong indentation after pressing enter in the code section
- Don't suggest instructions at top level of a file
- Don't suggest instructions inside of comments
- Show all simple types when completing after instructions, not just the comparable types
- Show stack visualization when the caret is on the code keyword
- Always show stack visualization when the caret in on an annotation, this is a limitation of the Tezos client
- Don't suppress updates to stack visualization if more than one document update was triggered at a time, e.g. at startup
- Fixes to the formatter

# Version 0.8.0
- Feature: Support `View > Quick documentation` for instructions, types and tags.
- Feature: Improvements to stack visualization: colored output, rendering nested types on multiple lines with indentation
- Feature: Suggest dynamic macro names which are available for the current stack, needs a running tezos-client
- Feature: Show the type of the new top of the stack next to code completion items, only available for macros for now
- Improve handling of `tezos-client` and `alphanet.sh`
- Remove obsolete section `RETURN` from the grammar
- Remove obsolete instruction `REDUCE` from the grammar

# Version 0.7.0
- Client settings: add button to autodetect the client on $PATH (tezos-client, mainnet.sh and alphanet.sh)
- Client settings: add button to copy the currently selected client configuration
- Improved layout of the stack visualization panel

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
