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
package org.apache.qpid.proton4j.codec.encoders.transport;

import org.apache.qpid.proton4j.amqp.Symbol;
import org.apache.qpid.proton4j.amqp.UnsignedLong;
import org.apache.qpid.proton4j.amqp.transport.ReceiverSettleMode;
import org.apache.qpid.proton4j.amqp.transport.Transfer;
import org.apache.qpid.proton4j.buffer.ProtonBuffer;
import org.apache.qpid.proton4j.codec.EncoderState;
import org.apache.qpid.proton4j.codec.EncodingCodes;
import org.apache.qpid.proton4j.codec.encoders.AbstractDescribedListTypeEncoder;

/**
 * Encoder of AMQP Transfer type values to a byte stream.
 */
public class TransferTypeEncoder extends AbstractDescribedListTypeEncoder<Transfer> {

    @Override
    public UnsignedLong getDescriptorCode() {
        return Transfer.DESCRIPTOR_CODE;
    }

    @Override
    public Symbol getDescriptorSymbol() {
        return Transfer.DESCRIPTOR_SYMBOL;
    }

    @Override
    public Class<Transfer> getTypeClass() {
        return Transfer.class;
    }

    @Override
    public void writeElement(Transfer transfer, int index, ProtonBuffer buffer, EncoderState state) {
        switch (index) {
            case 0:
                if (transfer.hasHandle()) {
                    state.getEncoder().writeUnsignedInteger(buffer, state, transfer.getHandle());
                } else {
                    buffer.writeByte(EncodingCodes.NULL);
                }
                break;
            case 1:
                if (transfer.hasDeliveryId()) {
                    state.getEncoder().writeUnsignedInteger(buffer, state, transfer.getDeliveryId());
                } else {
                    buffer.writeByte(EncodingCodes.NULL);
                }
                break;
            case 2:
                state.getEncoder().writeBinary(buffer, state, transfer.getDeliveryTag());
                break;
            case 3:
                if (transfer.hasMessageFormat()) {
                    state.getEncoder().writeUnsignedInteger(buffer, state, transfer.getMessageFormat());
                } else {
                    buffer.writeByte(EncodingCodes.NULL);
                }
                break;
            case 4:
                if (transfer.hasSettled()) {
                    state.getEncoder().writeBoolean(buffer, state, transfer.getSettled());
                } else {
                    buffer.writeByte(EncodingCodes.NULL);
                }
                break;
            case 5:
                if (transfer.hasMore()) {
                    state.getEncoder().writeBoolean(buffer, state, transfer.getMore());
                } else {
                    buffer.writeByte(EncodingCodes.NULL);
                }
                break;
            case 6:
                ReceiverSettleMode rcvSettleMode = transfer.getRcvSettleMode();
                state.getEncoder().writeObject(buffer, state, rcvSettleMode == null ? null : rcvSettleMode.getValue());
                break;
            case 7:
                state.getEncoder().writeObject(buffer, state, transfer.getState());
                break;
            case 8:
                if (transfer.hasResume()) {
                    state.getEncoder().writeBoolean(buffer, state, transfer.getResume());
                } else {
                    buffer.writeByte(EncodingCodes.NULL);
                }
                break;
            case 9:
                if (transfer.hasAborted()) {
                    state.getEncoder().writeBoolean(buffer, state, transfer.getAborted());
                } else {
                    buffer.writeByte(EncodingCodes.NULL);
                }
                break;
            case 10:
                if (transfer.hasBatchable()) {
                    state.getEncoder().writeBoolean(buffer, state, transfer.getBatchable());
                } else {
                    buffer.writeByte(EncodingCodes.NULL);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown Transfer value index: " + index);
        }
    }

    @Override
    public int getListEncoding(Transfer value) {
        if (value.getState() != null) {
            return EncodingCodes.LIST32;
        } else if (value.getDeliveryTag() != null && value.getDeliveryTag().getLength() > 200) {
            return EncodingCodes.LIST32;
        } else {
            return EncodingCodes.LIST8;
        }
    }

    @Override
    public int getElementCount(Transfer transfer) {
        return transfer.getElementCount();
    }
}
