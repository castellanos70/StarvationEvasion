package starvationevasion.PreprocessingTools;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.opencsv.CSVWriter;

/**
 * 
 * Class used to split the file to just get the vegetables and special data
 * 
 * @author Tommy Manzanares
 */
public class VegetableOnlySplit
{

  private static String allDataCSV = "C:/Users/Tommy/Desktop/TradeData.csv";//"C:/Users/Tommy/Desktop/TradeData.csv";
  private static String limitedTradeDataCSV = "C:/Users/Tommy/Desktop/vegetableData.csv";//"C:/Users/Tommy/Desktop/tempData.csv";
  private BufferedReader csvReader;                                   //FormattedTradeData
  private CSVWriter csvWriter;
  
  // Data that we will search for
  private String[] dataToAdd = {"Bananas", "Anise", "Chocolate", "Potato", "Olives", "Pepper",
                                "Spices", "Tea", "Tomato", "Artichokes", "Asparagus", 
                                "Avocado", "Cabbages", "Carrots", "Cauliflower", "Chick peas",
                                "Cinnamon", "Cucumbers", "Eggplant", "Garlic", "Ginger", "Leeks", "Lettuce",
                                "Spinach", "Vanilla"};
  
  //private String[] extendedDataToAdd;
  
  //Fresh nes vegetables
  //Vegetables, fresh nes
  //Vegetables, preserved nes
  //green beans
  //Potatoes frozen
  
  
  
  public VegetableOnlySplit() throws IOException
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
   * Splits the line from the csv file. Then only writes the line if it contains something from the dataToAdd array.
   * 
   * @param dataReader
   * @throws IOException
   */
  private void splitData(BufferedReader dataReader) throws IOException
  {
    String line = dataReader.readLine();
    String[] splitLine;
    String limitedData = "";
    int countryIdx = 1;
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
      
      if(splitLine[1].contains("EU(12)ex")) break;
      if(splitLine[elementIdx].contains("Value")) continue;
      
      Integer year = Integer.parseInt(splitLine[yearIdx].replaceAll("\"", ""));
      if(year < 2000 || year > 2009) continue;
      
      //if(splitLine[itemIdx].contains("and") || splitLine[itemIdx].contains("+") || splitLine[itemIdx].contains("&") || splitLine[itemIdx].contains("-")) continue;
      if(splitLine[itemIdx].contains("+") || splitLine[itemIdx].contains("vinegar") || splitLine[itemIdx].contains("oil") || splitLine[itemIdx].contains("Sugar")) continue;
      
      //System.out.println(splitLine[itemIdx - 1]);
      //System.out.println(splitLine[elementIdx]);
      for (int i = 0; i < dataToAdd.length; i++)
      {
        if (splitLine[itemIdx].contains(dataToAdd[i]))// || splitLine[itemIdx].contains("Veg"))
        {
          
          if (splitLine[elementIdx].startsWith("\"Export\""))
          {
            splitLine[elementIdx] = ("Export");
            Integer vegData = (int) Double.parseDouble(splitLine[valueIdx].replaceAll("\"", ""));
            splitLine[valueIdx] = Integer.toString(vegData);
          }

          if (splitLine[elementIdx].startsWith("\"Import\""))
          {
            splitLine[elementIdx] = ("Import");
            Integer vegData = (int) Double.parseDouble(splitLine[valueIdx].replaceAll("\"", ""));
            splitLine[valueIdx] = Integer.toString(vegData);
          }

          if(splitLine[valueIdx].equals("")) limitedData = splitLine[1] + "," + splitLine[itemIdx] + "," + splitLine[elementIdx] + "," + splitLine[yearIdx] + "," + 0;
          else limitedData = splitLine[1] + "," + splitLine[itemIdx] + "," + splitLine[elementIdx] + "," + splitLine[yearIdx] + "," + splitLine[valueIdx];
          
          limitedData = limitedData.replaceAll("\"", "");
          csvWriter.writeNext(limitedData.split("\\s*,\\s*"));
          break;
        }
      }
      
      if (splitLine[itemIdx - 1].replaceAll("\"", "").equals("Vegetables") && (splitLine[itemIdx].contains("preserved nes") || splitLine[itemIdx].contains("fresh nes")))// || splitLine[itemIdx].contains("Veg"))
      {
        if(splitLine[valueIdx].equals("")) limitedData = splitLine[countryIdx] + "," + ("" + splitLine[itemIdx - 1] + " " + splitLine[itemIdx]) + "," + splitLine[elementIdx] + "," + splitLine[yearIdx] + "," + 0;
        else limitedData = splitLine[countryIdx] + "," + ("" + splitLine[itemIdx - 1] + " " + splitLine[itemIdx]) + "," + splitLine[elementIdx] + "," + splitLine[yearIdx] + "," + splitLine[valueIdx];
        limitedData = limitedData.replaceAll("\"", "");
        csvWriter.writeNext(limitedData.split("\\s*,\\s*"));
      }
      
      if (splitLine[itemIdx - 1].replaceAll("\"", "").equals("Honey") && (splitLine[itemIdx].contains("natural")))
      {
        if(splitLine[valueIdx].equals("")) limitedData = splitLine[countryIdx] + "," + splitLine[itemIdx - 1] + "," + splitLine[elementIdx] + "," + splitLine[yearIdx] + "," + 0;
        else limitedData = splitLine[countryIdx] + "," + splitLine[itemIdx - 1] + "," + splitLine[elementIdx] + "," + splitLine[yearIdx] + "," + splitLine[valueIdx];
        limitedData = limitedData.replaceAll("\"", "");
        csvWriter.writeNext(limitedData.split("\\s*,\\s*"));
      }
      
      //Might break
      /*if(splitLine[itemIdx].replaceAll("\"", "").equals("Vegetables"))
      {
        if (splitLine[itemIdx + 1].replaceAll("\"", "").equals("fresh nes") || splitLine[itemIdx + 1].replaceAll("\"", "").equals("preserved nes"))
        {
          splitLine[itemIdx] = splitLine[itemIdx] + " " + splitLine[itemIdx + 1];
          
          //itemIdx = 4;
          
          limitedData = splitLine[1] + "," + splitLine[itemIdx] + "," + splitLine[elementIdx] + "," + splitLine[yearIdx] + "," + splitLine[valueIdx];
          limitedData = limitedData.replaceAll("\"", "");
          csvWriter.writeNext(limitedData.split("\\s*,\\s*"));
        }
      }*/
      
      
    }
    
    System.out.println("Finished");
    csvWriter.close();
  }
  
  public static void main(String[] args)
  {
    VegetableOnlySplit data = null;
    try
    {
      data = new VegetableOnlySplit();
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