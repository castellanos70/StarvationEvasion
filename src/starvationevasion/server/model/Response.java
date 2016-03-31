package starvationevasion.server.model;


import com.oracle.javafx.jmx.json.JSONDocument;

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

  @Override
  public String toString ()
  {
    return String.valueOf(time) + " " + data;
  }

  @Override
  public String toJSONString ()
  {
    return toJSON().toString();
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
