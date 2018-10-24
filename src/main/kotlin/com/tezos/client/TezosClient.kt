package com.tezos.client

import com.tezos.client.stack.MichelsonStackTransformations

/**
 * Interface to communicate with the Tezos client. It's an interface because there a different ways of the client:
 * - alphanet.sh which calls the client insode of a running docker container
 * - tezos-client which is directly called
 *
 * @author jansorg
 */
interface TezosClient {
    /**
     * Parses the given content as emacs-style output of the Tezos client. Any additional prefix or suffix must have
     * been removed from content. It is expected to start with "(" and end with ")".
     */
    fun typecheck(content: String): MichelsonStackTransformations?
}