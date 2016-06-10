package starvationevasion.server.model;


import starvationevasion.common.EnumRegion;
import starvationevasion.common.card.AbstractPolicy;

/**
 * Class that creates requests.
 *
 * Keeps the code a little cleaner. Makes the Request creation centralized to prevent bugs
 * especially since we have many different ways of serializing objects in this codebase.
 */
public class RequestFactory
{

  /**
   * Build Request without a payload
   *
   * @param time current client time
   * @param endpoint where to send the request. Lookup at Restful API's and endpoints
   *
   * @return Request
   */
  public Request build (double time, Endpoint endpoint)
  {
    return new Request(time, endpoint);
  }

  /**
   * Build Request without a payload
   *
   * @param time current client time
   * @param endpoint where to send the request. Lookup at Restful API's and endpoints
   * @param data Sendable object to send with request
   *
   * @return Request
   */
  public Request build (double time, Sendable data, Endpoint endpoint)
  {
    Request request = new RequestFactory().build(time, endpoint);

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

  /**
   * Build Request without a payload
   *
   * @param time current client time
   * @param endpoint where to send the request. Lookup at Restful API's and endpoints
   * @param data Sendable object to send with request
   * @param message text to be sent with request
   *
   * @return Request
   */
  public Request build (double time, Sendable data, String message, Endpoint endpoint)
  {
    Request request = new RequestFactory().build(time, data, endpoint);
    request.getPayload().putMessage(message);
    return request;
  }



  /**
   * Create a chat Request
   *
   * @param time current client time
   * @param destination of where to send the chat
   * @param text to be sent with chat
   * @param card card to be sent with chat
   * @param <T> Type of destination (String, EnumRegion)
   *
   * @return chat request
   */
  public <T> Request chat(double time, T destination, String text, AbstractPolicy card)
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

    data.put("text", text);

    return new RequestFactory().build(time, data, Endpoint.CHAT);

  }

  /**
   * Create a Login Request
   *
   * @param time current client time
   * @param username username of the user
   * @param password string password
   * @param region region of user. Can be null
   *
   * @return Request built with given information
   */
  public Request login (double time, String username, String password, EnumRegion region)
  {
    Payload data = new Payload();
    data.put("username", username);
    data.put("password", password);

    if (region != null)
    {
      data.put("region", region);
    }

    return new RequestFactory().build(time, data,Endpoint.LOGIN);
  }
}
