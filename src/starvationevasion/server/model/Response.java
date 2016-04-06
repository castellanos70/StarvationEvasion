package starvationevasion.server.model;

/**
 * @author Javier Chavez (javierc@cs.unm.edu)
 */

import com.oracle.javafx.jmx.json.JSONDocument;
import starvationevasion.server.io.JSON;

public class Response implements Sendable
{

  private Payload data = new Payload();
  private Type type = Type.BROADCAST;
  private String message = "";
  private double time = 0f;


  public Response (double time, Payload payload)
  {
    this.time = time;
    this.data = payload;
  }

  public Response (double time, String msg)
  {
    this.time = time;
    this.message = msg;
  }

  public Response (Payload data)
  {
    this.data = data;
  }

  public Response (String message)
  {
    this.message = message;
  }

  @Override
  public String toString ()
  {
    return String.valueOf(time) + " " + data;
  }

  @Override
  public JSONDocument toJSON ()
  {
    data.put("time", time);
    data.put("type", data.getOrDefault("type", type));
    data.put("message", data.getOrDefault("message", message));

    return data.toJSON();
  }

  @Override
  public void fromJSON (Object doc)
  {
    JSONDocument json = JSON.Parser.toJSON(data);


    time = (double) json.getNumber("time");
    type = Type.valueOf(json.getString("type"));
    message = json.getString("message");

    JSONDocument _data = json.get("data");
    data.fromJSON(_data);
  }

  public void setType (Type type)
  {
    this.type = type;
  }

  @Override
  public Type getType ()
  {
    return type;
  }

  public Payload getPayload ()
  {
    return data;
  }

  public double getTime ()
  {
    return time;
  }

  public void setMessage (String message)
  {
    this.message = message;
  }
}
