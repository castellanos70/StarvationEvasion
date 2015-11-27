package starvationevasion.io;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* CSVReader is a quick replacement for the Apache CSV library used by the previous
 * semester's team.  It may not be completely robust.
*/
public class CSVReader
{
  private InputStream stream;
  private BufferedReader reader;
  private String[] columns;
  private List<CSVRecord> records;

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

  /** Reads CSV data from an input file, parsing it into a record structure compatible
  ** with the Apache reader.
  ** @param file An input file.
  */
  public void read(final File file) throws IOException
  {
    read(new FileInputStream(file));
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
   * @return Closes the reader.
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
	 * @param the key to lookup.
	 * @return the value stored under the key.
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
