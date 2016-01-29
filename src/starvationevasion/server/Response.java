package starvationevasion.server;


/**
 * @author Javier Chavez
 */


import com.oracle.javafx.jmx.json.JSONDocument;

/**
 * Responses are sent from server to client.
 */
public class Response extends TCP<Response>
{


  public Response (ActionType type, double time, JSONDocument json)
  {
    super(type, time, json);
  }

  public Response(String s) throws Exception
  {
    super(s);
  }


  /**
   * Format is constrained to a school specification.
   *
   * @return string with tcp data
   */
  @Override
  public String toString()
  {
    JSONDocument _response = new JSONDocument(JSONDocument.Type.OBJECT);
    _response.setNumber("status", 200);
    _response.setNumber("time", getTime());
    _response.set("data", getPayload());
    return _response.toString()+"\r\n";

  }
}