/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.qpid.proton4j.transport;

import org.apache.qpid.proton4j.amqp.Binary;

/**
 * Base class for Frames that travel through the Transport
 *
 * TODO - Creating a new frame instance for every incoming frame may
 *        become expensive in GC terms, pooling could reduce GC pressure
 *        but increase CPU time to get and return pooled frames
 */
public abstract class Frame<V> {

    private final V body;
    private final short channel;
    private final byte type;
    private final Binary payload;

    public Frame(V body, short channel, byte type, Binary payload) {
        this.body = body;
        this.channel = channel;
        this.type = type;
        this.payload = payload;
    }

    public V getBody() {
        return body;
    }

    public short getChannel() {
        return channel;
    }

    public byte getType() {
        return type;
    }

    public Binary getPayload() {
        return payload;
    }

    /**
     * @return the body contained in the frame and release the frame.
     */
    public V unwrap() {
        return body;
    }
}