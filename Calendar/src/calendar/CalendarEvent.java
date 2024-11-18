

/**
 * This class represents an event in the calendar application.
 *
 */

public class CalendarEvent {
    private String header;
    private String details;
    private String startTime;
    private String endTime;

    /**
     * Constructs an event object with the specified event title, event details,
     * when the event starts and when it ends.
     *
     * @param header the header of the event
     * @param details the details of the event
     * @param startTime the time the event starts
     * @param endTime the time the event ends
     */
    public CalendarEvent(String header, String details, String startTime, String endTime) {
        this.header = header;
        this.details = details;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    /**
     * Returns the header of the event.
     *
     * @return the header of the event
     */
    public String getHeader() {
        return header;
    }

    /**
     * Sets the header of the event.
     *
     * @param details the header of the event
     */
    public void setHeader(String header) {
        this.header = header;
    }

    /**
     * Returns the details of the event.
     *
     * @return the details of the event
     */
    public String getDetails() {
        return details;
    }

    /**
     * Sets the details of the event.
     *
     * @param details the details of the event
     */
    public void setDetails(String details) {
        this.details = details;
    }

    /**
     * Returns the time the event starts at.
     *
     * @return the time the event starts at
     */
    public String getStartTime() {
        return startTime;
    }

    /**
     * Sets the time the event starts at.
     *
     * @param time the time the event starts at
     */
    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    /**
     * Returns the time the event ends at.
     *
     * @return the time the event ends at
     */
    public String getEndTime() {
        return endTime;
    }

    /**
     * Sets the time the event ends at.
     *
     * @param time the time the event ends at
     */
    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof CalendarEvent) {
            if (this.header.equals(((CalendarEvent) obj).header)
                    && this.startTime.equals(((CalendarEvent) obj).startTime))
                return true;
        }

        return false;
    }
}