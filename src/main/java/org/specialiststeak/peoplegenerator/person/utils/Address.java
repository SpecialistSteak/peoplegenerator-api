package org.specialiststeak.peoplegenerator.person.utils;

import lombok.Data;

import java.util.Locale;

import static org.specialiststeak.peoplegenerator.person.peoplelist.Person.*;

@Data
public class Address {
    String address;
    String nationality;
    String countryCode;
    String phoneNumber;
    String IPAddress;

    public Address(String address, String nationality, String countryCode, String phoneNumber, String ipAddress) {
        this.address = address;
        this.nationality = nationality;
        this.countryCode = countryCode;
        this.phoneNumber = phoneNumber;
        this.IPAddress = ipAddress;
    }
}

