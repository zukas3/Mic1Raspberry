import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

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

    String PathToFiles()
    {
        String path = Main.class.getProtectionDomain().getCodeSource().getLocation().getPath() + "/Programs";

        File f = new File(path);
        if(!f.isDirectory())
        {
            f.mkdir();
        }

        return path;
    }
}
