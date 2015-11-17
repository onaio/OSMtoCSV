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
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

/**
 *
 * @author onamacuser
 */
public class CreateMergedCSV1 {

    public static void main(String[] argv) throws FileNotFoundException, SQLException, IOException {
        System.out.println("-------- PostgreSQL "
                + "JDBC Connection Testing ------------");

        try {

            Class.forName("org.postgresql.Driver");

        } catch (ClassNotFoundException e) {

            System.out.println("Where is your PostgreSQL JDBC Driver? "
                    + "Include in your library path!");
            e.printStackTrace();
            return;

        }

        System.out.println("PostgreSQL JDBC Driver Registered!");

        Connection connection = null;

        try {

            connection = DriverManager.getConnection(
                    "jdbc:postgresql://127.0.0.1:5432/csv", "username",
                    "password");

        } catch (SQLException e) {

            System.out.println("Connection Failed! Check output console");
            e.printStackTrace();
            return;

        }

        if (connection != null) {
            System.out.println("You made it, take control your database now!");
            // create a Statement from the connection
            Statement statement = connection.createStatement();
            Scanner in = new Scanner(new FileReader("/path/to/csv/households.csv"));
            PrintWriter out = new PrintWriter(new FileWriter("/path/to/csv/output1.csv"));
            String row;
            String[] columns;
            int rows = 0, rows_unmatched = 0, rows_duplicate = 0, cnt = 0;
            while (in.hasNextLine()) {
                cnt++;
                row = in.nextLine();
                columns = row.split(",");
                int id = Integer.parseInt(columns[0]);
                if (columns.length > 4 && columns[0].trim().length() > 0) {
                    // insert the data
                    long shpLen  = (long) (Double.parseDouble(columns[4])*HouseholdsToCSV.POW);
                    long shpArea = (long) (Double.parseDouble(columns[3])*HouseholdsToCSV.POW);
                    ResultSet rs = statement.executeQuery("SELECT * FROM household_1 WHERE shapelen = " + shpLen + " and shapearea = " + shpArea + "");
                     //System.out.println("No match "+cnt);

                    if (!rs.isBeforeFirst()) {          //res.isBeforeFirst() is true if the cursor
                        //is before the first row.  If res contains
                        //no rows, rs.isBeforeFirst() is false.
                        rows_unmatched++;
                        //System.out.println("No match "+cnt);
                        //System.out.println("shapelen = " + (Double.parseDouble(columns[4])*100000000000000L) + " and shapearea = " + (Double.parseDouble(columns[3])*100000000000000L));
                    } else {
                        if (getRows(rs) > 1) {
                            rows_duplicate++;
                            while (rs.next()) {
                                out.println(rs.getInt("id") + "," + rs.getDouble("lat") + "," + rs.getDouble("lon") + "," + rs.getDouble("shapelen")/HouseholdsToCSV.POW + "," + rs.getDouble("shapearea")/HouseholdsToCSV.POW);
                            }
                        } else {
                            rows++;

                            //out.println(rs.getInt("id")+","+rs.getString("district")+","+rs.getString("ta")+","+rs.getString("shapelen")+","+rs.getDouble("shapearea")+","+columns[1]+","+columns[2]);
                        }
                    }
                }
            }
            //out.flush();
            out.close();
            System.out.println("Matched " + rows + "Unmatched " + rows_unmatched + " Duplicate " + rows_duplicate);
        }
    }

    public static int getRows(ResultSet res) {
        int totalRows = 0;
        try {
            res.last();
            totalRows = res.getRow();
            res.beforeFirst();
        } catch (Exception ex) {
            return 0;
        }
        return totalRows;
    }
}
