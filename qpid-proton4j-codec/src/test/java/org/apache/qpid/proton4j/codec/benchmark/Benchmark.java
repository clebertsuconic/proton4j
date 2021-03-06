/*
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 *
 */
package org.apache.qpid.proton4j.codec.benchmark;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.apache.qpid.proton4j.amqp.Binary;
import org.apache.qpid.proton4j.amqp.Symbol;
import org.apache.qpid.proton4j.amqp.UnsignedByte;
import org.apache.qpid.proton4j.amqp.UnsignedInteger;
import org.apache.qpid.proton4j.amqp.UnsignedShort;
import org.apache.qpid.proton4j.amqp.messaging.Accepted;
import org.apache.qpid.proton4j.amqp.messaging.ApplicationProperties;
import org.apache.qpid.proton4j.amqp.messaging.Data;
import org.apache.qpid.proton4j.amqp.messaging.Header;
import org.apache.qpid.proton4j.amqp.messaging.MessageAnnotations;
import org.apache.qpid.proton4j.amqp.messaging.Properties;
import org.apache.qpid.proton4j.amqp.transport.Disposition;
import org.apache.qpid.proton4j.amqp.transport.Flow;
import org.apache.qpid.proton4j.amqp.transport.Role;
import org.apache.qpid.proton4j.amqp.transport.Transfer;
import org.apache.qpid.proton4j.buffer.ProtonBuffer;
import org.apache.qpid.proton4j.buffer.ProtonByteBufferAllocator;
import org.apache.qpid.proton4j.codec.CodecFactory;
import org.apache.qpid.proton4j.codec.Decoder;
import org.apache.qpid.proton4j.codec.DecoderState;
import org.apache.qpid.proton4j.codec.Encoder;
import org.apache.qpid.proton4j.codec.EncoderState;

public class Benchmark implements Runnable {

    private static final int ITERATIONS = 10 * 1024 * 1024;

    ProtonBuffer buffer = ProtonByteBufferAllocator.DEFAULT.allocate(8192);
    private BenchmarkResult resultSet = new BenchmarkResult();
    private boolean warming = true;

    private Encoder encoder = CodecFactory.getDefaultEncoder();
    private EncoderState encoderState = encoder.newEncoderState();
    private Decoder decoder = CodecFactory.getDefaultDecoder();
    private DecoderState decoderState = decoder.newDecoderState();

    public static final void main(String[] args) throws IOException, InterruptedException {
        System.out.println("Current PID: " + ManagementFactory.getRuntimeMXBean().getName());
        Benchmark benchmark = new Benchmark();
        benchmark.run();
    }

    @Override
    public void run() {
        try {
            doBenchmarks();
            warming = false;
            doBenchmarks();
        } catch (IOException e) {
            System.out.println("Unexpected error: " + e.getMessage());
        }
    }

    private void time(String message, BenchmarkResult resultSet) {
        if (!warming) {
            System.out.println("Benchamrk of type: " + message + ": ");
            System.out.println("    Encode time = " + resultSet.getEncodeTimeMills());
            System.out.println("    Decode time = " + resultSet.getDecodeTimeMills());
        }
    }

    private final void doBenchmarks() throws IOException {
        benchmarkListOfInts();
        benchmarkUUIDs();
        benchmarkHeader();
        benchmarkProperties();
        benchmarkMessageAnnotations();
        benchmarkApplicationProperties();
        benchmarkSymbols();
        benchmarkTransfer();
        benchmarkFlow();
        benchmarkDisposition();
        benchmarkString();
        benchmarkData();
        warming = false;
    }

    private void benchmarkListOfInts() throws IOException {
        ArrayList<Object> list = new ArrayList<>(10);
        for (int j = 0; j < 10; j++) {
            list.add(0);
        }

        resultSet.start();
        for (int i = 0; i < ITERATIONS; i++) {
            buffer.clear();
            encoder.writeList(buffer, encoderState, list);
        }
        resultSet.encodesComplete();

        resultSet.start();
        for (int i = 0; i < ITERATIONS; i++) {
            buffer.setReadIndex(0);
            decoder.readList(buffer, decoderState);
        }
        resultSet.decodesComplete();

        time("List<Integer>", resultSet);
    }

    private void benchmarkUUIDs() throws IOException {
        UUID uuid = UUID.randomUUID();

        resultSet.start();
        for (int i = 0; i < ITERATIONS; i++) {
            buffer.clear();
            encoder.writeUUID(buffer, encoderState, uuid);
        }
        resultSet.encodesComplete();

        resultSet.start();
        for (int i = 0; i < ITERATIONS; i++) {
            buffer.setReadIndex(0);
            decoder.readUUID(buffer, decoderState);
        }
        resultSet.decodesComplete();

        time("UUID", resultSet);
    }

    private void benchmarkTransfer() throws IOException {
        Transfer transfer = new Transfer();
        transfer.setDeliveryTag(new Binary(new byte[] {1, 2, 3}));
        transfer.setHandle(1024);
        transfer.setMessageFormat(0);

        resultSet.start();
        for (int i = 0; i < ITERATIONS; i++) {
            buffer.clear();
            encoder.writeObject(buffer, encoderState, transfer);
        }
        resultSet.encodesComplete();

        resultSet.start();
        for (int i = 0; i < ITERATIONS; i++) {
            buffer.setReadIndex(0);
            decoder.readObject(buffer, decoderState);
        }
        resultSet.decodesComplete();

        time("Transfer", resultSet);
    }

    private void benchmarkFlow() throws IOException {
        Flow flow = new Flow();
        flow.setNextIncomingId(1);
        flow.setIncomingWindow(2047);
        flow.setNextOutgoingId(1);
        flow.setOutgoingWindow(Integer.MAX_VALUE);
        flow.setHandle(UnsignedInteger.ZERO.longValue());
        flow.setDeliveryCount(10);
        flow.setLinkCredit(1000);

        resultSet.start();
        for (int i = 0; i < ITERATIONS; i++) {
            buffer.clear();
            encoder.writeObject(buffer, encoderState, flow);
        }
        resultSet.encodesComplete();

        resultSet.start();
        for (int i = 0; i < ITERATIONS; i++) {
            buffer.setReadIndex(0);
            decoder.readObject(buffer, decoderState);
        }
        resultSet.decodesComplete();

        time("Flow", resultSet);
    }

    private void benchmarkHeader() throws IOException {
        Header header = new Header();
        header.setDurable(true);
        header.setFirstAcquirer(true);

        resultSet.start();
        for (int i = 0; i < ITERATIONS; i++) {
            buffer.clear();
            encoder.writeObject(buffer, encoderState, header);
        }
        resultSet.encodesComplete();

        resultSet.start();
        for (int i = 0; i < ITERATIONS; i++) {
            buffer.setReadIndex(0);
            decoder.readObject(buffer, decoderState);
        }
        resultSet.decodesComplete();

        time("Header", resultSet);
    }

    private void benchmarkProperties() throws IOException {
        Properties properties = new Properties();
        properties.setTo("queue:1");
        properties.setMessageId("ID:Message:1");
        properties.setCreationTime(System.currentTimeMillis());

        resultSet.start();
        for (int i = 0; i < ITERATIONS; i++) {
            buffer.clear();
            encoder.writeObject(buffer, encoderState, properties);
        }
        resultSet.encodesComplete();

        resultSet.start();
        for (int i = 0; i < ITERATIONS; i++) {
            buffer.setReadIndex(0);
            decoder.readObject(buffer, decoderState);
        }
        resultSet.decodesComplete();

        time("Properties", resultSet);
    }

    private void benchmarkMessageAnnotations() throws IOException {
        MessageAnnotations annotations = new MessageAnnotations(new HashMap<>());
        annotations.getValue().put(Symbol.valueOf("test1"), UnsignedByte.valueOf((byte) 128));
        annotations.getValue().put(Symbol.valueOf("test2"), UnsignedShort.valueOf((short) 128));
        annotations.getValue().put(Symbol.valueOf("test3"), UnsignedInteger.valueOf((byte) 128));

        resultSet.start();
        for (int i = 0; i < ITERATIONS; i++) {
            buffer.clear();
            encoder.writeObject(buffer, encoderState, annotations);
        }
        resultSet.encodesComplete();

        resultSet.start();
        for (int i = 0; i < ITERATIONS; i++) {
            buffer.setReadIndex(0);
            decoder.readObject(buffer, decoderState);
        }
        resultSet.decodesComplete();

        time("MessageAnnotations", resultSet);
    }

    private void benchmarkApplicationProperties() throws IOException {
        ApplicationProperties properties = new ApplicationProperties(new HashMap<>());
        properties.getValue().put("test1", UnsignedByte.valueOf((byte) 128));
        properties.getValue().put("test2", UnsignedShort.valueOf((short) 128));
        properties.getValue().put("test3", UnsignedInteger.valueOf((byte) 128));

        resultSet.start();
        for (int i = 0; i < ITERATIONS; i++) {
            buffer.clear();
            encoder.writeObject(buffer, encoderState, properties);
        }
        resultSet.encodesComplete();

        resultSet.start();
        for (int i = 0; i < ITERATIONS; i++) {
            buffer.setReadIndex(0);
            decoder.readObject(buffer, decoderState);
        }
        resultSet.decodesComplete();

        time("ApplicationProperties", resultSet);
    }

    private void benchmarkSymbols() throws IOException {
        Symbol symbol1 = Symbol.valueOf("Symbol-1");
        Symbol symbol2 = Symbol.valueOf("Symbol-2");
        Symbol symbol3 = Symbol.valueOf("Symbol-3");

        resultSet.start();
        for (int i = 0; i < ITERATIONS; i++) {
            buffer.clear();
            encoder.writeSymbol(buffer, encoderState, symbol1);
            encoder.writeSymbol(buffer, encoderState, symbol2);
            encoder.writeSymbol(buffer, encoderState, symbol3);
        }
        resultSet.encodesComplete();

        resultSet.start();
        for (int i = 0; i < ITERATIONS; i++) {
            buffer.setReadIndex(0);
            decoder.readSymbol(buffer, decoderState);
            decoder.readSymbol(buffer, decoderState);
            decoder.readSymbol(buffer, decoderState);
        }
        resultSet.decodesComplete();

        time("Symbol", resultSet);
    }

    private void benchmarkString() throws IOException {
        String string1 = new String("String-1");
        String string2 = new String("String-2");
        String string3 = new String("String-3");

        resultSet.start();
        for (int i = 0; i < ITERATIONS; i++) {
            buffer.clear();
            encoder.writeString(buffer, encoderState, string1);
            encoder.writeString(buffer, encoderState, string2);
            encoder.writeString(buffer, encoderState, string3);
        }
        resultSet.encodesComplete();

        resultSet.start();
        for (int i = 0; i < ITERATIONS; i++) {
            buffer.setReadIndex(0);
            decoder.readString(buffer, decoderState);
            decoder.readString(buffer, decoderState);
            decoder.readString(buffer, decoderState);
        }
        resultSet.decodesComplete();

        time("String", resultSet);
    }

    private void benchmarkDisposition() throws IOException {
        Disposition disposition = new Disposition();
        disposition.setRole(Role.RECEIVER);
        disposition.setSettled(true);
        disposition.setState(Accepted.getInstance());
        disposition.setFirst(UnsignedInteger.valueOf(2));
        disposition.setLast(UnsignedInteger.valueOf(2));

        resultSet.start();
        for (int i = 0; i < ITERATIONS; i++) {
            buffer.clear();
            encoder.writeObject(buffer, encoderState, disposition);
        }
        resultSet.encodesComplete();

        resultSet.start();
        for (int i = 0; i < ITERATIONS; i++) {
            buffer.setReadIndex(0);
            decoder.readObject(buffer, decoderState);
        }
        resultSet.decodesComplete();

        time("Disposition", resultSet);
    }

    private void benchmarkData() throws IOException {
        Data data1 = new Data(new Binary(new byte[] {1, 2, 3}));
        Data data2 = new Data(new Binary(new byte[] {4, 5, 6}));
        Data data3 = new Data(new Binary(new byte[] {7, 8, 9}));

        resultSet.start();
        for (int i = 0; i < ITERATIONS; i++) {
            buffer.clear();
            encoder.writeObject(buffer, encoderState, data1);
            encoder.writeObject(buffer, encoderState, data2);
            encoder.writeObject(buffer, encoderState, data3);
        }
        resultSet.encodesComplete();

        resultSet.start();
        for (int i = 0; i < ITERATIONS; i++) {
            buffer.setReadIndex(0);
            decoder.readObject(buffer, decoderState);
            decoder.readObject(buffer, decoderState);
            decoder.readObject(buffer, decoderState);
        }
        resultSet.decodesComplete();

        time("Data", resultSet);
    }

    private static class BenchmarkResult {

        private long startTime;

        private long encodeTime;
        private long decodeTime;

        public void start() {
            startTime = System.nanoTime();
        }

        public void encodesComplete() {
            encodeTime = System.nanoTime() - startTime;
        }

        public void decodesComplete() {
            decodeTime = System.nanoTime() - startTime;
        }

        public long getEncodeTimeMills() {
            return TimeUnit.NANOSECONDS.toMillis(encodeTime);
        }

        public long getDecodeTimeMills() {
            return TimeUnit.NANOSECONDS.toMillis(decodeTime);
        }
    }
}
