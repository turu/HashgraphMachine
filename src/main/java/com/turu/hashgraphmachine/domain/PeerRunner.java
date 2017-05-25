package com.turu.hashgraphmachine.domain;

import java.util.List;

/**
 * Created by turu on 21/05/17.
 */
public class PeerRunner implements Runnable {
    private final HashgraphPeer localNode;
    private final Hashgraph hashgraph;
    private final PeerSelectionStrategy peerSelectionStrategy;
    private final TransactionSource transactionSource;

    public PeerRunner(HashgraphPeer localNode, Hashgraph hashgraph, PeerSelectionStrategy peerSelectionStrategy, TransactionSource transactionSource) {
        this.localNode = localNode;
        this.hashgraph = hashgraph;
        this.peerSelectionStrategy = peerSelectionStrategy;
        this.transactionSource = transactionSource;
    }

    public void advance() {
        final HashgraphPeer peer = peerSelectionStrategy.nextPeer();
        localNode.syncPeer(peer);
    }

    @Override
    public void run() {
        hashgraph.getLatestEventCreatedByPeer(localNode);
        while (!Thread.currentThread().isInterrupted()) {
            advance();
        }
    }
}
