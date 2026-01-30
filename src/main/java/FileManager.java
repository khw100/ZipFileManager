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
import java.sql.SQLOutput;
import java.util.Scanner;


public class FileManager {
    Scanner sc = new Scanner(System.in);

    //Sets path adapted to different usernames for multiple users
    final Path mainFolder = Paths.get(System.getProperty("user.home"),"Documents").resolve("ZipManager");
    final Path tempExtractFolder = mainFolder.resolve("Temp");
    String tempEF = tempExtractFolder.toString();

    //Class for user's log
    Logger l = new Logger();

    //Creates directory for log storage and testing
    public void mainFilesCreate() {

        try{
            if(!Files.exists(mainFolder)) {
                Files.createDirectory(mainFolder);

                //Creates log file in new main directory
                l.sqlCreate();

                //Creates temporary folder for zip file extraction
                Files.createDirectory(tempExtractFolder);

            }
        }
        catch(IOException e) {
            System.out.println(e);
        }
    }

    //Refreshes log id list
    public void refresh(){
        l.readIDS();
    }

    //Displays log on user request
    public void listFiles(){
        l.displayModList();
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
    public void zipExtract(Path origin, Path destination) throws IOException {

        String og = origin.toString();

        try(ZipFile z = new ZipFile(og)) {
            //Extracts user file to temp folder
            z.extractAll(tempEF);

            System.out.println("\nZip has been extracted to the directory at: " + destination + "\n");
        }
        catch (ZipException ze) {
            System.out.println(ze);
        }

        //Calls logger class methods to add log entry and display the file list for each extraction
        l.addEntry(origin.getFileName().toString(), destination.toString());
        l.displayModList();
    }

    //Deletes full directory using recursion or singe file
    public void delete(File toDelete) {

        //Makes sure the user doesn't accidentally delete too much
        if(toDelete.getParentFile() == null){
            System.out.println("Cannot delete root.");
            return;
        }


        if(toDelete.isDirectory()){

            //Reads files into array to iterate through and delete
            File[] trash = toDelete.listFiles();

            if(trash!=null) {
                for(File f : trash) {
                    if(f.isDirectory()) {
                        delete(f);
                    }
                    else {
                        f.delete();
                    }
                }
            }
            //Deletes empty directory
            toDelete.delete();
        }

        //Deletes single files
        else{
            toDelete.delete();
        }
    }

    //Unimplemented, Deletes only the extracted files of a specific zip even in a populated folder
    public void deleteExtractedFiles(int i){

        int size = l.getIDListSize();

        while(true){
            if(size < 0 || i > size){
                System.out.println("Invalid input. Number must be a listed index. Enter a new number.");
                i = sc.nextInt();
            }
            else{
                File f = new File(l.getFilePath(i));
                delete(f);
                l.removeEntry(i);
                break;
            }
        }
    }

    public int getIDListSizeFM(){
        return l.getIDListSize();
    }
}
