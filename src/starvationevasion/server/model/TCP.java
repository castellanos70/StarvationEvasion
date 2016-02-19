package starvationevasion.server.model;

/**
 * @author Javier Chavez
 *
 */


import com.oracle.javafx.jmx.json.JSONDocument;
import com.oracle.javafx.jmx.json.impl.JSONStreamReaderImpl;

import java.io.StringReader;


/**
 * Class that mimics the TCP layer
 *
 * @param <T> class extending tcp
 */
public abstract class TCP<T extends TCP> implements TCPI
{
  public static String DELIMITER=" ";
  private double time;
  private String msg = " ";
  private String path = "";
  private String from = "";
  private JSONDocument payload;





  public TCP(double time, String data)
  {

    this.msg = data;
    this.time = time;
  }


  public TCP(double time, JSONDocument payload)
  {

    this.payload = payload;
    this.time = time;
  }


  /**
   * Turn a string into a TCP
   *
   * @param s valid TCP string (see. toString())
   */
  public TCP(String s) throws Exception
  {
    System.out.println(s);

    String[] _dataArray = s.split(DELIMITER);

    if (_dataArray.length == 0)
    {
      throw new Exception("Improper format");
    }


    // payload may bot be avail
    if (_dataArray.length == 1)
    {
      return;
    }

    JSONStreamReaderImpl sk = new JSONStreamReaderImpl(new StringReader(_dataArray[1]));
    payload = sk.build();


  }

  @Override
  public void setTime (double time)
  {
    this.time = time;
  }

  @Override
  public void setMsg (String msg)
  {
    this.msg = msg;
  }

  @Override
  public String getMsg ()
  {
    return msg;
  }

  @Override
  public double getTime ()
  {
    return time;
  }

  @Override
  public String getStringTime ()
  {
    return String.format("%.3f", time);
  }

  @Override
  public String getFrom ()
  {
    return from;
  }

  @Override
  public void setFrom (String from)
  {
    this.from = from;
  }

  @Override
  public JSONDocument getPayload ()
  {
    return payload;
  }

  @Override
  public void setPayload (JSONDocument payload)
  {
    this.payload = payload;
  }
}