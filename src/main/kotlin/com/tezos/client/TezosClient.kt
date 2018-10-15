package com.tezos.client

import com.tezos.client.stack.MichelsonStackTransformations
import java.nio.file.Path

/**
 * Interface to communicate with the Tezos client. It's an interface because there a different ways of the client:
 * - alphanet.sh which calls the client insode of a running docker container
 * - tezos-client which is directly called
 *
 * @author jansorg
 */
interface TezosClient {
    /**
     * Calls the typechecking functionality using the Emacs output mode.
     */
    fun typecheckOutput(file: Path): String?

    fun typecheck(file: Path): MichelsonStackTransformations?
}