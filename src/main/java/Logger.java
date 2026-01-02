import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;


import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Logger {

    //Gets user adapted path for main directory
    final Path logDirectoryPath = Paths.get(System.getProperty("user.home"),"Documents").resolve("ZipManager");

    //Sets path for log file under main directory
    final Path logFilePath = logDirectoryPath.resolve("log.json");
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

}
