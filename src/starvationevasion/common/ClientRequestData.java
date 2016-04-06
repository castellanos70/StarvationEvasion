package starvationevasion.common;


public class ClientRequestData
{

  /**
   * If true, the client is requesting Latitude and Longitude polygons for each game region.
   */
  boolean requestRegionPolygons = false;


  /**
   * If true, the client is telling the server that it has finished the current turn.
   */
  boolean clientTurnDone = false;


  /**
   * Year from which the client is requesting WorldData objects. If worldDataFromYear == 0,
   * then the server will return a null ArrayList. Otherwise the server will return a sorted
   * ArrayList of WorldData objects for the requested years.
  */
  int worldDataFromYear = 0;

  /**
   * Year through which the client is requesting WorldData objects.
   * If worldDataFrom equals worldDataThroughYear then the server return an ArrayList of one
   * WorldData object for the given year.<br><br>
   *
   * If worldDataThroughYear==0 or worldDataThroughYear &gt;= currentYear,
   * the server will default worldDataThroughYear to one less than the current year.<br><br>
   *
   * Note: If clientTurnDone==true, then client is requesting that the server advance the
   * simulator one turn (Constant.YEARS_PER_TURN years) before returning a result. <br>
   * In that case, the client's currentYear at the time this request is sent will be
   * Constant.YEARS_PER_TURN years less than the server's year at the time the message is returned.
   */
  int worldDataThroughYear = 0;


  /**
   * leave == 0 to request no climateData.
   */
  int climateDataFromYear = 0 ;
  int climateDataThroughYear = 0;
}
