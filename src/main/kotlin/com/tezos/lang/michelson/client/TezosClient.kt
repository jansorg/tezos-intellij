package com.tezos.lang.michelson.client

import java.nio.file.Path

/**
 * Interface to communicate with the Tezos client. It's an interface because there a different ways of the client:
 * - alphanet.sh which calls the client insode of a running docker container
 * - tezos-client which is directly called
 * @author jansorg
 */
interface TezosClient {
    /**
     * Calls the typechecking functionality using the Emacs output mode.
     */
    fun typecheckScript(file: Path): String?
}