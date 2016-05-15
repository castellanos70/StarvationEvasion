package starvationevasion.PreprocessingTools;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.opencsv.CSVWriter;
/**
 * This file is used to limit the amount of data parsed from the raw 
 * production and yield data collected. 
 * @author Robert Spidle
 *
 */
public class ProductionCropSplit
{
  private static String allDataCSV = "C:/Users/Rob/Google Drive/CS 351/SE Data/Production_Crops_E_All_Data_(Norm).csv";
  private static String limitedDataCSV = "C:/Users/Rob/Google Drive/CS 351/SE Data/limitedData.csv";
  private BufferedReader csvReader;
  private CSVWriter csvWriter;
  private String[] limitedData = new String[500000];

  public ProductionCropSplit() throws IOException
  {
    try
    {
      this.csvReader = new BufferedReader(new FileReader(allDataCSV));
      this.csvWriter = new CSVWriter(new FileWriter(limitedDataCSV));
    } catch (FileNotFoundException e)
    {
      e.printStackTrace();
    }
  }

  private void splitData(BufferedReader dataReader) throws IOException
  {
    String line = dataReader.readLine();
    String[] splitLine;
    int yearIdx, valueIdx, elementIdx, itemIdx;
    int countryIdx = 1;
    int counter = 1;
    limitedData[0] = "Country, Item, Element, Year, Value";
    
    while ((line = dataReader.readLine()) != null)
    {
      
      splitLine = line.split("\\s*,\\s*", -1);

      yearIdx = splitLine.length - 4;
      valueIdx = splitLine.length -2;
      elementIdx = splitLine.length -6;
      itemIdx = 3;

      if (splitLine[elementIdx].startsWith("\"Area") || splitLine[elementIdx].startsWith("\"Seed\""))
      {
        continue;
      }
      
      Integer year = Integer.parseInt(splitLine[yearIdx].replaceAll("\"", ""));
      if (year < 2000 || year > 2010)
      {
        continue;
      }
      
      if(splitLine[countryIdx].replaceAll("\"", "").equals("Saint Helena"))
      {
      	continue;
      }
      
      if(splitLine[elementIdx].startsWith("\"Yield\""))
      {
        Integer yieldInt = (int)Double.parseDouble(splitLine[valueIdx].replaceAll("\"", ""));
        yieldInt /= 100;
        splitLine[valueIdx] = Integer.toString(yieldInt);
        
      	if(splitLine[valueIdx].equals(""))
      	{
      		splitLine[valueIdx] = "0";
      	}
      }
      
      if(splitLine[elementIdx].startsWith("\"Production\""))
      {
      	if(splitLine[valueIdx].equals(""))
      	{
      		splitLine[valueIdx] = "0";
      	}
      }

      
      if(splitLine[countryIdx].replaceAll("\"", "").equals("China"))
      {
      	if(splitLine[countryIdx + 1].replaceAll("\"", "").equals("mainland") || 
      			splitLine[countryIdx + 1].replaceAll("\"", "").equals("Taiwan Province of") ||
      			splitLine[countryIdx + 1].replaceAll("\"", "").equals("Hong Kong SAR") ||
      			splitLine[countryIdx + 1].replaceAll("\"", "").equals("Macao SAR"))
      	{
          splitLine[countryIdx] = splitLine[countryIdx] + " " + splitLine[countryIdx + 1];
          itemIdx = 4;
      	}
      }
      
      limitedData[counter] = splitLine[countryIdx] + "," + splitLine[itemIdx] + "," + splitLine[elementIdx] +
      																								"," + splitLine[yearIdx] + "," + splitLine[valueIdx];
      limitedData[counter] = limitedData[counter].replaceAll("\"", "");
      counter++;
      
    }
    for(int i = 0; i < limitedData.length; i++)
    {
      if(limitedData[i] != null)
      {
        csvWriter.writeNext(limitedData[i].split("\\s*,\\s*"));
      }
      
    }
    csvWriter.close();
  }

  public static void main(String[] args)
  {
    ProductionCropSplit data = null;
    try
    {
      data = new ProductionCropSplit();
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