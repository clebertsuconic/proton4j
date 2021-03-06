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
package org.apache.qpid.proton4j.codec.encoders.primitives;

import java.util.Date;

import org.apache.qpid.proton4j.buffer.ProtonBuffer;
import org.apache.qpid.proton4j.codec.EncoderState;
import org.apache.qpid.proton4j.codec.EncodingCodes;
import org.apache.qpid.proton4j.codec.encoders.AbstractPrimitiveTypeEncoder;

/**
 * Encoder of AMQP Timestamp type values to a byte stream.
 */
public class TimestampTypeEncoder extends AbstractPrimitiveTypeEncoder<Date> {

    @Override
    public Class<Date> getTypeClass() {
        return Date.class;
    }

    @Override
    public void writeType(ProtonBuffer buffer, EncoderState state, Date value) {
        buffer.writeByte(EncodingCodes.TIMESTAMP);
        buffer.writeLong(value.getTime());
    }

    public void writeType(ProtonBuffer buffer, EncoderState state, long value) {
        buffer.writeByte(EncodingCodes.TIMESTAMP);
        buffer.writeLong(value);
    }

    @Override
    public void writeRawArray(ProtonBuffer buffer, EncoderState state, Object[] values) {
        buffer.writeByte(EncodingCodes.LONG);
        for (Object value : values) {
            buffer.writeLong(((Date) value).getTime());
        }
    }
}
