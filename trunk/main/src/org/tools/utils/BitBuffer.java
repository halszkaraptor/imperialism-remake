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

import java.nio.charset.Charset;

/**
 * Circular bit buffer allowing reading and writing chunks up to 32 bits to and
 * from the buffer at the same time. It's length is not self-adjusting but the
 * capacity can be changed during usage.
 *
 * Not thread safe.
 *
 * For usage in XML input/output the conversion to/from String are included.
 *
 * Bit operations on integers are also included in java.util.BitSet and
 * java.lang.Integer but neither of them offers the abilities to store/read a
 * couple of bits at the same time.
 */
public class BitBuffer {

    private static int BITS_PER_INT = 32;
    private static int ALL_BITS_SET = ~0;
    private int[] buffer;
    private int start, end;
    private int size, capacity;

    /**
     * Private constructor for copying.
     */
    private BitBuffer() {

    }

    /**
     *
     * @param length
     */
    public BitBuffer(int length) {
        int len = length / BITS_PER_INT;
        if (length % BITS_PER_INT != 0) {
            len++;
        }
        buffer = new int[len];
        capacity = len * BITS_PER_INT;
        initializeIndices();
    }

    /**
     *
     */
    private void initializeIndices() {
        start = 0;
        end = 0;
        size = 0;
    }

    /**
     * Convenience method.
     *
     * @param bit
     */
    public void add(boolean bit) {
        if (bit) {
            add(1, 1);
        } else {
            add(0, 1);
        }
    }

    /**
     *
     * @param bits Integer holding the bits (in lowest bits)
     * @param number Number of bits (1-BITS_PER_INT)
     */
    public void add(int bits, int number) {
        if (number < 1 || number > BITS_PER_INT) {
            throw new IllegalArgumentException("Illegal of bits " + String.valueOf(number));
        }
        if (capacity - size < number) {
            throw new IllegalArgumentException("Not enough free capacity " + String.valueOf(capacity - size));
        }
        int index = end / BITS_PER_INT;
        int position = end % BITS_PER_INT;
        buffer[index] &= ALL_BITS_SET >>> (BITS_PER_INT - position);
        buffer[index] += bits << position;

        int excess = number - (BITS_PER_INT - position);
        if (excess > 0) {
            buffer[(index + 1) % buffer.length] = bits >>> (number - excess);
        }

        end += number;
        if (end > capacity) {
            end = end % capacity;
        }
        size += number;
    }

    /**
     * Convenience method.
     *
     * @return
     */
    public boolean get() {
        return get(1) == 1;
    }

    /**
     *
     * @param number
     * @return
     */
    public int get(int number) {
        if (number < 1 || number > BITS_PER_INT) {
            throw new IllegalArgumentException("Illegal of bits " + String.valueOf(number));
        }
        if (number > size) {
            throw new IllegalArgumentException("Not enough bits existing " + String.valueOf(size));
        }
        int value;
        int index = start / BITS_PER_INT;
        int position = start % BITS_PER_INT;

        int excess = number - (BITS_PER_INT - position);
        if (excess > 0) {
            // excess, get the buffer value at index
            value = buffer[index] >>> position;
            // add excess bits from the next buffer entry
            int temp = buffer[(index + 1) % buffer.length] & (ALL_BITS_SET >>> (BITS_PER_INT - excess));
            value += temp << (number - excess);
        } else {
            // no excess, get the buffer value at index
            value = buffer[index] >>> position;
            // only keep number bits
            value &= ALL_BITS_SET >>> (BITS_PER_INT - number);
        }

        start += number;
        if (start > capacity) {
            start = start % capacity;
        }

        size -= number;

        return value;
    }

    /**
     * Deletes all the content without changing the capacity.
     */
    public void clear() {
        initializeIndices();
    }

    /**
     * How many bits are in the buffer.
     *
     * @return The size of the used bits in the buffer.
     */
    public int size() {
        return size;
    }

    /**
     * Total number of bits the buffer can hold.
     *
     * @return The total capacity for bits.
     */
    public int capacity() {
        return capacity;
    }

    /**
     * Also moves the currently used bits to the start of the buffer. Not
     * thread-safe. The buffer mustn't be changed during this operation.
     *
     * @param length Will be rounded to next multiple of BITS_PER_INT.
     */
    public void extend(int length) {
        if (length < size) {
            throw new IllegalArgumentException("Cannot decrease below actual size " + String.valueOf(size));
        }

        int len = length / BITS_PER_INT;
        if (length % BITS_PER_INT != 0) {
            len++;
        }
        int[] temp = new int[len];
        capacity = len * BITS_PER_INT;

        // copy them all
        int index = 0;
        int oldsize = size;
        while (size > 0) {
            int value = get(Math.min(size, BITS_PER_INT));
            temp[index++] = value;
        }
        buffer = temp;

        // set new indices
        start = 0;
        end = oldsize;
        size = oldsize;
    }

    /* */
    private static final Charset charset = Charset.forName("UTF-8");

    /**
     * For XML writing a BitBuffer must be encoded in a String. We do it by
     * using UTF-8 encoding and setting the most significant bit always to zero,
     * i.e. only using 7 bits per byte. Since we cannot write half bytes, the
     * last byte might contains additionally zeros. So size is not preserved,
     * cannot be in fact.
     *
     * @param buffer
     * @return
     */
    public static String fromBuffer(BitBuffer buffer) {
        int size = buffer.size();
        int length = size / 7;
        boolean excess = size % 7 != 0;
        byte[] bytes = new byte[length + (excess ? 1 : 0)];
        for (int i = 0; i < length; i++) {
            bytes[i] = (byte) buffer.get(7);
        }
        if (excess) {
            bytes[length] = (byte) buffer.get(buffer.size());
        }

        return new String(bytes, charset);
    }

    /**
     * A string that is supposedly encoded in UTF-8 and only consists of bytes
     * with the most significant bit not set (i.e. 7 bits used per byte) is read
     * into a BitBuffer. This is need when reading a BitBuffer from XML, since
     * XML is inherently String/Text based.
     *
     * @param string The Input string.
     * @return A BitBuffer.
     */
    public static BitBuffer fromString(String string) {
        byte[] bytes = string.getBytes(charset);
        BitBuffer buffer = new BitBuffer(bytes.length * 7);
        for (int i = 0; i < bytes.length; i++) {
            buffer.add(bytes[i], 7);
        }
        return buffer;
    }

    /**
     * Prepare deep copy.
     *
     * @return
     */
    public BitBuffer copy() {
        BitBuffer copy = new BitBuffer();

        copy.start = start;
        copy.end = end;
        copy.size = size;
        copy.capacity = capacity;

        copy.buffer = buffer.clone(); 

        return copy;
    }
}