/**
 * @author - Charles Grady, James Shelton, Nicholas Bryant, Vinoth Kumar Manickavasagam
 *
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

        String user;
        String pass;

        DBService dbs;

        boolean connected = false;

        while (!connected) {
            Scanner reader = new Scanner(System.in);
            System.out.print("Enter database username: ");
            user = reader.next();
            System.out.print("Enter database password: ");
            pass = reader.next();

            dbs = new DBService();
            connected = dbs.initConnection(user, pass);
        }
    }
}
