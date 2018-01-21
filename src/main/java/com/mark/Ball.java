package com.mark;

import java.awt.*;

/**
 * This Class outlines the design of a Ball object.
 */
public class Ball implements Globals {
    // Defines Ball's location and velocity/speed variables.
    protected int x_loc;
    protected int y_loc;
    protected int x_spd;
    protected int y_spd;
    private int radius;

    private int rightWall;
    private int floor;

    // Flag to show ball has gone off the deep end.
    protected boolean floorHit = false;


    // Getters.
    public int getX() { return this.x_loc; }
    public int getY() { return this.y_loc; }
    public boolean getFloorHit() { return this.floorHit; }
    public void setFloorHit(boolean changeIt) { this.floorHit = changeIt; }




    // Constructor.
    public Ball() {
        radius = BALL_DIAMETER / 2;
        rightWall = BOARD_WIDTH - BALL_DIAMETER;
        floor = TOTAL_HEIGHT - BALL_DIAMETER;
        // Sets the Ball's starting location to 1/3 of window's width
        // and 1/2 of window's height.
        resetBall();
    }




    // Draw/redraw method.
    protected void draw(int paddleX, int paddleY, Graphics g) {
        // Adds the current speeds to the Ball's current location.
        this.x_loc += this.x_spd;
        this.y_loc += this.y_spd;
        // Draws circle.
        g.fillOval(this.x_loc, this.y_loc, BALL_DIAMETER, BALL_DIAMETER);
        // Runs wall collision detecting method.
        // May be replaced with a global method later on.
        changeDirectionHitWall(paddleX, paddleY);
    }





    protected void changeDirectionHitWall(int paddleX, int paddleY) {
        // Checks if the Ball is at the left wall.
        if (this.x_loc <= 0 && this.y_loc > STATS_HEIGHT) {
            this.x_spd *= -1;
        }
        // Checks if the Ball is at the right wall.
        else if (this.x_loc >= rightWall && this.y_loc > STATS_HEIGHT) {
            this.x_spd *= -1;
        }
        // Checks if the Ball is at the top wall.
        else if (this.y_loc <= STATS_HEIGHT && this.x_loc > 0) {
            this.y_spd *= -1;
        }
        // Detects floor to remove life.
        else if (this.y_loc >= floor && this.x_loc > 0) {
            this.floorHit = true;
        }

        // Checks if ball hit the paddle.
        if ((this.y_loc + BALL_DIAMETER) >= paddleY) {
            if (((this.x_loc + radius) >= paddleX) &&
                    ((this.x_loc + BALL_DIAMETER) <= (paddleX + PADDLE_WIDTH) + radius)) {
                if (this.y_spd >= 0) {
                    this.y_spd *= -1;
                }
            }
        }
    }




    // Moves the ball back to its starting position.
    protected void resetBall() {
        this.x_loc = BALL_X;
        this.y_loc = BALL_Y;
        this.x_spd = BALL_X_SPD;
        this.y_spd = BALL_Y_SPD;
    }




    // Uses passed variables to change the direction of the ball when
    // a brick is hit.
    protected void changeDirectionHitBrick(int xoverlap, int yoverlap) {
        if (xoverlap == yoverlap) {
            this.x_spd *= -1;
            this.y_spd *= -1;
        }
        else if (xoverlap < yoverlap) {
            this.x_spd *= -1;
        }
        else {
            this.y_spd *= -1;
        }
    }
}
