package starvationevasion.server.model;


import com.oracle.javafx.jmx.json.JSONDocument;
import starvationevasion.server.io.JSON;

public class Response implements Sendable
{

  private Object data;
  private String message = "";
  private String type = "";
  private double time = 0f;


  public Response (double time, Object data, String message)
  {
    this.time = time;
    this.data = data;
    this.message = message;
  }

  public Response (double time, Object data)
  {
    this(time, data, "");
  }

  public Response (Object data)
  {
    this.data = data;
  }

  @Override
  public String toString ()
  {
    if (data instanceof String)
    {
      return data.toString();
    }
    return String.valueOf(time) + " " + data;

  }

  @Override
  public JSONDocument toJSON ()
  {
    JSONDocument document = JSONDocument.createObject();
    document.setNumber("time", time);
    if (data instanceof JSONDocument)
    {
      document.set("data", ((JSONDocument) data));
    }
    else if (data instanceof String)
    {
      document.setString("data", ((String) data));
    }

    if (!message.isEmpty())
    {
      document.setString("message", message);
    }

    return document;
  }

  @Override
  public void fromJSON (Object doc)
  {
    JSONDocument json = JSON.Parser.toJSON(data);


    time = (double) json.getNumber("time");
    data = json.get("data");
    type = json.getString("type");
    message = json.getString("message");
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
