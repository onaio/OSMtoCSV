/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csvreplace;

/**
 *
 * @author onamacuser
 */
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class HouseholdsToCSV {
public static final long POW = 100000000000000L;
    public static void main(String[] argv) throws SQLException, FileNotFoundException {

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
            String row;
            String[] columns;
            while (in.hasNextLine()) {
                row = in.nextLine();
                columns = row.split(",");
                int id = Integer.parseInt(columns[0]);
                if (columns.length > 4 && columns[0].trim().length() > 0) {
                    // insert the data
                    long shpLen  = (long) (Double.parseDouble(columns[4])*POW);
                    long shpArea = (long) (Double.parseDouble(columns[3])*POW);
                    statement.executeUpdate("INSERT INTO household_1 " + "VALUES (" + id + ", " + Double.parseDouble(columns[2]) + ", " + Double.parseDouble(columns[1]) + ", " + shpLen  + ", " + shpArea + ")");
                }
            }
        } else {
            System.out.println("Failed to make connection!");
        }
    }

}
