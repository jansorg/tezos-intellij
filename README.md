# IntelliJ support for the Tezos plaform

IntelliJ support for the Tezos platform.
This plugin adds support of technologies and languages used by the
Tezos platform to the IntelliJ products.

<img src="docs/mainwindow.png"/>

## Reporting issues and whishes
Please use GitHub's issues to report any issue your uncover. Please free to
suggest features and possible extensions there as well.

### Logfile
If you would like to submit the logfile, then:
- enable logging of the Tezos plugin by adding the line `#tezos` to the content of action at `Help > Debug Log Settings`
- attach the `idea.log` file shown by `Help > Show Log in ...`. Make sure to remove any private information from the file before uploading it.

## Compatibility

This plugin is compatible with versions 2020.2 (builds 202.x) and later.
This includes:
- IntelliJ
- PyCharm
- PhpStorm
- RubyMine

# Documentation

The documentation is available at [www.plugin-dev.com/project/tezos-michelson/](https://www.plugin-dev.com/project/tezos-michelson/).
Please submit an issue here if there's something missing.

# License
This software is licensed under the BSD-3-clause license, see LICENSE.txt for details.

This repository contains script files at `src/test/data/contracts/tezos-repo/`
which were obtained at https://gitlab.com/tezos/tezos and which are under the MIT license.

# Links
- [Tezos](https://tezos.com/)
- [Tezos foundation](http://tezosfoundation.ch/)
- [Michelson language whitepaper](http://tezos.gitlab.io/betanet/whitedoc/michelson.html)

# Development links
- [The original parser](https://gitlab.com/tezos/tezos/blob/master/src/proto_alpha/lib_protocol/src/script_ir_translator.ml)