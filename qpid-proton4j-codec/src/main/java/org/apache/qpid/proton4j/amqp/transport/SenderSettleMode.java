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
package org.apache.qpid.proton4j.amqp.transport;

import org.apache.qpid.proton4j.amqp.UnsignedByte;

public enum SenderSettleMode {

    UNSETTLED(0), SETTLED(1), MIXED(2);

    private UnsignedByte value;

    private SenderSettleMode(int value) {
        this.value = UnsignedByte.valueOf((byte)value);
    }

    public static SenderSettleMode valueOf(UnsignedByte value) {
        switch (value.intValue()) {
            case 0:
                return SenderSettleMode.UNSETTLED;
            case 1:
                return SenderSettleMode.SETTLED;
            case 2:
                return SenderSettleMode.MIXED;
            default:
                throw new IllegalArgumentException("The value can be only 0 (for UNSETTLED), 1 (for SETTLED) and 2 (for MIXED)");
        }
    }

    public UnsignedByte getValue() {
        return this.value;
    }
}
