/* File for entry screen */

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.File;
import javax.imageio.ImageIO;

import java.util.*;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class entry extends JPanel{

    private JFrame frame;
    private JPanel redTeamPanel;
    private JPanel greenTeamPanel;
    private JPanel buttonPanel;

    public void run() {

        // Create frame
        frame = new JFrame("Entry Terminal");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        // Create panels for teams
        redTeamPanel = new JPanel(new GridLayout(20, 1));
        greenTeamPanel = new JPanel(new GridLayout(20, 1));

        // Add checkboxes for each player for each team
        redTeamPanel.add(new JLabel("RED TEAM", JLabel.CENTER));
        redTeamPanel.setBackground(Color.RED);
        greenTeamPanel.add(new JLabel("GREEN TEAM", JLabel.CENTER));
        greenTeamPanel.setBackground(Color.GREEN);

        // Add checkboxes
        for (int i = 1; i <= 19; i++) {
            redTeamPanel.add(new JCheckBox("Player " + i));
            greenTeamPanel.add(new JCheckBox("Player " + i));
        }

        // Create panel to hold both team panels
        JPanel mainPanel = new JPanel(new GridLayout(1, 2));
        mainPanel.add(redTeamPanel);
        mainPanel.add(greenTeamPanel);

        // Setting buttons
        buttonPanel = new JPanel(new GridLayout(1, 8));
        buttonPanel.add(new JButton("Edit Game"));
        buttonPanel.add(new JButton("Game Parameters"));
        buttonPanel.add(new JButton("Start Game"));
        buttonPanel.add(new JButton("Preferred Games"));
        buttonPanel.add(new JButton("View Game"));
        buttonPanel.add(new JButton("Flick Sync"));
        buttonPanel.add(new JButton("Clear Game"));

        // Add panels to frame
        frame.add(mainPanel, BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    public static void main(String[] args) {
        entry entryScreen = new entry();
        entryScreen.run();
    }
}