package org.specialiststeak.peoplegenerator.person.utils;

import lombok.Data;

@Data
public final class AgeRange {
    private int low;
    private int high;

    public int getHigh() {
        return high;
    }

    public int getLow() {
        return low;
    }

    public AgeRange(int low, int high) {
        this.low = low;
        this.high = high;
    }
}
