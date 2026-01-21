/**
 * Class for displaying a menu.
 */

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Scanner;

public class Menu {

    public void menuDisplay(FileManager fm) {

        //To store user choice
        int choice = 0;
        Scanner sc = new Scanner(System.in);

        System.out.println("Kyler's Zip Manager Project");
        System.out.println("\nLogs are stored at " + fm.mainFolder);
        System.out.println("\nThis program is for extracting zips, then moving and deleting the contents.");

        while(choice!=4) {
            System.out.println("Enter 1 to display extracted zips.");
            System.out.println("Enter 2 to extract a zip file.");
            System.out.println("Enter 3 to delete files.");
            System.out.println("Enter 4 to exit.");

            if(sc.hasNextInt()){
                choice = sc.nextInt();
            }

            if(choice == 1){
                fm.listFiles();
            }

            //2. File Extraction
            else if(choice == 2) {
                System.out.println("\nEnter the path of the file to be extracted, then the path of the destination.");

                File source = fm.pathChecker();
                File destination = fm.pathChecker();

                Path s = source.toPath();
                Path d = destination.toPath();

                try {
                    fm.zipExtract(s, d);
                }
                catch(IOException e) {
                    System.out.println(e);
                }
            }
            //3. File deletion
            else if(choice == 3) {
                System.out.println("\nEnter the path of the file to be deleted.");

                String verify;

                File deleteOrigin = fm.pathChecker();

                //Double checks deletion target with user
                System.out.println("\nAbout to delete " + deleteOrigin.getAbsolutePath());
                System.out.println("\nAre you sure you want to continue?");
                System.out.println("Enter yes to continue. Any other input will cancel.");

                verify = sc.next();

                if(verify.equalsIgnoreCase("yes")) {
                    fm.deleteDir(deleteOrigin);
                    System.out.println("Deleted Successfully.\n");

                }
                else {
                    System.out.println("Returning.\n\n");
                }

            }
            //4. Exit program
            else if(choice == 4) {
                break;
            }
            else {
                System.out.println("Invalid input.\n");
            }
        }
        System.out.println("Exited Program.");
    }
}

