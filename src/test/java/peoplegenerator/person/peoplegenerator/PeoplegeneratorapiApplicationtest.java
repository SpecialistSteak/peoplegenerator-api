package peoplegenerator.person.peoplegenerator;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.specialiststeak.peoplegenerator.PeoplegeneratorapiApplication;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.specialiststeak.peoplegenerator.person.utils.Utils.startup;

class PeoplegeneratorapiApplicationTests {

    @BeforeAll
    static void setup() {
        startup(true);
        startup(true);
    }

    @Test
    void testIndex() {
        PeoplegeneratorapiApplication controller = new PeoplegeneratorapiApplication();
        String result = controller.index();
        assertEquals("index", result);
    }

    @Test
    void testGetStarted() {
        PeoplegeneratorapiApplication controller = new PeoplegeneratorapiApplication();
        String result = controller.getStarted();
        assertEquals("get-started", result);
    }

    @Test
    void testEndpoints() {
        PeoplegeneratorapiApplication controller = new PeoplegeneratorapiApplication();
        String result = controller.endpoints();
        assertEquals("endpoints", result);
    }

    @Test
    void testFields() {
        PeoplegeneratorapiApplication controller = new PeoplegeneratorapiApplication();
        String result = controller.fields();
        assertEquals("fields", result);
    }

    @Test
    void testAbout() {
        PeoplegeneratorapiApplication controller = new PeoplegeneratorapiApplication();
        String result = controller.about();
        assertEquals("about", result);
    }
}

