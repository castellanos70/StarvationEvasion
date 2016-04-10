package starvationevasion.server.model;


import starvationevasion.common.EnumRegion;
import starvationevasion.common.PolicyCard;

public class RequestFactory
{
  private static RequestFactory ourInstance = new RequestFactory();

  public static RequestFactory init ()
  {
    return ourInstance;
  }

  private RequestFactory ()
  {
  }

  public static Request build (double time, Sendable data, Endpoint endpoint)
  {
    Request request = new Request(time, endpoint);
    if (data instanceof Payload)
    {
      request.setData((Payload) data);
    }
    else
    {
      Payload payload = new Payload();
      request.setData(payload);
      payload.putData(data);
    }
    return request;
  }

  public static Request build (double time, Sendable data, String message, Endpoint endpoint)
  {
    Request request = RequestFactory.build(time, data, endpoint);
    request.getPayload().putMessage(message);
    return request;
  }

  public static Request build (double time, Endpoint endpoint)
  {
    return RequestFactory.build(time, new Payload(), endpoint);
  }

  public static <T> Request chat(double time, T destination, String msg, PolicyCard card)
  {
    Payload data = new Payload();

    if (destination instanceof String)
    {
      if (destination.equals("ALL"))
      {
        data.put("to-region", "ALL");
      }
      else
      {
        data.put("to-username", destination);
      }
    }
    else if (destination instanceof EnumRegion)
    {
      data.put("to-region", destination);
    }
    else
    {
      System.out.println("Not a valid destination");
      return null;
    }

    if (card == null)
    {
      data.put("card", "");
    }
    else
    {
      data.put("card", card);
    }

    data.put("text", msg);

    return RequestFactory.build(time, data, Endpoint.CHAT);

  }

  public static Request login (double time, String username, String password, EnumRegion region)
  {
    Payload data = new Payload();
    data.put("username", username);
    data.put("password", password);

    if (region != null)
    {
      data.put("region", region);
    }

    return RequestFactory.build(time, data,Endpoint.LOGIN);
  }
}
