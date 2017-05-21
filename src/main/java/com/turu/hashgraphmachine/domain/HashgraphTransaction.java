package com.turu.hashgraphmachine.domain;

import java.nio.ByteBuffer;

/**
 * Created by turu on 21/05/17.
 */
public interface HashgraphTransaction {
    ByteBuffer getPayloadBytes();
}
