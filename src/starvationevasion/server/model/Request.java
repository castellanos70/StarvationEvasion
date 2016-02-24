package starvationevasion.server.model;


import java.util.Arrays;

public class Request
{
  private String[] data;
  private double time;
  private Endpoint destination;

  /**
   * Expecting at least 2 args.
   * @param data  data[0] shall be the time
   *              data[1] shall be the rest of the data
   * @throws Exception
   */
  public Request (String ...data) throws Exception
  {
    if (data.length < 2)
    {
      throw new Exception("Not enough data");
    }

    // get the time
    this.time = Double.parseDouble(data[0]);
    this.destination = Endpoint.valueOf(data[1].toUpperCase());
    this.data = new String[data.length];

    if (data.length >= 3)
    {
      System.arraycopy(data, 2, this.data, 0, data.length - 2);
    }

  }

  public double getTime ()
  {
    return time;
  }

  public String[] getData ()
  {
    return data;
  }

  public Endpoint getDestination ()
  {
    return destination;
  }

  public void setTime (double time)
  {
    this.time = time;
  }

}
