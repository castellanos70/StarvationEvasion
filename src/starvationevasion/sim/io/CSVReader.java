package starvationevasion.sim.io;

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


  /**
   * Opens the given file from the root data path. Sets class state variables.<br>
   * Then reads and trashes headerLines.<br>
   * @param resource Path to resource.
   * @param headerLines Number of lines to skip.
   */
  public CSVReader(String resource, int headerLines)
  {
    InputStream inputStream = getClass().getResourceAsStream(resource);
    try
    {
      reader = new BufferedReader(new InputStreamReader(inputStream));
      for (int i=0; i<headerLines; i++)
      { reader.readLine(); //trash line i
      }
    }
    catch (Exception e)
    {
      System.out.println("ERROR reading " + resource);
      e.printStackTrace();
      System.exit(0);
    }
  }


  /**
   * Opens the given file from the given inputStream. Sets class state variables.<br>
   * Then reads and trashes headerLines.<br>
   * @param inputStream stream to resource.
   * @param headerLines Number of lines to skip.
   */
  public CSVReader(InputStream inputStream, int headerLines)
  {
    try
    {
      reader = new BufferedReader(new InputStreamReader(inputStream));
      for (int i=0; i<headerLines; i++)
      { reader.readLine(); //trash line i
      }
    }
    catch (Exception e)
    {
      System.out.println("ERROR reading " + inputStream);
      e.printStackTrace();
      System.exit(0);
    }
  }

  /** Reads CSV data from an input stream, parsing it into a record structure compatible
  ** with the Apache reader.
  ** @param stream An input stream.
  */
  public void read(final InputStream stream)
  {
    this.stream = stream;
    this.reader = new BufferedReader(new InputStreamReader(stream));
    this.records = new ArrayList<>();

    int lineNo = 0;
    String line;
    try
    {

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
    catch (IOException e)
    {
      e.printStackTrace();
      System.exit(0);
    }

  }





  /**
   * Reads a record from class field reader<br>
   * Automatically closes the file if end-of-file or a record is read with
   * a number of fields not equal to the input fieldCount.
   * @param fieldCount expected number of fields. If fieldCount == 0, then reads any number of fields.
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
    {
      e.printStackTrace();
      System.exit(0);
    }

    String[] fields = str.split(",");
    if (fieldCount > 0 && fields.length > fieldCount)
    {
      LOGGER.severe("****ERROR reading " + path + ": Expected " + fieldCount +
        " fields but read "+ fields.length + "\ndata=["+str+"]");
      Thread.dumpStack();
      System.exit(0);
    }
    return fields;
  }



  /**
   * Reads a record from class field reader<br>
   * Automatically closes the file if end-of-file.
   * @return String[] of fields of that record or null if end-of-file or error.
   */
  public String[] readRecord()
  {
    return readRecord(0);
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
      System.exit(0);
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
  public void close()
  {
    try {
    reader.close();
    }
    catch (IOException e)
    {
      e.printStackTrace();
      System.exit(0);
    }
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

  public static void writeRecord(BufferedWriter writer, String[] record) throws IOException
  {
    String str = "";
    for (int i=0; i<record.length; i++)
    {
      if (i<record.length-1) str +=record[i]+',';
      else str +=record[i]+'\n';
    }
    writer.write (str);
  }


  public static void removeConsecutiveRepeatedRecords(String inFile, String outFile)
  {
    try
    {
      CSVReader fileReader = new CSVReader(inFile, 0);

      BufferedWriter writer = new BufferedWriter(new FileWriter(new File(outFile)));

      String[] fieldList1 = fileReader.readRecord();
      String[] fieldList2 = fileReader.readRecord();

      while (fieldList2 != null)
      {
        if (fieldList1.length != fieldList2.length) writeRecord(writer, fieldList1);
        else
        {
          for (int i=0; i<fieldList2.length; i++)
          {
            if(!fieldList1[i].equals(fieldList2[i]))
            {
              writeRecord(writer, fieldList1);
              break;
            }
          }
        }
        fieldList1 = fieldList2;
        fieldList2 = fileReader.readRecord();
      }
      writer.close();

    }
    catch(Exception e)
    {
      e.printStackTrace();
      System.exit(0);
    }
  }

  public static void main(String[] args)
  {
    removeConsecutiveRepeatedRecords("/tmp.csv", "tmp2.csv");
  }
}
