package com.mark;

import java.awt.*;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * This Class outlines the design for a Game Manager object,
 * which controls a lot of the game processing.
 */

public class GameMgr implements Runnable, Globals{
    // Defines single object variables.
    protected Game_GUI gameFrame;
    protected Thread gameInstance;
    protected BufferStrategy bufferStrategy;
    protected Graphics graphics;
    protected ArrayList<Brick> bricks;
    protected Paddle paddle;
    protected Ball ball;
    protected Timer gameTime;
    protected DBmanager dbMgr;
    protected ArrayList<Score> allScores;
    protected Score scoreKeeper;

    // Flag variable to indicate "game state."
    protected boolean gameON = false;
    // Determines speed of redraw.
    protected int fps = FPS;
    // Creates array of color objects.
    protected Color[] brickColors = { Color.red, Color.orange, Color.yellow,
            Color.green, Color.blue, Color.magenta };

    // Defines non-mutable variables.
    protected int bricksPerRow;
    // Can change later on to make more "difficult."
    protected int rowsOfBricks = 6;


    protected int score;
    protected int lives = LIVES_START;
    protected int timeInGame;




/*************
 *  Constructor.
 *  **************/
    public GameMgr() {
        // Instantiates single objects.
        gameFrame = new Game_GUI();
        paddle = new Paddle();
        ball = new Ball();

        // Calculates how many bricks can fit on the board and
        // sets variable value.
        bricksPerRow = BOARD_WIDTH / BRICK_WIDTH;
        // Creates array to hold all the Brick objects.
        bricks = new ArrayList<Brick>();
        // Runs initial build method of bricks.
        makeBricks();

        // Sets up game timer for the clock.
        gameTime = new Timer();
        gameTime.schedule(new TimerTask() {
            @Override
            public void run() {
                timeInGame += 1;
            }
        },0,1000);

        allScores = new ArrayList<Score>();
        dbMgr = new DBmanager();
    }




/*************
 *  The RUN Method
 *  **************/
    // Standard run method that runs the app.
    public void run() {
        // Calculates how many nanoseconds are in one cycle.
        double microTicks = 1000000000/fps;
        double diff = 0;
        // Makes variable to hold time values down to the nanosecond.
        long currentTime;
        long prevCurrTime = System.nanoTime();

        // Loops until variable value is changed.
        while (gameON) {
            // Gets current time in nanoseconds.
            currentTime = System.nanoTime();
            // Calculates how much time has passed since the last cycle.
            diff += (currentTime - prevCurrTime) / microTicks;
            prevCurrTime = currentTime;
            // Checks if enough time has passed to redraw game.
            if (diff >= 1) {
                draw();
                diff = 0;
            }
        }
        // Runs Thread ending method as a redundant safety action.
        makeScore(score);
        gameFrame.showHighScores(allScores);
    }



/*************
 *  The DRAW Method
 *  **************/
    private void draw() {
        // Generates a BufferedStrategy object to utilize buffer frames
        // between renders.
        bufferStrategy = gameFrame.getBufferStrategy();
        // Checks if an instance already exists and creates one if not.
        if (bufferStrategy == null) {
            // Note: One buffer is not enough.
            gameFrame.createBufferStrategy(3);
            return;
        }
        // Makes graphics object for drawing.
        graphics = bufferStrategy.getDrawGraphics();
        // Clears board.
        graphics.clearRect(0, 0, BOARD_WIDTH, TOTAL_HEIGHT);

        /** Draw commands: */
        // Runs the background draw methods.
        gameFrame.draw(graphics);
        gameFrame.drawScoreboard(score, timeInGame, lives, graphics);
        // Runs the Paddle's draw method and passes an integer. The
        // integer comes from the game frame's keypressed event.
        paddle.draw(gameFrame.getMoveDirection(), graphics);
        // Runs the Ball's draw method.
        ball.draw(paddle.getX_loc(), paddle.getY_loc(), graphics);
        // Runs method to draw all active bricks.
        drawBricks();
        /***/

        // Runs method that detects any collisions between the ball and each
        // brick object.
        detectCollisions();

        // Checks if boolean flag in Ball Class is indicating the ball has hit
        // the floor and should be punished by losing a life.
        floorCheck();

        // Finalize process.
        bufferStrategy.show();
        graphics.dispose();
    }


/*************
 *  Game Thread Start and End.
 *  **************/
    // Creates new game instance/thread.
    public synchronized void startGame() {
        if (gameON) {
            return;
        }
        else {
            gameON = true;
        }
        gameInstance = new Thread(this);
        gameInstance.start();
    }

    // Ends game instance.
    public synchronized void endGame() {
        if (!gameON) {
            return;
        }
        else {
            gameON = false;
        }
        try {
            gameInstance.join();
        }
        catch (InterruptedException err) {
            err.printStackTrace();
        }
    }




/*************
 *  Makes all Bricks at start
 *  **************/
    protected void makeBricks() {
        // Makes Brick objects and store in array.
        for (int i = 0; i < rowsOfBricks; i++) {       // for num of rows...
            for (int k = 0; k < bricksPerRow; k++) {    // for bricks in each row...
                Brick b = new Brick(
                        (k * BRICK_WIDTH),       // x coord
                        ((i * BRICK_HEIGHT) + STATS_HEIGHT),      // y coord
                        brickColors[i]);         // color of brick
                bricks.add(b);
            }
        }
    }




/*************
 *  Brick Drawer
 *  **************/
    protected void drawBricks() {
        for (Brick b : bricks) {
            b.draw(graphics);
        }
    }




/*************
 *  Collision Detection
 *  **************/
    protected void detectCollisions() {
        // Gets the ball's current location.
        int ballx = ball.getX();
        int bally = ball.getY();
        // Creates a Rectangle object of the ball's location.
        Rectangle ballRect = new Rectangle(ballx, bally, BALL_DIAMETER, BALL_DIAMETER);
        // Loops through each brick and creates a Rectangle object for easy comparison
        // using builtin methods.
        for (Brick b : bricks) {
            Rectangle brickRect = b.getBrickRect();
            // Checks if the two objects intersect.
            if (ballRect.intersects(brickRect)) {
                // Creates two variables that hold how much of an overlap the two
                // objects have. This helps determine what side of the brick was hit.
                int xoverlap;
                int yoverlap;

                if (ballRect.getCenterX() < brickRect.getCenterX()) {
                    xoverlap = (ballx + BALL_DIAMETER) - b.x_loc;
                }
                else {
                    xoverlap = (b.x_loc + BRICK_WIDTH) - ballx;
                }

                if (ballRect.getCenterY() < brickRect.getCenterY()) {
                    yoverlap = (bally + BALL_DIAMETER) - b.y_loc;
                }
                else {
                    yoverlap = (b.y_loc + BRICK_HEIGHT) - bally;
                }
                // Runs ball's method to alter the direction it goes.
                ball.changeDirectionHitBrick(xoverlap, yoverlap);
                // Removes the struck brick from array so it won't be
                // drawn anymore.
                bricks.remove(b);
                // Adds points to total score and exits loop.
                score += POINTS_PER_HIT;
                return;
            }
        }
    }




    private void floorCheck() {
        if (ball.getFloorHit()) {
            lives--;
            // Checks if the player has run out of lives and ends the game if so.
            if (lives == 0) {
                gameover();
            }
            else {
                ball.setFloorHit(false);
                ball.resetBall();
            }
        }
    }



/*************
 *  Gameover (Visual) Method
 *  **************/
    protected void gameover() {
        // Runs game frame's draw method to clear all drawings.
        gameFrame.draw(graphics);
        // Sets the color and font of the upcoming messages.
        graphics.setColor(Color.red);
        graphics.setFont(new Font("Rockwell", Font.PLAIN, 40));
        // Displays message.
        int interval = (TOTAL_HEIGHT)/4;
        graphics.drawString("Gameover!", BALL_X/2, interval);
        graphics.setFont(new Font("Rockwell", Font.PLAIN, 33));
        graphics.drawString("Your Score: " + score, BALL_X/2, interval * 3);

        // Turns off loop and ends thread.
        gameON = false;
        endGame();
    }

    protected void makeScore(int score) {
        gameFrame.promptUsername();
        String username = gameFrame.getGoodSubmit();
        allScores = dbMgr.selectAll();
        java.sql.Date currDate = new java.sql.Date(new java.util.Date().getTime());
        boolean dbCheck = dbMgr.isExist(username, allScores);
        if (dbCheck) {
            dbMgr.updateEntry(username, score);
            for (Score s : allScores) {
                if (s.username.equalsIgnoreCase(username)) {
                    s.score = score;
                    s.scoreDate = currDate;
                    return;
                }
            }
        }
        else {
            scoreKeeper = new Score(username, score, currDate);
            dbMgr.addNewEntry(username, score);
            allScores.add(scoreKeeper);
        }
    }
}




