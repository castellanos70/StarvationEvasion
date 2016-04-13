package starvationevasion.server.model;

/**
 * @author Javier Chavez (javierc@cs.unm.edu)
 */

/**
 * Class that creates responses.
 *
 * Keeps the code a little cleaner. Makes the Response creation centralized to prevent bugs
 * especially since we have many different ways of serializing objects in this codebase.
 */
public class ResponseFactory
{

  /**
   * Returns a Response
   * @param time current server time
   * @param data data to be added
   * @param type type of response we are sending
   *
   * @return Response containing all the supplied data
   */
  public Response build (double time, Sendable data, Type type)
  {
    Payload payload = new Payload();
    // if the data is null create a new payload
    if (data == null)
    {
      return new Response(time, new Payload(), type);
    }
    // if it's already a payload just call constructor with data
    if (data instanceof Payload)
    {
      return new Response(time, (Payload) data, type);
    }
    else
    {
      // add the data
      payload.putData(data);
      return new Response(time, payload, type);
    }
  }

  /**
   * Returns a Response
   * @param time current server time
   * @param data data to be added
   * @param type type of response we are sending
   * @param message string to be added to payload "message"
   *
   * @return Response containing all the supplied data
   */
  public Response build (double time, Sendable data, Type type, String message)
  {
    Response response = new ResponseFactory().build(time, data, type);
    response.getPayload().putMessage(message);
    return response;
  }


}
