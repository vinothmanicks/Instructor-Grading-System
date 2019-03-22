package com.cannapaceus.services;

import com.opencsv.*;

import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CSVService {

    public void ExportCSV(List<String[]> data){
        try {
            // create FileWriter object with file as parameter
            String filename = Today() + ".csv";
            FileWriter outputfile = new FileWriter(filename);

            // create CSVWriter object filewriter object as parameter
            CSVWriter writer = new CSVWriter(outputfile);

            // create a List which contains String array

            writer.writeAll(data);

            // closing writer connection
            writer.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
    public void ExportCSV() {
        java.sql.ResultSet myResultSet = getResultSetFromSomewhere();
        writer.writeAll(myResultSet, includeHeaders); //writer is instance of CSVWriter
    }*/

    public void ImportCSV() {

    }

    public String Today() {
        String pattern = "yyyyMMdd_HHmmss";

        // Create an instance of SimpleDateFormat used for formatting
        // the string representation of date according to the chosen pattern
        DateFormat df = new SimpleDateFormat(pattern);

        // Get the today date using Calendar object.
        Date today = Calendar.getInstance().getTime();
        // Using DateFormat format method we can create a string
        // representation of a date with the defined format.
        String todayAsString = df.format(today);

        return todayAsString;
    }
}
