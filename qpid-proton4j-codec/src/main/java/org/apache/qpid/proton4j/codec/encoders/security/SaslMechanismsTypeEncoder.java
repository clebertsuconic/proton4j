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
package org.apache.qpid.proton4j.codec.encoders.security;

import org.apache.qpid.proton4j.amqp.Symbol;
import org.apache.qpid.proton4j.amqp.UnsignedLong;
import org.apache.qpid.proton4j.amqp.security.SaslMechanisms;
import org.apache.qpid.proton4j.buffer.ProtonBuffer;
import org.apache.qpid.proton4j.codec.EncoderState;
import org.apache.qpid.proton4j.codec.encoders.AbstractDescribedListTypeEncoder;

/**
 * Encoder of AMQP SaslMechanisms type values to a byte stream
 */
public class SaslMechanismsTypeEncoder extends AbstractDescribedListTypeEncoder<SaslMechanisms> {

    @Override
    public Class<SaslMechanisms> getTypeClass() {
        return SaslMechanisms.class;
    }

    @Override
    public UnsignedLong getDescriptorCode() {
        return SaslMechanisms.DESCRIPTOR_CODE;
    }

    @Override
    public Symbol getDescriptorSymbol() {
        return SaslMechanisms.DESCRIPTOR_SYMBOL;
    }

    @Override
    public void writeElement(SaslMechanisms mechanisms, int index, ProtonBuffer buffer, EncoderState state) {
        switch (index) {
            case 0:
                state.getEncoder().writeArray(buffer, state, mechanisms.getSaslServerMechanisms());
                break;
            default:
                throw new IllegalArgumentException("Unknown SaslChallenge value index: " + index);
        }
    }

    @Override
    public int getElementCount(SaslMechanisms challenge) {
        return 1;
    }
}
