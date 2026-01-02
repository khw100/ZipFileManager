/**
 * Class for defining the objects and values being logged in JSON.
 */

import java.time.ZonedDateTime;

public class FileInfo
{
    private String filePath;
    private ZonedDateTime version;

    public FileInfo(){

    }

    public FileInfo(String filePath, ZonedDateTime version){
        this.filePath = filePath;
        this.version = version;
    }

    public String getFilePath(){return filePath;}

    public void setFilePath(String filePath){
        this.filePath = filePath;
    }

    public ZonedDateTime getVersion(){return version;}

    public void setVersion(ZonedDateTime version){
        this.version = version;
    }
}
