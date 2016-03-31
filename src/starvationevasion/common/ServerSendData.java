package starvationevasion.common;


import java.util.ArrayList;

public class ServerSendData
{
  public ArrayList<WorldData> worldDataList = null;

  // TODO: change <Object> to whatever structure will hold climate data.
  public ArrayList<Object> climateDataList = null;

  /**
   * When non-null, this is an array of ArrayLists of GeographicAreas. Each ArrayList in the array
   * corresponds to the region with the matching ordinal of EnumRegion.
   */
  public ArrayList<GeographicArea>[] geographicAreaList = null;
}
