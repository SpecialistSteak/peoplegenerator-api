package org.specialiststeak.peoplegenerator;

import com.opencsv.exceptions.CsvValidationException;
import lombok.experimental.UtilityClass;
import org.specialiststeak.peoplegenerator.person.peoplelist.Person;

import java.io.IOException;

import static org.specialiststeak.peoplegenerator.person.utils.Utils.startup;

@UtilityClass
public class Testing {
    public static void main(String[] args) throws CsvValidationException, IOException {
        startup(false);
        System.out.println(new Person());
        long start = System.nanoTime();
        new Person();
        long end = System.nanoTime();
        System.out.println("Took " + (end - start) + "ns");
    }
}
