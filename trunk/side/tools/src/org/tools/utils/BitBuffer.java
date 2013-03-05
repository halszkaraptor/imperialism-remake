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
 * Circular (LIFO) bit buffer allowing reading and writing chunks up to 32 bits
 * to and from the buffer at the same time. The buffer's length is not
 * self-adjusting but the capacity can be changed during usage. However this
 * will be costly because the buffer needs to be copied.
 *
 * Not thread safe!
 *
 * For usage in XML input/output the conversions to/from XML safe Strings are
 * included.
 *
 * Comment: Bit operations on integers are also included in java.util.BitSet and
 * java.lang.Integer but neither of them offers the abilities to store/read a
 * couple of bits at the same time.
 */
public class BitBuffer {

    /* The logger */
    private static final Logger LOG = Logger.getLogger(BitBuffer.class.getName());
    /* Bits per int = 32 */
    private static int BITS_PER_INT = 32;
    /* An int with all bits set */
    private static int ALL_BITS_SET = ~0;
    /* The circular buffer */
    private int[] buffer;
    /* start and end position of the data in the circular buffer */
    private int start, end;
    /* current number of bits stored in data (= end - start)*/
    private int size;
    /* Total capacity in bits (multiple of BITS_PER_INT */
    private int capacity;

    /**
     * Private constructor, used for cloning.
     */
    private BitBuffer() {
    }

    /**
     * @param length Initial capacity in bits. (Can be slightly larger due to
     * integer storage)
     */
    public BitBuffer(int length) {

        // estimate number of ints necessary to store length bits
        int len = length / BITS_PER_INT;
        if (length % BITS_PER_INT != 0) {
            len++;
        }

        // allocate buffer
        buffer = new int[len];
        capacity = len * BITS_PER_INT;

        // initialize start, end, .. pointers
        reset();
    }

    /**
     * Convenience method. Add a single bit.
     *
     * @param bit single bit presented by a boolean
     */
    public void add(boolean bit) {
        if (bit) {
            add(1, 1);
        } else {
            add(0, 1);
        }
    }

    /**
     * Add a number of bits (up to 32 bits at once). The number of bits and the
     * bits are given in int variables.
     *
     * @param bits Integer holding the bits (in lowest bits)
     * @param number Number of bits (1 - BITS_PER_INT)
     */
    public void add(int bits, int number) {
        if (number < 1 || number > BITS_PER_INT) {
            throw new IllegalArgumentException("Illegal number of bits " + String.valueOf(number));
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
            end %= capacity;
        }
        size += number;
    }

    /**
     * Convenience method. Returns one bit and casts to a boolean.
     *
     * @return The value of the actual last bit in the buffer.
     */
    public boolean get() {
        return get(1) == 1;
    }

    /**
     * Returns a number of bits (up to 32 bits at once).
     *
     * @param number Number of bits to be returned.
     * @return Integer holding the bits (lowest bits).
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
     * @return The number of used bits in the buffer.
     */
    public int size() {
        return size;
    }

    /**
     * @return The current buffer's total capacity for bits.
     */
    public int capacity() {
        return capacity;
    }

    /**
     * Sets start and end pointer to zero.
     */
    private void reset() {
        start = 0;
        end = 0;
        size = 0;
    }

    /**
     * Deletes a certain number of bits from the buffer. If number exceeds size,
     * all bits are deleted. If number is negative nothing is done.
     *
     * @param number Number of bits to delete.
     */
    public void trim(int number) {
        // negative number, do nothing
        if (number < 0) {
            return;
        }

        if (number >= size) {
            // clear all
            reset();
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
     * Convenience method: Deletes all bits but the indicated number. Does not
     * change the capacity.
     *
     * @param size Number of bits to keep.
     */
    public void trimTo(int size) {
        if (size >= 0 && size <= this.size) {
            trim(this.size - size);
        }
    }

    /**
     * Convenience method: Deletes all the content without changing the
     * capacity.
     */
    public void clear() {
        trimTo(0);
    }

    /**
     * Sets a new capacity for the buffer. The new capacity cannot be smaller
     * than the current size. It might be slightly larger due to the buffer
     * being int. This will allocate a new buffer and copy the content of the
     * old buffer.
     *
     * Not thread-safe! The buffer mustn't be changed during this operation.
     *
     * @param length New capacity. Will be rounded to next multiple of
     * BITS_PER_INT.
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
     * (range 0x100-0x8100) that means 15 bits used for 16 bits stored.
     *
     * There might be excess space in the last char, so the total size of the
     * BitBuffer should be stored externally (e.g. in an XML attribute).
     *
     * @return char[] based String
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
     * @return A new BitBuffer.
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
     * A deep copy/clone method.
     *
     * @return A new BitBuffer holding exactly the same data.
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