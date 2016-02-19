package starvationevasion.server.model;

import com.oracle.javafx.jmx.json.JSONDocument;


public interface TCPI
{
  void setTime (double time);

  void setMsg (String msg);

  String getMsg ();

  double getTime ();

  String getStringTime ();

  String getFrom ();

  void setFrom (String from);

  JSONDocument getPayload ();

  void setPayload (JSONDocument payload);
}
