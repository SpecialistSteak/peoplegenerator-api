package org.specialiststeak.peoplegenerator.person.temp;

import lombok.Data;

import static org.specialiststeak.peoplegenerator.person.temp.TimeUnits.staticConvertTime;

@Data
public class Times{
    private final long startTime;
    private long endTime;
    private long elapsedTime;
    private String title;

    /**
     * Creates a new Times object.
     *
     * @param startTime   The start time in nanoseconds.
     */
    public Times(long startTime) {
        this.startTime = startTime;
        this.endTime = 0;
        this.elapsedTime = 0;
        this.title = "";
    }

    /**
     * Creates a new Times object.
     *
     * @param startTime   The start time in nanoseconds.
     * @param title       The title of the times object.
     */
    public Times(long startTime, String title) {
        this.startTime = startTime;
        this.endTime = 0;
        this.elapsedTime = 0;
        this.title = title;
    }

    /**
     * @return The start time in nanoseconds.
     */
    public long getStartTime() {
        return startTime;
    }

    /**
     * @return The "string-ified" times object.
     */
    public String toString() {
        return "Times" + (
                (getTitle() == null || getTitle().equals("")) ?
                        "" :
                        (": \"" + getTitle() + "\"")) + " {" +
                "\n\t\tstartTime= " + startTime +
                "ns,\n\t\tendTime= " + endTime +
                "ns,\n\t\telapsedTime= " + elapsedTime +
                "ns\n\t}";
    }

    /**
     * Returns the "string-ified" times object with the time units specified.
     *
     * @param timeUnits The time units to use.
     * @return The "string-ified" times object.
     */
    public String toString(TimeUnits timeUnits) {
        return "Times" + ((getTitle() == null || getTitle().equals("")) ? "" : (": \"" + getTitle())) + "\" {" +
                "\n\t\tstartTime= " + staticConvertTime(startTime, timeUnits) + timeUnits.toString() +
                "\n\t\tendTime= " + staticConvertTime(endTime, timeUnits) + timeUnits.toString() +
                ",\n\t\telapsedTime= " + staticConvertTime(elapsedTime, timeUnits) + timeUnits.toString() +
                "\n\t}";
    }

    /**
     * sets the end time
     *
     * @param endTime the end time in nanoseconds
     */
    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    /**
     * sets the elapsed time
     *
     * @param l the elapsed time in nanoseconds
     */
    public void setElapsedTime(long l) {
        this.elapsedTime = l;
    }

    /**
     * @return the title of the times object
     */
    public String getTitle() {
        return title;
    }
}