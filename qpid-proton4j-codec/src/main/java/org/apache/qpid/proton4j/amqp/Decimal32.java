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
package org.apache.qpid.proton4j.amqp;

import java.lang.annotation.Native;
import java.math.BigDecimal;

public final class Decimal32 extends Number {

    private static final long serialVersionUID = 1404882516677613318L;

    /**
     * The number of bits used to represent an {@code Decimal128} value in two's
     * complement binary form.
     */
    @Native public static final int SIZE = 32;

    /**
     * The number of bytes used to represent a {@code Decimal128} value in two's
     * complement binary form.
     */
    public static final int BYTES = SIZE / Byte.SIZE;

    private final BigDecimal underlying;
    private final int bits;

    public Decimal32(BigDecimal underlying) {
        this.underlying = underlying;
        this.bits = calculateBits(underlying);
    }

    public Decimal32(final int bits) {
        this.bits = bits;
        this.underlying = calculateBigDecimal(bits);
    }

    static int calculateBits(final BigDecimal underlying) {
        return 0; // TODO.
    }

    static BigDecimal calculateBigDecimal(int bits) {
        return BigDecimal.ZERO; // TODO
    }

    @Override
    public int intValue() {
        return underlying.intValue();
    }

    @Override
    public long longValue() {
        return underlying.longValue();
    }

    @Override
    public float floatValue() {
        return underlying.floatValue();
    }

    @Override
    public double doubleValue() {
        return underlying.doubleValue();
    }

    public int getBits() {
        return bits;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final Decimal32 decimal32 = (Decimal32) o;

        if (bits != decimal32.bits) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return bits;
    }
}
