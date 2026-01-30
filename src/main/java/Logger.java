/**
 * Class for logging user's files with SQLite.
 */

import java.sql.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

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

    ArrayList<Integer> IDS = new ArrayList<Integer>();


    //Creates SQLite db file for storing user's file information
    public void sqlCreate(){

        //Sqlite statement for creating formatted db log file
        String createMainTable = """
        CREATE TABLE IF NOT EXISTS mods (
        ID INTEGER PRIMARY KEY,
        Name varchar(200),
        Version varchar(200),
        Path varchar(200)
    )
    """;

        //Creates and gets connection to db log file
        try(Connection c = DriverManager.getConnection(db);){
            Statement s = c.createStatement();

            //Formats and indexes table
            s.execute(createMainTable);
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

            //System.out.println("Adding " + source + " " + date + " " + destination);

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

        //Log index number
        int i = 1;

        try(Connection c = DriverManager.getConnection(db);){
            Statement s = c.createStatement();

            ResultSet rs = s.executeQuery("SELECT ID, Name, Version, Path FROM mods");

            System.out.println("\n_________________________________FILE LIST__________________________________________");

            while(rs.next()){

                System.out.printf("%-4s %-20s %-17s %-30s %n", i, rs.getString("Name"), rs.getString("Version"), rs.getString("Path"));

                i++;
            }

            System.out.println("____________________________________________________________________________________\n");

        }
        catch(SQLException se){
            System.out.println(se);
        }
    }

    //Unimplemented
    public void updateEntry(){

    }
    public void removeEntry(int i){
        try(Connection c = DriverManager.getConnection(db);){
            Statement s = c.createStatement();

            String delete = "DELETE FROM mods WHERE id = " + IDS.get(i-1);

            s.executeUpdate(delete);
        }
        catch(SQLException se){
            System.out.println(se);
        }
    }

    //Reads IDS from database to arraylist
    public void readIDS(){

        IDS.clear();

        try(Connection c = DriverManager.getConnection(db);
            Statement s = c.createStatement();
            ResultSet rs = s.executeQuery("SELECT ID FROM mods");){

            while(rs.next()){
                IDS.add(rs.getInt("ID"));
            }
        }
        catch(SQLException se){
            System.out.println(se);
        }
    }

    public String getFilePath(int i){

        String p = null;

        try(Connection c = DriverManager.getConnection(db);){
            Statement s = c.createStatement();

            ResultSet rs = s.executeQuery("SELECT Path FROM mods WHERE id = " + IDS.get(i-1));

            if(rs.next()){
                p = rs.getString("Path");
            }
        }
        catch(SQLException se){
            System.out.println(se);
        }

        return p;
    }

    public int getIDListSize(){
        return IDS.size();
    }

}
