package starvationevasion.sim;

import java.io.File;
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
  public static FileObject retrieveStateData(String path)
  {
    return new FileObject(path);
  }
}
