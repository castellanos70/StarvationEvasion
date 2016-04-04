package starvationevasion.server.handlers;

/**
 * @author Javier Chavez (javierc@cs.unm.edu)
 */

import starvationevasion.common.GeographicArea;
import starvationevasion.common.WorldData;
import starvationevasion.server.Server;
import starvationevasion.server.Worker;
import starvationevasion.server.model.*;

import java.util.ArrayList;

public class DataHandler extends AbstractHandler
{
  public DataHandler (Server server, Worker client)
  {
    super(server, client);
  }

  @Override
  protected boolean handleRequestImpl (Request request)
  {
    Payload data = new Payload();
    m_response = new Response(server.uptime(), "Data");

    if (request.getDestination().equals(Endpoint.GAME_STATE))
    {

      data.putData(server.getGameState());
      m_response.setType(Type.GAME);
      getClient().send(m_response);
      return true;
    }
    else if (request.getDestination().equals(Endpoint.SERVER_UPTIME))
    {
      data.put("time", server.uptime());
      m_response.setType(Type.GAME);
      getClient().send(m_response);
      return true;
    }
    else if (request.getDestination().equals(Endpoint.AVAILABLE_REGIONS))
    {
      data.putData(server.getAvailableRegions());
      m_response.setType(Type.GAME);
      getClient().send(m_response);
      return true;
    }
    else if (request.getDestination().equals(Endpoint.WORLD_DATA))
    {

      // todo: if true advance then send
      boolean isComplete = (boolean) request.getPayload().get("client-done");

      boolean polygons = (boolean) request.getPayload().get("region-polygons");
      // boolean climate = (boolean) request.getPayload().get("climate");
      int dataStart = (int) request.getPayload().get("data-start");
      int dataEnd = (int) request.getPayload().get("data-end");

      int climateStart = (int) request.getPayload().get("climate-start");
      int climateEnd = (int) request.getPayload().get("climate-end");

      data.put("climate-data", "not yet impl.");

      if (dataStart >= 0 && dataEnd >= dataStart)
      {
        ArrayList<WorldData> worldDataList = server.getSimulator().getWorldData(dataStart, dataEnd);
        data.put("world-data", worldDataList);
      }

      if (polygons)
      {
        ArrayList<GeographicArea>[] geographicAreaList = server.getSimulator().getRegionBoundaries();
        data.put("geographic-data", geographicAreaList);
      }
      m_response = new Response(data);


      m_response.setType(Type.WORLD_DATA);
      getClient().send(m_response);

      return true;

    }

    return false;
  }
}
