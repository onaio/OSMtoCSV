/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csvreplace;

import static csvreplace.CreateMergedCSV.getRows;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Scanner;

/**
 *
 * @author onamacuser
 */
public class RunParallel {
    public static final long POW = 100000000000000L;
    public static void main(String[] args) throws FileNotFoundException, IOException {

        Scanner in = new Scanner(new FileReader("/path/to/csv/output.csv"));
        PrintWriter out = new PrintWriter(new FileWriter("/path/to/csv/res.csv"));
        String row;
        String[] columns;
        HashMap <Long, Long> map = new HashMap<>();
        int i = 0;
        int duplicates = 0;
            while (in.hasNextLine()) {
                i++;
                row = in.nextLine();
                columns = row.split(",");
                long key  = (long) (Double.parseDouble(columns[3])*POW);
                long value  = (long) (Double.parseDouble(columns[4])*POW);
                if (map.containsKey(key) && map.get(key) == value) {
                    System.out.println("Duplicates "+i);
                    out.println("Duplicates "+i);
                    duplicates++;
                } else {
                    map.put(key, value);
                }

            }
            System.out.println("Number of dups " + duplicates);
            out.close();
    }
}
