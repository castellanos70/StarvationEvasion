package starvationevasion.PreprocessingTools;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.opencsv.CSVWriter;

/**
 * 
 * Class used to split the data from the faoStat file into the format
 * country,item,import or export,year,value
 * 
 * This split file was later used in the compilation of one complete file with all of the data fields
 * 
 * @author Tommy Manzanares
 */
public class ImportExportSplit
{

  private static String allDataCSV = "C:/Users/Tommy/Desktop/TradeData.csv";//"C:/Users/Tommy/Desktop/TradeData.csv";
  private static String limitedTradeDataCSV = "C:/Users/Tommy/Desktop/testWriting.csv";//"C:/Users/Tommy/Desktop/tempData.csv";
  private BufferedReader csvReader;                                   //FormattedTradeData
  private CSVWriter csvWriter;
  //private String[] limitedData;// = new String[2000000];
  
  /**
   * Assigns our local csv variables a designated csv to use.
   * 
   * @throws IOException
   */
  public ImportExportSplit() throws IOException
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
  
  /**
   * Splits each piece of the line that is read in into an array.
   * This array can easily be processed for the data that it contains.
   * The while loop loops over the entire file and writes each lines import and export
   * 
   * @param dataReader
   * @throws IOException
   */
  private void splitData(BufferedReader dataReader) throws IOException
  {
    String line = dataReader.readLine();
    String[] splitLine;
    String limitedData = "";
    int itemIdx;
    int elementIdx;
    int yearIdx;
    int valueIdx;
    
    //limitedData[0] = "Country, Item, Element, Year, Value";
    csvWriter.writeNext(("Country, Item, Element, Year, Value").split("\\s*,\\s*"));
    
    while ((line = dataReader.readLine()) != null)
    {
      splitLine = line.split("\\s*,\\s*", -1);
      
      itemIdx = splitLine.length - 8;
      elementIdx = splitLine.length - 6;
      yearIdx = splitLine.length - 4;
      valueIdx = splitLine.length - 2;
      
      if(splitLine[elementIdx].contains("Value") ) continue;
      
      Integer year = Integer.parseInt(splitLine[yearIdx].replaceAll("\"", ""));
      if(year < 2000 || year > 2009) continue;
      
      if(splitLine[itemIdx].contains("and") || splitLine[itemIdx].contains("+") || splitLine[itemIdx].contains("&") || splitLine[itemIdx].contains("-")) continue;
      
      if(splitLine[elementIdx].startsWith("\"Export\""))// || splitLine[elementIdx].startsWith("\"Import\""))
      {
        //splitLine[elementIdx] = ("Export");
        Integer ieData = (int)Double.parseDouble(splitLine[valueIdx].replaceAll("\"", ""));
        splitLine[valueIdx] = Integer.toString(ieData);
      }
      
      if(splitLine[elementIdx].startsWith("\"Import\""))
      {
        //splitLine[elementIdx] = ("Import");
        Integer ieData = (int)Double.parseDouble(splitLine[valueIdx].replaceAll("\"", ""));
        splitLine[valueIdx] = Integer.toString(ieData);
      }
      
      limitedData = splitLine[1] + "," + splitLine[3] + "," + splitLine[elementIdx] + "," + splitLine[yearIdx] + "," + splitLine[valueIdx];
      limitedData = limitedData.replaceAll("\"", "");
      csvWriter.writeNext(limitedData.split("\\s*,\\s*"));
    }
    
    
    /*for(int i = 0; i < limitedData.length; i++)
    {
      if(limitedData[i] != null) csvWriter.writeNext(limitedData[i].split("\\s*,\\s*"));
      else break;
    }*/
    System.out.println("Finished");
    csvWriter.close();
  }
  
  public static void main(String[] args)
  {
    ImportExportSplit data = null;
    try
    {
      data = new ImportExportSplit();
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
