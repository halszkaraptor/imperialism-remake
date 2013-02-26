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

import java.util.logging.Logger;

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
     * @param values Integer holding the bits (in lowest bits)
     * @param number Number of bits (1-BITS_PER_INT)
     */
    public void add(int values, int number) {
        if (number < 1 || number > BITS_PER_INT) {
            throw new IllegalArgumentException("Illegal number of bits " + String.valueOf(number));
        }
        if (capacity - size < number) {
            throw new IllegalArgumentException("Not enough free capacity " + String.valueOf(capacity - size));
        }
        int index = end / BITS_PER_INT;
        int position = end % BITS_PER_INT;
        buffer[index] &= ALL_BITS_SET >>> (BITS_PER_INT - position);
        buffer[index] += values << position;

        int excess = number - (BITS_PER_INT - position);
        if (excess > 0) {
            buffer[(index + 1) % buffer.length] = values >>> (number - excess);
        }

        end += number;
        if (end > capacity) {
            end %= capacity;
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
            start %= capacity;
        }

        size -= number;

        return value;
    }

    /**
     * Deletes all the content without changing the capacity.
     */
    public void clear() {
        trim(size);
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
     * Delete some bits from the buffer. If number exceeds size, all bits are
     * deleted. If number is negative nothing is done.
     *
     * @param number
     */
    public void trim(int number) {
        // negative number, do nothing
        if (number < 0) {
            return;
        }

        if (number >= size) {
            // clear all
            start = 0;
            end = 0;
            size = 0;
        } else {
            // decrease size and end, but be careful with underflows for end
            end -= number;
            if (end < 0) {
                end += capacity;
            }
            size -= number;
        }
    }

    /**
     * Convenience methods. Basically sets a new size by deleting some using
     * trim() internally.
     *
     * @param size
     */
    public void trimTo(int size) {
        if (size >= 0 && size <= this.size) {
            trim(this.size - size);
        }
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

    /**
     * For XML writing a BitBuffer must be encoded in a String. Not all UTF-8
     * characters are valid XML characters, so we restrict ourselves to the
     * range between 0x20 and 0xD7FF
     * (http://www.w3.org/TR/2000/REC-xml-20001006#NT-Char) or even a bit less
     * (0x100-0x8100) that means 15 bits.
     *
     * There might be excess space in the last char, so the total size of the
     * BitBuffer should be stored externally (e.g. in an XML attribute).
     *
     * @param buffer
     * @return
     */
    public String toXMLString() {
        int length = size / 15;
        boolean excess = size % 15 != 0;
        char[] chars = new char[length + (excess ? 1 : 0)];
        for (int i = 0; i < length; i++) {
            chars[i] = (char) (get(15) + 256);
        }
        if (excess) {
            chars[length] = (char) (get(size) + 256);
        }

        return new String(chars);
    }

    /**
     * XML is inherently String based, so we have a way of storing a BitBuffer
     * in an XML String (see toXMLString) and this is the way back.
     *
     * @param string The Input string.
     * @return A BitBuffer.
     */
    public static BitBuffer fromXMLString(String string) {
        char[] chars = string.toCharArray();
        BitBuffer buffer = new BitBuffer(chars.length * 15);
        for (int i = 0; i < chars.length; i++) {
            buffer.add((int) (chars[i] - 256), 15);
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
    private static final Logger LOG = Logger.getLogger(BitBuffer.class.getName());
}