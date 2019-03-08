package com.cannapaceus.grader;

import java.sql.*;
import java.time.LocalDate;
import java.util.*;

class DBService {
    private String conString = "jdbc:h2:~/IGP;";

    private String user;
    private String pass;

    public void loginDB() {


        boolean connected = false;

        while (!connected) {
            Scanner reader = new Scanner(System.in);
            System.out.print("Enter database username: ");
            user = reader.next();
            System.out.print("Enter database password: ");
            pass = reader.next();

            connected = initConnection();
        }
    }

    private boolean initConnection() {
        Connection con = null;
        try {
            con = DriverManager.getConnection(conString + "IFEXISTS=TRUE", user, pass);

            System.out.println("Database exists");
            return true;
        } catch (SQLException e) {
            if (e.getErrorCode() == 90013) {
                System.out.println("Database does not exist");
                databaseSetup();

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
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) { /* ignored */}
            }
        }
    }

    private void databaseSetup() {
        Connection con = null;
        Statement stm = null;
        try {
            con = DriverManager.getConnection(conString, user, pass);
            stm = con.createStatement();

            stm.execute("CREATE TABLE TERMS (\n" +
                    "TERMID IDENTITY,\n" +
                    "ESEASON ENUM ('Winter', 'Spring', 'Summer', 'Fall'),\n" +
                    "IYEAR INT,\n" +
                    "BARCHIVED BOOLEAN\n" +
                    ");");

            stm.execute("CREATE TABLE COURSES ( \n" +
                    "COURSEID IDENTITY, \n" +
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
                    "CATEGORYID IDENTITY,\n" +
                    "SCATEGORYNAME VARCHAR (255),\n" +
                    "FWEIGHT REAL,\n" +
                    "ICOURSE BIGINT NOT NULL\n" +
                    ");");

            stm.execute("CREATE TABLE ASSIGNMENTS (\n" +
                    "ASSIGNMENTID IDENTITY,\n" +
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
                    "GRADEID IDENTITY,\n" +
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
                    "STUDENTID IDENTITY,\n" +
                    "SFIRSTMINAME VARCHAR(255),\n" +
                    "SLASTNAME VARCHAR(255),\n" +
                    "SSTUDENTID VARCHAR(50),\n" +
                    "SEMAIL VARCHAR(255),\n" +
                    "FAVERAGE REAL,\n" +
                    "ICOURSE BIGINT NOT NULL\n" +
                    ");");

            stm.execute("CREATE TABLE QUESTIONS (\n" +
                    "QUESTIONID IDENTITY,\n" +
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

    //TODO: boolean StoreTerm(Term termToStore)

    /**
     * Function to store a course in the database and set the DBID of the course that was passed in
     * @param courseToStore
     * @param lTermID ID of the term that the course belongs to
     * @return true if the course was stored successfully
     */
    boolean StoreCourse(Course courseToStore, long lTermID) {
        boolean retValue;

        Connection con = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        try {
            String sql = "INSERT INTO COURSES" +
                    "(SCOURSENAME, SCOURSEID, SCOURSEDEPT, FMEAN, FMEDIAN, FMODE, FSTDDEV, BARCHIVED, ITERM)" +
                    "VALUES" +
                    "(?, ?, ?, ?, ?, ?, ?, ?, ?)";

            con = DriverManager.getConnection(conString, user, pass);
            stm = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            stm.setString(1, courseToStore.getCourseName());
            stm.setString(2, courseToStore.getCourseID());
            stm.setString(3, null); //TODO: stm.setString(3, courseToStore.getDept());
            stm.setFloat(4, 0.0f);  //TODO: stm.setFloat(4, courseToStore.getStatistics().getMean());
            stm.setFloat(5, 0.0f);  //TODO: stm.setFloat(5, courseToStore.getStatistics().getMedian());
            stm.setFloat(6, 0.0f);  //TODO: stm.setFloat(6, courseToStore.getStatistics().getMode());
            stm.setFloat(7, 0.0f);  //TODO: stm.setFloat(7, courseToStore.getStatistics().getStdDev());
            stm.setBoolean(8, false);
            stm.setLong(9, lTermID);

            stm.executeUpdate();

            rs = stm.getGeneratedKeys();
            rs.next();
            courseToStore.setDBID(rs.getLong(1));

            retValue = true;

        } catch (Exception e) {
            e.printStackTrace();
            retValue = false;
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

        return retValue;
    }

    /**
     * Function to store a category in the database and set the DBID of the category that was passed in
     * @param categoryToStore
     * @param lCourseID ID of the course that the category belongs to
     * @return true if the category was stored successfully
     */
    boolean StoreCategory(Category categoryToStore, long lCourseID) {
        boolean retValue;

        Connection con = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        try {
            String sql = "INSERT INTO CATEGORIES" +
                    "(SCATEGORYNAME, FWEIGHT, ICOURSE)" +
                    "VALUES" +
                    "(?, ?, ?)";

            con = DriverManager.getConnection(conString, user, pass);
            stm = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            stm.setString(1, null); //TODO: stm.setString(1, categoryToStore.getCategoryName());
            stm.setFloat(2, 0);     //TODO: stm.setFloat(2, categoryToStore.getWeight());
            stm.setLong(3, lCourseID);

            stm.executeUpdate();

            rs = stm.getGeneratedKeys();
            rs.next();
            categoryToStore.setDBID(rs.getLong(1));

            retValue = true;

        } catch (Exception e) {
            e.printStackTrace();
            retValue = false;
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

        return retValue;
    }

    /**
     * Function to store an assignment in the database and set the DBID of the assignment that was passed in
     * @param assignmentToStore
     * @param lCourseID ID of the course that the assignment belongs to
     * @return true if the assignment was stored successfully
     */
    boolean StoreAssignment(Assignment assignmentToStore, long lCourseID) {
        boolean retValue;

        Connection con = null;
        PreparedStatement stm = null;
        ResultSet rs = null;

        Category catCopy = assignmentToStore.getCategory();

        long lCategoryID = 0;

        if (catCopy != null) {
            lCategoryID = catCopy.getDBID();
        }

        try {
            String sql = "INSERT INTO ASSIGNMENTS" +
                    "(SASSIGNMENTNAME, DTDUEDATE, DTASSIGNEDDATE, FMEAN, FMEDIAN, FMODE, FSTDDEV, BDROPPED, FMAXSCORE, " +
                    "FWEIGHT, ICATEGORY, ICOURSE)" +
                    "VALUES" +
                    "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            con = DriverManager.getConnection(conString, user, pass);
            stm = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            stm.setString(1, assignmentToStore.getAssignmentName());
            stm.setDate(2, java.sql.Date.valueOf(assignmentToStore.getDueDate()));
            stm.setDate(3, java.sql.Date.valueOf(assignmentToStore.getAssignedDate()));
            stm.setFloat(4, 0.0f);  //TODO: stm.setFloat(4, assignmentToStore.getStatistics().getMean());
            stm.setFloat(5, 0.0f);  //TODO: stm.setFloat(5, assignmentToStore.getStatistics().getMedian());
            stm.setFloat(6, 0.0f);  //TODO: stm.setFloat(6, assignmentToStore.getStatistics().getMode());
            stm.setFloat(7, 0.0f);  //TODO: stm.setFloat(7, assignmentToStore.getStatistics().getStdDev());
            stm.setBoolean(8, assignmentToStore.getDroppedAssignment());
            stm.setFloat(9, assignmentToStore.getMaxScore());
            stm.setFloat(10, assignmentToStore.getWeight());
            stm.setLong(11, lCategoryID);
            stm.setLong(12, lCourseID);

            stm.executeUpdate();

            rs = stm.getGeneratedKeys();
            rs.next();
            assignmentToStore.setDBID(rs.getLong(1));

            retValue = true;

        } catch (Exception e) {
            e.printStackTrace();
            retValue = false;
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

        return retValue;
    }

    /**
     * Function to store a student in the database and set the DBID of the student that was passed in
     * @param studentToStore
     * @param lCourseID ID of the course that the student belongs to
     * @return true if the student was stored successfully
     */
    boolean StoreStudent(Student studentToStore, long lCourseID) {
        boolean retValue;

        Connection con = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        try {
            String sql = "INSERT INTO STUDENTS" +
                    "(SFIRSTMINAME, SLASTNAME, SSTUDENTID, SEMAIL, FAVERAGE, ICOURSE)" +
                    "VALUES" +
                    "(?, ?, ?, ?, ?, ?)";

            con = DriverManager.getConnection(conString, user, pass);
            stm = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            stm.setString(1, studentToStore.getFirstMIName());
            stm.setString(2, studentToStore.getLastName());
            stm.setString(3, studentToStore.getStudentID());
            stm.setString(4, studentToStore.getStudentEmail());
            stm.setFloat(5, 0.0f);  //TODO: stm.setFloat(5, studentToStore.getAverage());
            stm.setLong(6, lCourseID);

            stm.executeUpdate();

            rs = stm.getGeneratedKeys();
            rs.next();
            studentToStore.setDBID(rs.getLong(1));

            retValue = true;

        } catch (Exception e) {
            e.printStackTrace();
            retValue = false;
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

        return retValue;
    }

    /**
     * Function to store a grade in the database and set the DBID of the grade that was passed in
     * @param gradeToStore
     * @param lCourseID ID of the course that the grade belongs to
     * @return true if the grade was stored successfully
     */
    boolean StoreGrade(Grade gradeToStore, long lCourseID) {
        boolean retValue;

        Connection con = null;
        PreparedStatement stm = null;
        ResultSet rs = null;

        Assignment assignCopy = gradeToStore.getAssignmentCopy();
        Student stuCopy = gradeToStore.getStudentCopy();

        long lAssignmentID = 0;
        long lStudentID = 0;

        if (assignCopy != null) {
            lAssignmentID = assignCopy.getDBID();
        }
        if (stuCopy != null) {
            lStudentID = stuCopy.getDBID();
        }

        try {
            String sql = "INSERT INTO GRADES" +
                    "(FGRADE, BSUBMITTED, BOVERDUE, BMISSING, BDROPPED, IASSIGNMENT, ISTUDENT, ICOURSE)" +
                    "VALUES" +
                    "(?, ?, ?, ?, ?, ?, ?, ?)";

            con = DriverManager.getConnection(conString, user, pass);
            stm = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            stm.setFloat(1, gradeToStore.getGrade());
            stm.setBoolean(2, gradeToStore.getSubmitted());
            stm.setBoolean(3, gradeToStore.getOverdue());
            stm.setBoolean(4, gradeToStore.getMissing());
            stm.setBoolean(5, gradeToStore.getDropped());
            stm.setLong(6, lAssignmentID);
            stm.setLong(7, lStudentID);
            stm.setLong(8, lCourseID);

            stm.executeUpdate();

            rs = stm.getGeneratedKeys();
            rs.next();
            gradeToStore.setDBID(rs.getLong(1));

            retValue = true;

        } catch (Exception e) {
            e.printStackTrace();
            retValue = false;
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

        return retValue;
    }

    //TODO: boolean StoreQuestion(Question questionToStore, lCourseID)

    Course retrieveCourseData(long lCourseID) {
        Course retValue = null;

        Connection con = null;
        PreparedStatement stm = null;
        ResultSet rs = null;

        try {
            //Get the course with the ID passed in
            String sql = "SELECT TOP 1 FROM COURSES" +
                    "WHERE COURSES.COURSEID = ?";

            con = DriverManager.getConnection(conString, user, pass);
            stm = con.prepareStatement(sql);

            stm.setLong(1, lCourseID);

            rs = stm.executeQuery();
            if (rs != null && rs.next()) {
                retValue = new Course(rs.getString(2),
                        rs.getString(3),
                        rs.getString(4));

                retValue.setDBID(rs.getLong(1));


                //Get categories for the course
                sql = "SELECT * FROM CATEGORIES" +
                        "WHERE CATEGORIES.ICOURSE = ?";

                stm = con.prepareStatement(sql);

                stm.setLong(1, lCourseID);

                rs = stm.executeQuery();

                if (rs != null) {
                    while(rs.next()) {
                        Category temp = new Category(rs.getString(2), rs.getFloat(3));
                        temp.setDBID(rs.getLong(1));

                        retValue.addCategory(temp);
                    }
                }

                //Get Assignments for the course

                sql = "SELECT * FROM ASSIGNMENTS" +
                        "WHERE ASSIGNMENTS.ICOURSE = ?";

                stm = con.prepareStatement(sql);

                stm.setLong(1, lCourseID);

                rs = stm.executeQuery();

                if (rs != null) {
                    while(rs.next()) {
                        Assignment temp = new Assignment(rs.getString(2),
                                rs.getDate(3).toLocalDate(),
                                rs.getDate(4).toLocalDate(),
                                rs.getBoolean(9),
                                rs.getFloat(10),
                                null,
                                rs.getFloat(11));

                        Statistics tempStats = new Statistics(); //TODO: set mean, median, mode for the statistics

                        //Set the assignment's category to the appropriate category in the course's list
                        long tempCatID = rs.getLong(12);

                        temp.setDBID(rs.getLong(1));

                        for (Category cat : retValue.getlCategories()) {
                            if(cat.getDBID() == tempCatID) {
                                temp.setCategory(cat);
                                break;
                            }
                        }

                        retValue.addAssignment(temp);
                    }
                }

                //TODO: retrieve students from database
                //TODO: retrieve grades from the database
            }

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

        return retValue;
    }
}
