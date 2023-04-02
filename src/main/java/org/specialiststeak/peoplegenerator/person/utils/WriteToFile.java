package org.specialiststeak.peoplegenerator.person.utils;

import lombok.experimental.UtilityClass;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

@UtilityClass
public class WriteToFile {
    /**
     * Writes given data to a file in a thread-safe manner and appends a newline character.
     *
     * @param data     The data to write to the file.
     * @param fileName The name of the file to write to.
     */
    public static synchronized void writeToFile(String data, String fileName) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write(data);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
