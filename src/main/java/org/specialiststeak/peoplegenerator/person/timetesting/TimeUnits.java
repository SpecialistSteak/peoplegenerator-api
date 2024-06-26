package org.specialiststeak.peoplegenerator.person.timetesting;

public enum TimeUnits {
    NANOSECONDS,
    MICROSECONDS,
    MILLISECONDS,
    SECONDS,
    MINUTES,
    HOURS,
    DAYS;

    /**
     * @return the string representation of the time unit.
     */
    @Override
    public String toString() {
        return switch (this) {
            case NANOSECONDS -> "ns";
            case MICROSECONDS -> "μs";
            case MILLISECONDS -> "ms";
            case SECONDS -> "s";
            case MINUTES -> "m";
            case HOURS -> "h";
            case DAYS -> "d";
        };
    }

    /**
     * Converts nanoseconds to other time units without the need for an instance.
     *
     * @param ns        the nanoseconds to convert.
     * @param timeUnits the time unit to convert to.
     * @return the converted time.
     */
    public static long staticConvertTime(long ns, TimeUnits timeUnits) {
        return switch (timeUnits) {
            case NANOSECONDS -> ns;
            case MICROSECONDS -> ns / 1000;
            case MILLISECONDS -> ns / 1000000;
            case SECONDS -> ns / 1000000000;
            case MINUTES -> ns / 60000000000L;
            case HOURS -> ns / 3600000000000L;
            case DAYS -> ns / 86400000000000L;
            default -> 0;
        };
    }
}
