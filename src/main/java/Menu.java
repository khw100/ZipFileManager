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

        while(choice!=6) {

            fm.refresh();

            System.out.println("Enter 1 to display extracted zips.");
            System.out.println("Enter 2 to extract a new zip file.");
            System.out.println("Enter 3 to update files. (Unimplemented)");
            System.out.println("Enter 4 to delete files.");
            System.out.println("Enter 5 to delete extracted files. (Debugging)");
            System.out.println("Enter 6 to exit.");

            //Makes sure user enters a number
            try{
                choice = Integer.parseInt(sc.nextLine());
            }
            catch(NumberFormatException nfe){
                System.out.println("Invalid Input.\n");
                continue;
            }

            switch (choice){

                //Display file log
                case 1 -> {
                    fm.listFiles();
                }

                //Extracts zip to location
                case 2 -> {
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

                //Unimplemented update file
                case 3 -> {
                    System.out.println("Unimplemented.\n");
                }

                //Delete files
                case 4 -> {

                    fm.listFiles();

                    System.out.println("\nEnter the path of the file to be deleted.");

                    String verify;

                    File deleteOrigin = fm.pathChecker();

                    //Double checks deletion target with user
                    System.out.println("\nAbout to delete " + deleteOrigin.getAbsolutePath());
                    System.out.println("\nAre you sure you want to continue?");
                    System.out.println("Enter yes to continue. Any other input will cancel.");

                    verify = sc.next();

                    if(verify.equalsIgnoreCase("yes")) {
                        fm.delete(deleteOrigin);
                        System.out.println("Deleted Successfully.\n");

                    }
                    else {
                        System.out.println("Returning.\n\n");
                    }
                }
                case 5 ->{

                    /**
                    int deleteIndex = -1;

                    fm.listFiles();

                    //Checks if there are zip files that have been extracted to delete
                    if(fm.getIDListSizeFM() == 0){
                        System.out.println("No files have been exracted. Returning.\n");
                        break;
                    }

                    while(true){
                        System.out.println("\nPlease enter the number of the file the contents of which you want to delete.");

                        try{
                            deleteIndex = Integer.parseInt(sc.nextLine());
                            break;
                        }
                        catch(NumberFormatException nfe){
                            System.out.println("Invalid Input.\n");
                        }

                    }
                    //Deletes entry from log
                    fm.deleteExtractedFiles(deleteIndex);
                     */
                }
                //Exit program
                case 6 -> {
                    return;
                }

                //For user inputs outside of range
                default -> System.out.println("Invalid input.\n");
            }
        }
        System.out.println("Exited Program.");
    }
}

