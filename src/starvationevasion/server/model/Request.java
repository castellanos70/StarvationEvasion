package starvationevasion.server.model;


import com.oracle.javafx.jmx.json.JSONDocument;
import com.oracle.javafx.jmx.json.impl.JSONStreamReaderImpl;

import java.io.Serializable;
import java.io.StringReader;

public class Request implements Serializable
{
  private Payload data = new Payload();
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
    System.out.println(data[2]);
    data[2] = data[2].replace(data[0] + " ", "");
    data[2] = data[2].replace(data[1], "");
    data[2] = data[2].trim();

    if (!data[2].isEmpty())
    {
      StringReader stringReader = new StringReader(data[2]);
      JSONStreamReaderImpl s = new JSONStreamReaderImpl(stringReader);
      JSONDocument _json = s.build();
      this.data.putAll(_json.object());
    }
  }

  public Request (double time, Endpoint destination)
  {
    this.time = time;
    this.destination = destination;
  }

  public void setData (Payload data)
  {
    this.data = data;
  }

  public double getTime ()
  {
    return time;
  }

  public Payload getData ()
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
