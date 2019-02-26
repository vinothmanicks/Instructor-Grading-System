package com.cannapaceus.grader;

import java.sql.*;
import java.util.Scanner;

class DBService {
    private String conString = "jdbc:h2:~/IGP;";

    private Connection con = null;
    private Statement stm = null;
    private ResultSet rs = null;
    private ResultSetMetaData rsmd = null;

    public void loginDB() {
        String user;
        String pass;


        boolean connected = false;

        while (!connected) {
            Scanner reader = new Scanner(System.in);
            System.out.print("Enter database username: ");
            user = reader.next();
            System.out.print("Enter database password: ");
            pass = reader.next();

            connected = initConnection(user, pass);
        }
    }

    boolean initConnection(String username, String password) {
        try {
            con = DriverManager.getConnection(conString + "IFEXISTS=TRUE", username, password);

            System.out.println("Database exists");
            return true;
        } catch (SQLException e) {
            if (e.getErrorCode() == 90013) {
                System.out.println("Database does not exist");
                databaseSetup(username, password);

                System.out.println("Database created");

                return true;
            } else if (e.getErrorCode() == 28000) {
                System.out.println("Incorrect username or password");
            } else {
                e.printStackTrace();
            }

            return false;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) { /* ignored */}
            }
            if (stm != null) {
                try {
                    stm.close();
                } catch (SQLException e) { /* ignored */}
            }
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) { /* ignored */}
            }
        }
    }

    private void databaseSetup(String username, String password) {
        try {
            con = DriverManager.getConnection(conString, username, password);
            stm = con.createStatement();

            stm.execute("CREATE TABLE TERMS (\n" +
                    "TERMID IDENTITY NOT NULL,\n" +
                    "ESEASON ENUM ('Winter', 'Spring', 'Summer', 'Fall'),\n" +
                    "IYEAR INT,\n" +
                    "BARCHIVED BOOLEAN\n" +
                    ");");

            stm.execute("CREATE TABLE COURSES ( \n" +
                    "COURSEID IDENTITY NOT NULL, \n" +
                    "SCOURSENAME VARCHAR (255), \n" +
                    "SCOURSEID VARCHAR (50),\n" +
                    "SCOURSEDEPT VARCHAR (50),\n" +
                    "FMEAN REAL, \n" +
                    "FMEDIAN REAL, \n" +
                    "FMODE REAL, \n" +
                    "FSTDDEV REAL, \n" +
                    "BARCHIVED BOOLEAN, \n" +
                    "ITERM BIGINT NOT NULL \n" +
                    ");");

            stm.execute("CREATE TABLE CATEGORIES (\n" +
                    "CATEGORYID IDENTITY NOT NULL,\n" +
                    "SCATEGORYNAME VARCHAR (255),\n" +
                    "FWEIGHT REAL,\n" +
                    "ICOURSE BIGINT NOT NULL\n" +
                    ");");

            stm.execute("CREATE TABLE ASSIGNMENTS (\n" +
                    "ASSIGNMENTID IDENTITY NOT NULL,\n" +
                    "SASSIGNMENTNAME VARCHAR (255),\n" +
                    "DTDUEDATE DATE,\n" +
                    "DTASSIGNEDDATE DATE,\n" +
                    "FMEAN REAL,\n" +
                    "FMEDIAN REAL,\n" +
                    "FMODE REAL,\n" +
                    "FSTDDEV REAL,\n" +
                    "BDROPPED BOOLEAN,\n" +
                    "FMAXSCORE REAL,\n" +
                    "FWEIGHT REAL,\n" +
                    "ICATEGORY BIGINT,\n" +
                    "ICOURSE BIGINT NOT NULL\n" +
                    ");");

            stm.execute("CREATE TABLE GRADES (\n" +
                    "GRADEID IDENTITY NOT NULL,\n" +
                    "FGRADE REAL,\n" +
                    "BSUBMITTED BOOLEAN,\n" +
                    "BOVERDUE BOOLEAN,\n" +
                    "BMISSING BOOLEAN,\n" +
                    "BDROPPED BOOLEAN,\n" +
                    "IASSIGNMENT BIGINT NOT NULL,\n" +
                    "ISTUDENT BIGINT NOT NULL,\n" +
                    "ICOURSE BIGINT NOT NULL,\n" +
                    ");");

            stm.execute("CREATE TABLE STUDENTS (\n" +
                    "STUDENTID IDENTITY NOT NULL,\n" +
                    "SFIRSTMINAME VARCHAR(255),\n" +
                    "SLASTNAME VARCHAR(255),\n" +
                    "SSTUDENTID VARCHAR(50),\n" +
                    "SEMAIL VARCHAR(255),\n" +
                    "ICOURSE BIGINT NOT NULL\n" +
                    ");");

            stm.execute("CREATE TABLE QUESTIONS (\n" +
                    "QUESTIONID IDENTITY NOT NULL,\n" +
                    "SQUESTION CLOB,\n" +
                    "BMC BOOLEAN,\n" +
                    "BTF BOOLEAN,\n" +
                    "BSA BOOLEAN,\n" +
                    "IMINS SMALLINT,\n" +
                    "IDIFFICULTY TINYINT,\n" +
                    "ARRSANSWERS ARRAY,\n" +
                    "ARRSCOURSENAMES ARRAY\n" +
                    ");");

        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) { /* ignored */}
            }
            if (stm != null) {
                try {
                    stm.close();
                } catch (SQLException e) { /* ignored */}
            }
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) { /* ignored */}
            }
        }
    }
}
