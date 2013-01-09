/*
 * Copyright (C) 2000 ymnk, JCraft,Inc.
 * Copyright (C) 2013 Trilarion
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
package org.vorbis.jcraft.jorbis;

class ChainingExample {

    public static void main(String[] arg) {
        // make sure the following file exists
        VorbisFile ov;
        try {
            ov = new VorbisFile("test.ogg");
        } catch (JOrbisException ex) {
            System.err.println(ex);
            return;
        }

        if (ov.seekable()) {
            System.out.println("Input bitstream contains " + ov.streams() + " logical bitstream section(s).");
            System.out.println("Total bitstream playing time: " + ov.time_total(-1) + " seconds\n");
        } else {
            System.out.println("Standard input was not seekable.");
            System.out.println("First logical bitstream information:\n");
        }

        for (int i = 0; i < ov.streams(); i++) {
            Info vi = ov.getInfo(i);
            System.out.println("  logical bitstream section " + (i + 1) + " information:");
            System.out.println("    " + vi.rate + "Hz " + vi.channels + " channels bitrate "
                    + (ov.bitrate(i) / 1000) + "kbps serial number=" + ov.serialnumber(i));
            System.out.println("    compressed length: " + ov.raw_total(i) + " bytes, play time: "
                    + ov.time_total(i) + "s");
            Comment vc = ov.getComment(i);
            System.out.println(vc);
        }
        //ov.clear();
    }
}
