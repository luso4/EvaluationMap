package org.example;

import java.util.Calendar;
import java.util.HashMap;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

/**
 * This class represents a calendar application.
 *
 */

@SuppressWarnings("serial")
public class MyCalendar extends JFrame implements MouseListener {
    private static final int WIDTH = 1100;
    private static final int HEIGHT = 800;
    private static final Dimension MIN_SIZE = new Dimension(960, 760);
    private static final int BASIC_BORDER = 1;
    private static final int THICK_BORDER = 3;
    private static final int ROWS = 0;
    private static final int COLUMNS = 7;
    private static final int WINDOW_X_FIX = 100;
    private static final int WINDOW_Y_FIX = 70;

    private static final Font DAY_FONT = new Font("Tahoma", Font.BOLD, 18);
    private static final Font MONTH_FONT = new Font("Tahoma", Font.BOLD, 38);
    private static final Font YEAR_FONT = new Font("Tahoma", Font.PLAIN, 84);
    private static final EmptyBorder PANEL_BORDER = new EmptyBorder(10, 10, 10, 10);
    private static final Dimension RIGID_AREA = new Dimension(30, 0);
    public static final Color LIGHT_BLACK = new Color(42, 42, 42);
    public static final Color DARK_GRAY = new Color(55, 55, 55);
    public static final Cursor DEFAULT_CURSOR = new Cursor(Cursor.DEFAULT_CURSOR);
    public static final Cursor HAND_CURSOR = new Cursor(Cursor.HAND_CURSOR);

    private HashMap<String, CalendarEvent[]> calendarEventsMap;

    private JPanel headerPanel;			// holds the year and month labels
    private JPanel tablePanel;			// main calendar panel
    private JPanel daysPanel;			// sub-panel holds the days of the week
    private JPanel calendarPanel;  		// holds the background image
    private JPanel[] dayPanel;			// sub-panels hold each day of the week separatly
    private JLabel[] dayLabel; 			// days of the week labels
    private JLabel yearLabel;
    private JLabel monthLabel;

    private CalendarDay[] calendarDay;  // custom JPanels represent a calendar day
    private YearSelect yearSelect;		// popup window to select a year
    private MonthSelect monthSelect;	// popup window to select a month
    private Calendar calendar;
    private int lastDayOfMonth;
    private int year;
    private int month;
    private int today;

    /**
     * Constructs a calendar object.
     */
    public MyCalendar() {
        super("MyCalendar");
        calendarPanel = new JPanel(new GridLayout(ROWS, COLUMNS));
        tablePanel = new JPanel(new BorderLayout());
        daysPanel = new JPanel(new GridLayout(1, COLUMNS));
        headerPanel = new JPanel();
        yearLabel = new JLabel();
        monthLabel = new JLabel();
        dayPanel = new JPanel[Calendar.DAY_OF_WEEK];
        dayLabel = new JLabel[Calendar.DAY_OF_WEEK];
        calendarEventsMap = new HashMap<String, CalendarEvent[]>();

        getCalendarData();
        calendarDay = new CalendarDay[lastDayOfMonth];

        // set first day of the current month to find what day of the week
        // the month starts with.
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        yearSelect = new YearSelect(calendarPanel, this);
        monthSelect = new MonthSelect(calendarPanel, this);

        // set frame properties
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(WIDTH, HEIGHT);
        setMinimumSize(MIN_SIZE);
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/calendar.png")));

        initHeader();
        initCalendarWeek();
        initCalendarTable();

        // add panels to this frame
        add(headerPanel, BorderLayout.NORTH);
        add(tablePanel, BorderLayout.CENTER);

        validate();
        setVisible(true);
    }

    /**
     * Changes the year this calendar should represent.
     *
     * @param year the year this calendar should represent
     */
    public void setYear(int year) {
        this.year = year;
    }

    /**
     * Returns the year this calendar represents.
     *
     * @return the year this calendar represents
     */
    public int getYear() {
        return year;
    }

    /**
     * Changes the month this calendar should represent.
     *
     * @param year the month this calendar should represent
     */
    public void setMonth(int month) {
        this.month = month;
    }

    /**
     * Returns the month this calendar represents.
     *
     * @return the month this calendar represents
     */
    public int getMonth() {
        return month;
    }

    /**
     * Adds a new event to this calendar on the specified date.
     *
     * @param date the date on which to add the event
     * @param event the event to add
     */
    public void addCalendarEvent(String date, CalendarEvent event) {
        // this date has no events yet
        if(calendarEventsMap.get(date) == null) {
            CalendarEvent[] eventArray = new CalendarEvent[CalendarDay.MAX_EVENTS];
            eventArray[0] = event;
            calendarEventsMap.put(date, eventArray);

            // atleast one event has already been added on this date.
            // maximum 4 events on a single date are allowed.
        } else {
            for(int i = 0; i < CalendarDay.MAX_EVENTS; i++) {
                if(calendarEventsMap.get(date)[i] == null) {
                    calendarEventsMap.get(date)[i] = event;
                    break;
                }
            }
        }
    }

    /**
     * Returns all events at the specified date.
     *
     * @param date the date on which the events to be retrieved.
     * @return all events at the specified date
     */
    public CalendarEvent[] getEvants(String date) {
        return calendarEventsMap.get(date);
    }

    /**
     * Updates the calendar to represent the selected date.
     */
    public void updateCalendar() {
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        yearLabel.setText(year + "");
        monthLabel.setText((Months.getMonthByIndex(month).toString()));
        lastDayOfMonth = (Months.getMonthByIndex(month).getLastDay());
        calendarDay = new CalendarDay[lastDayOfMonth];

        initCalendarTable();
    }

    /**
     *
     * @param date
     * @param header
     * @param time
     */
    public void modifyCalendarEvent(String date, int i, CalendarEvent modEvent) {
        for(CalendarEvent event : calendarEventsMap.get(date)) {
            if(calendarEventsMap.get(date)[i].equals(event))
                calendarEventsMap.get(date)[i] = modEvent;
        }
    }

    /*
     * Retrieves current date and the number of the month's last day.
     */
    private void getCalendarData() {
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        today = calendar.get(Calendar.DATE);
        lastDayOfMonth = (Months.getMonthByIndex(month).getLastDay());
    }

    /*
     * Initializes the header of the calendar, which includes the current year and
     * month.
     */
    private void initHeader() {
        // set header properties
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.LINE_AXIS));
        headerPanel.setBackground(LIGHT_BLACK);
        headerPanel.setBorder(new LineBorder(Color.WHITE));

        // set month label properties
        headerPanel.add(Box.createRigidArea(RIGID_AREA));
        monthLabel.setFont(MONTH_FONT);
        monthLabel.setForeground(Color.WHITE);
        monthLabel.setText((Months.getMonthByIndex(month).toString()));
        monthLabel.setToolTipText("Select a month");
        headerPanel.add(monthLabel);

        // set year label properties
        headerPanel.add(Box.createRigidArea(RIGID_AREA));
        yearLabel.setFont(YEAR_FONT);
        yearLabel.setForeground(Color.WHITE);
        yearLabel.setText(year + "");
        yearLabel.setToolTipText("Select a year");
        headerPanel.add(yearLabel);

        // add listeners
        yearLabel.addMouseListener(this);
        monthLabel.addMouseListener(this);
    }

    /*
     * Initializes the first row if the calendar which shows
     * the days of the week.
     */
    private void initCalendarWeek() {
        tablePanel.setBackground(DARK_GRAY);
        calendarPanel.setBackground(DARK_GRAY);
        daysPanel.setBackground(DARK_GRAY);
        daysPanel.setBorder(PANEL_BORDER);
        tablePanel.setBorder(PANEL_BORDER);
        tablePanel.add(calendarPanel, BorderLayout.CENTER);

        int i = 0;
        for (DaysOfWeek day : DaysOfWeek.values()) {
            dayPanel[i] = new JPanel();
            dayPanel[i].setBackground(DARK_GRAY);
            dayLabel[i] = new JLabel(day.toString());
            dayLabel[i].setFont(DAY_FONT);
            dayLabel[i].setForeground(Color.WHITE);
            dayPanel[i].add(dayLabel[i]);
            daysPanel.add(dayPanel[i]);
            i++;
        }

        tablePanel.add(daysPanel, BorderLayout.NORTH);
    }

    /*
     * Initializes the calendar body with table of panels.
     */
    private void initCalendarTable() {
        String date;

        // fill gaps until first day of the month
        for (int j = 1; j < calendar.get(Calendar.DAY_OF_WEEK); j++)
            calendarPanel.add(new JLabel());

        // create table
        for (int j = 0; j < lastDayOfMonth; j++) {
            calendarDay[j] = new CalendarDay(j + 1 + "", this);
            calendarDay[j].setForeground(Color.WHITE);
            calendarDay[j].addMouseListener(this);
            calendarPanel.add(calendarDay[j]);
            date = year + "." + month + "." + calendarDay[j].getDayOfMonth();

            // mark the current day
            if (j + 1 == today) {
                calendarDay[j].setBorderThickness(THICK_BORDER);
                calendarDay[j].setBorderColor(CalendarDay.LIGHT_ORANGE);
            } else {
                calendarDay[j].setBorderThickness(BASIC_BORDER);
                calendarDay[j].setBorderColor(Color.WHITE);
            }

            // mark staturdays in color
            if ((j + calendar.get(Calendar.DAY_OF_WEEK)) % DaysOfWeek.DAYS_IN_WEEK == 0)
                calendarDay[j].setBorderColor(CalendarDay.ORANGE);

            // retrieve events data from database
            if (calendarEventsMap.get(date) != null) {
                for(CalendarEvent event : calendarEventsMap.get(date)) {
                    if(event != null)
                        calendarDay[j].setEvent(event);
                }
            }
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        // year label
        if (e.getSource() == yearLabel) {
            yearSelect.setLocation((int) yearLabel.getLocationOnScreen().getX() + WINDOW_X_FIX,
                    (int) yearLabel.getLocationOnScreen().getY() + WINDOW_Y_FIX);
            yearSelect.setVisible(true);
        }

        // month label
        if (e.getSource() == monthLabel) {
            monthSelect.setLocation((int) monthLabel.getLocationOnScreen().getX() + WINDOW_X_FIX,
                    (int) monthLabel.getLocationOnScreen().getY() + WINDOW_Y_FIX);
            monthSelect.setVisible(true);
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        // year label
        if (e.getSource() == yearLabel) {
            yearLabel.setCursor(HAND_CURSOR);
            yearLabel.setForeground(CalendarDay.LIGHT_ORANGE);
        }

        // month label
        if (e.getSource() == monthLabel) {
            monthLabel.setCursor(HAND_CURSOR);
            monthLabel.setForeground(CalendarDay.LIGHT_ORANGE);
        }
    }

    @Override
    public void mouseExited(MouseEvent e) {
        // year label
        if (e.getSource() == yearLabel)
            yearLabel.setForeground(Color.WHITE);

        // month label
        if(e.getSource() == monthLabel)
            monthLabel.setForeground(Color.WHITE);
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }
}