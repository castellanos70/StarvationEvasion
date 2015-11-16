package starvationevasion.sim;

import java.util.ArrayList;
import java.util.regex.*;
import java.io.FileReader;
import java.io.BufferedReader;

/**
 * The class DataReader provides static methods to retrieve data.
 */
public class DataReader
{
  /**
   * This method is capable of returning the contents <br></>
   * of a CSV file as an ArrayList of Strings.
   *
   * @param path
   * @return
   */
  public static ArrayList<String> retrieveStateData(String path)
  {
    String file = System.getenv("PWD")+"/"+path;
    ArrayList<String> fileContents = new ArrayList<>();
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
          fileContents.add(line);
        }
      }
    }
    catch (Throwable t)
    {
      System.err.println("File Reader Exception: "+ t);
    }
    return fileContents;
  }
}
