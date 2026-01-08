import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.sql.*;


import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Logger {

    //Gets user adapted path for main directory
    final Path mainFolder = Paths.get(System.getProperty("user.home"),"Documents").resolve("ZipManager");
    String mainFolderStr = mainFolder.toAbsolutePath().toString();

    //Sets path for log file under main directory
    final Path logFilePath = mainFolder.resolve("log.json");
    final String logPathString = logFilePath.toString();

    //Log file
    File logs = new File(logPathString);
    ObjectMapper om = new ObjectMapper();

    //Array of objects to read JSON into
    List<FileInfo> log;

    public void logCreate() {
        try {
            logs.createNewFile();
            //Prevents Jackson Empty File Read Error
            om.writeValue(logs, new ArrayList<FileInfo>());
        }
        catch(IOException e) {
            System.out.println(e);
        }
    }

    public void logAdd(FileInfo fi) throws IOException{

        //Proper formatting and display of ZonedDateTime
        om.registerModule(new JavaTimeModule());
        om.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        om.enable(SerializationFeature.INDENT_OUTPUT);

        //Reads log file into arraylist
        log = om.readValue(logFilePath.toFile(), new TypeReference<List<FileInfo>>(){});
        //Adds new object to arraylist
        log.add(fi);

        //Rewrites JSON log with new object
        om.writeValue(logs, log);

    }

    //Unimplemented
    public List<FileInfo> logRetrieve() {
        return null;
    }

    //Unimplemented
    public void logDelete(String filePath) {

    }

    public void sqlCreate(){
        Connection c = null;

        String createTable = """
        CREATE TABLE IF NOT EXISTS mods (
        Name varchar(200),
        Version varchar(200),
        Path varchar(200)
    )
    """;

        try{
            c = DriverManager.getConnection("jdbc:sqlite:" + mainFolderStr + "/mods.db");
        }
        catch(SQLException se){
            System.out.println(se);
        }

        String test = """
                INSERT INTO mods (Name, Version, Path)
                VALUES (?, ?, ?)
        """;

        try{
            Statement s = c.createStatement();
            s.execute(createTable);

            PreparedStatement ps = c.prepareStatement(test);

            ps.setString(1, "Unofficial Patch");
            ps.setString(2, "1/5/26");
            ps.setString(3, mainFolderStr);

            ps.executeUpdate();

            ResultSet rs = s.executeQuery("SELECT Name, Version, Path FROM mods");

            while(rs.next()){
                System.out.print(rs.getString("Name") + " " + rs.getString("Version") + " " + rs.getString("Path"));
            }

        }
        catch(SQLException se){
            System.out.println(se);
        }



        if (c!=null){
            try{
                c.close();
                System.out.println("closed");
            }
            catch(SQLException se){
                System.out.println(se);
            }
        }


    }

}
//create 2 tables, one for mods one for files
//give each row unique ids for easy access
//prompt user for id navigation for deletion and extraction
