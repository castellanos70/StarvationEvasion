package starvationevasion.PreprocessingTools;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import com.opencsv.CSVWriter;

/**
 * This class serves the purpose of combining the data collected for various
 * countries crop yields, and new data collected on import, export, production,
 * and yield of numerous vegetables, special items, and bananas into the main
 * games WorldFoodProduction_2000-2009.csv file.
 * 
 * @author Robert Spidle
 *
 */
public class Data_Combine
{
  private ArrayList<String> bannanaData;
  private ArrayList<String> specialData;
  private ArrayList<String> veggieData;
  private HashMap<Integer, HashMap<Integer, String[]>> yieldMap;

  /**
   * Default constructor that initializes the array lists for the new data and
   * the map used in updating the existing crop yield data.
   */
  public Data_Combine()
  {
    this.bannanaData = new ArrayList<>();
    this.specialData = new ArrayList<>();
    this.veggieData = new ArrayList<>();
    this.yieldMap = new HashMap<>();
  }

  /**
   * Main function for reading in all necessary data files and parsing all the
   * data into the out file format.
   */
  private void updateYieldData()
  {
    BufferedReader yieldReader = null;
    BufferedReader gameReader = null;
    CSVWriter outputWriter = null;
    BufferedReader newData = null;

    try
    {
      yieldReader = new BufferedReader(
          new FileReader("C:/Users/Rob/Google Drive/CS 351/SE Data/All in game files/yieldData.csv"));
      gameReader = new BufferedReader(new FileReader(
          "C:/Users/Rob/Google Drive/CS 351/SE Data/All in game files/WorldFoodProduction_2000-2009.csv"));
      outputWriter = new CSVWriter(new FileWriter(
          "C:/Users/Rob/Google Drive/CS 351/SE Data/All in game files/WorldFoodProduction_2000-2009_V2.csv"));
      outputWriter.flush();
    } catch (IOException e)
    {
      System.out.println("You Done Goofed...");
      e.printStackTrace();
    }

    String yieldLine;
    String[] yieldSplit;
    try
    {
      yieldLine = yieldReader.readLine(); // read the header line
    } catch (IOException e1)
    {
      e1.printStackTrace();
    }
    System.out.println("Making Map...");
    try
    {
      String countryKey, cropKey;
      int valueIdx = 0;
      while ((yieldLine = yieldReader.readLine()) != null) // reads all the data
                                                           // lines
      {
        yieldSplit = yieldLine.replaceAll("\"", "").split("\\s*,\\s*");
        countryKey = yieldSplit[0]; // country value to be hashed
        cropKey = yieldSplit[1]; // crop value to be hashed
        switch (yieldSplit[3]) // switch over the year
        {
        case "2000":
          valueIdx = 0;
          break;
        case "2001":
          valueIdx = 1;
          break;
        case "2002":
          valueIdx = 2;
          break;
        case "2003":
          valueIdx = 3;
          break;
        case "2004":
          valueIdx = 4;
          break;
        case "2005":
          valueIdx = 5;
          break;
        case "2006":
          valueIdx = 6;
        case "2007":
          valueIdx = 7;
          break;
        case "2008":
          valueIdx = 8;
          break;
        case "2009":
          valueIdx = 9;
          break;
        }

        if (yieldMap.containsKey(countryKey.hashCode())) // check if the country
                                                         // is in the map
        {
          if (yieldMap.get(countryKey.hashCode()).containsKey(cropKey.hashCode())) // check
                                                                                   // if
                                                                                   // the
                                                                                   // crop
                                                                                   // is
                                                                                   // in
                                                                                   // the
                                                                                   // map
          {
            yieldMap.get(countryKey.hashCode()).get(cropKey.hashCode())[valueIdx] = yieldSplit[4]; // update
                                                                                                   // the
                                                                                                   // yield
                                                                                                   // for
                                                                                                   // the
                                                                                                   // given
                                                                                                   // year
          } else // make a new crop map and add the current yield
          {
            yieldMap.get(countryKey.hashCode()).put(cropKey.hashCode(), new String[10]);
            yieldMap.get(countryKey.hashCode()).get(cropKey.hashCode())[valueIdx] = yieldSplit[4];
          }
        } else // add the crop map for the current country
        {
          HashMap<Integer, String[]> cropMap = new HashMap<>();
          cropMap.put(cropKey.hashCode(), new String[10]);
          yieldMap.put(countryKey.hashCode(), cropMap);
          yieldMap.get(countryKey.hashCode()).get(cropKey.hashCode())[valueIdx] = yieldSplit[4];
        }
      }
    } catch (IOException e)
    {
      e.printStackTrace();
    }

    System.out.println("Map done, updating yields...");
    String gameLine, gameLineCountryKey, gameLineCropKey;
    String[] gameSplit;
    try
    {
      int valueIdx = 0;
      gameLine = gameReader.readLine() + ",source";
      outputWriter.writeNext(gameLine.split("\\s*,\\s*"));
      gameLine = gameReader.readLine() + ",Yield Source";
      outputWriter.writeNext(gameLine.split("\\s*,\\s*"));
      while ((gameLine = gameReader.readLine()) != null)
      {
        gameSplit = gameLine.replaceAll("\"", "").split("\\s*,\\s*"); // get the
                                                                      // line
                                                                      // from
                                                                      // the
                                                                      // original
                                                                      // file

        // skips the categories not included in our data
        if (gameSplit[1].equals("GRAIN") || gameSplit[1].equals("DAIRY") || gameSplit[1].equals("FEED")
            || gameSplit[1].equals("MEAT") || gameSplit[1].equals("OIL") || gameSplit[1].equals("FISH")
            || gameSplit[1].equals("POULTRY") || gameSplit[0].equals("Raisins")) // DAIRY,
                                                                                 // FEED,
                                                                                 // RAISINS,
                                                                                 // MEAT,
                                                                                 // OIL,
        {
          gameLine = gameLine + ",";
          gameSplit = gameLine.split("\\s*,\\s*", -1);
          outputWriter.writeNext(gameSplit);
          continue;
        }

        gameLineCountryKey = gameSplit[2]; // country value to be hashed
        gameLineCropKey = gameSplit[0]; // crop value to be hashed

        switch (gameSplit[4]) // year to check
        {
        case "2000":
          valueIdx = 0;
          break;
        case "2001":
          valueIdx = 1;
          break;
        case "2002":
          valueIdx = 2;
          break;
        case "2003":
          valueIdx = 3;
          break;
        case "2004":
          valueIdx = 4;
          break;
        case "2005":
          valueIdx = 5;
          break;
        case "2006":
          valueIdx = 6;
        case "2007":
          valueIdx = 7;
          break;
        case "2008":
          valueIdx = 8;
          break;
        case "2009":
          valueIdx = 9;
          break;
        }

        String yield;
        if (yieldMap.containsKey(gameLineCountryKey.hashCode()))
        {
          if (yieldMap.get(gameLineCountryKey.hashCode()).containsKey(gameLineCropKey.hashCode()))
          {
            yield = yieldMap.get(gameLineCountryKey.hashCode()).get(gameLineCropKey.hashCode())[valueIdx];
            gameLine = gameLine + "," + "FAOStats";
            gameSplit = gameLine.split("\\s*,\\s*", -1);
            gameSplit[8] = yield; // update the yield
          } else
          {
            gameLine = gameLine + ",";
            gameSplit = gameLine.split("\\s*,\\s*", -1);
            if (gameSplit[7].equals("0"))
            {
              gameSplit[8] = "-1"; // production is zero so yield is -1,
                                   // otherwise yield is unchanged
            }
          }
        } else
        {
          gameLine = gameLine + ",";
          gameSplit = gameLine.split("\\s*,\\s*", -1);
          if (gameSplit[7].equals("0"))
            gameSplit[8] = "-1";// production is zero so yield is -1
        }
        outputWriter.writeNext(gameSplit);
      }
      // outputWriter.flush();
    } catch (IOException e)
    {
      e.printStackTrace();
    }
    System.out.println("Yeilds done, making new data...");
    String dataLine, dataRegion, category;
    String[] dataSplit;
    try
    {
      newData = new BufferedReader(
          new FileReader("C:/Users/Rob/Google Drive/CS 351/SE Data/All in game files/missingDataToAdd.csv"));
      dataLine = newData.readLine();
      while ((dataLine = newData.readLine()) != null) // new data to be added
      {

        dataSplit = dataLine.replaceAll("\"", "").split("\\s*,\\s*");
        dataRegion = getRegion(dataSplit[2]);
        dataSplit[3] = dataRegion;

        category = dataSplit[1];
        String completeData;

        if (category.equals("FRUIT"))
        {
          completeData = "";
          for (int i = 0; i < dataSplit.length; i++)
          {
            if (i == 0)
              completeData = dataSplit[0];
            else
              completeData = completeData + "," + dataSplit[i];
          }
          bannanaData.add(completeData);
        } else if (category.equals("SPECIAL"))
        {
          completeData = "";
          for (int i = 0; i < dataSplit.length; i++)
          {
            if (i == 0)
              completeData = dataSplit[0];
            else
              completeData = completeData + "," + dataSplit[i];
          }
          specialData.add(completeData);
        } else if (category.equals("VEGGIES"))
        {
          completeData = "";
          for (int i = 0; i < dataSplit.length; i++)
          {
            if (i == 0)
              completeData = dataSplit[0];
            else
              completeData = completeData + "," + dataSplit[i];
          }
          veggieData.add(completeData);
        }
      }
      System.out.println("Writing new data...");
      writeArray(outputWriter, bannanaData); // write our new banana data
      writeArray(outputWriter, specialData); // write our new special data
      writeArray(outputWriter, veggieData); // write our new veggie data
      outputWriter.flush();
      outputWriter.close();

    } catch (IOException e)
    {
      e.printStackTrace();
    }
  }

  private String getRegion(String country) // gets the region for the country
  {
    switch (country)
    {
    case "Canada":
      return "ARCTIC_AMERICA";

    case "Afghanistan":
    case "Kazakhstan":
    case "Kyrgyzstan":
    case "Tajikistan":
    case "Turkmenistan":
    case "Uzbekistan":
      return "CENTRAL_ASIA";

    case "Cambodia":
    case "China":
    case "Hong Kong":
    case "Japan":
    case "Laos":
    case "Macao":
    case "Mongolia":
    case "Republic of Korea":
    case "South Korea":
    case "Taiwan":
    case "Vietnam":
      return "EAST_ASIA";

    case "Albania":
    case "Austria":
    case "Belarus":
    case "Belgium":
    case "Bosnia and Herzegovina":
    case "Bulgaria":
    case "Croatia":
    case "Czech Republic":
    case "Denmark":
    case "Estonia":
    case "EU-27":
    case "Europe":
    case "Faroe Islands":
    case "Finland":
    case "France":
    case "Germany":
    case "Greece":
    case "Hungary":
    case "Iceland":
    case "Ireland":
    case "Italy":
    case "Latvia":
    case "Lithuania":
    case "Macedonia":
    case "Malta":
    case "Moldova":
    case "Montenegro":
    case "Netherlands":
    case "Norway":
    case "Poland":
    case "Romania":
    case "Serbia":
    case "Serbia and Montenegro":
    case "Slovakia":
    case "Slovenia":
    case "Spain":
    case "Sweden":
    case "Switzerland":
    case "Ukraine":
    case "United Kingdom":
    case "Portugal":
    case "Luxembourg":
      return "EUROPE";

    case "Antigua and Barbuda":
    case "Bahamas":
    case "Barbados":
    case "Belize":
    case "Bermuda":
    case "Caribbean":
    case "Costa Rica":
    case "Cuba":
    case "Dominican Republic":
    case "El Salvador":
    case "Grenada":
    case "Guatemala":
    case "Haiti":
    case "Honduras":
    case "Jamaica":
    case "Mexico":
    case "Netherlands Antilles":
    case "Nicaragua":
    case "Panama":
    case "Saint Kitts and Nevis":
    case "Trinidad and Tobago":
      return "MIDDLE_AMERICA";

    case "Algeria":
    case "Bahrain":
    case "Cyprus":
    case "Egypt":
    case "Iran":
    case "Iraq":
    case "Israel":
    case "Jordan":
    case "Kuwait":
    case "Lebanon":
    case "Libya":
    case "Morocco":
    case "North Africa":
    case "Oman":
    case "Qatar":
    case "Saudi Arabia":
    case "Sudan":
    case "Syrian Arab Republic":
    case "Tunisia":
    case "Turkey":
    case "United Arab Emirates":
    case "Yemen":
      return "MIDDLE_EAST";

    case "Australia":
    case "Brunei Darussalam":
    case "Fiji":
    case "Indonesia":
    case "Malaysia":
    case "New Caledonia":
    case "New Zealand":
    case "Oceania":
    case "Papua New Guinea":
    case "Philippines":
    case "Samoa":
    case "Singapore":
    case "Tonga":
    case "Vanuata":
      return "OCEANIA";

    case "Armenia":
    case "Azerbaijan":
    case "Georgia":
    case "Russian Federation":
      return "RUSSIA";

    case "Argentina":
    case "Bolivia":
    case "Brazil":
    case "Chile":
    case "Colombia":
    case "Ecuador":
    case "Guyana":
    case "Latin America":
    case "Paraguay":
    case "Peru":
    case "Suriname":
    case "Uruguay":
    case "Venezuela":
      return "SOUTH_AMERICA";

    case "Bangladesh":
    case "Bhutan":
    case "India":
    case "Maldives":
    case "Myanmar":
    case "Nepal":
    case "Pakistan":
    case "Sri Lanka":
    case "Thailand":
      return "SOUTH_ASIA";

    case "Angola":
    case "Benin":
    case "Botswana":
    case "Burkina Faso":
    case "Burundi":
    case "Cameroon":
    case "Cape Verde":
    case "Central African Republic":
    case "Chad":
    case "Comoros":
    case "Congo":
    case "Congo-Kinshasa":
    case "Cote d'Ivoire":
    case "Dijibouti":
    case "Eritrea":
    case "Ethiopia":
    case "Gabon":
    case "Gambia":
    case "Ghana":
    case "Gibraltar":
    case "Guinea":
    case "Guinea-Bissau":
    case "Kenya":
    case "Lesotho":
    case "Liberia":
    case "Madagascar":
    case "Malawi":
    case "Mali":
    case "Mauritania":
    case "Mauritius":
    case "Mozambique":
    case "Namibia":
    case "Niger":
    case "Nigeria":
    case "R�union":
    case "Rwanda":
    case "Sao Tome and Principe":
    case "Senegal":
    case "Seychelles":
    case "Sierra Leone":
    case "Somalia":
    case "South Africa":
    case "Sub-Saharan":
    case "Swaziland":
    case "Tanzania":
    case "Togo":
    case "Uganda":
    case "Zambia":
    case "Zimbabwe":
      return "SUB_SAHARAN";

    case "United States":
      return "UNITED_STATES";

    default:
      return "";
    }
  }

  private void writeArray(CSVWriter writer, ArrayList<String> data)
  {
    for (String s : data)
    {
      s = s + "," + "FAOStat"; // all data being added is complete and from our
                               // FAOStat source
      writer.writeNext(s.split("\\s*,\\s*"));
    }
  }

  public static void main(String[] args)
  {
    Data_Combine data = new Data_Combine();
    data.updateYieldData();
    System.out.println("Finished.");
  }
}