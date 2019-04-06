package com.cannapaceus.services;

import java.util.ArrayList;
import java.util.List;

public class CSVServiceTester {

    public static void main(String args[])
    {
        List<String[]> data = new ArrayList<String[]>();
        data.add(new String[] { "Name", "Class", "Marks" });
        data.add(new String[] { "Aman", "10", "620" });
        data.add(new String[] { "Suraj", "10", "630" });

        CSVService csvService = new CSVService();
        csvService.ExportCSV(data);
    }
}
