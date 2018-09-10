# IntelliJ support for the Tezos plaform

IntelliJ support for the Tezos platform.
This plugin adds support of technologies and languages used by the
Tezos platform to the IntelliJ products.

<img src="docs/mainwindow.png"/>

** Please beware that this is a very early release. A lot of features will be added in the future. **

## Reporting issues and whishes
Please use GitHub's issues to report any issue your uncover. Please free to
suggest features and possible extensions there as well.

## Compatibility

This plugin is compatible with versions 2016.1 (builds 145.x) and later.
This includes:
- IntelliJ
- PyCharm
- PhpStorm
- RubyMine

## Michelson

Michelson is a programing language used to write smart contracts.
See [the whitepaper](http://tezos.gitlab.io/betanet/whitedoc/michelson.html#) for all the details.

### File types
This plugins maps `*.tz` to the new Michelson file type.

### File templates

You can create a new file using the built-in functionality:

<img src="docs/newfile.png">

There are basic templates included to create an empty contract:

<img src="docs/newfiledialog.png">

### Syntax highlighting

Syntax errors are highlighted:
- illegal escape characters used in strings
- unknown instruction
- unexpected arguments passed to instructions
- unexpected number of code blocks passed to instructions, e.g. `IF {} {} {}` or `IF {}`
- comparable type not used where expected
- unknown types
- unknown data values
- unexpected annotations, i.e. highlighting where an annotation wasn't expected
- unexpected number of annotations (type, variable and field annotations)

All syntax elements are configurable in the editor's color scheme settings.
Macro and instructions are highlighted differently.

<img src="docs/colorsettings.png"/>

# License
This software is licensed under the BSD 3-clause license, see LICENSE.txt for details.

This repository contains script files at `src/test/data/contracts/tezos-repo/`
which were obtained at https://gitlab.com/tezos/tezos and which are under the MIT license.

# Links
- [Tezos](https://tezos.com/)
- [Tezos foundation](http://tezosfoundation.ch/)
- [Michelson language whitepaper](http://tezos.gitlab.io/betanet/whitedoc/michelson.html)