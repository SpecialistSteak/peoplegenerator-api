package org.specialiststeak.peoplegenerator.person.utils;

import lombok.experimental.UtilityClass;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

@UtilityClass
public class WriteToFile {
    private static final File FILE_NAME = new File("ip.log");
    private static final BufferedWriter WRITER;

    static {
        try {
            WRITER = new BufferedWriter(new FileWriter(FILE_NAME));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static synchronized void writeToFile(String data) {
        try {
            WRITER.write(data);
            WRITER.newLine();
            WRITER.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}