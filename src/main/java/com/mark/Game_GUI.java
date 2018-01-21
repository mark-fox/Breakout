package com.mark;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.lang.reflect.Array;
import java.util.ArrayList;


/**
 * This Class outlines the design for the GUI object.
 */
public class Game_GUI extends JFrame implements KeyListener, Globals {
    private JTable table;
    private JPanel rootJPanel;

    // Variable represents what direction to move paddle.
    protected int moveDirection = 0;
    // Variable to hold username entry at end of game.
    protected String goodSubmit;
    // Ratio for placing strings on screen.
    private int interval = STATS_WIDTH / 7;


    // Getters.
    public int getMoveDirection() { return moveDirection; }
    public String getGoodSubmit() { return goodSubmit; }



    // Constructor.
    public Game_GUI() {
        // Sets up window aspects.
        this.setPreferredSize(new Dimension(BOARD_WIDTH, TOTAL_HEIGHT));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setVisible(true);

        // Adds key listener for paddle movements.
        this.addKeyListener(this);

        // Packs window.
        this.pack();

        // Adjusts window size.
        this.setSize(new Dimension(BOARD_WIDTH, TOTAL_HEIGHT));
        this.setLocationRelativeTo(null);
    }




    // Draw method.
    protected void draw(Graphics g) {
        g.setColor(Color.black);
        g.fillRect(0, 0, BOARD_WIDTH, TOTAL_HEIGHT);
    }




    // Draw method for the scoreboard at top.
    protected void drawScoreboard(int score, int time, int lives, Graphics g) {
        // Sets color for text.
        g.setColor(Color.white);

        // Formats the time into specific style.
        String timeTxt = formatTime(time);

        // Draws values to screen.
        g.drawString("Score: " + score, 5, 50);
        g.drawString("Lives: " + lives, interval * 6,50);
        g.drawString(timeTxt, STATS_WIDTH/2, 50);
    }




    // Transforms time value into a more appealing style.
    private String formatTime(int time) {
        String timeTxt;

        if (time >= 60) {
            int minutes = time / 60;
            int seconds = time % 60;
            if (seconds < 10) { timeTxt = minutes + ":0" + seconds; }
            else { timeTxt = minutes + ":" + seconds; }
        }
        else {
            if (time < 10) { timeTxt = "0:0" + time; }
            else { timeTxt = "0:" + time; }
        }
        return timeTxt;
    }




    // Method to display an input box for user input.
    protected void promptUsername() {
        String username = JOptionPane.showInputDialog("Please enter a username:");
        // Validates that user enters a value.
        while (username.equals("")) {
            username = JOptionPane.showInputDialog("Please enter a username:");
        }
        // Sets variable to input.
        goodSubmit = username;
    }





    protected void showHighScores(ArrayList<Score> scores) {
// TODO add timeout so still shows gameover screen briefly
        // Makes string arrays for the JTable.
        String[] columns =  {"Username", "High Score", "Date Achieved"};
        String[][] data = new String[scores.size()][scores.size()];
        // Loops through passed array.
        for (int i = 0; i < scores.size(); i++) {
            // Gets each Score object's values and adds them to a new string array.
            String[] temp = {scores.get(i).username, scores.get(i).score + "", scores.get(i).scoreDate + ""};
            data[i] = temp;
        }

        // Creates table, sets size, sets location, and adds to frame.
        table = new JTable(data, columns);
        table.setSize(BOARD_WIDTH, BOARD_HEIGHT/2);
        table.setLocation(0, (TOTAL_HEIGHT) / 5);
        this.add(new JScrollPane(table));
        // Needed method when overwriting frame from what I understand.
        this.validate();
    }





/******
 Keypress Listeners
 ********/
    @Override
    public void keyTyped(KeyEvent e) {

    }
    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            moveDirection = -1;
        }
        else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            moveDirection = 1;
        }
    }
    @Override
    public void keyReleased(KeyEvent e) {
        moveDirection = 0;
    }
}


