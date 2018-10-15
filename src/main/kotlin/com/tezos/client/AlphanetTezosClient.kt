package com.tezos.client

import java.nio.file.Path

class AlphanetTezosClient(scriptExecutable: Path) : StandaloneTezosClient(scriptExecutable) {
    override fun clientCommandArgs(vararg args: String): List<String> {
        return listOf("client") + args.toList()
    }

    override fun filename(file: Path): String {
        return "container:$file"
    }
}