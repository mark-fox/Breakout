package com.mark;

import java.awt.*;

import static java.awt.Color.black;

/**
 * This Class outlines the design for a Brick object.
 */
public class Brick implements Globals{

    // Defines location variables.
    protected int x_loc;
    protected int y_loc;
    protected Color color;
    private Rectangle brickRect;



    // Constructor.
    Brick(int x, int y, Color color) {
        // Sets Brick's location to the provided coordinates.
        this.x_loc = x;
        this.y_loc = y;
        this.color = color;

        // Creates rectangle object for comparing against Ball.
        brickRect = new Rectangle(x_loc, y_loc, BRICK_WIDTH, BRICK_HEIGHT);
    }




    protected void draw(Graphics g) {
        // Sets Brick's color and draws rectangle.
        g.setColor(this.color);
        g.fillRect(this.x_loc, this.y_loc, BRICK_WIDTH, BRICK_HEIGHT);
        // Sets color to black and draws a "border" around Brick.
        g.setColor(black);
        g.drawRect(this.x_loc, this.y_loc, BRICK_WIDTH, BRICK_HEIGHT);
    }



    // Getter.
    public Rectangle getBrickRect() {return brickRect; }
}
