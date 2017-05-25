package com.turu.hashgraphmachine.domain.events;

import com.turu.hashgraphmachine.domain.*;

import java.time.Instant;
import java.util.*;

/**
 * Created by turu on 21/05/17.
 */
public class LocalEvent implements HashgraphEvent {
    private final UUID creatorId;
    private final HashgraphEvent selfParent;
    private final HashgraphEvent otherParent;
    private final Instant creationTimestamp;
    private final List<HashgraphTransaction> transactions = new LinkedList<>();

    private boolean isWitness = false;
    private boolean isFamous = false;
    private boolean isCommited = false;
    private Instant commitTimestamp = Instant.MAX;
    private long roundCreated = 0;
    private long roundReceived = Long.MAX_VALUE;

    public LocalEvent(HashgraphPeer creator, HashgraphEvent selfParent, HashgraphEvent otherParent,
                      Instant creationTimestamp, List<HashgraphTransaction> transactions) {
        this.creatorId = creator.getUuid();
        this.selfParent = selfParent;
        this.otherParent = otherParent;
        this.creationTimestamp = creationTimestamp;
        this.transactions.addAll(transactions);
    }

    @Override
    public UUID getCreatorId() {
        return creatorId;
    }

    @Override
    public Instant getCreationTimestamp() {
        return creationTimestamp;
    }

    @Override
    public String getSelfParentHash() {
        return selfParent.getHash();
    }

    @Override
    public String getOtherParentHash() {
        return otherParent.getHash();
    }

    @Override
    public String getHash() {
        return null;
    }

    @Override
    public List<HashgraphTransaction> getTransactions() {
        return Collections.unmodifiableList(transactions);
    }

    @Override
    public long getRoundCreated() {
        return roundCreated;
    }

    @Override
    public boolean isWitness() {
        return isWitness;
    }

    @Override
    public boolean isFamous() {
        return isFamous;
    }

    @Override
    public long getRoundReceived() {
        return roundReceived;
    }

    @Override
    public Instant getCommitTimestamp() {
        return commitTimestamp;
    }

    @Override
    public boolean isCommited() {
        return isCommited;
    }

    @Override
    public void updateRoundCreated(Hashgraph hashgraph, int totalNumberOfPeers) {
        long maxRoundOfParents = Math.max(roundCreatedOfParent(selfParent), roundCreatedOfParent(otherParent));
        int majorityNeededToStartNextRound = 2 * totalNumberOfPeers / 3;
        int stronglySeenWitnesses = 0;
        for (HashgraphEvent witness : hashgraph.getWitnessesFromRound(maxRoundOfParents)) {
            if (canStronglySee(witness, hashgraph, totalNumberOfPeers)) {
                stronglySeenWitnesses++;
            }
        }
        if (stronglySeenWitnesses > majorityNeededToStartNextRound) {
            roundCreated = maxRoundOfParents + 1;
        } else {
            roundCreated = maxRoundOfParents;
        }
        isWitness = selfParent == null || roundCreated > selfParent.getRoundCreated();
    }

    private boolean canStronglySee(HashgraphEvent witness, Hashgraph hashgraph, int totalNumberOfPeers) {
        final int superMajority = 2 * totalNumberOfPeers / 3;
        return hashgraph.canSee(this, witness) && hashgraph.getNumberOfPeersOnPaths(this, witness) > superMajority;
    }

    private long roundCreatedOfParent(HashgraphEvent parent) {
        if (parent != null) {
            return parent.getRoundCreated();
        } else {
            return 1;
        }
    }

    @Override
    public void decideFame(Hashgraph hashgraph) {
    }
}
