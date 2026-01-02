/**
 * @Author Kyler W
 * @Version 1/1/26
 * Zip File Manager Project
 *
 * Extracts zip files to specified folder.
 * Tracks file location and zip extraction date.
 * Deletes files in directory.
 */

import java.io.IOException;

public class ZipManager {
    public static void main(String[]args) throws IOException{

        //File logic class start
        FileManager fm = new FileManager();

        //Logging class start
        Logger l = new Logger();

        //Creates directory for testing or log storage
        fm.mainDirectoryCreate();
        l.logCreate();

        //User console menu display
        Menu m = new Menu();
        m.menuDisplay(fm);
    }
}
