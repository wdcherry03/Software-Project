import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CountdownTimer extends JFrame {

    private JLabel timeLabel;
    private Timer timer;
    private int count;

    public CountdownTimer(int seconds) {
        setTitle("Countdown Timer");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new FlowLayout());

        timeLabel = new JLabel(formatTime(seconds));
        timeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        add(timeLabel);

        count = seconds;
        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                count--;
                timeLabel.setText(formatTime(count));

                if (count == 0) {
                    timer.stop();
                    JOptionPane.showMessageDialog(CountdownTimer.this, "Time's up!");
                }
            }
        });

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        timer.start();
    }

    private String formatTime(int seconds) {
        int minutes = seconds / 60;
        int remainingSeconds = seconds % 60;
        return String.format("%02d:%02d", minutes, remainingSeconds);
    }
}