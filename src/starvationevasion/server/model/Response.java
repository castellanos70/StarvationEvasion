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
    JSONDocument _json = JSONDocument.createObject();
    _json.setNumber("time", time);
    // _json.setString("message", message);
    _json.setString("type", type);
    // this needs some fixing
    _json.set("data", data.toJSON());

    return _json;
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
}
