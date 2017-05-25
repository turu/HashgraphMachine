package com.turu.hashgraphmachine.domain;

/**
 * Created by turu on 22/05/17.
 */
public interface PeerSelectionStrategy {
    HashgraphPeer nextPeer();
}
