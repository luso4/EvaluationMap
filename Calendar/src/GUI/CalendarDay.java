package org.example;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;



@SuppressWarnings("serial")
public class CalendarDay extends JPanel implements MouseListener {
    private static final int TEXT_X = 22;
    private static final int TEXT_Y = 12;
    private static final int RECT_X = 3;
    private static final int RECT_Y = 3;
    private static final int SIZE_OFFSET = 6;
    private static final int ARC = 30;
    public static final int MAX_EVENTS = 4;
    private static final int MAX_STR_SIZE = 10;
    private static final int EDIT_WINDOW_X_FIX = 150;
    private static final Font DAY_PANEL = new Font("Tahoma", Font.BOLD, 11);
    private static final Font EVENT_FONT = new Font("Calibri", Font.PLAIN, 14);
    private static final Border PANEL_BORDER = BorderFactory.createEmptyBorder(7, 7, 7, 7);
    public static final Color ORANGE = new Color(255, 178, 102);
    public static final Color LIGHT_ORANGE = new Color(255, 204, 153);

    private static EventEditor editEvent;
    private String dayOfMonth;
    private int borderThickness;
    private Color borderColor;
    private JPanel[] events;
    private MyCalendar calendar;

    /**
     * Constructs a CalendarDay with its associated day number as specified.
     *
     * @param dayOfMonth the day number to write on the JPanel surface
     * @param calendar the calendar in which to add this CalendarDay object
     */
    public CalendarDay(String dayOfMonth, MyCalendar calendar) {
        this.dayOfMonth = dayOfMonth;
        this.calendar = calendar;
        setLayout(new GridLayout(MAX_EVENTS, 1));
        setBackground(MyCalendar.DARK_GRAY);
        setBorder(PANEL_BORDER);
        borderThickness = 1;
        events = new JPanel[MAX_EVENTS];
        editEvent = new EventEditor(calendar);

        addMouseListener(this);
    }

    /**
     * Returns the date this panel represents.
     *
     * @return the date this panel represents
     */
    public String getDayOfMonth() {
        return dayOfMonth;
    }

    /**
     * Changes the thickness of the CalendarDay border.
     *
     * @param borderThickness the thickness of the CalendarDay border
     */
    public void setBorderThickness(int borderThickness) {
        this.borderThickness = borderThickness;
    }

    /**
     * Changes the color of the CalendarDay border.
     *
     * @param borderColor the color of the CalendarDay border
     */
    public void setBorderColor(Color borderColor) {
        this.borderColor = borderColor;
    }

    /**
     * Sets an event for this day.
     *
     * @param event the event to be set for this day.
     */
    public void setEvent(CalendarEvent event) {
        StringBuilder header = cutEventHeader(event);

        // search the next null panel and add the new event
        for (int i = 0; i < MAX_EVENTS; i++) {
            if (events[i] == null) {
                events[i] = new JPanel(new FlowLayout(FlowLayout.LEFT));
                events[i].setBackground(MyCalendar.DARK_GRAY);
                events[i].addMouseListener(this);
                JLabel bulletPoint = new JLabel("\u25CF");
                JLabel eventLbl = new JLabel(event.getStartTime() + " " + header);
                eventLbl.setForeground(Color.WHITE);
                eventLbl.setFont(EVENT_FONT);
                bulletPoint.setFont(EVENT_FONT);
                bulletPoint.setForeground(Color.GREEN);
                events[i].add(bulletPoint);
                events[i].add(eventLbl);
                add(events[i]);
                validate();
                break;
            }
        }
    }

    /**
     * Modifies the specified event.
     *
     * @param event the event to be modified
     * @param i the index of the event in the event list associated to this day
     */
    public void modifyEvent(CalendarEvent event, int i) {
        StringBuilder header = cutEventHeader(event);

        JLabel eventLbl = new JLabel(event.getStartTime() + " " + header);
        eventLbl.setFont(EVENT_FONT);
        eventLbl.setForeground(Color.WHITE);
        events[i].remove(1);
        events[i].add(eventLbl);
        validate();
    }

    /*
     * Cuts the event string to fit inside the panel if it
     * exceeds 10 characters.
     */
    private StringBuilder cutEventHeader(CalendarEvent event) {
        StringBuilder header = new StringBuilder();

        if (event.getHeader().length() > MAX_STR_SIZE)
            header.append(event.getHeader().substring(0, MAX_STR_SIZE));
        else
            header.append(event.getHeader());

        return header;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // draw panel border
        g2.setStroke(new BasicStroke(borderThickness));
        g2.setColor(borderColor);
        g2.drawRoundRect(RECT_X, RECT_Y, getWidth() - SIZE_OFFSET, getHeight() - SIZE_OFFSET, ARC, ARC);

        // write day number on panel surface
        if (dayOfMonth != null) {
            g2.setFont(DAY_PANEL);
            g2.setColor(Color.WHITE);
            g2.drawString(dayOfMonth, getWidth() - TEXT_X, getHeight() - TEXT_Y);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        String date = calendar.getYear() + "." + calendar.getMonth() + "." + dayOfMonth;
        editEvent.setLocation((int) getLocationOnScreen().getX() + EDIT_WINDOW_X_FIX,
                (int) getLocationOnScreen().getY());

        // add new event
        if (calendar.getEvants(date) == null || e.getSource() == this) {
            editEvent.setNewEvent(true);
            editEvent.displayNewEvent();
            editEvent.setCaller(this);
            editEvent.setVisible(true);
            return;

        }

        for (int i = 0; i < MAX_EVENTS; i++) {
            if (events[i] != null && e.getSource() == events[i]) {
                editEvent.displayEvent(date, i);
                editEvent.setNewEvent(false);
                editEvent.setCaller(this);
                break;
            }
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        setCursor(MyCalendar.HAND_CURSOR);
    }

    @Override
    public void mouseExited(MouseEvent e) {
        setCursor(MyCalendar.DEFAULT_CURSOR);
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }
}