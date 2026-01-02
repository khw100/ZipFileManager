/**
 * Class for file logic and management.
 */

//zip extraction
import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.ZonedDateTime;
import java.util.Scanner;


public class FileManager {
    Scanner sc = new Scanner(System.in);

    //Sets path adapted to different usernames for multiple users
    final Path logDirectoryPath = Paths.get(System.getProperty("user.home"),"Documents").resolve("ZipManager");

    //Creates directory for log storage and testing
    public void mainDirectoryCreate() {

        try{
            if(!Files.exists(logDirectoryPath)) {
                Files.createDirectory(logDirectoryPath);
            }
        }
        catch(IOException e) {
            System.out.println(e);
        }
    }

    //Checks if user input paths are valid, repeats if not, if valid, returns the file
    public File pathChecker() {

        File f;

        while(true) {
            System.out.println("Please enter the file or directory path.");

            String pathCheck = sc.nextLine().trim();

            f = new File(pathCheck);

            //Checks if user inputted path exists
            if(!f.exists()) {
                System.out.println("File or directory does not exist.");
                continue;
            }

            //Checks permissions
            if(f.isDirectory()) {
                if(!f.canWrite()) {
                    System.out.println("You don't have permission to write to this directory.");
                    continue;
                }
                if(!f.canRead()) {
                    System.out.println("You don't have permission to read from this directory.");
                    continue;
                }
            }
            break;
        }
        return f;
    }

    //Extracts zip files and logs their destination and extraction date/time
    public void zipExtract(String origin, String destination) throws IOException {
        try(ZipFile z = new ZipFile(origin)) {
            z.extractAll(destination);
            System.out.println("\nZip has been extracted to the directory at: " + destination + "\n");
        }
        catch (ZipException e) {
            System.out.println(e);
        }

        FileInfo fi = new FileInfo(destination, ZonedDateTime.now());
        Logger l = new Logger();
        l.logAdd(fi);
    }

    //Deletes full directory using recursion
    public void deleteDir(File dir) {
        File[] trash = dir.listFiles();

        if(trash!=null) {
            for(File f : trash) {
                if(f.isDirectory()) {
                    deleteDir(f);
                }
                else {
                    f.delete();
                }
            }
        }
        dir.delete();
    }

    //Unimplemented
    public static void deleteExtractedFiles(File log){

    }
}
