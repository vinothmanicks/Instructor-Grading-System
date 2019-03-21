package com.cannapaceus.grader;

import java.sql.*;
import java.util.*;

public class DBService {
    private static DBService instance = null;

    private String conString = "jdbc:h2:~/IGP;";

    private String user;
    private String pass;

    public static DBService getInstance() {
        if (instance == null)
            instance = new DBService();

        return instance;
    }

    private DBService() { };

    public boolean loginDB(String username, String password) {
        boolean connected = false;

        user = username;
        pass = password;

        connected = initConnection();

        return connected;
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

    /**
     * Function to store a term in the database and set the DBID of the term that was passed in
     * @param termToStore
     * @return true if the term was stored successfully
     */
    public boolean storeTerm(Term termToStore) {
        boolean retValue = false;

        Connection con = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        try {
            String sql = "INSERT INTO TERMS" +
                    "(ESEASON, IYEAR, BARCHIVED)" +
                    "VALUES" +
                    "(?, ?, ?)";

            con = DriverManager.getConnection(conString, user, pass);
            stm = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            stm.setString(1, termToStore.getSeason().toString());
            stm.setInt(2, termToStore.getYear());
            stm.setBoolean(3, termToStore.getArchivedStatus());

            stm.executeUpdate();

            rs = stm.getGeneratedKeys();
            rs.next();
            termToStore.setDBID(rs.getLong(1));

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
     * Function to store a course in the database and set the DBID of the course that was passed in
     * @param courseToStore
     * @param lTermID ID of the term that the course belongs to
     * @return true if the course was stored successfully
     */
    public boolean storeCourse(Course courseToStore, long lTermID) {
        boolean retValue = false;

        Connection con = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        try {
            String sql = "INSERT INTO COURSES " +
                    "(SCOURSENAME, SCOURSEID, SCOURSEDEPT, FMEAN, FMEDIAN, FMODE, FSTDDEV, BARCHIVED, ITERM) " +
                    "VALUES " +
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
    public boolean storeCategory(Category categoryToStore, long lCourseID) {
        boolean retValue = false;

        Connection con = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        try {
            String sql = "INSERT INTO CATEGORIES " +
                    "(SCATEGORYNAME, FWEIGHT, ICOURSE) " +
                    "VALUES " +
                    "(?, ?, ?)";

            con = DriverManager.getConnection(conString, user, pass);
            stm = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            stm.setString(1, categoryToStore.getName());
            stm.setFloat(2, categoryToStore.getWeight());
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
    public boolean storeAssignment(Assignment assignmentToStore, long lCourseID) {
        boolean retValue = false;

        Connection con = null;
        PreparedStatement stm = null;
        ResultSet rs = null;

        Category catCopy = assignmentToStore.getCategoryCopy();

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
    public boolean storeStudent(Student studentToStore, long lCourseID) {
        boolean retValue = false;

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
    public boolean storeGrade(Grade gradeToStore, long lCourseID) {
        boolean retValue = false;

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

    public ArrayList<Term> retrieveTerms() {
        ArrayList<Term> retValue = new ArrayList<>();

        Connection con = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        ResultSet rsc = null;

        try {
            //Get the course with the ID passed in
            String sql = "SELECT * FROM TERMS";

            con = DriverManager.getConnection(conString, user, pass);
            stm = con.prepareStatement(sql);

            rs = stm.executeQuery();

            if (rs != null) {
                while (rs.next()) {
                    Term temp = new Term(rs.getInt(3), eSeason.values()[rs.getShort(2)]);

                    temp.setDBID(rs.getLong(1));

                    sql = "SELECT * FROM COURSES " +
                            "WHERE COURSES.ITERM = ?";

                    stm = con.prepareStatement(sql);
                    stm.setLong(1, temp.getDBID());

                    rsc = stm.executeQuery();

                    if (rsc != null) {
                        while(rsc.next()) {
                            Course tempCourse = new Course(rsc.getString(2),
                                    rsc.getString(3),
                                    rsc.getString(4));

                            tempCourse.setDBID(rsc.getLong(1));

                            temp.addCourse(tempCourse);
                        }
                    }

                    retValue.add(temp);
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) { /* ignored */}
            }
            if (rsc != null) {
                try {
                    rsc.close();
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

    public Course retrieveCourseData(long lCourseID) {
        Course retValue = null;

        Connection con = null;
        PreparedStatement stm = null;
        ResultSet rs = null;

        try {
            //Get the course with the ID passed in
            String sql = "SELECT TOP(1) * FROM COURSES " +
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
                sql = "SELECT * FROM CATEGORIES " +
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

                sql = "SELECT * FROM ASSIGNMENTS " +
                        "WHERE ASSIGNMENTS.ICOURSE = ?";

                stm = con.prepareStatement(sql);

                stm.setLong(1, lCourseID);

                rs = stm.executeQuery();

                if (rs != null) {
                    while(rs.next()) {
                        //Set the assignment's category to the appropriate category in the course's list
                        long tempCatID = rs.getLong(12);
                        Category tempCat = null;

                        for (Category cat : retValue.getlCategories()) {
                            if(cat.getDBID() == tempCatID) {
                                tempCat = cat;
                                break;
                            }
                        }

                        Assignment temp = new Assignment(rs.getString(2),
                                rs.getDate(3).toLocalDate(),
                                rs.getDate(4).toLocalDate(),
                                rs.getBoolean(9),
                                rs.getFloat(10),
                                tempCat,
                                rs.getFloat(11));

                        Statistics tempStats = new Statistics(); //TODO: set mean, median, mode for the statistics

                        temp.setDBID(rs.getLong(1));



                        retValue.addAssignment(temp);
                    }
                }

                //Get categories for the course
                sql = "SELECT * FROM STUDENTS " +
                        "WHERE STUDENTS.ICOURSE = ?";

                stm = con.prepareStatement(sql);

                stm.setLong(1, lCourseID);

                rs = stm.executeQuery();

                if (rs != null) {
                    while(rs.next()) {
                        Student temp = new Student(rs.getString(2),
                                rs.getString(3),
                                rs.getString(4),
                                rs.getString(5));

                        temp.setDBID(rs.getLong(1));

                        retValue.addStudent(temp);
                    }
                }

                //Get Assignments for the course

                sql = "SELECT * FROM GRADES " +
                        "WHERE GRADES.ICOURSE = ?";

                stm = con.prepareStatement(sql);

                stm.setLong(1, lCourseID);

                rs = stm.executeQuery();

                if (rs != null) {
                    while(rs.next()) {

                        //Set the grade's student and assignment to the appropriate student and assignment in the course's list
                        long tempStuID = rs.getLong(7);
                        long tempAssignID = rs.getLong(8);

                        Student tempStu = null;
                        Assignment tempAssign = null;

                        for (Student stu : retValue.getlStudents()) {
                            if(stu.getDBID() == tempStuID) {
                                tempStu = stu;
                                break;
                            }
                        }

                        for (Assignment assign : retValue.getlAssignments()) {
                            if(assign.getDBID() == tempAssignID) {
                                tempAssign = assign;
                                break;
                            }
                        }

                        Grade temp = new Grade(rs.getFloat(2), tempStu, tempAssign);

                        temp.setDBID(rs.getLong(1));

                        tempStu.addGrade(temp);
                        tempAssign.addGrade(temp);
                        retValue.addGrade(temp);
                    }
                }
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

    public ArrayList<Term> retrieveModel() {
        ArrayList<Term> retValue = new ArrayList<>();

        Connection con = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        ResultSet rsc = null;

        try {
            //Get the course with the ID passed in
            String sql = "SELECT * FROM TERMS";

            con = DriverManager.getConnection(conString, user, pass);
            stm = con.prepareStatement(sql);

            rs = stm.executeQuery();

            if (rs != null) {
                while (rs.next()) {
                    Term temp = new Term(rs.getInt(3), eSeason.values()[rs.getShort(2)]);

                    temp.setDBID(rs.getLong(1));

                    sql = "SELECT * FROM COURSES " +
                            "WHERE COURSES.ITERM = ?";

                    stm = con.prepareStatement(sql);
                    stm.setLong(1, temp.getDBID());

                    rsc = stm.executeQuery();

                    if (rsc != null) {
                        while(rsc.next()) {
                            Course tempCourse = retrieveCourseData(rsc.getLong(1));

                            temp.addCourse(tempCourse);
                        }
                    }

                    retValue.add(temp);
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) { /* ignored */}
            }
            if (rsc != null) {
                try {
                    rsc.close();
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

    public void storeNewObjects(ArrayList<Object> lObjects, ArrayList<Term> lTerms) {
        for (Term t : lTerms) {     //Traverse all terms and store every object in lObjects
                                    //Need to traverse because the parent's DBID is needed
            if (lObjects.contains(t)) {
                storeTerm(t);
            }
            for (Course c : t.getCourses()) {
                if (lObjects.contains(c)) {
                    storeCourse(c, t.getDBID());
                }
                for (Student s : c.getlStudents()) {
                    if (lObjects.contains(s)) {
                        storeStudent(s, c.getDBID());
                    }
                }

                for (Category cat : c.getlCategories()) {
                    if (lObjects.contains(cat)) {
                        storeCategory(cat, c.getDBID());
                    }
                }

                for (Assignment a : c.getlAssignments()) {
                    if (lObjects.contains(a)) {
                        storeAssignment(a, c.getDBID());
                    }
                }

                for (Grade g : c.getlGrades()) {
                    if (lObjects.contains(g)) {
                        storeGrade(g, c.getDBID());
                    }
                }
            }
        }
    }

    public void updateObjects(ArrayList<Object> lObjects) {
        for (Object o : lObjects) {
            if (o instanceof Term) {
                updateTerm((Term) o);
            } else if (o instanceof Course) {
                updateCourse((Course) o);
            } else if (o instanceof Category) {
                updateCategory((Category) o);
            } else if (o instanceof Student) {
                updateStudent((Student) o);
            } else if (o instanceof Assignment) {
                updateAssignment((Assignment) o);
            } else if (o instanceof Grade) {
                updateGrade((Grade) o);
            }
        }
    }

    private boolean updateTerm(Term t) {
        boolean retValue = false;

        Connection con = null;
        PreparedStatement stm = null;
        try {
            String sql = "UPDATE TERMS " +
                    "SET ESEASON = ?, IYEAR = ?, BARCHIVED = ? " +
                    "WHERE TERMID = ?";
            con = DriverManager.getConnection(conString, user, pass);
            stm = con.prepareStatement(sql);

            stm.setString(1, t.getSeason().toString());
            stm.setInt(2, t.getYear());
            stm.setBoolean(3, t.getArchivedStatus());
            stm.setLong(4, t.getDBID());

            stm.executeUpdate();

            retValue = true;

        } catch (Exception e) {
            e.printStackTrace();
            retValue = false;
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

        return retValue;
    }

    private boolean updateCourse(Course c) {
        boolean retValue = false;

        Connection con = null;
        PreparedStatement stm = null;
        try {
            String sql = "UPDATE COURSES " +
                    "SET SCOURSENAME = ?, SCOURSEID = ?, SCOURSEDEPT = ?, FMEAN = ?, " +
                    "FMEDIAN = ?, FMODE = ?, FSTDDEV = ?, BARCHIVED = ? " +
                    "WHERE COURSEID = ? ";

            con = DriverManager.getConnection(conString, user, pass);
            stm = con.prepareStatement(sql);

            stm.setString(1, c.getCourseName());
            stm.setString(2, c.getCourseID());
            stm.setString(3, c.getDepartment());
            stm.setFloat(4, 0.0f);  //TODO: stm.setFloat(4, courseToStore.getStatistics().getMean());
            stm.setFloat(5, 0.0f);  //TODO: stm.setFloat(5, courseToStore.getStatistics().getMedian());
            stm.setFloat(6, 0.0f);  //TODO: stm.setFloat(6, courseToStore.getStatistics().getMode());
            stm.setFloat(7, 0.0f);  //TODO: stm.setFloat(7, courseToStore.getStatistics().getStdDev());
            stm.setBoolean(8, false);
            stm.setLong(9, c.getDBID());

            stm.executeUpdate();

            retValue = true;

        } catch (Exception e) {
            e.printStackTrace();
            retValue = false;
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

        return retValue;
    }

    private boolean updateCategory(Category cat) {
        boolean retValue = false;

        Connection con = null;
        PreparedStatement stm = null;
        try {
            String sql = "UPDATE CATEGORIES " +
                    "SET SCATEGORYNAME = ?, FWEIGHT = ? " +
                    "WHERE CATEGORYID = ? ";

            con = DriverManager.getConnection(conString, user, pass);
            stm = con.prepareStatement(sql);

            stm.setString(1, cat.getName());
            stm.setFloat(2, cat.getWeight());
            stm.setLong(3, cat.getDBID());

            stm.executeUpdate();

            retValue = true;

        } catch (Exception e) {
            e.printStackTrace();
            retValue = false;
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

        return retValue;
    }

    private boolean updateStudent(Student s) {
        boolean retValue = false;

        Connection con = null;
        PreparedStatement stm = null;
        try {
            String sql = "UPDATE STUDENTS" +
                    "SET SFIRSTMINAME = ?, SLASTNAME = ?, SSTUDENTID = ?, SEMAIL = ?, FAVERAGE = ? " +
                    "WHERE STUDENTID = ?";

            con = DriverManager.getConnection(conString, user, pass);
            stm = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            stm.setString(1, s.getFirstMIName());
            stm.setString(2, s.getLastName());
            stm.setString(3, s.getStudentID());
            stm.setString(4, s.getStudentEmail());
            stm.setFloat(5, 0.0f);  //TODO: stm.setFloat(5, studentToStore.getAverage());
            stm.setLong(6, s.getDBID());

            stm.executeUpdate();

            retValue = true;

        } catch (Exception e) {
            e.printStackTrace();
            retValue = false;
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

        return retValue;
    }

    private boolean updateAssignment(Assignment a) {
        boolean retValue = false;

        Connection con = null;
        PreparedStatement stm = null;

        Category catCopy = a.getCategoryCopy();

        long lCategoryID = 0;

        if (catCopy != null) {
            lCategoryID = catCopy.getDBID();
        }

        try {
            String sql = "UPDATE ASSIGNMENTS" +
                    "SET SASSIGNMENTNAME = ?, DTDUEDATE = ?, DTASSIGNEDDATE = ?, FMEAN = ?, FMEDIAN = ?, " +
                    "FMODE = ?, FSTDDEV = ?, BDROPPED = ?, FMAXSCORE = ?, FWEIGHT = ?, ICATEGORY = ? " +
                    "WHERE ASSIGNMENTID = ?";

            con = DriverManager.getConnection(conString, user, pass);
            stm = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            stm.setString(1, a.getAssignmentName());
            stm.setDate(2, java.sql.Date.valueOf(a.getDueDate()));
            stm.setDate(3, java.sql.Date.valueOf(a.getAssignedDate()));
            stm.setFloat(4, 0.0f);  //TODO: stm.setFloat(4, assignmentToStore.getStatistics().getMean());
            stm.setFloat(5, 0.0f);  //TODO: stm.setFloat(5, assignmentToStore.getStatistics().getMedian());
            stm.setFloat(6, 0.0f);  //TODO: stm.setFloat(6, assignmentToStore.getStatistics().getMode());
            stm.setFloat(7, 0.0f);  //TODO: stm.setFloat(7, assignmentToStore.getStatistics().getStdDev());
            stm.setBoolean(8, a.getDroppedAssignment());
            stm.setFloat(9, a.getMaxScore());
            stm.setFloat(10, a.getWeight());
            stm.setLong(11, lCategoryID);
            stm.setLong(12, a.getDBID());

            stm.executeUpdate();

            retValue = true;

        } catch (Exception e) {
            e.printStackTrace();
            retValue = false;
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

        return retValue;
    }

    private boolean updateGrade(Grade g) {
        boolean retValue = false;

        Connection con = null;
        PreparedStatement stm = null;

        Assignment assignCopy = g.getAssignmentCopy();
        Student stuCopy = g.getStudentCopy();

        long lAssignmentID = 0;
        long lStudentID = 0;

        if (assignCopy != null) {
            lAssignmentID = assignCopy.getDBID();
        }
        if (stuCopy != null) {
            lStudentID = stuCopy.getDBID();
        }

        try {
            String sql = "UPDATE GRADES" +
                    "SET FGRADE = ?, BSUBMITTED = ?, BOVERDUE = ?, BMISSING = ?, " +
                    "BDROPPED = ?, IASSIGNMENT = ?, ISTUDENT = ? " +
                    "WHERE GRADEID = ?";

            con = DriverManager.getConnection(conString, user, pass);
            stm = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            stm.setFloat(1, g.getGrade());
            stm.setBoolean(2, g.getSubmitted());
            stm.setBoolean(3, g.getOverdue());
            stm.setBoolean(4, g.getMissing());
            stm.setBoolean(5, g.getDropped());
            stm.setLong(6, lAssignmentID);
            stm.setLong(7, lStudentID);
            stm.setLong(8, g.getDBID());

            stm.executeUpdate();

            retValue = true;

        } catch (Exception e) {
            e.printStackTrace();
            retValue = false;
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

        return retValue;
    }
}
