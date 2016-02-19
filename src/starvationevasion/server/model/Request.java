package starvationevasion.server.model;


public class Request implements Action
{
  private String[] data;
  private double time;

  public Request (String ...data) throws Exception
  {
    if (data.length < 2)
    {
      throw new Exception("Not enough data");
    }

    this.data = data;
    this.time = Double.parseDouble(data[0]);
  }

//  public Payload<?, ?> getPayload ()
//  {
//    return data;
//  }

  public double getTime ()
  {
    return time;
  }

//  public void setData (Payload<?, ?> data)
//  {
//    this.data = data;
//  }

  public void setTime (double time)
  {
    this.time = time;
  }

}
