package com.turu.hashgraphmachine.domain;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by turu on 21/05/17.
 */
public interface HashgraphPeer {
    UUID getUuid();
    void syncPeer(HashgraphPeer peer);
    void acceptSync(HashgraphPeer peer, List<HashgraphEvent> eventsToSync);
    int getNumberOfKnownEventsCreatedByPeer(HashgraphPeer peer);
    Map<UUID, Integer> getNumberOfKnownEventsByPeer();
}
