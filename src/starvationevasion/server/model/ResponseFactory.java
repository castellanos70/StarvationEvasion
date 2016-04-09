package starvationevasion.server.model;


public class ResponseFactory
{
  private static ResponseFactory ourInstance = new ResponseFactory();
  protected static Payload payload = new Payload();

  public static ResponseFactory init ()
  {
    return ourInstance;
  }

  private ResponseFactory ()
  {
  }

  public static Response build (double time, Sendable data, Type type)
  {

    if (data == null)
    {
      return new Response(time, new Payload(), type);
    }

    if (data instanceof Payload)
    {
      return new Response(time, (Payload) data, type);
    }
    else
    {
      payload.clear();
      payload.putData(data);
      return new Response(time, payload, type);
    }
  }

  public static Response build (double time, Sendable data, String message, Type type)
  {
    Response response = ResponseFactory.build(time, data, type);
    response.getPayload().putMessage(message);
    return response;
  }


}
