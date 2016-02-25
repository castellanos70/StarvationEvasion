package starvationevasion.server.model;


import com.oracle.javafx.jmx.json.JSONDocument;
import starvationevasion.server.io.JSON;

import java.io.Serializable;

public class Response implements JSON, Serializable
{

  String data = "";
  double time = 0f;


  public Response (String ...data)
  {


//    if (data.length < 2)
//    {
//      throw new Exception("Not enough data");
//    }
//
//    // get the time
    this.time = Double.parseDouble(data[0]);
    this.data = data[1];
//    this.destination = Endpoint.valueOf(data[1].toUpperCase());
//    this.data = new String[data.length];

//    if (data.length >= 3)
//    {
//      System.arraycopy(data, 2, this.data, 0, data.length - 2);
//    }

  }

  @Override
  public String toString ()
  {
    return String.valueOf(time) + " " + data;
  }

  @Override
  public JSONDocument toJSON ()
  {
    JSONDocument document = JSONDocument.createObject();
    document.setNumber("time", time);
    document.setString("data", data);

    return document;
  }
}
