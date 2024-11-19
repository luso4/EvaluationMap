
/**
 * This class represents a date as year, month and day in the month.
 *
 *
 */

public class Date {
    private final int year;
    private final int month;
    private final int day;

    /**
     * Constructs an immutable date object with the specified year, month and day.
     *
     * @param year
     * @param month
     * @param day
     */
    public Date(int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
    }

    /**
     * Returns the year of this date.
     *
     * @return the year of this date
     */
    public int getYear() {
        return year;
    }

    /**
     * Returns the month of this date.
     *
     * @return the month of this date
     */
    public int getMonth() {
        return month;
    }

    /**
     * Returns the day of this date.
     *
     * @return the day of this date
     */
    public int getDay() {
        return day;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Date) {
            if(this.year == ((Date)obj).year && this.month == ((Date)obj).month && this.day == ((Date)obj).day)
                return true;
        }

        return false;
    }

    @Override
    public String toString() {
        return day + "." + month + "." + year;
    }
}