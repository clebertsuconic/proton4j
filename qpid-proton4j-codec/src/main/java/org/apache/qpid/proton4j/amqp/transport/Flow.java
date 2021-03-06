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
package org.apache.qpid.proton4j.amqp.transport;

import java.util.Map;

import org.apache.qpid.proton4j.amqp.Binary;
import org.apache.qpid.proton4j.amqp.Symbol;
import org.apache.qpid.proton4j.amqp.UnsignedLong;

public final class Flow implements Performative {

    public static final UnsignedLong DESCRIPTOR_CODE = UnsignedLong.valueOf(0x0000000000000013L);
    public static final Symbol DESCRIPTOR_SYMBOL = Symbol.valueOf("amqp:flow:list");

    private static final long UINT_MAX = 0xFFFFFFFFL;

    private static int NEXT_INCOMING_ID = 1;
    private static int INCOMING_WINDOW = 2;
    private static int NEXT_OUTGOING_ID = 4;
    private static int OUTGOING_WINDOW = 8;
    private static int HANDLE = 16;
    private static int DELIVERY_COUNT = 32;
    private static int LINK_CREDIT = 64;
    private static int AVAILABLE = 128;
    private static int DRAIN = 256;
    private static int ECHO = 512;
    private static int PROPERTIES = 1024;

    private int modified = 0;

    private long nextIncomingId;
    private long incomingWindow;
    private long nextOutgoingId;
    private long outgoingWindow;
    private long handle;
    private long deliveryCount;
    private long linkCredit;
    private long available;
    private boolean drain;
    private boolean echo;
    private Map<Object, Object> properties;

    //----- Query the state of the Header object -----------------------------//

    public boolean isEmpty() {
        return modified == 0;
    }

    public int getElementCount() {
        return 32 - Integer.numberOfLeadingZeros(modified);
    }

    public boolean hasNextIncomingId() {
        return (modified & NEXT_INCOMING_ID) == NEXT_INCOMING_ID;
    }

    public boolean hasIncomingWindow() {
        return (modified & INCOMING_WINDOW) == INCOMING_WINDOW;
    }

    public boolean hasNextOutgoingId() {
        return (modified & NEXT_OUTGOING_ID) == NEXT_OUTGOING_ID;
    }

    public boolean hasOutgoingWindow() {
        return (modified & OUTGOING_WINDOW) == OUTGOING_WINDOW;
    }

    public boolean hasHandle() {
        return (modified & HANDLE) == HANDLE;
    }

    public boolean hasDeliveryCount() {
        return (modified & DELIVERY_COUNT) == DELIVERY_COUNT;
    }

    public boolean hasLinkCredit() {
        return (modified & LINK_CREDIT) == LINK_CREDIT;
    }

    public boolean hasAvailable() {
        return (modified & AVAILABLE) == AVAILABLE;
    }

    public boolean hasDrain() {
        return (modified & DRAIN) == DRAIN;
    }

    public boolean hasEcho() {
        return (modified & ECHO) == ECHO;
    }

    public boolean hasProperties() {
        return (modified & PROPERTIES) == PROPERTIES;
    }

    //----- Access the AMQP Transfer object ------------------------------------//

    public long getNextIncomingId() {
        return nextIncomingId;
    }

    public void setNextIncomingId(long nextIncomingId) {
        if (nextIncomingId < 0 || nextIncomingId > UINT_MAX) {
            throw new IllegalArgumentException("Next Incoming Id value given is out of range: " + nextIncomingId);
        } else if (nextIncomingId == 0) {
            modified &= ~NEXT_INCOMING_ID;
        } else {
            modified |= NEXT_INCOMING_ID;
        }

        this.nextIncomingId = nextIncomingId;
    }

    public long getIncomingWindow() {
        return incomingWindow;
    }

    public void setIncomingWindow(long incomingWindow) {
        if (incomingWindow < 0 || incomingWindow > UINT_MAX) {
            throw new IllegalArgumentException("Incoming Window value given is out of range: " + incomingWindow);
        } else if (incomingWindow == 0) {
            modified &= ~INCOMING_WINDOW;
        } else {
            modified |= INCOMING_WINDOW;
        }

        this.incomingWindow = incomingWindow;
    }

    public long getNextOutgoingId() {
        return nextOutgoingId;
    }

    public void setNextOutgoingId(long nextOutgoingId) {
        if (nextOutgoingId < 0 || nextOutgoingId > UINT_MAX) {
            throw new IllegalArgumentException("Next Outgoing Id value given is out of range: " + nextOutgoingId);
        } else if (nextOutgoingId == 0) {
            modified &= ~NEXT_OUTGOING_ID;
        } else {
            modified |= NEXT_OUTGOING_ID;
        }

        this.nextOutgoingId = nextOutgoingId;
    }

    public long getOutgoingWindow() {
        return outgoingWindow;
    }

    public void setOutgoingWindow(long outgoingWindow) {
        if (outgoingWindow < 0 || outgoingWindow > UINT_MAX) {
            throw new IllegalArgumentException("Outgoing Window value given is out of range: " + outgoingWindow);
        } else if (outgoingWindow == 0) {
            modified &= ~OUTGOING_WINDOW;
        } else {
            modified |= OUTGOING_WINDOW;
        }

        this.outgoingWindow = outgoingWindow;
    }

    public long getHandle() {
        return handle;
    }

    public void setHandle(long handle) {
        if (handle < 0 || handle > UINT_MAX) {
            throw new IllegalArgumentException("Handle value given is out of range: " + handle);
        } else if (handle == 0) {
            modified &= ~HANDLE;
        } else {
            modified |= HANDLE;
        }

        this.handle = handle;
    }

    public long getDeliveryCount() {
        return deliveryCount;
    }

    public void setDeliveryCount(long deliveryCount) {
        if (deliveryCount < 0 || deliveryCount > UINT_MAX) {
            throw new IllegalArgumentException("Delivery Count value given is out of range: " + deliveryCount);
        } else if (deliveryCount == 0) {
            modified &= ~DELIVERY_COUNT;
        } else {
            modified |= DELIVERY_COUNT;
        }

        this.deliveryCount = deliveryCount;
    }

    public long getLinkCredit() {
        return linkCredit;
    }

    public void setLinkCredit(long linkCredit) {
        if (linkCredit < 0 || linkCredit > UINT_MAX) {
            throw new IllegalArgumentException("Link Credit value given is out of range: " + linkCredit);
        } else if (linkCredit == 0) {
            modified &= ~LINK_CREDIT;
        } else {
            modified |= LINK_CREDIT;
        }

        this.linkCredit = linkCredit;
    }

    public long getAvailable() {
        return available;
    }

    public void setAvailable(long available) {
        if (available < 0 || available > UINT_MAX) {
            throw new IllegalArgumentException("Available value given is out of range: " + available);
        } else if (available == 0) {
            modified &= ~AVAILABLE;
        } else {
            modified |= AVAILABLE;
        }

        this.available = available;
    }

    public boolean getDrain() {
        return drain;
    }

    public void setDrain(boolean drain) {
        if (drain) {
            modified |= DRAIN;
        } else {
            modified &= ~DRAIN;
        }

        this.drain = drain;
    }

    public boolean getEcho() {
        return echo;
    }

    public void setEcho(boolean echo) {
        if (echo) {
            modified |= ECHO;
        } else {
            modified &= ~ECHO;
        }

        this.echo = echo;
    }

    public Map<Object, Object> getProperties() {
        return properties;
    }

    public void setProperties(Map<Object, Object> properties) {
        if (properties != null) {
            modified |= PROPERTIES;
        } else {
            modified &= ~PROPERTIES;
        }

        this.properties = properties;
    }

    @Override
    public PerformativeType getPerformativeType() {
        return PerformativeType.FLOW;
    }

    @Override
    public <E> void invoke(PerformativeHandler<E> handler, Binary payload, E context) {
        handler.handleFlow(this, payload, context);
    }

    @Override
    public String toString() {
        return "Flow{" +
               "nextIncomingId=" + nextIncomingId +
               ", incomingWindow=" + incomingWindow +
               ", nextOutgoingId=" + nextOutgoingId +
               ", outgoingWindow=" + outgoingWindow +
               ", handle=" + handle +
               ", deliveryCount=" + deliveryCount +
               ", linkCredit=" + linkCredit +
               ", available=" + available +
               ", drain=" + drain +
               ", echo=" + echo +
               ", properties=" + properties +
               '}';
    }
}
