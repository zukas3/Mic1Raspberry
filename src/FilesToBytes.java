import java.io.*;
import java.io.IOException;

public class FilesToBytes {

    public static byte[] ToBytes(String filePath)
    {
        File file = new File(filePath);

        byte[] b = new byte[(int) file.length()];
        try
        {
            FileInputStream fileInputStream = new FileInputStream(file);
            fileInputStream.read(b);
            for (int i = 0; i < b.length; i++)
            {
                System.out.print((char)b[i]);
            }

            return b;
        }
        catch (FileNotFoundException e)
        {
            System.out.println("File Nor Found");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
return null;
    }




}
