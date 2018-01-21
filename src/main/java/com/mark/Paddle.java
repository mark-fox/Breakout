package com.mark;

import java.awt.*;

/**
 * This Class outlines the design of a Paddle object.
 */
public class Paddle implements Globals{

    protected int START_X;
    protected int START_Y;

    protected int x_loc;
    protected int y_loc;
    // How many pixels paddle moves when user moves it.
    protected int moveDist = 5;

    // Getters for current location.
    public int getX_loc() { return x_loc; }
    public int getY_loc() { return y_loc; }




    // Constructor.
    Paddle() {
        this.x_loc = ((BOARD_WIDTH/2) - (PADDLE_WIDTH/2));
        this.y_loc = ((BOARD_HEIGHT + STATS_HEIGHT) - (2*PADDLE_HEIGHT));

        // Might use to reset position after losing a life or something.
        this.START_X = this.x_loc;
        this.START_Y = this.y_loc;
    }




    // Draw method.
    protected void draw(int direction, Graphics g) {
        this.x_loc += (moveDist * direction);

        if (this.x_loc < 0) { this.x_loc = 0; }
        if (this.x_loc + PADDLE_WIDTH > BOARD_WIDTH) {
            this.x_loc = BOARD_WIDTH - PADDLE_WIDTH;
        }
        g.setColor(Color.white);
        g.fillRect(this.x_loc, this.y_loc, PADDLE_WIDTH, PADDLE_HEIGHT);
    }
}
