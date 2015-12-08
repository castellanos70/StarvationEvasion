package starvationevasion.vis.controller;

import starvationevasion.common.EnumRegion;
import starvationevasion.common.MapPoint;
import starvationevasion.io.XMLparsers.GeographyXMLparser;
import starvationevasion.sim.GeographicArea;

import java.util.*;


/**
 * Created by Tess Daughton on 11/15/15.
 * This class will be called by Visualizer in order to parse the simulation data that the
 * Visualizer is passed from the client
 * Displays the selected country
 */
public class SimParser {
  private HashMap<GeographicArea, EnumRegion> regions = new HashMap<>();
  private Collection<GeographicArea> modelGeography;

  /**
   * Creates a new parser for the simulations XML file to get the country and region data's lat longs
   */
  public SimParser() {
    modelGeography = new GeographyXMLparser().getGeography();
    for (GeographicArea g : modelGeography) {
      switch (g.getName()) {

        //region: ARCTIC_AMERICA
        case "US-Alaska":
          regions.put(g, EnumRegion.ARCTIC_AMERICA);
          break;
        case "Canada":
          regions.put(g, EnumRegion.ARCTIC_AMERICA);
          break;
        case "Denmark":
          regions.put(g, EnumRegion.ARCTIC_AMERICA);
          break;
        //endregion

        //region: CENTRAL_ASIA
        case "Kazakhstan":
          regions.put(g, EnumRegion.CENTRAL_ASIA);
          break;
        case "Kyrgyzstan":
          regions.put(g, EnumRegion.CENTRAL_ASIA);
          break;
        //endregion

        //region: EAST_ASIA
        case "China":
          regions.put(g, EnumRegion.EAST_ASIA);
          break;
        case "Japan":
          regions.put(g, EnumRegion.EAST_ASIA);
          break;
        case "Mongolia":
          regions.put(g, EnumRegion.EAST_ASIA);
          break;
        case "Democratic People's Republic of Korea":
          regions.put(g, EnumRegion.EAST_ASIA);
          break;
        case "Republic of Korea":
          regions.put(g, EnumRegion.EAST_ASIA);
          break;
        case "Tajikistan":
          regions.put(g, EnumRegion.EAST_ASIA);
          break;
        case "Turkmenistan":
          regions.put(g, EnumRegion.EAST_ASIA);
          break;
        case "Uzbekistan":
          regions.put(g, EnumRegion.EAST_ASIA);
          break;
        //endregion

        //region; EUROPE
        case "Albania":
          regions.put(g, EnumRegion.EUROPE);
          break;
        case "Andorra":
          regions.put(g, EnumRegion.EUROPE);
          break;
        case "Austria":
          regions.put(g, EnumRegion.EUROPE);
          break;
        case "Belarus":
          regions.put(g, EnumRegion.EUROPE);
          break;
        case "Belgium":
          regions.put(g, EnumRegion.EUROPE);
          break;
        case "Bosnia and Herzegovina":
          regions.put(g, EnumRegion.EUROPE);
          break;
        case "Bulgaria":
          regions.put(g, EnumRegion.EUROPE);
          break;
        case "Croatia":
          regions.put(g, EnumRegion.EUROPE);
          break;
        case "Czech Republic":
          regions.put(g, EnumRegion.EUROPE);
          break;
        case "Estonia":
          regions.put(g, EnumRegion.EUROPE);
          break;
        case "Finland":
          regions.put(g, EnumRegion.EUROPE);
          break;
        case "France":
          regions.put(g, EnumRegion.EUROPE);
          break;
        case "Germany":
          regions.put(g, EnumRegion.EUROPE);
          break;
        case "Greece":
          regions.put(g, EnumRegion.EUROPE);
          break;
        case "Hungary":
          regions.put(g, EnumRegion.EUROPE);
          break;
        case "Iceland":
          regions.put(g, EnumRegion.EUROPE);
          break;
        case "Ireland":
          regions.put(g, EnumRegion.EUROPE);
          break;
        case "Italy":
          regions.put(g, EnumRegion.EUROPE);
          break;
        case "Latvia":
          regions.put(g, EnumRegion.EUROPE);
          break;
        case "Liechtenstein":
          regions.put(g, EnumRegion.EUROPE);
          break;
        case "Lithuania":
          regions.put(g, EnumRegion.EUROPE);
          break;
        case "Luxembourg":
          regions.put(g, EnumRegion.EUROPE);
          break;
        case "Republic of Macedonia":
          regions.put(g, EnumRegion.EUROPE);
          break;
        case "Malta":
          regions.put(g, EnumRegion.EUROPE);
          break;
        case "Republic of Moldova":
          regions.put(g, EnumRegion.EUROPE);
          break;
        case "Monaco":
          regions.put(g, EnumRegion.EUROPE);
          break;
        case "Montenegro":
          regions.put(g, EnumRegion.EUROPE);
          break;
        case "Netherlands":
          regions.put(g, EnumRegion.EUROPE);
          break;
        case "Norway":
          regions.put(g, EnumRegion.EUROPE);
          break;
        case "Poland":
          regions.put(g, EnumRegion.EUROPE);
          break;
        case "Portugal":
          regions.put(g, EnumRegion.EUROPE);
          break;
        case "Romania":
          regions.put(g, EnumRegion.EUROPE);
          break;
        case "San Marino":
          regions.put(g, EnumRegion.EUROPE);
          break;
        case "Serbia":
          regions.put(g, EnumRegion.EUROPE);
          break;
        case "Slovakia":
          regions.put(g, EnumRegion.EUROPE);
          break;
        case "Slovenia":
          regions.put(g, EnumRegion.EUROPE);
          break;
        case "Spain":
          regions.put(g, EnumRegion.EUROPE);
          break;
        case "Sweden":
          regions.put(g, EnumRegion.EUROPE);
          break;
        case "Switzerland":
          regions.put(g, EnumRegion.EUROPE);
          break;
        case "Ukraine":
          regions.put(g, EnumRegion.EUROPE);
          break;
        case "United Kingdom of Great Britain and Northern Ireland":
          regions.put(g, EnumRegion.EUROPE);
          break;
        //endregion

        //region: MIDDLE_AMERICA
        case "Bahamas":
          regions.put(g, EnumRegion.MIDDLE_AMERICA);
          break;
        case "Belize":
          regions.put(g, EnumRegion.MIDDLE_AMERICA);
          break;
        case "Costa Rica":
          regions.put(g, EnumRegion.MIDDLE_AMERICA);
          break;
        case "Cuba":
          regions.put(g, EnumRegion.MIDDLE_AMERICA);
          break;
        case "Dominica":
          regions.put(g, EnumRegion.MIDDLE_AMERICA);
          break;
        case "Dominican Republic":
          regions.put(g, EnumRegion.MIDDLE_AMERICA);
          break;
        case "El Salvador":
          regions.put(g, EnumRegion.MIDDLE_AMERICA);
          break;
        case "Grenada":
          regions.put(g, EnumRegion.MIDDLE_AMERICA);
          break;
        case "Guatemala":
          regions.put(g, EnumRegion.MIDDLE_AMERICA);
          break;
        case "Haiti":
          regions.put(g, EnumRegion.MIDDLE_AMERICA);
          break;
        case "Honduras":
          regions.put(g, EnumRegion.MIDDLE_AMERICA);
          break;
        case "Jamaica":
          regions.put(g, EnumRegion.MIDDLE_AMERICA);
          break;
        case "Mexico":
          regions.put(g, EnumRegion.MIDDLE_AMERICA);
          break;
        case "Nicaragua":
          regions.put(g, EnumRegion.MIDDLE_AMERICA);
          break;
        case "Palau":
          regions.put(g, EnumRegion.MIDDLE_AMERICA);
          break;
        case "Panama":
          regions.put(g, EnumRegion.MIDDLE_AMERICA);
          break;
        case "Saint Lucia":
          regions.put(g, EnumRegion.MIDDLE_AMERICA);
          break;
        case "Saint Kitts and Nevis":
          regions.put(g, EnumRegion.MIDDLE_AMERICA);
          break;
        case "Saint Vincent and the Grenadines":
          regions.put(g, EnumRegion.MIDDLE_AMERICA);
          break;
        case "Trinidad and Tobago":
          regions.put(g, EnumRegion.MIDDLE_AMERICA);
          break;
        //endregion

        //region: MIDDLE EAST
        case "Algeria":
          regions.put(g, EnumRegion.MIDDLE_EAST);
          break;
        case "Bahrain":
          regions.put(g, EnumRegion.MIDDLE_EAST);
          break;
        case "Cyprus":
          regions.put(g, EnumRegion.MIDDLE_EAST);
          break;
        case "Egypt":
          regions.put(g, EnumRegion.MIDDLE_EAST);
          break;
        case "Iran":
          regions.put(g, EnumRegion.MIDDLE_EAST);
          break;
        case "Iraq":
          regions.put(g, EnumRegion.MIDDLE_EAST);
          break;
        case "Israel":
          regions.put(g, EnumRegion.MIDDLE_EAST);
          break;
        case "Jordan":
          regions.put(g, EnumRegion.MIDDLE_EAST);
          break;
        case "Kuwait":
          regions.put(g, EnumRegion.MIDDLE_EAST);
          break;
        case "Lebanon":
          regions.put(g, EnumRegion.MIDDLE_EAST);
          break;
        case "Libya":
          regions.put(g, EnumRegion.MIDDLE_EAST);
          break;
        case "Morocco":
          regions.put(g, EnumRegion.MIDDLE_EAST);
          break;
        case "Oman":
          regions.put(g, EnumRegion.MIDDLE_EAST);
          break;
        case "Qatar":
          regions.put(g, EnumRegion.MIDDLE_EAST);
          break;
        case "Saudi Arabia":
          regions.put(g, EnumRegion.MIDDLE_EAST);
          break;
        case "Sudan":
          regions.put(g, EnumRegion.MIDDLE_EAST);
          break;
        case "Syrian Arab Republic":
          regions.put(g, EnumRegion.MIDDLE_EAST);
          break;
        case "Tunisia":
          regions.put(g, EnumRegion.MIDDLE_EAST);
          break;
        case "Turkey":
          regions.put(g, EnumRegion.MIDDLE_EAST);
          break;
        case "United Arab Emirates":
          regions.put(g, EnumRegion.MIDDLE_EAST);
          break;
        case "Yemen":
          regions.put(g, EnumRegion.MIDDLE_EAST);
          break;
        //endregion

        //region: OCEANIA
        case "US-Hawaii":
          regions.put(g, EnumRegion.OCEANIA);
          break;
        case "Australia":
          regions.put(g, EnumRegion.OCEANIA);
          break;
        case "Fiji":
          regions.put(g, EnumRegion.OCEANIA);
          break;
        case "Kiribati":
          regions.put(g, EnumRegion.OCEANIA);
          break;
        case "Marshall Islands":
          regions.put(g, EnumRegion.OCEANIA);
          break;
        case "Micronesia":
          regions.put(g, EnumRegion.OCEANIA);
          break;
        case "Nauru":
          regions.put(g, EnumRegion.OCEANIA);
          break;
        case "New Zealand":
          regions.put(g, EnumRegion.OCEANIA);
          break;
        case "Papua New Guinea":
          regions.put(g, EnumRegion.OCEANIA);
          break;
        case "Samoa":
          regions.put(g, EnumRegion.OCEANIA);
          break;
        case "Solomon Islands":
          regions.put(g, EnumRegion.OCEANIA);
          break;
        case "Tonga":
          regions.put(g, EnumRegion.OCEANIA);
          break;
        case "Tuvalu":
          regions.put(g, EnumRegion.OCEANIA);
          break;
        case "Vanuatu":
          regions.put(g, EnumRegion.OCEANIA);
          break;
        //endregion

        //region: RUSSIA
        case "Armenia":
          regions.put(g, EnumRegion.RUSSIA);
          break;
        case "Azerbaijan":
          regions.put(g, EnumRegion.RUSSIA);
          break;
        case "Georgia":
          regions.put(g, EnumRegion.RUSSIA);
          break;
        case "Russian Federation":
          regions.put(g, EnumRegion.RUSSIA);
          break;
        //endregion

        //region: SUB_SAHARA
        case "Angola":
          regions.put(g, EnumRegion.SUB_SAHARAN);
          break;
        case "Burkina Faso":
          regions.put(g, EnumRegion.SUB_SAHARAN);
          break;
        case "Burundi":
          regions.put(g, EnumRegion.SUB_SAHARAN);
          break;
        case "Benin":
          regions.put(g, EnumRegion.SUB_SAHARAN);
          break;
        case "Botswana":
          regions.put(g, EnumRegion.SUB_SAHARAN);
          break;
        case "Cabo Verde":
          regions.put(g, EnumRegion.SUB_SAHARAN);
          break;
        case "Cameroon":
          regions.put(g, EnumRegion.SUB_SAHARAN);
          break;
        case "Central African Republic":
          regions.put(g, EnumRegion.SUB_SAHARAN);
          break;
        case "Chad":
          regions.put(g, EnumRegion.SUB_SAHARAN);
          break;
        case "Comoros":
          regions.put(g, EnumRegion.SUB_SAHARAN);
          break;
        case "Congo":
          regions.put(g, EnumRegion.SUB_SAHARAN);
          break;
        case "Democratic Republic of the Congo":
          regions.put(g, EnumRegion.SUB_SAHARAN);
          break;
        case "Djibouti":
          regions.put(g, EnumRegion.SUB_SAHARAN);
          break;
        case "Equatorial Guinea":
          regions.put(g, EnumRegion.SUB_SAHARAN);
          break;
        case "Eritrea":
          regions.put(g, EnumRegion.SUB_SAHARAN);
          break;
        case "Ethiopia":
          regions.put(g, EnumRegion.SUB_SAHARAN);
          break;
        case "Gabon":
          regions.put(g, EnumRegion.SUB_SAHARAN);
          break;
        case "Gambia":
          regions.put(g, EnumRegion.SUB_SAHARAN);
          break;
        case "Ghana":
          regions.put(g, EnumRegion.SUB_SAHARAN);
          break;
        case "Guinea":
          regions.put(g, EnumRegion.SUB_SAHARAN);
          break;
        case "Guinea-Bissau":
          regions.put(g, EnumRegion.SUB_SAHARAN);
          break;
        case "Cote d'Ivoire":
          regions.put(g, EnumRegion.SUB_SAHARAN);
          break;
        case "Kenya":
          regions.put(g, EnumRegion.SUB_SAHARAN);
          break;
        case "Lesotho":
          regions.put(g, EnumRegion.SUB_SAHARAN);
          break;
        case "Liberia":
          regions.put(g, EnumRegion.SUB_SAHARAN);
          break;
        case "Madagascar":
          regions.put(g, EnumRegion.SUB_SAHARAN);
          break;
        case "Malawi":
          regions.put(g, EnumRegion.SUB_SAHARAN);
          break;
        case "Mali":
          regions.put(g, EnumRegion.SUB_SAHARAN);
          break;
        case "Mauritania":
          regions.put(g, EnumRegion.SUB_SAHARAN);
          break;
        case "Mauritius":
          regions.put(g, EnumRegion.SUB_SAHARAN);
          break;
        case "Mozambique":
          regions.put(g, EnumRegion.SUB_SAHARAN);
          break;
        case "Namibia":
          regions.put(g, EnumRegion.SUB_SAHARAN);
          break;
        case "Niger":
          regions.put(g, EnumRegion.SUB_SAHARAN);
          break;
        case "Nigeria":
          regions.put(g, EnumRegion.SUB_SAHARAN);
          break;
        case "Rwanda":
          regions.put(g, EnumRegion.SUB_SAHARAN);
          break;
        case "Sao Tome and Principe":
          regions.put(g, EnumRegion.SUB_SAHARAN);
          break;
        case "Senegal":
          regions.put(g, EnumRegion.SUB_SAHARAN);
          break;
        case "Seychelles":
          regions.put(g, EnumRegion.SUB_SAHARAN);
          break;
        case "Sierra Leone":
          regions.put(g, EnumRegion.SUB_SAHARAN);
          break;
        case "Somalia":
          regions.put(g, EnumRegion.SUB_SAHARAN);
          break;
        case "South Africa":
          regions.put(g, EnumRegion.SUB_SAHARAN);
          break;
        case "South Sudan":
          regions.put(g, EnumRegion.SUB_SAHARAN);
          break;
        case "Swaziland":
          regions.put(g, EnumRegion.SUB_SAHARAN);
          break;
        case "Togo":
          regions.put(g, EnumRegion.SUB_SAHARAN);
          break;
        case "Uganda":
          regions.put(g, EnumRegion.SUB_SAHARAN);
          break;
        case "United Republic of Tanzania":
          regions.put(g, EnumRegion.SUB_SAHARAN);
          break;
        case "Zambia":
          regions.put(g, EnumRegion.SUB_SAHARAN);
          break;
        case "Zimbabwe":
          regions.put(g, EnumRegion.SUB_SAHARAN);
          break;
        //endregion

        //region: SOUTH AMERICA
        case "Antigua and Barbuda":
          regions.put(g, EnumRegion.SOUTH_AMERICA);
          break;
        case "Argentina":
          regions.put(g, EnumRegion.SOUTH_AMERICA);
          break;
        case "Chile":
          regions.put(g, EnumRegion.SOUTH_AMERICA);
          break;
        case "Brazil":
          regions.put(g, EnumRegion.SOUTH_AMERICA);
          break;
        case "Barbados":
          regions.put(g, EnumRegion.SOUTH_AMERICA);
          break;
        case "Bolivia":
          regions.put(g, EnumRegion.SOUTH_AMERICA);
          break;
        case "Colombia":
          regions.put(g, EnumRegion.SOUTH_AMERICA);
          break;
        case "Ecuador":
          regions.put(g, EnumRegion.SOUTH_AMERICA);
          break;
        case "Guyana":
          regions.put(g, EnumRegion.SOUTH_AMERICA);
          break;
        case "Paraguay":
          regions.put(g, EnumRegion.SOUTH_AMERICA);
          break;
        case "Peru":
          regions.put(g, EnumRegion.SOUTH_AMERICA);
          break;
        case "Suriname":
          regions.put(g, EnumRegion.SOUTH_AMERICA);
          break;
        case "Uruguay":
          regions.put(g, EnumRegion.SOUTH_AMERICA);
          break;
        case "Venezuela":
          regions.put(g, EnumRegion.SOUTH_AMERICA);
          break;


        //endregion

        //region: SOUTH_ASIA
        case "Afghanistan":
          regions.put(g, EnumRegion.SOUTH_ASIA);
          break;
        case "Bangladesh":
          regions.put(g, EnumRegion.SOUTH_ASIA);
          break;
        case "India":
          regions.put(g, EnumRegion.SOUTH_ASIA);
          break;
        case "Pakistan":
          regions.put(g, EnumRegion.SOUTH_ASIA);
          break;
        case "Bhutan":
          regions.put(g, EnumRegion.SOUTH_ASIA);
          break;
        case "Maldives":
          regions.put(g, EnumRegion.SOUTH_ASIA);
          break;
        case "Nepal":
          regions.put(g, EnumRegion.SOUTH_ASIA);
          break;
        case "Sri Lanka":
          regions.put(g, EnumRegion.SOUTH_ASIA);
          break;
        //endregion

        //region: SOUTHEAST_ASIA
        case "Brunei Darussalam":
          regions.put(g, EnumRegion.SOUTHEAST_ASIA);
          break;
        case "Cambodia":
          regions.put(g, EnumRegion.SOUTHEAST_ASIA);
          break;
        case "Indonesia":
          regions.put(g, EnumRegion.SOUTHEAST_ASIA);
          break;
        case "Lao People's Democratic Republic":
          regions.put(g, EnumRegion.SOUTHEAST_ASIA);
          break;
        case "Malaysia":
          regions.put(g, EnumRegion.SOUTHEAST_ASIA);
          break;
        case "Myanmar":
          regions.put(g, EnumRegion.SOUTHEAST_ASIA);
          break;
        case "Philippines":
          regions.put(g, EnumRegion.SOUTHEAST_ASIA);
          break;
        case "Singapore":
          regions.put(g, EnumRegion.SOUTHEAST_ASIA);
          break;
        case "Thailand":
          regions.put(g, EnumRegion.SOUTHEAST_ASIA);
          break;
        case "Timor-Leste":
          regions.put(g, EnumRegion.SOUTHEAST_ASIA);
          break;
        case "Viet Nam":
          regions.put(g, EnumRegion.SOUTHEAST_ASIA);
          break;
        //endregion

        //region: USA
        //region: California
        case "US-California":
          regions.put(g, EnumRegion.CALIFORNIA);
          break;
        //endregion
        //region: HeartLand
        case "US-Iowa":
          regions.put(g, EnumRegion.HEARTLAND);
          break;
        case "US-Missouri":
          regions.put(g, EnumRegion.HEARTLAND);
          break;
        case "US-Illinois":
          regions.put(g, EnumRegion.HEARTLAND);
          break;
        case "US-Indiana":
          regions.put(g, EnumRegion.HEARTLAND);
          break;
        //endregion
        //region: NORTHERN PLAINS
        case "US-SouthDakota":
          regions.put(g, EnumRegion.NORTHERN_PLAINS);
          break;
        case "US-NorthDakota":
          regions.put(g, EnumRegion.NORTHERN_PLAINS);
          break;
        case "US-Nebraska":
          regions.put(g, EnumRegion.NORTHERN_PLAINS);
          break;
        case "US-Minnesota":
          regions.put(g, EnumRegion.NORTHERN_PLAINS);
          break;
        case "US-Kansas":
          regions.put(g, EnumRegion.NORTHERN_PLAINS);
          break;
        //endregion
        //region: SOUTHEAST
        case "US-Mississippi":
          regions.put(g, EnumRegion.SOUTHEAST);
          break;
        case "US-Florida":
          regions.put(g, EnumRegion.SOUTHEAST);
          break;
        case "US-Georgia":
          regions.put(g, EnumRegion.SOUTHEAST);
          break;
        case "US-Alabama":
          regions.put(g, EnumRegion.SOUTHEAST);
          break;
        case "US-Tennessee":
          regions.put(g, EnumRegion.SOUTHEAST);
          break;
        case "US-NorthCarolina":
          regions.put(g, EnumRegion.SOUTHEAST);
          break;
        case "US-SouthCarolina":
          regions.put(g, EnumRegion.SOUTHEAST);
          break;
        case "US-Kentucky":
          regions.put(g, EnumRegion.SOUTHEAST);
          break;
        case "US-WestVirginia":
          regions.put(g, EnumRegion.SOUTHEAST);
          break;
        case "US-Virginia":
          regions.put(g, EnumRegion.SOUTHEAST);
          break;
        //endregion
        //region: NORTHERN_CRESCENT
        case "US-Maine":
          regions.put(g, EnumRegion.NORTHERN_CRESCENT);
          break;
        case "US-Vermont":
          regions.put(g, EnumRegion.NORTHERN_CRESCENT);
          break;
        case "US-NewHampshire":
          regions.put(g, EnumRegion.NORTHERN_CRESCENT);
          break;
        case "US-Massachusetts":
          regions.put(g, EnumRegion.NORTHERN_CRESCENT);
          break;
        case "US-RhodeIsland":
          regions.put(g, EnumRegion.NORTHERN_CRESCENT);
          break;
        case "US-Connecticut":
          regions.put(g, EnumRegion.NORTHERN_CRESCENT);
          break;
        case "US-NewJersey":
          regions.put(g, EnumRegion.NORTHERN_CRESCENT);
          break;
        case "US-NewYork":
          regions.put(g, EnumRegion.NORTHERN_CRESCENT);
          break;
        case "US-Pennsylvania":
          regions.put(g, EnumRegion.NORTHERN_CRESCENT);
          break;
        case "US-Delaware":
          regions.put(g, EnumRegion.NORTHERN_CRESCENT);
          break;
        case "US-Maryland":
          regions.put(g, EnumRegion.NORTHERN_CRESCENT);
          break;
        case "US-Ohio":
          regions.put(g, EnumRegion.NORTHERN_CRESCENT);
          break;
        case "US-Wisconsin":
          regions.put(g, EnumRegion.NORTHERN_CRESCENT);
          break;
        case "US-Michigan":
          regions.put(g, EnumRegion.NORTHERN_CRESCENT);
          break;
        //endregion
        //region: SOUTHERN_PLAINS
        case "US-Texas":
          regions.put(g, EnumRegion.SOUTHERN_PLAINS);
          break;
        case "US-Louisiana":
          regions.put(g, EnumRegion.SOUTHERN_PLAINS);
          break;
        case "US-Oklahoma":
          regions.put(g, EnumRegion.SOUTHERN_PLAINS);
          break;
        case "US-Arkansas":
          regions.put(g, EnumRegion.SOUTHERN_PLAINS);
          break;
        //endregion
        //region: MOUNTAIN
        case "US-Washington":
          regions.put(g, EnumRegion.MOUNTAIN);
          break;
        case "US-Oregon":
          regions.put(g, EnumRegion.MOUNTAIN);
          break;
        case "US-Idaho":
          regions.put(g, EnumRegion.MOUNTAIN);
          break;
        case "US-Nevada":
          regions.put(g, EnumRegion.MOUNTAIN);
          break;
        case "US-Montana":
          regions.put(g, EnumRegion.MOUNTAIN);
          break;
        case "US-Wyoming":
          regions.put(g, EnumRegion.MOUNTAIN);
          break;
        case "US-Utah":
          regions.put(g, EnumRegion.MOUNTAIN);
          break;
        case "US-Colorado":
          regions.put(g, EnumRegion.MOUNTAIN);
          break;
        case "US-Arizona":
          regions.put(g, EnumRegion.MOUNTAIN);
          break;
        case "US-NewMexico":
          regions.put(g, EnumRegion.MOUNTAIN);
          break;
        //endregion
        //endregion
      }
    }
  }

  /**
   * Gets the region given a lat long
   *
   * @param center MapPoint containing the lat and long
   * @return returns a region if a country is found. Else returns a null
   */
  public EnumRegion getRegion(MapPoint center) {
    return getRegion(center.latitude, center.longitude);
  }


  /**
   * Gets the region given a lat and long
   *
   * @param lat latitude to query
   * @param lon longitude to query
   * @return returns a region if a country is found. Else returns a null
   */
  public EnumRegion getRegion(double lat, double lon) {
    for (Map.Entry<GeographicArea, EnumRegion> e : regions.entrySet()) {
      if (e.getKey().containsMapPoint(new MapPoint(lat, lon))) return e.getValue();
    }
    return null;
  }

  /**
   * Gets the country name given a lat and long
   *
   * @param lat latitude to query
   * @param lon longtitude to query
   * @return Returns the country's name if found, else returns 'no name on record'
   */
  public String parse(double lat, double lon) {
    if (modelGeography == null) modelGeography = new GeographyXMLparser().getGeography();
    MapPoint p = new MapPoint(lat, lon);

      /*parse the location data to find where the user clicked on the map*/
    // The GeographicArea is a closed polygon representing a part of a territory.  For example,
    // Catalina Island off of the coast of California would be one GeographicArea, while mainland
    // California is another region.  Both are aggregated into The Territory of California.  It is
    // a convenient coincidence that the polygon's name is the same as a Territory in most cases,
    // but I haven't verified that this is always true. -- Peter Blemel
    //
    for (GeographicArea a : regions.keySet()) {
      if (a.containsMapPoint(p)) {
//        System.out.println("clicked on " + a.getName());
        return a.getName();
      }
    }
    return "No Name on Record";
  }

}
