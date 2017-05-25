package com.turu.hashgraphmachine.domain.peers;

import com.turu.hashgraphmachine.domain.*;
import com.turu.hashgraphmachine.domain.events.LocalEvent;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by turu on 21/05/17.
 */
public class LocalPeer implements HashgraphPeer {
    private final Hashgraph hashgraph;
    private final PeerRegistry peerRegistry;
    private final TransactionSource transactionSource;

    public LocalPeer(Hashgraph hashgraph, PeerRegistry peerRegistry, TransactionSource transactionSource) {
        this.hashgraph = hashgraph;
        this.peerRegistry = peerRegistry;
        this.transactionSource = transactionSource;
    }

    @Override
    public UUID getUuid() {
        return null;
    }

    @Override
    public synchronized void syncPeer(HashgraphPeer peer) {
        final Map<UUID, Integer> ourEventCountsByPeer = this.getNumberOfKnownEventsByPeer();
        final Map<UUID, Integer> theirEventCountsByPeer = peer.getNumberOfKnownEventsByPeer();
        final List<HashgraphEvent> eventsToSync = new LinkedList<>();
        for (Map.Entry<UUID, Integer> peerEntry : ourEventCountsByPeer.entrySet()) {
            final UUID peerId = peerEntry.getKey();
            final int ourCountsForPeer = peerEntry.getValue();
            final int theirCountsForPeer = theirEventCountsByPeer.getOrDefault(peerId, 0);
            if (ourCountsForPeer > theirCountsForPeer) {
                final int theirLagInNumberOfEventsForPeer = ourCountsForPeer - theirCountsForPeer;
                eventsToSync.addAll(
                        hashgraph.getNLatestEventsCreatedByPeer(this, theirLagInNumberOfEventsForPeer));
            }
        }
        peer.acceptSync(this, eventsToSync);
    }

    @Override
    public int getNumberOfKnownEventsCreatedByPeer(HashgraphPeer peer) {
        return hashgraph.getNumberOfEventsCreatedByPeer(peer);
    }

    @Override
    public Map<UUID, Integer> getNumberOfKnownEventsByPeer() {
        return peerRegistry.getAllPeers().stream()
                .collect(Collectors.toMap(HashgraphPeer::getUuid, hashgraph::getNumberOfEventsCreatedByPeer));
    }

    @Override
    public synchronized void acceptSync(HashgraphPeer peer, List<HashgraphEvent> eventsToSync) {
        hashgraph.addEvents(eventsToSync);
        addSyncingEventWithEmbeddedTransactions(peer);
        divideIncomingEventsIntoRounds(eventsToSync);
        decideFame();
    }

    private void addSyncingEventWithEmbeddedTransactions(HashgraphPeer peer) {
        final Optional<HashgraphEvent> ourLatestEvent = hashgraph.getLatestEventCreatedByPeer(this);
        final Optional<HashgraphEvent> theirLatestEvent = hashgraph.getLatestEventCreatedByPeer(peer);
        if (ourLatestEvent.isPresent() && theirLatestEvent.isPresent()) {
            final List<HashgraphTransaction> pendingTransactions = transactionSource.nextBatch();
            final HashgraphEvent syncAcceptedEvent = new LocalEvent(this, ourLatestEvent.get(),
                    theirLatestEvent.get(), Instant.now(), pendingTransactions);
            hashgraph.addEvent(syncAcceptedEvent);
        }
    }

    private void divideIncomingEventsIntoRounds(List<HashgraphEvent> eventsToSync) {
        for (HashgraphEvent hashgraphEvent : eventsToSync) {
            hashgraphEvent.updateRoundCreated(hashgraph, peerRegistry.getTotalNumberOfPeers());
        }
    }

    private void decideFame() {
        for (HashgraphEvent witness : hashgraph.getWitnessesInOrderOfRoundCreated()) {
            witness.decideFame(hashgraph);
        }
    }
}
