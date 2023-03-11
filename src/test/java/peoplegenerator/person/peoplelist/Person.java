//package peoplegenerator.person.peoplelist;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.databind.SerializationFeature;
//import com.opencsv.exceptions.CsvValidationException;
//import org.junit.jupiter.api.Test;
//import org.specialiststeak.peoplegenerator.person.peoplelist.Person;
//
//import java.io.IOException;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.specialiststeak.peoplegenerator.person.utils.Utils.startup;
//
//class PersonTest {
//
//    @Test
//    void testConstructor() throws CsvValidationException, IOException {
//        startup(false);
//        Person person = new Person();
//        assertNotNull(person.getName());
//        assertNotEquals(0, person.getAge());
//        assertNotNull(person.getJob());
//        assertNotNull(person.getDateOfBirth());
//        assertNotEquals(0, person.getIncomeInUSD());
//        assertNotEquals(0, person.getCreditScore());
//        assertNotNull(person.getCreditCardNumber());
//        assertNotNull(person.getGender());
//        assertNotEquals(0, person.getHeight());
//        assertNotEquals(0, person.getWeight());
//        assertNotNull(person.getEyeColor());
//        assertNotNull(person.getEmail());
//        assertNotEquals(0, person.getGPA());
//        assertNotNull(person.getBloodType());
//        assertNotNull(person.getUsername());
//        assertNotEquals(0, person.getPoliticalLeaning());
//        assertNotNull(person.getReligion());
//        assertNotNull(person.getAddress());
//    }
//
//    @Test
//    void testJsonSerialization() throws Exception {
//        startup(false);
//        ObjectMapper mapper = new ObjectMapper();
//        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
//        Person person = new Person();
//        String jsonString = mapper.writeValueAsString(person);
//        assertNotNull(jsonString);
//        assertFalse(jsonString.isBlank());
//    }
//}