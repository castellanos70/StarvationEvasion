package starvationevasion.PreprocessingTools;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.opencsv.CSVWriter;

/**
 * Helper class used to remove a line type that is not wanted.
 * In this case it was removing any data for any year that is 2010.
 * 
 * @author Tommy Manzanares
 *
 */
public class removeTenthYear
{
  private static String allDataCSV = "C:/Users/Tommy/Desktop/filteredData(no10).csv";//"C:/Users/Tommy/Desktop/TradeData.csv";
  private static String limitedTradeDataCSV = "C:/Users/Tommy/Desktop/filteredDataV3";//"C:/Users/Tommy/Desktop/tempData.csv";
  private BufferedReader csvReader;                                   //FormattedTradeData
  private CSVWriter csvWriter;
  //private String[] limitedData;// = new String[2000000];

  /**
   * Makes the csvs
   * 
   * @throws IOException
   */
  public removeTenthYear() throws IOException
  {
    try
    {
      this.csvReader = new BufferedReader(new FileReader(allDataCSV));
      this.csvWriter = new CSVWriter(new FileWriter(limitedTradeDataCSV));
    } catch (FileNotFoundException e)
    {
      e.printStackTrace();
    }
  }

  private void splitData(BufferedReader dataReader) throws IOException
  {
    String line = dataReader.readLine();
    String[] splitLine;
    String limitedData = "";

    //limitedData[0] = "Country, Item, Element, Year, Value";
    csvWriter.writeNext(("Country, Item, Element, Year, Value").split("\\s*,\\s*"));

    while ((line = dataReader.readLine()) != null)
    {
      splitLine = line.split("\\s*,\\s*", -1);

      //if (splitLine[1].contains("Fruit") || splitLine[1].contains("Vegetable")) continue;

      limitedData = splitLine[0] + "," + splitLine[1] + "," + splitLine[2] + "," + splitLine[3] + "," + splitLine[4];
      // System.out.println(limitedData);
      // limitedData = line;
      limitedData = limitedData.replaceAll("\"", "");
      csvWriter.writeNext(limitedData.split("\\s*,\\s*"));
    }

    csvWriter.flush();
    csvWriter.close();
    System.out.println("Finished");
  }

  public static void main(String[] args)
  {
    removeTenthYear data = null;
    try
    {
      data = new removeTenthYear();
    } catch (IOException e1)
    {
      e1.printStackTrace();
    }
    try
    {
      data.splitData(data.csvReader);
    } catch (IOException e)
    {
      e.printStackTrace();
    }
  }
}
