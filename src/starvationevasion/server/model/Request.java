package starvationevasion.server.model;


public class Request
{
  private String data;
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
    this.time = Double.parseDouble(data[0]);
    this.destination = Endpoint.valueOf(data[1].toUpperCase());
    data[2] = data[2].replace(data[0] + " ", "");
    data[2] = data[2].replace(data[1] + " ", "");

    this.data = data[2];

  }

  public String chomp()
  {
    String[] arr = getData().split("\\s+");
    setData(getData().replace(arr[0] + " ", ""));
    return arr[0];
  }

  public void setData (String data)
  {
    this.data = data;
  }

  public double getTime ()
  {
    return time;
  }

  public String getData ()
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
