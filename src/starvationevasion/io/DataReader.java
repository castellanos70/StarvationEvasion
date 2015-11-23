package starvationevasion.io;


import starvationevasion.io.FileObject;

/**
 * The class DataReader provides static methods to retrieve data.
 */
public class DataReader
{
  /**
   * This method is capable of returning the contents
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
