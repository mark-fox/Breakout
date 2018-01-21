package com.mark;

import java.nio.file.Paths;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;

/**
 * This Class outlines the design for managing a SQLite database connection.
 */
public class DBmanager {
    protected String dirPath;
    protected String createTbl;
    protected String showAll;
    protected String insertNew;
    protected String updateOld;

    // String for database url.
    final String DB_URL = "jdbc:sqlite:highscores.db";


    // Constructor.
    public DBmanager() {
        try {
            Class.forName("org.sqlite.JDBC").newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        // Creates sql statements formatted for prepared statements.
        createTbl = "CREATE TABLE IF NOT EXISTS scores (" +
                "username text PRIMARY KEY, " +
                "high_score integer NOT NULL," +
                "score_date date NOT NULL)";
        showAll = "SELECT * FROM scores";
        insertNew = "INSERT INTO scores VALUES(?, ?, ?)";       // not sure if needs column names
        updateOld = "UPDATE scores SET high_score = ? WHERE username = ?";
        // Runs method to create database table.
        createTable();
    }

    private void createTable() {
// TODO move statics to interface class later when working
        // Connects to database and performs query to create table.
        try (Connection connection = DriverManager.getConnection(DB_URL);
            Statement statement = connection.createStatement()) {
                statement.executeUpdate(createTbl);
        }
        catch (SQLException err) {
            err.printStackTrace();
        }
        System.out.println("database created successfully!");
    }






    protected ArrayList<Score> selectAll() {
        // Connects to database and runs select all query.
        try (Connection connection = DriverManager.getConnection(DB_URL);
             Statement statement = connection.createStatement()) {
            // Stores results in ResultSet.
            ResultSet rs = statement.executeQuery(showAll);
            if (rs != null) {
                // Creates new array and stores data in it as new Score objects.
                ArrayList<Score> scores = new ArrayList<>();
                try {
                    while (rs.next()) {
                        String username = rs.getString("username");
                        int score = rs.getInt("high_score");
                        java.sql.Date scoreDate = rs.getDate("score_date");
                        scores.add(new Score(username, score, scoreDate));
                    }
                }
                catch (SQLException err) {
                    err.printStackTrace();
                }
                return scores;
            }
        }
        catch (SQLException err) {
            err.printStackTrace();
        }
        return null;
    }





    protected void addNewEntry(String username, int score) {
        // Connects to database and runs new entry query.
        try (Connection connection = DriverManager.getConnection(DB_URL);
             PreparedStatement ps = connection.prepareStatement(insertNew)) {
            // Sets prepared statement.
            ps.setString(1, username);
            ps.setInt(2, score);
            Date currDate = new Date();
            // Makes Date object that is formatted for SQL.
            java.sql.Date sqlDate = new java.sql.Date(currDate.getTime());
            ps.setDate(3, sqlDate);
            ps.executeUpdate();
        }
        catch (SQLException err) {
            err.printStackTrace();
        }
    }





    protected void updateEntry(String username, int newScore) {
        // Connects to database and runs update query.
        try (Connection connection = DriverManager.getConnection(DB_URL);
        PreparedStatement ps = connection.prepareStatement(updateOld)) {
            ps.setInt(1, newScore);
            ps.setString(2, username);
            ps.executeUpdate();
        }
        catch (SQLException err) {
            err.printStackTrace();
        }
    }






    protected boolean isExist(String name, ArrayList<Score> scores) {
        // Loops through array and checks the username.
        for (Score s : scores) {
            if (s.username.equalsIgnoreCase(name)) {
                return true;
            }
        }
        return false;
    }





// TODO   for testing purposes: remove later.
    public static void main(String[] args) {
        DBmanager mgr = new DBmanager();
//        mgr.addNewEntry("test2", 42);
        ArrayList<Score> scores = mgr.selectAll();
//        ArrayList<Score> scores = mgr.getRSscores(rs);
//        try {
//            String name = rs.getCursorName();
//            System.out.println(name);
//            System.out.println(rs.getString("username"));
//            while (rs.next()) {
//                name = rs.getCursorName();
//                System.out.println(name);
//                System.out.println(rs.getString("username"));
//            }
//        }
//        catch (SQLException err) {
//            err.printStackTrace();
//        }
//        ArrayList<Score> scores = new ArrayList<>();

            System.out.println(scores.size());
        System.out.println(scores.get(0).scoreDate);
        // -446731200
    }
}






