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
package org.apache.qpid.proton4j.codec.encoders.transactions;

import org.apache.qpid.proton4j.amqp.Symbol;
import org.apache.qpid.proton4j.amqp.UnsignedLong;
import org.apache.qpid.proton4j.amqp.transactions.Declared;
import org.apache.qpid.proton4j.codec.DescribedListTypeEncoder;
import org.apache.qpid.proton4j.codec.EncoderState;

import io.netty.buffer.ByteBuf;

/**
 * Encoder of AMQP Declared type values to a byte stream.
 */
public class DeclaredTypeEncoder implements DescribedListTypeEncoder<Declared> {

    @Override
    public UnsignedLong getDescriptorCode() {
        return Declared.DESCRIPTOR_CODE;
    }

    @Override
    public Symbol getDescriptorSymbol() {
        return Declared.DESCRIPTOR_SYMBOL;
    }

    @Override
    public Class<Declared> getTypeClass() {
        return Declared.class;
    }

    @Override
    public void writeElement(Declared declared, int index, ByteBuf buffer, EncoderState state) {
        switch (index) {
            case 0:
                state.getEncoder().writeBinary(buffer, state, declared.getTxnId());
                break;
            default:
                throw new IllegalArgumentException("Unknown Declared value index: " + index);
        }
    }

    @Override
    public int getElementCount(Declared declared) {
        return 1;
    }
}