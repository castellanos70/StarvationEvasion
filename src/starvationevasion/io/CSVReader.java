package starvationevasion.io;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/* CSVReader is a quick replacement for the Apache CSV library used by the previous
 * semester's team.  It may not be completely robust.
*/
public class CSVReader
{

  private final static Logger LOGGER = Logger.getLogger(CSVReader.class.getName());

  private InputStream stream;
  private BufferedReader reader;
  private String[] columns;
  private List<CSVRecord> records;
  private String path;

  /** Constructs a new CSVReader
  */
  public CSVReader()
  {
  }

  /** Reads CSV data from an input stream, parsing it into a record structure compatible
  ** with the Apache reader.
  ** @param stream An input stream.
  */
  public void read(final InputStream stream) throws IOException
  {
    this.stream = stream;
    this.reader = new BufferedReader(new InputStreamReader(stream));
    this.records = new ArrayList<>();

    int lineNo = 0;
    String line;
    while ((line = reader.readLine()) != null)
    { // Process the line.
      //
      String[] tokens = line.split(",");
      if (lineNo == 0) 
      { // The first line contains the column names.
        //
        columns = tokens;
      }
      else
      { Map<String, String> tokenMap = new HashMap<>();
        for (int i = 0 ; i < columns.length ; i += 1)
        { String tok = "";
          if (i < tokens.length) tok = tokens[i];
          tokenMap.put(columns[i], tok);
        }

        records.add(new CSVRecord(lineNo, tokenMap));
      }

      lineNo += 1;
    }
  }



  /**
   * Opens the given file from the root data path. Sets class state variables.<br>
   * Then reads and trashes headerLines.<br>
   * @param resource Path to resource.
   * @param headerLines Numer of lines to skip.
   */
  public CSVReader(String resource, int headerLines) throws FileNotFoundException
  {
    InputStream inputStream = getClass().getResourceAsStream(resource);
    if (inputStream == null) throw new FileNotFoundException(resource);
    this.path = resource;
    try
    {
      reader = new BufferedReader(new InputStreamReader(inputStream));
      for (int i=0; i<headerLines; i++)
      { reader.readLine(); //trash line i
      }
    }
    catch (IOException e)
    { throw new IllegalArgumentException(e.getMessage());
    }
  }

  /**
   * Reads a record from class field reader<br>
   * Automatically closes the file if end-of-file or a record is read with
   * a number of fields not equal to the input fieldCount.
   * @param fieldCount expected number of fields.
   * @return String[] of fields of that record or null if end-of-file or error.
   */
  public String[] readRecord(int fieldCount)
  {
    String str = null;
    try
    {
      str = reader.readLine();
      //System.out.println(str);
      if (str == null)
      { reader.close();
        return null;
      }
    }
    catch (IOException e)
    { LOGGER.severe(e.getMessage());
      e.printStackTrace();
    }

    String[] fields = str.split(",");
    if (fields.length > fieldCount)
    {
      LOGGER.severe("****ERROR reading " + path + ": Expected " + fieldCount +
        " fields but read "+ fields.length);
      return null;
    }
    return fields;
  }



  public void trashRecord()
  {
    try
    {
      reader.readLine();
    }
    catch (IOException e)
    { LOGGER.severe(e.getMessage());
      e.printStackTrace();
    }
  }





  /**
   * @return The list of parsed records.
  */
  public List<CSVRecord> getRecords()
  { return records;
  }

  /**
   * @return The header text parsed from the CSV file.
  */
  public String[] getHeaders()
  { return columns;
  }

  /**
   * Closes the reader.
  */
  public void close() throws IOException
  {
    reader.close();;
    stream.hashCode();
  }

  /**
   * The data structure for storing the parsed rows.
  */
  public static class CSVRecord
  {
    private int record = 0;
    private Map<String, String> tokens;

    public CSVRecord(int number, Map<String, String> tokens)
    {
      this.record = number;
      this.tokens = tokens;
    }

    /**
	 * @return the record number of this row.
	 */
    public int getRecordNumber()
    { return record;
    }

    /**
	 * @param key to lookup.
	 * @return value stored under the key.
	 */
    public String get(String key)
    { return tokens.get(key);
    }

    /**
	 * @return the map of all keys to data.
	 */
    public Map<String,String> toMap()
    {
      return tokens;
    }
  }
}
