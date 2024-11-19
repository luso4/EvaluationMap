package org.example;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 * This class represents an event editor, which handles the event user input.
 *
 */

@SuppressWarnings("serial")
public class EventEditor extends JFrame implements ActionListener {
    private static final int WIDTH = 390;
    private static final int HEIGHT = 180;
    private static final int EXPANSION_HEIGHT = 380;
    private static final int DETAILS_ROWS = 8;
    private static final int DETAILS_COLS = 20;
    private static final int MAX_EMPTY_LBLS = 4;

    private static final Font EVENT_FONT = new Font("Sans Serif", Font.PLAIN, 18);
    private static final Font BTN_FONT = new Font("Calibri", Font.BOLD, 16);
    private static final Font TIME_FONT = new Font("Sans Serif", Font.BOLD, 16);
    private static final Dimension EDIT_FIELD_SIZE = new Dimension(230, 28);
    private static final Dimension SAVE_BTN_SIZE = new Dimension(70, 30);
    private static final Dimension DET_BTN_SIZE = new Dimension(110, 30);
    private static final Color RED = new Color(153, 0, 0);
    private static final Insets MARGIN = new Insets(0, 10, 0, 10);
    private static final String[] TIME = { "06:00", "06:30", "07:00", "07:30", "08:00", "08:30", "09:00", "09:30",
            "10:00", "10:30", "11:00", "11:30", "12:00", "12:30", "13:00", "13:30", "14:00", "14:30", "15:00", "15:30",
            "16:00", "16:30", "17:00", "17:30", "18:00", "18:30", "19:00", "19:30", "20:00", "20:30", "21:00", "21:30",
            "22:00", "22:30", "23:00", "23:30" };

    private JScrollPane scroll;
    private JPanel mainEditPanel;	// main panel - holding all north region of the editor
    private JPanel iconsPanel;		// holds the icons representing the edit fields
    private JPanel eventPanel; 		// holds the event haeder and time editabels
    private JPanel eventEditPanel;  // sub-panel holds the event textField
    private JPanel eventTimePanel;  // sub-panel holds the event time edit
    private JPanel btnsPanel; 		// sub-panel holds the buttons
    private JPanel detailsPanel;	// holds the details text area
    private JTextField eventField;
    private JLabel calendarIconLbl;
    private JLabel eventTimeFromLbl;
    private JLabel eventTimeToLbl;
    private JLabel clockIconLbl;
    private JLabel emptyLbl[];
    private JComboBox<String> timeFrom;
    private JComboBox<String> timeTo;
    private JTextArea detailsArea;
    private JButton saveBtn;
    private JButton detailsBtn;
    private CalendarDay caller; 	// the day panel in the calendar which called this editor
    private MyCalendar calendar;
    private ImageIcon clockIcon;
    private ImageIcon calendarIcon;
    private String date;
    private int eventIndex;
    private boolean newEvent;

    /**
     * Constructs an event editor window, which handles the event
     * user input.
     *
     * @param calendar the calendar in which to add or edit an event.
     */
    public EventEditor(MyCalendar calendar) {
        super("Event Editor");
        this.calendar = calendar;
        mainEditPanel = new JPanel(new BorderLayout());
        iconsPanel = new JPanel(new GridLayout(2, 3));
        eventPanel = new JPanel(new GridLayout(2, 1));
        eventEditPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        eventTimePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        detailsPanel = new JPanel();
        btnsPanel = new JPanel();
        eventField = new JTextField("Add an event");
        calendarIconLbl = new JLabel();
        eventTimeFromLbl = new JLabel("From ");
        eventTimeToLbl = new JLabel(" to ");
        clockIconLbl = new JLabel();
        emptyLbl = new JLabel[MAX_EMPTY_LBLS];
        timeFrom = new JComboBox<String>(TIME);
        timeTo = new JComboBox<String>(TIME);
        detailsArea = new JTextArea("Add more details", DETAILS_ROWS, DETAILS_COLS);
        scroll = new JScrollPane(detailsArea);
        saveBtn = new JButton("Save");
        detailsBtn = new JButton("Add details");
        MouseHandler mouseHandler = new MouseHandler();

        for(int i = 0; i < MAX_EMPTY_LBLS; i++)
            emptyLbl[i] = new JLabel();

        try {
            clockIcon = new ImageIcon(ImageIO.read(getClass().getResourceAsStream("/clock.png")));
            calendarIcon = new ImageIcon(ImageIO.read(getClass().getResourceAsStream("/calendar.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }

        setSize(WIDTH, HEIGHT);
        getContentPane().setBackground(MyCalendar.LIGHT_BLACK);
        setResizable(false);
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/calendar.png")));

        addWindowListener(new WindowHandler());
        detailsBtn.addActionListener(this);
        detailsBtn.addMouseListener(mouseHandler);
        saveBtn.addActionListener(this);
        saveBtn.addMouseListener(mouseHandler);
        eventField.addMouseListener(mouseHandler);

        buildComponentsHierarchy();
        customizeComponents();
        validate();
    }

    /**
     * Returns the event title field text of this window.
     *
     * @return the event title field text of this window
     */
    public JTextField getEventField() {
        return eventField;
    }

    /**
     * Returns the event details text area of this window.
     *
     * @return the event details text area of this window
     */
    public JTextArea getDetailsArea() {
        return detailsArea;
    }

    /**
     * Sets the CalendarDay object which called this event editor.
     *
     * @param caller the CalendarDay object which called this event editor
     */
    public void setCaller(CalendarDay caller) {
        this.caller = caller;
    }

    /**
     * Returns true if the added event is new, otherwise returns false.
     *
     * @return true if the added event is new, otherwise returns false.
     */
    public boolean isNewEvent() {
        return newEvent;
    }

    /**
     * Changes whether the added event is new or not, in which case is being edited.
     *
     * @param newEvent true if the added event is new, false otherwise
     */
    public void setNewEvent(boolean newEvent) {
        this.newEvent = newEvent;
    }

    /**
     * Initializes a new event display.
     */
    public void displayNewEvent() {
        eventField.setText("Add an event");
        timeFrom.setSelectedItem("06:00");
        timeTo.setSelectedItem("06:00");
        detailsArea.setText("Add more details");

        setVisible(true);
    }

    /**
     * Displays an event in a calendar from a specified date and event index.
     *
     * @param date the date from which to retrieve the event
     * @param i the index of the event in the event list of the specified date
     */
    public void displayEvent(String date, int i) {
        this.date = date;
        this.eventIndex = i;

        eventField.setText(calendar.getEvants(date)[i].getHeader());
        timeFrom.setSelectedItem((String) calendar.getEvants(date)[i].getStartTime());
        timeTo.setSelectedItem((String) calendar.getEvants(date)[i].getEndTime());
        detailsArea.setText(calendar.getEvants(date)[i].getDetails());

        setVisible(true);
    }

    /*
     * Builds the structure of this window's components.
     */
    private void buildComponentsHierarchy() {
        // add components to sub-panels
        eventEditPanel.add(eventField);
        eventTimePanel.add(eventTimeFromLbl);
        eventTimePanel.add(timeFrom);
        eventTimePanel.add(eventTimeToLbl);
        eventTimePanel.add(timeTo);

        iconsPanel.add(emptyLbl[0]);
        iconsPanel.add(calendarIconLbl);
        iconsPanel.add(emptyLbl[1]);
        iconsPanel.add(emptyLbl[2]);
        iconsPanel.add(clockIconLbl);
        iconsPanel.add(emptyLbl[3]);

        detailsPanel.add(scroll);
        btnsPanel.add(detailsBtn);
        btnsPanel.add(saveBtn);

        // add sub-panels to main panels
        eventPanel.add(eventEditPanel);
        eventPanel.add(eventTimePanel);

        mainEditPanel.add(eventPanel, BorderLayout.CENTER);
        mainEditPanel.add(iconsPanel, BorderLayout.WEST);

        // add main panels to this frame
        add(mainEditPanel, BorderLayout.NORTH);
        add(btnsPanel, BorderLayout.SOUTH);
    }

    /*
     * Sets the properties for this window's components.
     */
    private void customizeComponents() {
        // buttons sizes
        saveBtn.setPreferredSize(SAVE_BTN_SIZE);
        detailsBtn.setPreferredSize(DET_BTN_SIZE);
        eventField.setPreferredSize(EDIT_FIELD_SIZE);

        // background colors
        mainEditPanel.setBackground(MyCalendar.LIGHT_BLACK);
        iconsPanel.setBackground(MyCalendar.LIGHT_BLACK);
        eventPanel.setBackground(MyCalendar.LIGHT_BLACK);
        eventEditPanel.setBackground(MyCalendar.LIGHT_BLACK);
        eventTimePanel.setBackground(MyCalendar.LIGHT_BLACK);
        btnsPanel.setBackground(MyCalendar.LIGHT_BLACK);
        detailsPanel.setBackground(MyCalendar.LIGHT_BLACK);
        detailsBtn.setBackground(Color.GRAY);
        saveBtn.setBackground(RED);
        eventField.setBackground(Color.DARK_GRAY);
        detailsArea.setBackground(Color.DARK_GRAY);

        // text colors
        eventTimeToLbl.setForeground(Color.LIGHT_GRAY);
        eventTimeFromLbl.setForeground(Color.LIGHT_GRAY);
        detailsBtn.setForeground(Color.WHITE);
        saveBtn.setForeground(Color.WHITE);
        eventField.setForeground(Color.GRAY);
        detailsArea.setForeground(Color.WHITE);
        timeFrom.setForeground(Color.DARK_GRAY);
        timeTo.setForeground(Color.DARK_GRAY);

        // fonts
        eventField.setFont(EVENT_FONT);
        eventTimeFromLbl.setFont(TIME_FONT);
        eventTimeToLbl.setFont(TIME_FONT);
        timeFrom.setFont(TIME_FONT);
        timeTo.setFont(TIME_FONT);
        detailsArea.setFont(EVENT_FONT);
        detailsBtn.setFont(BTN_FONT);
        saveBtn.setFont(BTN_FONT);

        // transparency
        saveBtn.setContentAreaFilled(false);
        saveBtn.setOpaque(true);
        detailsBtn.setContentAreaFilled(false);
        detailsBtn.setOpaque(true);

        // other properties
        scroll.setForeground(Color.WHITE);
        eventField.setCaretColor(Color.WHITE);
        detailsArea.setCaretColor(Color.WHITE);
        eventField.setMargin(MARGIN);
        detailsArea.setMargin(MARGIN);
        clockIconLbl.setIcon(clockIcon);
        calendarIconLbl.setIcon(calendarIcon);

        eventField.setEditable(false);
    }

    /*
     * Creates a new CalendarEvent object once the save button is clicked,
     * if a new event has been added. otherwise modifies an existing event.
     */
    private void saveEvent() {
        String date = calendar.getYear() + "." + calendar.getMonth() + "." + caller.getDayOfMonth();
        CalendarEvent event = new CalendarEvent(eventField.getText(), detailsArea.getText(),
                (String) timeFrom.getSelectedItem(), (String) timeTo.getSelectedItem());

        if (newEvent) {
            calendar.addCalendarEvent(date, event);
            caller.setEvent(event);

        } else {
            calendar.modifyCalendarEvent(this.date, eventIndex, event);
            caller.modifyEvent(event, eventIndex);
        }
    }

    /*
     * Restores the original size of this window.
     */
    private void shrink() {
        remove(detailsPanel);
        setSize(WIDTH, HEIGHT);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == saveBtn) {
            saveEvent();
            shrink();
            setVisible(false);

        } else if (e.getSource() == detailsBtn) {
            add(detailsPanel, BorderLayout.CENTER);
            setSize(WIDTH, EXPANSION_HEIGHT);

        }

        calendar.validate();
        calendar.repaint();
    }

    /*
     * This class handles the EventEditor window visibility and size.
     */
    private class WindowHandler extends WindowAdapter {
        @Override
        public void windowClosing(WindowEvent e) {
            shrink();
            setVisible(false);
            validate();
        }
    }

    /*
     * This class only extends the MouseAdapter to handle mouse events.
     */
    private class MouseHandler extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getSource() == eventField) {
                eventField.setEditable(true);
                eventField.setForeground(Color.WHITE);
                validate();
            }
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            if(e.getSource() == saveBtn)
                saveBtn.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

            else if(e.getSource() == detailsBtn)
                detailsBtn.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        }

        @Override
        public void mouseExited(MouseEvent e) {
            if(e.getSource() == saveBtn)
                saveBtn.setBorder(null);

            else if(e.getSource() == detailsBtn)
                detailsBtn.setBorder(null);
        }
    }
}