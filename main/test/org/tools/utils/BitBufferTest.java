/*
 * Copyright (C) 2012 Trilarion
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.tools.utils;

import junit.framework.Assert;
import org.junit.Test;

/**
 * Tests the BitBuffer class.
 */
public class BitBufferTest {

    /**
     * Tests correct calculation of size.
     */
    @Test
    public void TestSize() {
        System.out.println();
        System.out.println("Test sizing");

        BitBuffer buffer = new BitBuffer(100);
        System.out.println("Capacity " + buffer.capacity());

        int p = 5;
        buffer.add(~0, p);
        System.out.println("Size " + buffer.size());

        int out = buffer.get(p);
        System.out.println("Size " + buffer.size());
        System.out.println("Output " + Integer.toBinaryString(out));

        Assert.assertEquals(~0 >>> (32 - p), out);
    }

    /**
     * Tests reading and writing of some variable length bit fields.
     */
    @Test
    public void TestCrossIntBorderReadWrite() {
        System.out.println();
        System.out.println("Test cross int border reading and writing");

        BitBuffer buffer = new BitBuffer(100);
        buffer.add(~0, 31);
        buffer.add(0, 30);
        buffer.add(~0, 29);

        System.out.println("Size " + buffer.size());
        Assert.assertEquals(90, buffer.size());

        int x = buffer.get(31);
        System.out.println(Integer.toBinaryString(x));
        Assert.assertEquals(~0 >>> 1, x);

        x = buffer.get(30);
        System.out.println(Integer.toBinaryString(x));
        Assert.assertEquals(0, x);

        x = buffer.get(29);
        System.out.println(Integer.toBinaryString(x));
        Assert.assertEquals(~0 >>> 3, x);

        System.out.println("Size " + buffer.size());
        Assert.assertEquals(0, buffer.size());
    }

    /**
     * Tests reading and writing beyond the end of the internal buffer array.
     */
    @Test
    public void TestCyclicReadingWriting() {
        System.out.println();
        System.out.println("Test cross buffer border reading and writing");

        BitBuffer buffer = new BitBuffer(32);

        buffer.add(~0, 30);
        int x = buffer.get(20);
        System.out.println(Integer.toBinaryString(x));
        Assert.assertEquals(~0 >>> 12, x);

        buffer.add(0, 10);
        x = buffer.get(20);
        System.out.println(Integer.toBinaryString(x));
        Assert.assertEquals(0, x);

        Assert.assertEquals(0, buffer.size());

    }

    /**
     *
     */
    @Test
    public void TestExtendCapacity() {
        System.out.println();
        System.out.println("Test extending the capacity in between");

        BitBuffer buffer = new BitBuffer(200);

        buffer.add(~0, 32);
        buffer.add(~0, 16);
        buffer.add(0, 32);
        buffer.add(~0, 32);
        buffer.get(16);

        buffer.extend(320);

        int x = buffer.get(32);
        Assert.assertEquals(~0, x);

        x = buffer.get(32);
        Assert.assertEquals(0, x);

        x = buffer.get(32);
        Assert.assertEquals(~0, x);

        Assert.assertEquals(0, buffer.size());
        Assert.assertEquals(320, buffer.capacity());
    }

    /**
     *
     */
    @Test
    public void TestStringConversion() {
        System.out.println();
        System.out.println("Test converting to string and back");

        BitBuffer buffer = new BitBuffer(100);

        buffer.add(~0, 20);
        buffer.add(0, 14);
        buffer.add(~0, 14);
        buffer.add(0, 14);
        buffer.get(6);

        BitBuffer copy = buffer.copy();

        String string = BitBuffer.fromBuffer(buffer);
        System.out.println(string);

        buffer = BitBuffer.fromString(string);

        int x = buffer.get(32);
        int y = copy.get(32);
        Assert.assertEquals(y, x);

        x = buffer.get(24);
        y = copy.get(24);
        Assert.assertEquals(y, x);

        Assert.assertEquals(0, buffer.size());
        Assert.assertEquals(0, copy.size());
    }
}
