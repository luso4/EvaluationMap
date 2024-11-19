

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * This class represents a frame of months to select from within the
 * calendar frame.
 *
 */
@SuppressWarnings("serial")
public class MonthSelect extends JFrame implements ActionListener, MouseListener {
    private static final int WIDTH = 500;
    private static final int HEIGHT = 300;
    private static final int MONTHS = 12;
    private static final int ROWS = 3;
    private static final int COLUMNS = 4;
    private static final Font MONTH_FONT = new Font("Sans Serif", Font.BOLD, 16);
    private static final Font HOVER_FONT = new Font("Sans Serif", Font.BOLD, 20);

    private JPanel monthPanel;
    private JPanel tablePanel;
    private JButton[] monthBtn;
    private MyCalendar calendar;

    /**
     * Constructs a frame of months to select from. Parameter calendar, the class
     * MyCalendar which contains this private class.
     */
    public MonthSelect(JPanel tablePanel, MyCalendar calendar) {
        super("Calendar month selection");
        this.tablePanel = tablePanel;
        this.calendar = calendar;
        monthPanel = new JPanel(new GridLayout(ROWS, COLUMNS));
        monthBtn = new JButton[MONTHS];

        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setSize(WIDTH, HEIGHT);
        setResizable(false);
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/calendar.png")));

        initTable();
        add(monthPanel);
        validate();
    }

    /*
     * Initializes the table of months to select from.
     */
    private void initTable() {
        int i = 0;
        for (Months month : Months.values()) {
            monthBtn[i] = new JButton(month.toString());
            monthBtn[i].setBackground(MyCalendar.LIGHT_BLACK);
            monthBtn[i].setForeground(Color.WHITE);
            monthBtn[i].setFont(MONTH_FONT);
            monthBtn[i].setContentAreaFilled(false);
            monthBtn[i].setOpaque(true);
            monthBtn[i].addActionListener(this);
            monthBtn[i].addMouseListener(this);
            monthPanel.add(monthBtn[i]);
            i++;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        for (int i = 0; i < MONTHS; i++) {
            if (e.getSource() == monthBtn[i]) {
                calendar.setMonth(i);
                tablePanel.removeAll();
                calendar.updateCalendar();
                tablePanel.repaint();
                setVisible(false);
                break;
            }
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        for (int i = 0; i < MONTHS; i++) {
            if (e.getSource() == monthBtn[i]) {
                monthBtn[i].setBackground(Color.DARK_GRAY);
                monthBtn[i].setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
                monthBtn[i].setFont(HOVER_FONT);
                break;
            }
        }
    }

    @Override
    public void mouseExited(MouseEvent e) {
        for (int i = 0; i < MONTHS; i++) {
            if (e.getSource() == monthBtn[i]) {
                monthBtn[i].setBackground(MyCalendar.LIGHT_BLACK);
                monthBtn[i].setBorder(null);
                monthBtn[i].setFont(MONTH_FONT);
                break;
            }
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }
}