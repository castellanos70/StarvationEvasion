package starvationevasion.server.handlers;

/**
 * @author Javier Chavez (javierc@cs.unm.edu)
 */

import starvationevasion.common.WorldData;
import starvationevasion.server.Connector;
import starvationevasion.server.Server;
import starvationevasion.server.model.Endpoint;
import starvationevasion.server.model.Payload;
import starvationevasion.server.model.Request;
import starvationevasion.server.model.ResponseFactory;
import starvationevasion.server.model.Type;

import java.awt.geom.Area;
import java.util.ArrayList;

public class DataHandler extends AbstractHandler
{
  public DataHandler (Server server, Connector client)
  {
    super(server, client);
  }

  @Override
  protected boolean handleRequestImpl (Request request)
  {

    if (request.getDestination().equals(Endpoint.GAME_STATE))
    {

      getClient().send(new ResponseFactory().build(server.uptime(),
                                             server.getGameState(),
                                             Type.GAME_STATE));
      return true;
    }
    else if (request.getDestination().equals(Endpoint.SERVER_UPTIME))
    {
      getClient().send(new ResponseFactory().build(server.uptime(),
                                             new Payload(server.uptime()),
                                             Type.TIME));
      return true;
    }
    else if (request.getDestination().equals(Endpoint.AVAILABLE_REGIONS))
    {
      getClient().send(new ResponseFactory().build(server.uptime(),
                                             new Payload(server.getAvailableRegions()),
                                             Type.AVAILABLE_REGIONS));
      return true;
    }
    else if (request.getDestination().equals(Endpoint.WORLD_DATA))
    {
      Payload data = new Payload();


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
        data.putData(worldDataList);
      }

      if (polygons)
      {
        Area[] geographicAreaList = server.getSimulator().getRegionBoundaries();
        data.put("geographic-data", geographicAreaList);
      }

      getClient().send(new ResponseFactory().build(server.uptime(),
                                             data,
                                             Type.WORLD_DATA_LIST));

      return true;

    }

    return false;
  }
}
