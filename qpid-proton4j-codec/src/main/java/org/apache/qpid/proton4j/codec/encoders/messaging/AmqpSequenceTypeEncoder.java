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
package org.apache.qpid.proton4j.codec.encoders.messaging;

import java.util.List;

import org.apache.qpid.proton4j.amqp.Symbol;
import org.apache.qpid.proton4j.amqp.UnsignedLong;
import org.apache.qpid.proton4j.amqp.messaging.AmqpSequence;
import org.apache.qpid.proton4j.buffer.ProtonBuffer;
import org.apache.qpid.proton4j.codec.EncoderState;
import org.apache.qpid.proton4j.codec.EncodingCodes;
import org.apache.qpid.proton4j.codec.TypeEncoder;
import org.apache.qpid.proton4j.codec.encoders.AbstractDescribedTypeEncoder;

/**
 * Encoder of AMQP AmqpSequence type values to a byte stream.
 */
public class AmqpSequenceTypeEncoder extends AbstractDescribedTypeEncoder<AmqpSequence> {

    @Override
    public Class<AmqpSequence> getTypeClass() {
        return AmqpSequence.class;
    }

    @Override
    public UnsignedLong getDescriptorCode() {
        return AmqpSequence.DESCRIPTOR_CODE;
    }

    @Override
    public Symbol getDescriptorSymbol() {
        return AmqpSequence.DESCRIPTOR_SYMBOL;
    }

    @Override
    public void writeType(ProtonBuffer buffer, EncoderState state, AmqpSequence value) {
        buffer.writeByte(EncodingCodes.DESCRIBED_TYPE_INDICATOR);
        state.getEncoder().writeUnsignedLong(buffer, state, getDescriptorCode());
        state.getEncoder().writeList(buffer, state, value.getValue());
    }

    @Override
    public void writeArray(ProtonBuffer buffer, EncoderState state, Object[] values) {
        // Write the Array Type encoding code, we don't optimize here.
        buffer.writeByte(EncodingCodes.ARRAY32);

        int startIndex = buffer.getWriteIndex();

        // Reserve space for the size and write the count of list elements.
        buffer.writeInt(0);
        buffer.writeInt(values.length);

        writeRawArray(buffer, state, values);

        // Move back and write the size
        int endIndex = buffer.getWriteIndex();
        long writeSize = endIndex - startIndex - Integer.BYTES;

        if (writeSize > Integer.MAX_VALUE) {
            throw new IllegalArgumentException("Cannot encode given array, encoded size to large: " + writeSize);
        }

        buffer.setInt(startIndex, (int) writeSize);
    }

    @SuppressWarnings("rawtypes")
    @Override
    public void writeRawArray(ProtonBuffer buffer, EncoderState state, Object[] values) {
        buffer.writeByte(EncodingCodes.DESCRIBED_TYPE_INDICATOR);
        state.getEncoder().writeUnsignedLong(buffer, state, getDescriptorCode());

        List[] elements = new List[values.length];

        for (int i = 0; i < values.length; ++i) {
            AmqpSequence sequence = (AmqpSequence) values[i];
            elements[i] = sequence.getValue();
        }

        TypeEncoder<?> entryEncoder = state.getEncoder().getTypeEncoder(List.class);
        entryEncoder.writeArray(buffer, state, elements);
    }
}
