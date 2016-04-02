package starvationevasion.PreprocessingTools;

//import org.jsoup.Jsoup;
//import org.jsoup.nodes.Document;
//import org.jsoup.nodes.Element;
//import org.jsoup.select.Elements;
//
//import java.io.IOException;
//import java.io.PrintWriter;

/**
 * Created by James on 3/30/2016.
 */

public class JavaWebCrawler_IndexMundi
{
//
//
//  private final static Country[] objIntercept = new Country[]{new Country("Afghanistan", "af"), new Country("Albania", "al"), new Country("Algeria", "dz"), new Country("Argentina", "ar"), new Country("Armenia", "am"), new Country("Australia", "au"), new Country("Azerbaijan", "az"), new Country("Belarus", "by"), new Country("Bolivia", "bo"), new Country("Bosnia and Herzegovina", "ba"), new Country("Brazil", "br"), new Country("Bulgaria", "bg"), new Country("Canada", "ca"), new Country("Chile", "cl"), new Country("China", "cn"), new Country("Colombia", "co"), new Country("Croatia", "hr"), new Country("Cuba", "cu"), new Country("Cyprus", "cy"), new Country("Czech Republic", "cz"), new Country("Czechoslovakia", "c2"), new Country("Ecuador", "ec"), new Country("Egypt", "eg"), new Country("Estonia", "ee"), new Country("Ethiopia", "et"), new Country("Georgia", "ge"), new Country("Hungary", "hu"), new Country("India", "in"), new Country("Islamic Republic Of Iran", "ir"), new Country("Iraq", "iq"), new Country("Israel", "il"), new Country("Japan", "jp"), new Country("Kazakhstan", "kz"), new Country("Kenya", "ke"), new Country("Republic Of Korea", "kr"), new Country("Kyrgyzstan", "kg"), new Country("Latvia", "lv"), new Country("Lebanon", "lb"), new Country("Lithuania", "lt"), new Country("The Former Yugoslav Republic Of Macedonia", "mk"), new Country("Mexico", "mx"), new Country("Republic Of Moldova", "md"), new Country("Morocco", "ma"), new Country("Norway", "no"), new Country("Pakistan", "pk"), new Country("Peru", "pe"), new Country("Poland", "pl"), new Country("Romania", "ro"), new Country("Russian Federation", "ru"), new Country("Serbia", "rs"), new Country("Serbia and Montenegro", "cs"), new Country("Singapore", "sg"), new Country("Slovakia", "sk"), new Country("Slovenia", "si"), new Country("South Africa", "za"), new Country("Switzerland", "ch"), new Country("Syrian Arab Republic", "sy"), new Country("Province Of China Taiwan", "tw"), new Country("Tajikistan", "tj"), new Country("Tunisia", "tn"), new Country("Turkey", "tr"), new Country("Turkmenistan", "tm"), new Country("U.S.S.R.", "su"), new Country("Ukraine", "ua"), new Country("United States", "us"), new Country("Uruguay", "uy"), new Country("Uzbekistan", "uz"), new Country("Venezuela", "ve"), new Country("Yemen", "ye"), new Country("Yemen (Sanaa)", "ys"), new Country("Yugoslavia", "yu"), new Country("Zimbabwe", "zw")};
//  //private final static String[] Commodities = {"barley", "corn", "cotton"};
//  private final static String[] Graphs = {"imports", "exports", "production", "domestic-consumption", "yield"};
//  private final static int startYear = 2000;
//  private final static int endYear = 2015;
//
//  public static void main(String[] args) throws IOException
//  {
//    Commodity[] foodList = instantiateFoodList();
//    Country[] countryList = instantiateCountryList();
//    PrintWriter writer = new PrintWriter("data\\sim\\WorldFoodProduction2.csv", "UTF-8");
//    writer.println("Commodity,Category,Country,Region,Market Year,Imports,Unit,Exports,Unit,Domestic Production,Unit,Domestic Consumption,Unit,Yield,Unit");
//
//    for (Commodity food : foodList)
//      for (Country region : objIntercept)
//      {
//        String[] strBuilder = new String[1 + (endYear - startYear)]; //Inclusive on year
//        String starter = food.name + ",," + region.name + ",,"; //Category and Region are parsed empty
//        for (int i = 0; i < 1 + (endYear - startYear); i++)
//          strBuilder[i] = starter + String.valueOf(startYear + i);
//        boolean[] dataFlag = new boolean[1 + (endYear - startYear)];
//        for (String value : Graphs)
//        {
//
//          Document document = Jsoup.connect("http://www.indexmundi.com/agriculture/?Country=" + region.id + "&commodity=" + food.id + "&graph=" + value).get();
//          Elements table = document.select("#gvData");
//          for (Element row : table.select("tr"))
//          {
//            Elements tds = row.select("td");
//            if (tds.size() > 2 && Integer.parseInt(tds.get(0).text()) >= startYear && Integer.parseInt(tds.get(0).text()) <= endYear)
//            {
//              //int tempVal = Integer.parseInt(tds.get(1).text());
//              //System.out.println("Check this:" + tds.get(1) +" "+ tds.get(2));
//              /*if (!tds.get(2).text().contains("(MT/HA)"))
//              {
//                String temp = tds.get(2).text().substring(1).replace("MT)", "").replace("HEAD)", "").replace(" ","").replace("MT CWE)","");
//                //System.out.println(temp + "," + temp.length());
//                //System.out.println(temp.replace("MT)", ""));
//                if(temp.length() != 0)
//                 tempVal *= Integer.parseInt(temp);
//              }*/
//              int index = Integer.parseInt(tds.get(0).text()) - startYear;
//              strBuilder[index] += "," + tds.get(1).text() + "," + tds.get(2).text();
//              dataFlag[index] = true;
//            }
//          }
//        }
//        for (int i = 0; i < 1 + (endYear - startYear); i++)
//          if(dataFlag[i])
//            writer.println(strBuilder[i]);
//        System.out.println(strBuilder[15]);
//      }
//    writer.flush();
//    writer.close();
//  }
//
//  private static class Country
//  {
//    String name = "";
//    String id = "";
//
//
//    Country(String name, String id)
//    {
//      this.name = name;
//      this.id = id;
//    }
//  }
//
//  public static Country[] instantiateCountryList()
//  {
//    return new Country[]{new Country("Afghanistan", "af"), new Country("Albania", "al"), new Country("Algeria", "dz"), new Country("American Samoa", "as"), new Country("Andorra", "ad"), new Country("Angola", "ao"), new Country("Anguilla", "ai"), new Country("Antarctica", "aq"), new Country("Antigua and Barbuda", "ag"), new Country("Argentina", "ar"), new Country("Armenia", "am"), new Country("Aruba", "aw"), new Country("Australia", "au"), new Country("Austria", "at"), new Country("Azerbaijan", "az"), new Country("Bahamas", "bs"), new Country("Bahrain", "bh"), new Country("Bangladesh", "bd"), new Country("Barbados", "bb"), new Country("Belarus", "by"), new Country("Belgium", "be"), new Country("Belize", "bz"), new Country("Benin", "bj"), new Country("Bermuda", "bm"), new Country("Bhutan", "bt"), new Country("Bolivia", "bo"), new Country("Bosnia and Herzegovina", "ba"), new Country("Botswana", "bw"), new Country("Bouvet Island", "bv"), new Country("Brazil", "br"), new Country("British Indian Ocean Territory", "io"), new Country("Brunei Darussalam", "bn"), new Country("Bulgaria", "bg"), new Country("Burkina Faso", "bf"), new Country("Burundi", "bi"), new Country("Cambodia", "kh"), new Country("Cameroon", "cm"), new Country("Canada", "ca"), new Country("Cape Verde", "cv"), new Country("Cayman Islands", "ky"), new Country("Central African Republic", "cf"), new Country("Chad", "td"), new Country("Chile", "cl"), new Country("China", "cn"), new Country("Christmas Island", "cx"), new Country("Cocos (Keeling) Islands", "cc"), new Country("Colombia", "co"), new Country("Comoros", "km"), new Country("Congo", "cg"), new Country("The Democratic Republic Of The Congo", "cd"), new Country("Cook Islands", "ck"), new Country("Costa Rica", "cr"), new Country("Côte D'ivoire", "ci"), new Country("Croatia", "hr"), new Country("Cuba", "cu"), new Country("Cyprus", "cy"), new Country("Czech Republic", "cz"), new Country("Denmark", "dk"), new Country("Djibouti", "dj"), new Country("Dominica", "dm"), new Country("Dominican Republic", "do"), new Country("Ecuador", "ec"), new Country("Egypt", "eg"), new Country("El Salvador", "sv"), new Country("Equatorial Guinea", "gq"), new Country("Eritrea", "er"), new Country("Estonia", "ee"), new Country("Ethiopia", "et"), new Country("Falkland Islands (Malvinas)", "fk"), new Country("Faroe Islands", "fo"), new Country("Fiji", "fj"), new Country("Finland", "fi"), new Country("France", "fr"), new Country("French Guiana", "gf"), new Country("French Polynesia", "pf"), new Country("French Southern Territories", "tf"), new Country("Gabon", "ga"), new Country("Gambia", "gm"), new Country("Georgia", "ge"), new Country("German Democratic Republic", "dd"), new Country("Germany", "de"), new Country("Ghana", "gh"), new Country("Gibraltar", "gi"), new Country("Greece", "gr"), new Country("Greenland", "gl"), new Country("Grenada", "gd"), new Country("Guadeloupe", "gp"), new Country("Guam", "gu"), new Country("Guatemala", "gt"), new Country("Guernsey", "gg"), new Country("Guinea", "gn"), new Country("Guinea-Bissau", "gw"), new Country("Guyana", "gy"), new Country("Haiti", "ht"), new Country("Heard Island and Mcdonald Islands", "hm"), new Country("Holy See (Vatican City State)", "va"), new Country("Honduras", "hn"), new Country("Hong Kong", "hk"), new Country("Hungary", "hu"), new Country("Iceland", "is"), new Country("India", "in"), new Country("Indonesia", "id"), new Country("Islamic Republic Of Iran", "ir"), new Country("Iraq", "iq"), new Country("Ireland", "ie"), new Country("Isle Of Man", "im"), new Country("Israel", "il"), new Country("Italy", "it"), new Country("Jamaica", "jm"), new Country("Japan", "jp"), new Country("Jersey", "je"), new Country("Jordan", "jo"), new Country("Kazakhstan", "kz"), new Country("Kenya", "ke"), new Country("Kiribati", "ki"), new Country("Democratic People's Republic Of Korea", "kp"), new Country("Republic Of Korea", "kr"), new Country("Kuwait", "kw"), new Country("Kyrgyzstan", "kg"), new Country("Lao People's Democratic Republic", "la"), new Country("Latvia", "lv"), new Country("Lebanon", "lb"), new Country("Lesotho", "ls"), new Country("Liberia", "lr"), new Country("Libya", "ly"), new Country("Liechtenstein", "li"), new Country("Lithuania", "lt"), new Country("Luxembourg", "lu"), new Country("Macao", "mo"), new Country("The Former Yugoslav Republic Of Macedonia", "mk"), new Country("Madagascar", "mg"), new Country("Malawi", "mw"), new Country("Malaysia", "my"), new Country("Maldives", "mv"), new Country("Mali", "ml"), new Country("Malta", "mt"), new Country("Marshall Islands", "mh"), new Country("Martinique", "mq"), new Country("Mauritania", "mr"), new Country("Mauritius", "mu"), new Country("Mayotte", "yt"), new Country("Mexico", "mx"), new Country("Federated States Of Micronesia", "fm"), new Country("Republic Of Moldova", "md"), new Country("Monaco", "mc"), new Country("Mongolia", "mn"), new Country("Montenegro", "me"), new Country("Montserrat", "ms"), new Country("Morocco", "ma"), new Country("Mozambique", "mz"), new Country("Myanmar", "mm"), new Country("Namibia", "na"), new Country("Nauru", "nr"), new Country("Nepal", "np"), new Country("Netherlands", "nl"), new Country("Netherlands Antilles", "an"), new Country("New Caledonia", "nc"), new Country("New Zealand", "nz"), new Country("Nicaragua", "ni"), new Country("Niger", "ne"), new Country("Nigeria", "ng"), new Country("Niue", "nu"), new Country("Norfolk Island", "nf"), new Country("Northern Mariana Islands", "mp"), new Country("Norway", "no"), new Country("Oman", "om"), new Country("Pakistan", "pk"), new Country("Palau", "pw"), new Country("Panama", "pa"), new Country("Papua New Guinea", "pg"), new Country("Paraguay", "py"), new Country("Peru", "pe"), new Country("Philippines", "ph"), new Country("Pitcairn", "pn"), new Country("Poland", "pl"), new Country("Portugal", "pt"), new Country("Puerto Rico", "pr"), new Country("Qatar", "qa"), new Country("Réunion", "re"), new Country("Romania", "ro"), new Country("Russian Federation", "ru"), new Country("Rwanda", "rw"), new Country("Saint Helena", "sh"), new Country("Saint Kitts and Nevis", "kn"), new Country("Saint Lucia", "lc"), new Country("Saint Pierre and Miquelon", "pm"), new Country("Saint Vincent and the Grenadines", "vc"), new Country("Samoa", "ws"), new Country("San Marino", "sm"), new Country("Sao Tome and Principe", "st"), new Country("Saudi Arabia", "sa"), new Country("Senegal", "sn"), new Country("Serbia", "rs"), new Country("Serbia and Montenegro", "cs"), new Country("Seychelles", "sc"), new Country("Sierra Leone", "sl"), new Country("Singapore", "sg"), new Country("Slovakia", "sk"), new Country("Slovenia", "si"), new Country("Solomon Islands", "sb"), new Country("Somalia", "so"), new Country("South Africa", "za"), new Country("South Georgia and the South Sandwich Islands", "gs"), new Country("Spain", "es"), new Country("Sri Lanka", "lk"), new Country("Sudan", "sd"), new Country("Suriname", "sr"), new Country("Svalbard and Jan Mayen", "sj"), new Country("Swaziland", "sz"), new Country("Sweden", "se"), new Country("Switzerland", "ch"), new Country("Syrian Arab Republic", "sy"), new Country("Province Of China Taiwan", "tw"), new Country("Tajikistan", "tj"), new Country("United Republic Of Tanzania", "tz"), new Country("Thailand", "th"), new Country("Timor-Leste", "tl"), new Country("Togo", "tg"), new Country("Tokelau", "tk"), new Country("Tonga", "to"), new Country("Trinidad and Tobago", "tt"), new Country("Tunisia", "tn"), new Country("Turkey", "tr"), new Country("Turkmenistan", "tm"), new Country("Turks and Caicos Islands", "tc"), new Country("Tuvalu", "tv"), new Country("U.S.S.R.", "su"), new Country("Uganda", "ug"), new Country("Ukraine", "ua"), new Country("United Arab Emirates", "ae"), new Country("United Kingdom", "gb"), new Country("United States", "us"), new Country("Uruguay", "uy"), new Country("Uzbekistan", "uz"), new Country("Vanuatu", "vu"), new Country("Venezuela", "ve"), new Country("Viet Nam", "vn"), new Country("Virgin Islands British", "vg"), new Country("Virgin Islands U.S.", "vi"), new Country("Wake Island", "wk"), new Country("Wallis and Futuna", "wf"), new Country("Western Sahara", "eh"), new Country("Yemen", "ye"), new Country("Yugoslavia", "yu"), new Country("Zambia", "zm"), new Country("Zimbabwe", "zw"), new Country("EU-27,", "eu")};
//
//  }
//
//  private static class Commodity
//  {
//    final String name;
//    final String id;
//
//
//    Commodity(String name, String id)
//    {
//      this.name = name;
//      this.id = id;
//    }
//  }
//
//  public static Commodity[] instantiateFoodList()
//  {
//    Commodity[] finisher = new Commodity[]{//new Commodity("Almonds", "almonds"), new Commodity("Cattle", "cattle"), new Commodity("Swine", "swine"), new Commodity("Barley", "barley"), new Commodity("Beef and Veal Meat", "beef-and-veal-meat"), new Commodity("Broiler Meat (Poultry)", "broiler-meat"), new Commodity("Centrifugal Sugar", "centrifugal-sugar"), new Commodity("Coconut Oil", "coconut-oil"), new Commodity("Copra Meal", "copra-meal"), new Commodity("Copra Oilseed", "copra-oilseed"), new Commodity("Corn", "corn"), new Commodity("Cottonseed Meal", "cottonseed-meal"), new Commodity("Cottonseed Oil", "cottonseed-oil"), new Commodity("Cottonseed Oilseed", "cottonseed-oilseed"), new Commodity("Butter", "butter"), new Commodity("Cheese", "cheese"), new Commodity("Dry Whole Milk Powder", "powdered-whole-milk"), new Commodity("Fluid Milk", "milk"),
//       new Commodity("Nonfat Dry Milk", "nonfat-dry-milk")//,
//            //new Commodity("Filberts", "filberts"), new Commodity("Fish Meal", "fish-meal"), new Commodity("Fresh Apples", "apples"), new Commodity("Fresh Cherries", "cherries"), new Commodity("Fresh Grapefruit", "grapefruit"), new Commodity("Fresh Lemons", "lemons"), new Commodity("Fresh Oranges", "oranges"), new Commodity("Fresh Peaches & Nectarines", "peaches-and-nectarines"), new Commodity("Fresh Pears", "pears"), new Commodity("Fresh Table Grapes", "grapes"), new Commodity("Fresh Tangerines", "tangerines"), new Commodity("Green Coffee", "green-coffee"), new Commodity("Milled Rice", "milled-rice"), new Commodity("Millet", "millet"), new Commodity("Mixed Grain", "mixed-grain"), new Commodity("Oats", "oats"), new Commodity("Olive Oil", "olive-oil"), new Commodity("Orange Juice", "orange-juice"), new Commodity("Palm Kernel Meal", "palm-kernel-meal"), new Commodity("Palm Kernel Oil", "palm-kernel-oil"), new Commodity("Palm Kernel Oilseed", "palm-kernel-oilseed"), new Commodity("Palm Oil", "palm-oil"), new Commodity("Peanut Meal", "peanut-meal"), new Commodity("Peanut Oil", "peanut-oil"), new Commodity("Peanut Oilseed", "peanut-oilseed"), new Commodity("Pistachios", "pistachios"), new Commodity("Raisins", "raisins"), new Commodity("Rapeseed Meal", "rapeseed-meal"), new Commodity("Rapeseed Oil", "rapeseed-oil"), new Commodity("Rapeseed Oilseed", "rapeseed-oilseed"), new Commodity("Rye", "rye"), new Commodity("Sorghum", "sorghum"), new Commodity("Soybean (Local) Meal", "local-soybean-meal"), new Commodity("Soybean (Local) Oil", "local-soybean-oil"), new Commodity("Soybean Meal", "soybean-meal"), new Commodity("Soybean Oil", "soybean-oil"), new Commodity("Soybean Oilseed", "soybean-oilseed"), new Commodity("Soybean Oilseed (Local)", "local-soybean-oilseed"), new Commodity("Sunflowerseed Meal", "sunflowerseed-meal"), new Commodity("Sunflowerseed Oil", "sunflowerseed-oil"), new Commodity("Sunflowerseed Oilseed", "sunflowerseed-oilseed"), new Commodity("Swine Meat", "swine-meat"), new Commodity("Turkey Meat (Poultry)", "turkey-meat"), new Commodity("Walnuts", "walnuts"), new Commodity("Wheat", "wheat")
//     };
//
//    return finisher;
//  }
}
