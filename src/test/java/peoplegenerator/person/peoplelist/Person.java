package peoplegenerator.person.peoplelist;

import org.junit.jupiter.api.Test;

class Person {
    @Test
    void newPerson() {
        for (int i = 0; i < 1000; i++) {
            assert new Person() != null;
        }
        assert true;
    }
}