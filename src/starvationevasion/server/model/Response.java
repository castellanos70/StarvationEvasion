package starvationevasion.server.model;

/**
 * @author Javier Chavez (javierc@cs.unm.edu)
 */


public class Response extends NetworkData
{
  private Request initiator;

  public <T extends Payload> Response (double time, T data, String msg, Type type)
  {
    super(time, data, msg, type);
  }

  public Response (double time, Sendable data, String msg, Type type)
  {
    super(time, data, msg, type);
  }

  public Response (double time, Payload data, String msg)
  {
    super(time, data, msg);
  }

  public <T extends Payload> Response (double time, T data, Type type)
  {
    super(time, data, type);
  }

  public Response (double time, Sendable data, Type type)
  {
    super(time, data, type);
  }



  public Request getInitiator ()
  {
    return initiator;
  }

  public Response setInitiator (Request request)
  {
    initiator = request;
    return this;
  }

}
