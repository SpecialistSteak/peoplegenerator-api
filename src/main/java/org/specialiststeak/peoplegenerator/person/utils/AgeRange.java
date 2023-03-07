package org.specialiststeak.peoplegenerator.person.utils;

public record AgeRange(int low, int high){
    public int getLow() {
        return low;
    }
    public int getHigh() {
        return high;
    }
}
