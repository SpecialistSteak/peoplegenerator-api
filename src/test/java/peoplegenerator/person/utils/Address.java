package peoplegenerator.person.utils;

import org.junit.jupiter.api.Test;

public class Address {

    @Test
    void newAddress() {
        for (int i = 0; i < 1000; i++) {
            assert new Address() != null;
        }
        assert true;
    }
}