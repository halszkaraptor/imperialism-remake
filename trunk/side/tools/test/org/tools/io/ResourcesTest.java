/*
 * Copyright (C) 2011 Trilarion
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
package org.tools.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import org.junit.Test;

/**
 * Tests the Resources implementation of the org.tools.io package.
 */
public class ResourcesTest {

    /**
     *
     */
    @Test
    public void PerformanceTest() {
    }

    @Test
    public void FileOpening() {
        System.out.println("TEST file open");
        File file = new File("readme.txt");
        System.out.println(" file readme.txt exists: " + file.exists());
        file = new File("readme.txti");
        System.out.println(" file readme.txti exists: " + file.exists());
        file = new File(file, "humpdidumpty");
        System.out.println(" with nonsense addition: " + file.exists());
        file = new File("c:\\humpdidumpty");
        System.out.println(" or nonsense alone: " + file.exists());
        // conclusion: a new File instance can always be created, it does not have to exist
    }

    @Test
    public void FileCreation() throws IOException {
        System.out.println("TEST file creation");
        File file = new File("readyou.txt");
        if (!file.createNewFile()) {
            System.out.println("file readyou.txt could not be created");
        }
        if (!file.createNewFile()) {
            System.out.println("file readyou.txt could not be created on second time");
        }
        if (!file.delete()) {
            System.out.println("could not delete readyou.txt");
        }
        System.out.println("result of call to exist now " + file.exists());

        // conculsion: createNewFile throws an error if the file cannot be created for other reasons than that it is already existing
        // deletion should check (canWrite, isDirecty(), list.isEmpty(), and then call delete(), call to exists then gives righty false
    }

    @Test
    public void ReadingAFile() throws IOException {
        System.out.println("TEST reading a file");
        Resource res = ResourceUtils.asResource("readme.txt");
        String n = res.getName();
        System.out.println("file: " + n);
        BufferedReader r = ResourceUtils.getReader(res);
        System.out.println(" first line: " + r.readLine()); // only print first line
        r.close();
    }

    @Test
    public void ReadingInsideArchive() throws IOException {
        System.out.println("TEST reading inside an archive");
        Resource res = ResourceUtils.asResource("JLokalize.jar/lang/JLokalize_de.properties");
        String n = res.getName();
        System.out.println("file: " + n);
        BufferedReader r = ResourceUtils.getReader(res);
        System.out.println(" first line: " + r.readLine()); // only print first line
        r.close();
    }

    @Test
    public void ReadingTwiceInsideArchiveAtTheSameTime() throws IOException {
        System.out.println("TEST reading in an archive two times at the same time");
        Resource res1 = ResourceUtils.asResource("JLokalize.jar/lang/JLokalize_de.properties");
        Resource res2 = ResourceUtils.asResource("JLokalize.jar/lang/JLokalize_en.properties");

        String n1 = res1.getName();
        System.out.println("file: " + n1);
        BufferedReader r1 = ResourceUtils.getReader(res1);
        System.out.println(" first line: " + r1.readLine()); // only print first line

        String n2 = res2.getName();
        System.out.println("file: " + n2);
        BufferedReader r2 = ResourceUtils.getReader(res2);
        System.out.println(" first line: " + r2.readLine()); // only print first line

        r1.close();
        r2.close();
    }

    @Test
    public void ReadingArchiveNoPath() throws IOException {
        System.out.println("TEST operations inside an archive");
        Resource res = ResourceUtils.asResource("JLokalize.jar");

        System.out.println(" dir * on: " + res.getName());
        for (Resource r : res.list(".*")) {
            System.out.println(r.getName());
        }

        res = ResourceUtils.asResource("JLokalize.jar/lang/");
        System.out.println(" dir JLokalize*.properties on: " + res.getName());
        for (Resource r : res.list("^(JLokalize).*(\\.properties)$")) {
            System.out.println(r.getName());
        }
    }
}
