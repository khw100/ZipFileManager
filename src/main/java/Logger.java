/**
 * Class for logging user's files with SQLite.
 */

import java.sql.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Logger {

    //Gets user computer platform adjusted path for main directory
    final Path mainFolder = Paths.get(System.getProperty("user.home"),"Documents").resolve("ZipManager");
    String mainFolderStr = mainFolder.toAbsolutePath().toString();

    //SQLite Log File Path
    String db = "jdbc:sqlite:" + mainFolderStr + "/mods.db";

    //SQLite statement for inserting entries into log
    String test = """
                INSERT INTO mods (Name, Version, Path)
                VALUES (?, ?, ?)
        """;

    //Initialization for getting current date for logging user activity
    DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("MMM d, yyyy");
    String date;


    //Creates SQLite db file for storing user's file information
    public void sqlCreate(){

        //Sqlite statement for creating formatted db log file
        String createTable = """
        CREATE TABLE IF NOT EXISTS mods (
        Name varchar(200),
        Version varchar(200),
        Path varchar(200)
    )
    """;

        //SQLite statement for creating log index for search and retrieval
        String createIndex = """
        CREATE INDEX IF NOT EXISTS mod_index ON mods(Name)
        """;

        //Creates and gets connection to db log file
        try(Connection c = DriverManager.getConnection(db);){
            Statement s = c.createStatement();

            //Formats and indexes table
            s.execute(createTable);
            s.execute(createIndex);
        }
        catch(SQLException se){
            System.out.println(se);
        }

    }

    //Adds entry to log file for every zip extracted
    public void addEntry(String source, String destination){

        try(Connection c = DriverManager.getConnection(db);){
            PreparedStatement ps = c.prepareStatement(test);

            //Gets current date to store in log
            date = LocalDate.now().format(dateFormat);

            System.out.println("Adding " + source + " " + date + " " + destination);

            //Adds log entry
            ps.setString(1, source);
            ps.setString(2, date);
            ps.setString(3, destination);

            ps.executeUpdate();

        }
        catch(SQLException se){
            System.out.println(se);
        }
    }

    //Displays log for user
    public void displayModList(){

        try(Connection c = DriverManager.getConnection(db);){
            Statement s = c.createStatement();

            ResultSet rs = s.executeQuery("SELECT Name, Version, Path FROM mods");

            System.out.println("\n_________________________________FILE LIST__________________________________________");

            while(rs.next()){
                System.out.println("| " + rs.getString("Name") + " | " + rs.getString("Version") + " | " + rs.getString("Path") + " | ");
            }

            System.out.println("____________________________________________________________________________________\n");

        }
        catch(SQLException se){
            System.out.println(se);
        }
    }
    public void updateEntry(){

    }
    public void removeEntry(){

    }
    public void closeLog(){

    }

}
