package starvationevasion.PreprocessingTools;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by James on 3/30/2016.
 */

public class JavaWebCrawler_IndexMundi
{

  Country[] objIntercept = new Country[]{new Country("Afghanistan", "af"), new Country("Albania", "al"), new Country("Algeria", "dz"), new Country("Argentina", "ar"), new Country("Armenia", "am"), new Country("Australia", "au"), new Country("Azerbaijan", "az"), new Country("Belarus", "by"), new Country("Bolivia", "bo"), new Country("Bosnia and Herzegovina", "ba"), new Country("Brazil", "br"), new Country("Bulgaria", "bg"), new Country("Canada", "ca"), new Country("Chile", "cl"), new Country("China", "cn"), new Country("Colombia", "co"), new Country("Croatia", "hr"), new Country("Cuba", "cu"), new Country("Cyprus", "cy"), new Country("Czech Republic", "cz"), new Country("Czechoslovakia", "c2"), new Country("Ecuador", "ec"), new Country("Egypt", "eg"), new Country("Estonia", "ee"), new Country("Ethiopia", "et"), new Country("Georgia", "ge"), new Country("Hungary", "hu"), new Country("India", "in"), new Country("Iran, Islamic Republic Of", "ir"), new Country("Iraq", "iq"), new Country("Israel", "il"), new Country("Japan", "jp"), new Country("Kazakhstan", "kz"), new Country("Kenya", "ke"), new Country("Korea, Republic Of", "kr"), new Country("Kyrgyzstan", "kg"), new Country("Latvia", "lv"), new Country("Lebanon", "lb"), new Country("Lithuania", "lt"), new Country("Macedonia, The Former Yugoslav Republic Of", "mk"), new Country("Mexico", "mx"), new Country("Moldova, Republic Of", "md"), new Country("Morocco", "ma"), new Country("Norway", "no"), new Country("Pakistan", "pk"), new Country("Peru", "pe"), new Country("Poland", "pl"), new Country("Romania", "ro"), new Country("Russian Federation", "ru"), new Country("Serbia", "rs"), new Country("Serbia and Montenegro", "cs"), new Country("Singapore", "sg"), new Country("Slovakia", "sk"), new Country("Slovenia", "si"), new Country("South Africa", "za"), new Country("Switzerland", "ch"), new Country("Syrian Arab Republic", "sy"), new Country("Taiwan, Province Of China", "tw"), new Country("Tajikistan", "tj"), new Country("Tunisia", "tn"), new Country("Turkey", "tr"), new Country("Turkmenistan", "tm"), new Country("U.S.S.R.", "su"), new Country("Ukraine", "ua"), new Country("United States", "us"), new Country("Uruguay", "uy"), new Country("Uzbekistan", "uz"), new Country("Venezuela", "ve"), new Country("Yemen", "ye"), new Country("Yemen (Sanaa)", "ys"), new Country("Yugoslavia", "yu"), new Country("Zimbabwe", "zw")};
  String[] Commodities = {"barley", "corn", "cotton"};
  String[] Graphs = {"exports", "imports", "yield"};
  String blank = ",,";
  String c = ",";
  public static void main(String[] args) throws IOException
  {
    PrintWriter writer = new PrintWriter(".txt", "UTF-8");
    writer.println("Food, Category, Country, Region, Year, Imports, Exports, Production, Consumption, Yield");
    writer.println("The second line");
    writer.close();

    Document document = Jsoup.connect("http://www.indexmundi.com/agriculture/?Country=af&commodity=barley&graph=imports").get();
    Elements table = document.select("#gvData");
    System.out.println(table.size());
    for (Element row : table.select("tr")) {
      Elements tds = row.select("td");
      if (tds.size() > 2 && Integer.parseInt(tds.get(0).text()) > 1999) {
        System.out.println(tds.get(0).text() + ":" + tds.get(1).text() + ":" + tds.get(2).text());
      }
    }
  }
  class Country
  {
    String name = "";
    String id = "";
    Country(String name, String id)
    {
      this.name = name;
      this.id = id;
    }
  }
}
