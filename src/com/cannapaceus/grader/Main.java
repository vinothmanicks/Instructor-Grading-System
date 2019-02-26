/**
 * @author - Charles Grady, James Shelton, Nicholas Bryant, Vinoth Kumar Manickavasagam
 */
package com.cannapaceus.grader;

import java.util.*;

public class Main {

    /**
     * The program execution starts here
     * @param args
     */

    public static void main (String[] args) {
        // Do not remove below print statement
        System.out.println("Instructor Grading Program");

        DBService db = new DBService();
        db.loginDB();
    }

}
