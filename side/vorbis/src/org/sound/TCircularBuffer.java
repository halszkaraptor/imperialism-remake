/*
 * Copyright (C) 1999 Matthias Pfisterer
 *               2013 Trilarion
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.sound;

public class TCircularBuffer {

    private boolean blockingRead;
    private boolean blockingWrite;
    private byte[] buffer;
    private int size;
    private int readPosition;
    private int writePosition;
    private Trigger trigger;
    private boolean isOpen;

    /**
     *
     * @param size
     * @param blockingRead
     * @param blockingWrite
     * @param trigger
     */
    public TCircularBuffer(int size, boolean blockingRead, boolean blockingWrite, Trigger trigger) {

        this.blockingRead = blockingRead;
        this.blockingWrite = blockingWrite;

        this.size = size;
        buffer = new byte[size];

        this.trigger = trigger;
        isOpen = true;
    }

    /**
     *
     */
    public void close() {
        isOpen = false;
        // TODO: call notify() ?
    }

    /**
     *
     * @return
     */
    private boolean isOpen() {
        return isOpen;
    }

    /**
     *
     * @return
     */
    public int availableRead() {
        return writePosition - readPosition;
    }

    /**
     *
     * @return
     */
    public int availableWrite() {
        return size - availableRead();
    }

    /**
     *
     * @return
     */
    private int getReadPos() {
        return readPosition % size;
    }

    /**
     *
     * @return
     */
    private int getWritePos() {
        return writePosition % size;
    }

    /**
     *
     * @param data
     * @return
     */
    public int read(byte[] data) {
        return read(data, 0, data.length);
    }

    /**
     *
     * @param data
     * @param offset
     * @param length
     * @return
     */
    public int read(byte[] data, int offset, int length) {
        if (TDebug.TraceCircularBuffer) {
            TDebug.out(">TCircularBuffer.read(): called.");
            dumpInternalState();
        }
        if (!isOpen()) {
            if (availableRead() > 0) {
                length = Math.min(length, availableRead());
                if (TDebug.TraceCircularBuffer) {
                    TDebug.out("reading rest in closed buffer, length: " + length);
                }
            } else {
                if (TDebug.TraceCircularBuffer) {
                    TDebug.out("< not open. returning -1.");
                }
                return -1;
            }
        }
        synchronized (this) {
            if (trigger != null && availableRead() < length) {
                if (TDebug.TraceCircularBuffer) {
                    TDebug.out("executing trigger.");
                }
                trigger.execute();
            }
            if (!blockingRead) {
                length = Math.min(availableRead(), length);
            }
            int nRemainingBytes = length;
            while (nRemainingBytes > 0) {
                while (availableRead() == 0) {
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        if (TDebug.TraceAllExceptions) {
                            TDebug.out(e);
                        }
                    }
                }
                int nAvailable = Math.min(availableRead(), nRemainingBytes);
                while (nAvailable > 0) {
                    int nToRead = Math.min(nAvailable, size - getReadPos());
                    System.arraycopy(buffer, getReadPos(), data, offset, nToRead);
                    readPosition += nToRead;
                    offset += nToRead;
                    nAvailable -= nToRead;
                    nRemainingBytes -= nToRead;
                }
                notifyAll();
            }
            if (TDebug.TraceCircularBuffer) {
                TDebug.out("After read:");
                dumpInternalState();
                TDebug.out("< completed. Read " + length + " bytes");
            }
            return length;
        }
    }

    /**
     *
     * @param data
     * @return
     */
    public int write(byte[] data) {
        return write(data, 0, data.length);
    }

    /**
     *
     * @param data
     * @param offset
     * @param length
     * @return
     */
    public int write(byte[] data, int offset, int length) {
        if (TDebug.TraceCircularBuffer) {
            TDebug.out(">TCircularBuffer.write(): called; nLength: " + length);
            dumpInternalState();
        }
        synchronized (this) {
            if (TDebug.TraceCircularBuffer) {
                TDebug.out("entered synchronized block.");
            }
            if (!blockingWrite) {
                length = Math.min(availableWrite(), length);
            }
            int nRemainingBytes = length;
            while (nRemainingBytes > 0) {
                while (availableWrite() == 0) {
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        if (TDebug.TraceAllExceptions) {
                            TDebug.out(e);
                        }
                    }
                }
                int nAvailable = Math.min(availableWrite(), nRemainingBytes);
                while (nAvailable > 0) {
                    int nToWrite = Math.min(nAvailable, size - getWritePos());
                    //TDebug.out("src buf size= " + abData.length + ", offset = " + nOffset + ", dst buf size=" + m_abData.length + " write pos=" + getWritePos() + " len=" + nToWrite);
                    System.arraycopy(data, offset, buffer, getWritePos(), nToWrite);
                    writePosition += nToWrite;
                    offset += nToWrite;
                    nAvailable -= nToWrite;
                    nRemainingBytes -= nToWrite;
                }
                notifyAll();
            }
            if (TDebug.TraceCircularBuffer) {
                TDebug.out("After write:");
                dumpInternalState();
                TDebug.out("< completed. Wrote " + length + " bytes");
            }
            return length;
        }
    }

    /**
     *
     */
    private void dumpInternalState() {
        TDebug.out("m_lReadPos  = " + readPosition + " ^= " + getReadPos());
        TDebug.out("m_lWritePos = " + writePosition + " ^= " + getWritePos());
        TDebug.out("availableRead()  = " + availableRead());
        TDebug.out("availableWrite() = " + availableWrite());
    }
}
