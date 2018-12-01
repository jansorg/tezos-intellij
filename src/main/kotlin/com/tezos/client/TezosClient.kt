package com.tezos.client

import com.tezos.client.stack.MichelsonStackTransformations

/**
 * Interface to communicate with the Tezos client. It's an interface because there a different types of clients:
 * - mainnet.sh/alphanet.sh which call the client inside of a running docker container
 * - tezos-client which working with a node running on the local machine
 *
 * @author jansorg
 */
interface TezosClient {
    /**
     * Parses the given content as emacs-style output of the Tezos client. Any additional prefix or suffix must have
     * been removed from content. It is expected to start with "(" and end with ")".
     * @throws IllegalStateException
     * @throws com.tezos.client.stack.TezosClientError When the client returned an error or exited with a non-zero exit cod
     */
    fun typecheck(content: String): MichelsonStackTransformations?
}