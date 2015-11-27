package spring2015code.io;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by winston on 1/22/15.
 * CS 351 spring 2015
 */
public class IOHelpers
{
  /**
   * given a file name returns the absolute path of that file in URL format.
   * taken from:
   * http://docs.oracle.com/javase/tutorial/jaxp/sax/parsing.html
   *
   * @param filename
   * @return
   */
  public static String convertToFileURL(String filename)
  {
    String path = new File(filename).getAbsolutePath();
    if (File.separatorChar != '/')
    {
      path = path.replace(File.separatorChar, '/');
    }

    if (!path.startsWith("/"))
    {
      path = "/" + path;
    }
    return "file:" + path;
  }

  public static List<String> getFilesInDir(String dirName)
  {
    File folder = new File(dirName);

    if (!folder.isDirectory()) return null;

    List<String> files = new LinkedList<>();

    for (File f : folder.listFiles())
    {
      if (!f.isHidden()) files.add(f.getPath());
    }
    return files;
  }

  public static List<String> readIndex(String indexPath) throws FileNotFoundException, IOException
  {
    InputStream resourceStream = IOHelpers.class.getResourceAsStream(indexPath);
    if (resourceStream == null) throw new FileNotFoundException(indexPath);

    BufferedReader reader = new BufferedReader(new InputStreamReader(resourceStream));

    List<String> files = new ArrayList<>();

    try
    {
      String entry;
      while ((entry = reader.readLine()) != null)
      {
        files.add(entry);
      }
    } catch (IOException ex)
    {
      Logger.getGlobal().log(Level.SEVERE, "Error parsing geography index", ex);
      ex.printStackTrace();
    }

    reader.close();
    resourceStream.close();

    return files;
  }
}
