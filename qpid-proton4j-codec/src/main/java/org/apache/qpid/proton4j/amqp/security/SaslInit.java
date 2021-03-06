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
package org.apache.qpid.proton4j.amqp.security;

import org.apache.qpid.proton4j.amqp.Binary;
import org.apache.qpid.proton4j.amqp.Symbol;
import org.apache.qpid.proton4j.amqp.UnsignedLong;

public final class SaslInit implements SaslPerformative {

    public static final UnsignedLong DESCRIPTOR_CODE = UnsignedLong.valueOf(0x0000000000000041L);
    public static final Symbol DESCRIPTOR_SYMBOL = Symbol.valueOf("amqp:sasl-init:list");

    private Symbol mechanism;
    private Binary initialResponse;
    private String hostname;

    public Symbol getMechanism() {
        return mechanism;
    }

    public void setMechanism(Symbol mechanism) {
        if (mechanism == null) {
            throw new NullPointerException("the mechanism field is mandatory");
        }

        this.mechanism = mechanism;
    }

    public Binary getInitialResponse() {
        return initialResponse;
    }

    public void setInitialResponse(Binary initialResponse) {
        this.initialResponse = initialResponse;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    @Override
    public String toString() {
        return "SaslInit{" +
               "mechanism=" + mechanism +
               ", initialResponse=" + initialResponse +
               ", hostname='" + hostname + '\'' + '}';
    }

    @Override
    public SaslPerformativeType getPerformativeType() {
        return SaslPerformativeType.INIT;
    }

    @Override
    public <E> void invoke(SaslPerformativeHandler<E> handler, E context) {
        handler.handleInit(this, context);
    }
}
