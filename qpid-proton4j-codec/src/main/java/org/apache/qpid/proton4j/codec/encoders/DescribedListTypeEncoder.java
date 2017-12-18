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
package org.apache.qpid.proton4j.codec.encoders;

import org.apache.qpid.proton4j.buffer.ProtonBuffer;
import org.apache.qpid.proton4j.codec.EncoderState;
import org.apache.qpid.proton4j.codec.EncodingCodes;

/**
 * Base class used for all Described Type objects that are represented as a List
 *
 * @param <V> the type that is being encoded
 */
public interface DescribedListTypeEncoder<V> extends DescribedTypeEncoder<V> {

    /**
     * Determine the list type the given value can be encoded to based on the number
     * of bytes that would be needed to hold the encoded form of the resulting list
     * entries.
     * <p>
     * Most encoders will return LIST32 but for cases where the type is known to
     * be encoded to LIST8 or always encodes an empty list (LIST0) the encoder can
     * optimize the encode step and not compute sizes.
     *
     * @param value
     *      The value that is to be encoded.
     *
     * @return the encoding code of the list type encoding needed for this object.
     */
    default int getListEncoding(V value) {
        return EncodingCodes.LIST32 & 0xff;
    }

    /**
     * Instructs the encoder to write the element identified with the given index
     *
     * @param source
     *      the source of the list elements to write
     * @param index
     *      the element index that needs to be written
     * @param buffer
     *      the buffer to write the element to
     * @param state
     *      the current EncoderState value to use.
     */
    void writeElement(V source, int index, ProtonBuffer buffer, EncoderState state);

    /**
     * Gets the number of elements that will result when this type is encoded
     * into an AMQP List type.
     *
     * @param value
     *      the value which will be encoded as a list type.
     *
     * @return the number of elements that should comprise the encoded list.
     */
    int getElementCount(V value);

    @Override
    default void writeType(ProtonBuffer buffer, EncoderState state, V value) {
        buffer.writeByte(EncodingCodes.DESCRIBED_TYPE_INDICATOR);
        state.getEncoder().writeUnsignedLong(buffer, state, getDescriptorCode());

        int count = getElementCount(value);
        int encodingCode = getListEncoding(value);

        // Optimized step, no other data to be written.
        if (count == 0 || encodingCode == EncodingCodes.LIST0) {
            buffer.writeByte(EncodingCodes.LIST0);
            return;
        }

        final int fieldWidth;

        if (encodingCode == EncodingCodes.LIST8) {
            fieldWidth = 1;
            buffer.writeByte(EncodingCodes.LIST8);
        } else {
            fieldWidth = 4;
            buffer.writeByte(EncodingCodes.LIST32);
        }

        int startIndex = buffer.getWriteIndex();

        // Reserve space for the size and write the count of list elements.
        if (fieldWidth == 1) {
            buffer.writeByte((byte) 0);
            buffer.writeByte((byte) count);
        } else {
            buffer.writeInt(0);
            buffer.writeInt(count);
        }

        // Write the list elements and then compute total size written.
        for (int i = 0; i < count; ++i) {
            writeElement(value, i, buffer, state);
        }

        // Move back and write the size
        int endIndex = buffer.getWriteIndex();
        int writeSize = endIndex - startIndex - fieldWidth;

        if (fieldWidth == 1) {
            buffer.setByte(startIndex, writeSize);
        } else {
            buffer.setInt(startIndex, writeSize);
        }
    }

    @Override
    default void writeArray(ProtonBuffer buffer, EncoderState state, Object[] values) {
        // Write the Array Type encoding code, we don't optimize here.
        buffer.writeByte(EncodingCodes.ARRAY32);

        int startIndex = buffer.getWriteIndex();

        // Reserve space for the size and write the count of list elements.
        buffer.writeInt(0);
        buffer.writeInt(values.length);

        buffer.writeByte(EncodingCodes.DESCRIBED_TYPE_INDICATOR);
        state.getEncoder().writeUnsignedLong(buffer, state, getDescriptorCode());

        writeRawArray(buffer, state, values);

        // Move back and write the size
        long writeSize = buffer.getWriteIndex() - startIndex - Integer.BYTES;

        if (writeSize > Integer.MAX_VALUE) {
            throw new IllegalArgumentException("Cannot encode given array, encoded size to large: " + writeSize);
        }

        buffer.setInt(startIndex, (int) writeSize);
    }

    @SuppressWarnings("unchecked")
    @Override
    default void writeRawArray(ProtonBuffer buffer, EncoderState state, Object[] values) {
        buffer.writeByte(EncodingCodes.LIST32);

        for (int i = 0; i < values.length; ++i) {
            V listType = (V) values[i];

            int count = getElementCount(listType);

            int elementStartIndex = buffer.getWriteIndex();

            // Reserve space for the size and write the count of list elements.
            buffer.writeInt(0);
            buffer.writeInt(count);

            // Write the list elements and then compute total size written.
            for (int j = 0; j < count; ++j) {
                writeElement(listType, j, buffer, state);
            }

            // Move back and write the size
            int listWriteSize = buffer.getWriteIndex() - elementStartIndex - Integer.BYTES;

            buffer.setInt(elementStartIndex, listWriteSize);
        }
    }
}