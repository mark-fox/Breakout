package com.mark;

import java.sql.Date;

/**
 * This Class outlines the design of a Score object.
 */
public class Score {
    protected String username;
    protected int score;
    protected Date scoreDate;

    // Constructor.
    public Score(String user, int aScore, Date theDate) {
        this.username = user;
        this.score = aScore;
        this.scoreDate = theDate;
    }

    @Override
    public String toString() {
        return "User: " + username + "'s high score: " + score;
    }
}
