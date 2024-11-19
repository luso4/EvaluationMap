
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
 * This class represents a frame of years to select from within the
 * calendar frame.
 *
 */
@SuppressWarnings("serial")
public class YearSelect extends JFrame implements ActionListener, MouseListener {
    private static final int WIDTH = 500;
    private static final int HEIGHT = 300;
    private static final int YEARS = 16;
    private static final int ROWS = 4;
    private static final int COLUMNS = 4;
    private static final Font YEAR_FONT = new Font("Sans Serif", Font.BOLD, 18);
    private static final Font HOVER_FONT = new Font("Sans Serif", Font.BOLD, 22);

    private JPanel yearPanel;
    private JPanel tablePanel;
    private JButton[] yearBtn;
    private MyCalendar calendar;

    /**
     * Constructs a frame of years to select from. Parameter calendar, the class
     * MyCalendar which contains this private class.
     */
    public YearSelect(JPanel tablePanel, MyCalendar calendar) {
        super("Calendar year selection");
        this.tablePanel = tablePanel;
        this.calendar = calendar;
        yearPanel = new JPanel(new GridLayout(ROWS, COLUMNS));
        yearBtn = new JButton[YEARS];

        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setSize(WIDTH, HEIGHT);
        setResizable(false);
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/calendar.png")));

        initTable();
        add(yearPanel);
        validate();
    }

    /*
     * Initializes the table of years to select from.
     */
    private void initTable() {
        for (int i = 0, j = 2010; i < YEARS; i++, j++) {
            yearBtn[i] = new JButton(j + "");
            yearBtn[i].setBackground(MyCalendar.LIGHT_BLACK);
            yearBtn[i].setForeground(Color.WHITE);
            yearBtn[i].setFont(YEAR_FONT);
            yearBtn[i].setContentAreaFilled(false);
            yearBtn[i].setOpaque(true);
            yearBtn[i].addActionListener(this);
            yearBtn[i].addMouseListener(this);
            yearPanel.add(yearBtn[i]);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        for (JButton yearBtn : yearBtn) {
            if (e.getSource() == yearBtn) {
                calendar.setYear(Integer.parseInt(yearBtn.getText()));
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
        for (JButton yearBtn : yearBtn) {
            if (e.getSource() == yearBtn) {
                yearBtn.setBackground(Color.DARK_GRAY);
                yearBtn.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
                yearBtn.setFont(HOVER_FONT);
                break;
            }
        }
    }

    @Override
    public void mouseExited(MouseEvent e) {
        for (JButton yearBtn : yearBtn) {
            if (e.getSource() == yearBtn) {
                yearBtn.setBackground(MyCalendar.LIGHT_BLACK);
                yearBtn.setBorder(null);
                yearBtn.setFont(YEAR_FONT);
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