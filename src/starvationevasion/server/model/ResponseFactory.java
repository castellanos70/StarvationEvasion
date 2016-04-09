package starvationevasion.server.model;


public class ResponseFactory
{
  protected double time;
  private static ResponseFactory ourInstance = new ResponseFactory();

  public static ResponseFactory init (double uptime)
  {
    ourInstance.time = uptime;
    return ourInstance;
  }

  private ResponseFactory ()
  {
  }


}
