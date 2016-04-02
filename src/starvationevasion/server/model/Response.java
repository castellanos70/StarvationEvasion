package starvationevasion.server.model;


import com.oracle.javafx.jmx.json.JSONDocument;
import starvationevasion.server.io.JSON;

public class Response implements Sendable
{

  private Payload data = new Payload();
  private String type = "";
  private double time = 0f;


  public Response (double time, Payload data)
  {
    this.time = time;
    this.data = data;
  }

  public Response (double time, String msg)
  {
    this.time = time;
    this.data.put("message", msg);
  }

  public Response (Payload data)
  {
    this.data = data;
  }

  public Response (String data)
  {
    this.data.put("message", data);
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
    data.put("type", type);

    return data.toJSON();
  }

  @Override
  public void fromJSON (Object doc)
  {
    JSONDocument json = JSON.Parser.toJSON(data);


    time = (double) json.getNumber("time");
    type = json.getString("type");
    // message = json.getString("message");

    JSONDocument _data = json.get("data");
    data.fromJSON(_data);
  }

  @Override
  public void setType (String type)
  {
    this.type = type;
  }

  @Override
  public String getType ()
  {
    return null;
  }

  public Payload getData ()
  {
    return data;
  }

  public double getTime ()
  {
    return time;
  }
}
