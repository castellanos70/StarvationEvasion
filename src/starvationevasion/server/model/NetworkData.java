package starvationevasion.server.model;

import com.oracle.javafx.jmx.json.JSONDocument;

import starvationevasion.server.io.JSON;

public abstract class NetworkData implements Sendable
{
  private Payload data;
  private Type type;
  private String message;// header
  private double time = 0f;

  public <T extends Payload> NetworkData(double time, T data, String msg, Type type)
  {
    this.time = time;
    this.data = data;
    this.message = msg;
    this.type = type;
  }

  public NetworkData(double time, Sendable data, String msg, Type type)
  {
    this(time, new Payload(), msg, type);
    getPayload().putData(data);
  }

  public NetworkData(double time, Payload data, String msg)
  {
    this(time, data, msg, Type.BROADCAST);
  }

  public <T extends Payload> NetworkData(double time, T data, Type type)
  {
    this(time, data, "", type);
  }

  public NetworkData(double time, Sendable data, Type type)
  {
    this(time, new Payload(data), "", type);
  }

  @Override
  public String toString()
  {
    return String.valueOf(time) + " " + data;
  }

  @Override
  public JSONDocument toJSON()
  {
    data.put("time", time);
    data.put("type", data.getOrDefault("type", type));
    data.put("message", data.getOrDefault("message", message));

    return data.toJSON();
  }

  @Override
  public void fromJSON(Object doc)
  {
    JSONDocument json = JSON.Parser.toJSON(data);

    time = (double) json.getNumber("time");
    type = Type.valueOf(json.getString("type"));
    message = json.getString("message");

    JSONDocument _data = json.get("data");
    data.fromJSON(_data);
  }

  public void setType(Type type)
  {
    this.type = type;
  }

  @Override
  public Type getType()
  {
    return type;
  }

  public Payload getPayload()
  {
    return data;
  }

  public double getTime()
  {
    return time;
  }

  public void setMessage(String message)
  {
    this.message = message;
  }

}
