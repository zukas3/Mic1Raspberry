import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;


/**
 *  File manager that was supposed to handle multiple programs and storage of them
 *  Might be revisited in the future
 */

public class FileManager
{
    File[] files;

    public FileManager()
    {

    }

    public void SaveBytesToFile(byte[] bytes, String fileName)
    {
        try (FileOutputStream fos = new FileOutputStream(PathToFiles() + "/" + fileName)) {
            fos.write(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void RefreshFileList()
    {
        File folder = new File(PathToFiles());
        files = folder.listFiles();

        for(int i = 0; i < files.length; i++)
        {

        }
    }

    public String PathToFiles()
    {
        String path = System.getProperty("user.dir") + "/Programs";

        File f = new File(path);
        if(!f.isDirectory())
        {
            f.mkdir();
        }

        return path;
    }

    public String PathToMicroprogram()
    {
        String path =  System.getProperty("user.dir") + "/mic1ijvm.mic1";
        return path;
    }


}
