package starvationevasion.sim;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Alfred on 11/17/15.
 *
 * FileObject class is meant to represent a CSV file, which can
 * can be queried.
 */
public class FileObject
{

  private ArrayList<String>      rawData;
  private HashMap<String, int[]> fileFields;

  /**
   * Class constructor returns a new FileObject that
   * represents path
   *
   * @param path
   */
  public FileObject(String path)
  {
    String file = System.getenv("PWD")+"/"+path;
    rawData = new ArrayList<>();
    //read in data
    Pattern stateDataPattern = Pattern.compile("^\\w+,\\d+");
    try
    {
      BufferedReader reader = new BufferedReader(new FileReader(file));
      String line = null;
      while ((line = reader.readLine()) != null)
      {
        Matcher stateDataMatcher = stateDataPattern.matcher(line);
        if (stateDataMatcher.find())
        {
          rawData.add(line);
        }
      }
    }
    catch (Throwable t)
    {
      System.err.println("File Reader Exception: "+ t);
    }

    fileFields = new HashMap<>();
    int numRecords = rawData.size();

    for (int i = 0; i < numRecords; i++)
    {
      String data = rawData.get(i);
      String[] components = data.split(",");

      int j = 0;
      int numComponents = components.length;
      int [] fields = new int[numComponents-1];
      String currentState = null;
      for (String c : components)
      {
        if (j == 0)
        {
          currentState = c;
        }
        else
        {
          fields[j-1] = Integer.parseInt(c);
        }

        j++;
      }

      fileFields.put(currentState, fields);
    }
  }

  /**
   * Method returns the original file contents as an ArrayList
   * of Strings.
   *
   * @return
   */
  public ArrayList<String> getRawData()
  {
    return rawData;
  }

  /**
   * Method provides a "record," given the proper key, else
   * returns null.
   *
   * @param key
   * @return
   */
  public int[] dataFields(String key)
  {
    return fileFields.get(key);
  }

  /**
   * Method returns a Set of Strings which are iterable
   * over the entire data set.
   *
   * @return
   */
  public Set<String> dataKeys()
  {
    return fileFields.keySet();
  }
}
