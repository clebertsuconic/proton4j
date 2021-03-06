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
package org.apache.qpid.proton4j.codec;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.apache.qpid.proton4j.amqp.Symbol;
import org.apache.qpid.proton4j.buffer.ProtonBuffer;
import org.apache.qpid.proton4j.buffer.ProtonByteBufferAllocator;
import org.junit.Test;

public class SymbolTypeCodecTest extends CodecTestSupport {

    private final String SMALL_SYMBOL_VALUIE = "Small String";
    private final String LARGE_SYMBOL_VALUIE = "Large String: " +
        "The quick brown fox jumps over the lazy dog. " +
        "The quick brown fox jumps over the lazy dog. " +
        "The quick brown fox jumps over the lazy dog. " +
        "The quick brown fox jumps over the lazy dog. " +
        "The quick brown fox jumps over the lazy dog. " +
        "The quick brown fox jumps over the lazy dog. " +
        "The quick brown fox jumps over the lazy dog. " +
        "The quick brown fox jumps over the lazy dog.";

    @Test
    public void testEncodeSmallSymbol() throws IOException {
        doTestEncodeDecode(Symbol.valueOf(SMALL_SYMBOL_VALUIE));
    }

    @Test
    public void testEncodeLargeSymbol() throws IOException {
        doTestEncodeDecode(Symbol.valueOf(LARGE_SYMBOL_VALUIE));
    }

    @Test
    public void testEncodeEmptySymbol() throws IOException {
        doTestEncodeDecode(Symbol.valueOf(""));
    }

    @Test
    public void testEncodeNullSymbol() throws IOException {
        doTestEncodeDecode(null);
    }

    private void doTestEncodeDecode(Symbol value) throws IOException {
        ProtonBuffer buffer = ProtonByteBufferAllocator.DEFAULT.allocate();

        encoder.writeSymbol(buffer, encoderState, value);

        final Object result = decoder.readSymbol(buffer, decoderState);

        if (value != null) {
            assertNotNull(result);
            assertTrue(result instanceof Symbol);
        } else {
            assertNull(result);
        }

        assertEquals(value, result);
    }

    @Test
    public void testDecodeSmallSeriesOfSymbols() throws IOException {
        doTestDecodeSymbolSeries(SMALL_SIZE);
    }

    @Test
    public void testDecodeLargeSeriesOfSymbols() throws IOException {
        doTestDecodeSymbolSeries(LARGE_SIZE);
    }

    private void doTestDecodeSymbolSeries(int size) throws IOException {
        ProtonBuffer buffer = ProtonByteBufferAllocator.DEFAULT.allocate();

        for (int i = 0; i < size; ++i) {
            encoder.writeSymbol(buffer, encoderState, Symbol.valueOf(LARGE_SYMBOL_VALUIE));
        }

        for (int i = 0; i < size; ++i) {
            final Object result = decoder.readSymbol(buffer, decoderState);

            assertNotNull(result);
            assertTrue(result instanceof Symbol);
            assertEquals(LARGE_SYMBOL_VALUIE, result.toString());
        }
    }

    @Test
    public void testDecodeSmallSymbolArray() throws IOException {
        doTestDecodeSymbolArrayType(SMALL_ARRAY_SIZE);
    }

    @Test
    public void testDecodeLargeSymbolArray() throws IOException {
        doTestDecodeSymbolArrayType(LARGE_ARRAY_SIZE);
    }

    private void doTestDecodeSymbolArrayType(int size) throws IOException {
        ProtonBuffer buffer = ProtonByteBufferAllocator.DEFAULT.allocate();

        Symbol[] source = new Symbol[size];
        for (int i = 0; i < size; ++i) {
            source[i] = Symbol.valueOf("test->" + i);
        }

        encoder.writeArray(buffer, encoderState, source);

        Object result = decoder.readObject(buffer, decoderState);
        assertNotNull(result);
        assertTrue(result.getClass().isArray());

        Symbol[] array = (Symbol[]) result;
        assertEquals(size, array.length);

        for (int i = 0; i < size; ++i) {
            assertEquals(source[i], array[i]);
        }
    }
}
