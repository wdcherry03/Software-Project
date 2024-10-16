// Holds a player's information in a JPanel

import java.util.*;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class PlayerPanel extends JPanel {

    // Variables
    Player player;
    JTextField playerID;
    JTextField playerCodename;
    JTextField playerHardwareID;

    // Constructor
    public PlayerPanel(Player p) {

        // Sets the dimensions of this JPanel
        this.setLayout(new GridLayout(1, 3));

        // Loads Player
        player = p;

        // Loads text fields
        playerCodename = new JTextField(player.codename);
        playerCodename.setEditable(false);

        playerID = new JTextField(String.valueOf(player.playerID));
        playerID.setEditable(false);

        playerHardwareID = new JTextField(String.valueOf(player.hardwareID));
        playerHardwareID.setEditable(false);

        // Adds them to this panel
        this.add(playerCodename);
        this.add(playerID);
        this.add(playerHardwareID);
    }
}