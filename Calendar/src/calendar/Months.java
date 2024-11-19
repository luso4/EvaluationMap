public enum Months {
    JANUARY("January", 31, 0),
    FEBRUARY("February", 28, 1),
    MARCH("March", 31, 2),
    APRIL("April", 30, 3),
    MAY("May", 31, 4),
    JUNE("June", 30, 5),
    JULY("July", 31, 6),
    AUGUST("August", 31, 7),
    SEPTEMBER("September", 30, 8),
    OCTOBER("October", 31, 9),
    NOVEMBER("November", 30, 10),
    DECEMBER("December", 31, 11);

    final private String month;
    final private int index;
    private int lastDay;

    /**
     * Constructs a Month with a specified title-case letters name,
     * last day of the month and index as the month number.
     *
     * @param month the month name in low letters
     * @param lastDay the last day of the month
     * @param index the number of the month
     */
    Months(String month, int lastDay, int index) {
        this.month = month;
        this.lastDay = lastDay;
        this.index = index;
    }

    /**
     * Sets February month to be 28 or 29 days long.
     *
     * @param leapYear true if this is a leap year. false otherwise
     */
    public void setLeapYear(boolean leapYear) {
        FEBRUARY.lastDay = leapYear ? 29 : 28;
    }

    /**
     * Returns the last day of the month.
     *
     * @return the last day of the month
     */
    public int getLastDay() {
        return lastDay;
    }

    /**
     * Returns the index of the month.
     *
     * @return the index of the month
     */
    public int getIndex() {
        return index;
    }

    /**
     * Returns the month name from a specified index.
     *
     * @param index the index of the month
     * @return the month name from a specified index
     */
    public static Months getMonthByIndex(int index) {
        switch (index) {
            case 0:
                return JANUARY;
            case 1:
                return FEBRUARY;
            case 2:
                return MARCH;
            case 3:
                return APRIL;
            case 4:
                return MAY;
            case 5:
                return JUNE;
            case 6:
                return JULY;
            case 7:
                return AUGUST;
            case 8:
                return SEPTEMBER;
            case 9:
                return OCTOBER;
            case 10:
                return NOVEMBER;
            case 11:
                return DECEMBER;
            default:
                return null;
        }
    }

    @Override
    public String toString() {
        return month;
    }
}