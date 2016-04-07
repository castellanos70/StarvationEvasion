package starvationevasion.server.model;

/**
 * @author Javier Chavez (javierc@cs.unm.edu)
 */

import com.oracle.javafx.jmx.json.JSONDocument;
import starvationevasion.server.Worker;
import starvationevasion.server.io.EndpointException;
import starvationevasion.server.io.JSON;

import java.beans.Transient;

public class Request implements Sendable
{
  private Payload data = new Payload();
  private double time;
  private Endpoint destination;
  private transient Worker worker;

  /**
   * Expecting at least 2 args.
   *
   * @param data is an array where data[0] time, data[1] endpoint, data[2] is not explicitly
   * required but the endpoint might require it.
   *
   * @throws EndpointException when an endpoint is not found
   */
  public Request (String... data) throws EndpointException
  {
    this.time = Double.parseDouble(data[0]);
    try
    {
      this.destination = Endpoint.valueOf(data[1].toUpperCase());

    }
    catch(IllegalArgumentException e)
    {
      throw new EndpointException("Endpoint for " + data[1] + " was not found.");
    }

    data[2] = data[2].replace(data[0] + " ", "");
    data[2] = data[2].replace(data[1], "");
    data[2] = data[2].trim();

    if (!data[2].isEmpty())
    {
      JSONDocument _json = JSON.Parser.toJSON(data[2]);
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

  public Payload getPayload ()
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

  @Override
  public Type getType ()
  {
    return null;
  }

  @Override
  public JSONDocument toJSON ()
  {
    return null;
  }

  @Override
  public void fromJSON (Object doc)
  {

  }

  @Transient
  public Worker getWorker ()
  {
    return worker;
  }

  @Transient
  public void setWorker (Worker worker)
  {
    this.worker = worker;
  }
}
