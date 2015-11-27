package starvationevasion.io.CSVhelpers;

import java.io.File;

/**
 * Exception class for errors when parsing CSV file.
 * @author  jessica
 * @version Mar-15-2015
 */
public class CSVParsingException extends RuntimeException
{
  public String field;
  public Object record;
  public File csvFile;
  
  /**
   * Constructor takes info about the file and the particular record (i.e. row) causing error.
   * @param record    CSVRecord causing error
   * @param csvFile   File containing record
   */
  public CSVParsingException(String field, Object record, File csvFile)
  {
    super();
    this.field = field;
    this.record = record;
    this.csvFile = csvFile;
  }
 
}
