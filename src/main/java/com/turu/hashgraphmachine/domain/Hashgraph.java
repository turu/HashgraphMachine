package com.turu.hashgraphmachine.domain;

import java.util.List;
import java.util.Optional;

/**
 * Created by turu on 21/05/17.
 */
public interface Hashgraph {
    void addEvent(HashgraphEvent event);

    void addEvents(List<HashgraphEvent> events);

    Optional<HashgraphEvent> getLatestEventCreatedByPeer(HashgraphPeer hashgraphPeer);

    List<HashgraphEvent> getNLatestEventsCreatedByPeer(HashgraphPeer hashgraphPeer, int numberOfEvents);

    int getNumberOfEventsCreatedByPeer(HashgraphPeer peer);

    List<HashgraphEvent> getWitnessesInOrderOfRoundCreated();

    List<HashgraphEvent> getWitnessesInOrderOfRoundCreated(long startingRound);

    List<HashgraphEvent> getWitnessesFromRound(long roundNumber);

    boolean canSee(HashgraphEvent from, HashgraphEvent to);

    int getNumberOfPeersOnPaths(HashgraphEvent from, HashgraphEvent to);
}
