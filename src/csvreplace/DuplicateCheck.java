/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csvreplace;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Scanner;

/**
 *
 * @author onamacuser
 */
public class DuplicateCheck {
    public static void main(String[] args) throws FileNotFoundException, IOException {
        Scanner in = new Scanner(new FileReader("/path/to/csv/output.csv"));
        PrintWriter out = new PrintWriter(new FileWriter("/path/to/csv/res.csv"));
        String row;
        String[] columns;
        HashMap <String, String> map = new HashMap<>();
        int i = 0;
        int duplicates = 0;
            while (in.hasNextLine()) {
                i++;
                row = in.nextLine();
                columns = row.split(",");
                double key  = Double.parseDouble(columns[4]);
                double value  = Double.parseDouble(columns[3]);
                if (map.containsKey(columns[0]+columns[2])) {
                    System.out.println("Duplicates "+i);
                    duplicates++;
                } else {
                    map.put(columns[0]+columns[2], "");
                    out.println(row);
                }

            }
            System.out.println("Number of dups " + duplicates);
            out.close();
    }
}
