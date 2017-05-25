package com.turu.hashgraphmachine.domain;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

/**
 * Created by turu on 21/05/17.
 */
public interface HashgraphEvent {
    UUID getCreatorId();

    Instant getCreationTimestamp();

    String getSelfParentHash();

    String getOtherParentHash();

    String getHash();

    List<HashgraphTransaction> getTransactions();

    long getRoundCreated();

    boolean isWitness();

    boolean isFamous();

    long getRoundReceived();

    Instant getCommitTimestamp();

    boolean isCommited();

    void updateRoundCreated(Hashgraph hashgraph, int totalNumberOfPeers);

    void decideFame(Hashgraph hashgraph);
}
