package com.mark;

/**
 * This Interface defines the static values used between objects.
 */
public interface Globals {
    int BOARD_WIDTH = 300;
    int BOARD_HEIGHT = 400;
    int PADDLE_WIDTH = 50;
    int PADDLE_HEIGHT = 10;
    int BRICK_WIDTH = 100;
    int BRICK_HEIGHT = 20;
    int BALL_DIAMETER = 20;
    int STATS_WIDTH = BOARD_WIDTH;
    int STATS_HEIGHT = 60;
    int FPS = 30;
    int LIVES_START = 3;
    int POINTS_PER_HIT = 20;
    int TOTAL_HEIGHT = BOARD_HEIGHT + STATS_HEIGHT;
    int BALL_X = ((BOARD_WIDTH / 3) - (BALL_DIAMETER / 2));
    int BALL_Y = ((TOTAL_HEIGHT / 2) - (BALL_DIAMETER / 2));
    int BALL_X_SPD = 2;
    int BALL_Y_SPD = 5;
}




