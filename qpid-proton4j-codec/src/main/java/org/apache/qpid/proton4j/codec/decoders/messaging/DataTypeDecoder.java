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
package org.apache.qpid.proton4j.codec.decoders.messaging;

import java.io.IOException;

import org.apache.qpid.proton4j.amqp.Binary;
import org.apache.qpid.proton4j.amqp.Symbol;
import org.apache.qpid.proton4j.amqp.UnsignedLong;
import org.apache.qpid.proton4j.amqp.messaging.Data;
import org.apache.qpid.proton4j.buffer.ProtonBuffer;
import org.apache.qpid.proton4j.codec.DecoderState;
import org.apache.qpid.proton4j.codec.TypeDecoder;
import org.apache.qpid.proton4j.codec.decoders.AbstractDescribedTypeDecoder;
import org.apache.qpid.proton4j.codec.decoders.primitives.BinaryTypeDecoder;

/**
 * Decoder of AMQP Data type values from a byte stream.
 */
public class DataTypeDecoder extends AbstractDescribedTypeDecoder<Data> {

    @Override
    public Class<Data> getTypeClass() {
        return Data.class;
    }

    @Override
    public UnsignedLong getDescriptorCode() {
        return Data.DESCRIPTOR_CODE;
    }

    @Override
    public Symbol getDescriptorSymbol() {
        return Data.DESCRIPTOR_SYMBOL;
    }

    @Override
    public Data readValue(ProtonBuffer buffer, DecoderState state) throws IOException {
        TypeDecoder<?> decoder = state.getDecoder().readNextTypeDecoder(buffer, state);

        if (!(decoder instanceof BinaryTypeDecoder)) {
            throw new IOException("Expected Binary type indicator but got decoder for type: " + decoder.getClass().getSimpleName());
        }

        BinaryTypeDecoder valueDecoder = (BinaryTypeDecoder) decoder;
        Binary result = valueDecoder.readValue(buffer, state);

        return new Data(result);
    }

    @Override
    public Data[] readArrayElements(ProtonBuffer buffer, DecoderState state, int count) throws IOException {
        TypeDecoder<?> decoder = state.getDecoder().readNextTypeDecoder(buffer, state);

        if (!(decoder instanceof BinaryTypeDecoder)) {
            throw new IOException("Expected Binary type indicator but got decoder for type: " + decoder.getClass().getSimpleName());
        }

        BinaryTypeDecoder valueDecoder = (BinaryTypeDecoder) decoder;
        Binary[] binaryArray = valueDecoder.readArrayElements(buffer, state, count);

        Data[] dataArray = new Data[count];
        for (int i = 0; i < count; ++i) {
            dataArray[i] = new Data(binaryArray[i]);
        }

        return dataArray;
    }

    @Override
    public void skipValue(ProtonBuffer buffer, DecoderState state) throws IOException {
        TypeDecoder<?> decoder = state.getDecoder().readNextTypeDecoder(buffer, state);

        if (!(decoder instanceof BinaryTypeDecoder)) {
            throw new IOException("Expected Binary type indicator but got decoder for type: " + decoder.getClass().getSimpleName());
        }

        decoder.skipValue(buffer, state);
    }
}
