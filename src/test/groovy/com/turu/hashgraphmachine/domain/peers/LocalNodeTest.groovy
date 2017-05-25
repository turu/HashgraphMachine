package com.turu.hashgraphmachine.domain.peers

import com.turu.hashgraphmachine.domain.HashgraphEvent
import spock.lang.Specification

/**
 * Created by turu on 21/05/17.
 */
class LocalNodeTest extends Specification {
    LocalPeer localNode

    def setup() {
        localNode = new LocalPeer(hashgraph, nodeRegistry, transactionSource)
    }

    def "acceptEvent should not fail given HashgraphEvent"() {
        given:
        def event = Mock(HashgraphEvent)

        when:
        localNode.acceptEvent(event)

        then:
        1 == 1
    }
}
