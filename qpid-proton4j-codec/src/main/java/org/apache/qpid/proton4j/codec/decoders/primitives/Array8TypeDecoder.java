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
package org.apache.qpid.proton4j.codec.decoders.primitives;

import org.apache.qpid.proton4j.buffer.ProtonBuffer;
import org.apache.qpid.proton4j.codec.EncodingCodes;

/**
 * Decoder of AMQP Arrays from a byte stream.
 */
public class Array8TypeDecoder extends AbstractArrayTypeDecoder {

    @Override
    public int getTypeCode() {
        return EncodingCodes.ARRAY8 & 0xff;
    }

    @Override
    protected int readSize(ProtonBuffer buffer) {
        return buffer.readByte() & 0xff;
    }

    @Override
    protected int readCount(ProtonBuffer buffer) {
        return buffer.readByte() & 0xff;
    }
}
