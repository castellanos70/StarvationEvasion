package starvationevasion.server;

/**
 * @author Javier Chavez
 */


/**
 * Requests are sent from client to server
 */
public class Request extends TCP<Request>
{
  public Request(ActionType type, double time, String data)
  {
    super(type, time, data);
  }

  public Request (String data) throws Exception
  {
    super(data);
  }
}
