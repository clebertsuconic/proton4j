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
package org.apache.qpid.proton4j.codec.util;

import java.io.IOException;

import org.apache.qpid.proton4j.amqp.Symbol;
import org.apache.qpid.proton4j.amqp.UnsignedLong;
import org.apache.qpid.proton4j.buffer.ProtonBuffer;
import org.apache.qpid.proton4j.codec.DecoderState;
import org.apache.qpid.proton4j.codec.decoders.AbstractDescribedTypeDecoder;

public class NoLocalTypeDecoder extends AbstractDescribedTypeDecoder<NoLocalType> {

    @Override
    public Class<NoLocalType> getTypeClass() {
        return NoLocalType.class;
    }

    @Override
    public UnsignedLong getDescriptorCode() {
        return NoLocalType.DESCRIPTOR_CODE;
    }

    @Override
    public Symbol getDescriptorSymbol() {
        return NoLocalType.DESCRIPTOR_SYMBOL;
    }

    @Override
    public NoLocalType readValue(ProtonBuffer buffer, DecoderState state) throws IOException {
        state.getDecoder().readString(buffer, state);

        return NoLocalType.NO_LOCAL;
    }

    @Override
    public NoLocalType[] readArrayElements(ProtonBuffer buffer, DecoderState state, int count) throws IOException {
        NoLocalType[] array = new NoLocalType[count];
        for (int i = 0; i < count; ++i) {
            array[i] = readValue(buffer, state);
        }

        return array;
    }

    @Override
    public void skipValue(ProtonBuffer buffer, DecoderState state) throws IOException {
        state.getDecoder().readNextTypeDecoder(buffer, state).skipValue(buffer, state);;
    }
}
