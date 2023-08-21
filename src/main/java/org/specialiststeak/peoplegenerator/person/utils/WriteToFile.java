package org.specialiststeak.peoplegenerator.person.utils;

import lombok.experimental.UtilityClass;

import java.awt.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

@UtilityClass
public class WriteToFile {
    private static final BufferedWriter WRITER;
    private static final String FILE_NAME = "ip.log";

    static {
        try {
            WRITER = new BufferedWriter(new FileWriter(FILE_NAME, true));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static synchronized void writeToFile(String data) {
        try {
            WRITER.append(data);
            WRITER.append("\n");
            WRITER.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}