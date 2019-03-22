package com.cannapaceus.services;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CSVServiceTest {

    @Test
    void exportCSV() {
        List<String[]> data = new ArrayList<String[]>();
        data.add(new String[] { "Name", "Class", "Marks" });
        data.add(new String[] { "Aman", "10", "620" });
        data.add(new String[] { "Suraj", "10", "630" });

        CSVService csvService = new CSVService();
        csvService.ExportCSV(data);
    }

    @Test
    void today() {
        CSVService csvService = new CSVService();
        System.out.println(csvService.Today());
    }
}